package chen.eric.token.server.api;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.thrift.TException;

import chen.eric.token.api.Game;
import chen.eric.token.api.GameState;
import chen.eric.token.api.InvalidBucketException;
import chen.eric.token.api.InvalidGameTokenException;
import chen.eric.token.api.InvalidRemoveCountException;
import chen.eric.token.api.OutOfTurnException;
import chen.eric.token.api.ServerException;
import chen.eric.token.api.TokenGame;
import chen.eric.token.server.game.GameMaster;
import chen.eric.token.server.game.Player;

@Singleton
public class ApiHandler implements TokenGame.Iface {

	@Inject
	GameMaster gameMaster;

	@Inject
	GameFactory gameFactory;

	@Inject
	GameStateFactory gameStateFactory;

	@Inject
	ApiHandler() {}

	@Override
	public Game autoMatch(String playerName) throws ServerException, TException {
		try {
			final Player player = gameMaster.createPlayer(playerName);
			final chen.eric.token.server.game.Game game = gameMaster.startGame(player);
			synchronized (game) {
				return gameFactory.createGame(game, player.getGameToken());
			}
		}
		catch (InterruptedException exception) {
			throw new ServerException("Interrupted");
		}
	}

	@Override
	public GameState move(String gameToken, int bucketIndex, int removeCount) 
		throws InvalidGameTokenException, InvalidBucketException, 
			InvalidRemoveCountException, OutOfTurnException, ServerException, 
			TException
	{
		final chen.eric.token.server.game.Game game = gameMaster.getGame(gameToken);
		if (game == null) {
			throw new InvalidGameTokenException("Invalid game token: " + gameToken);
		}
		synchronized (game) {
			game.move(gameToken, bucketIndex, removeCount);
			game.notifyAll();
			return gameStateFactory.createGameState(game, gameToken);
		}
	}

	@Override
	public GameState awaitMove(String gameToken) 
		throws InvalidGameTokenException, ServerException, TException
	{
		final chen.eric.token.server.game.Game game = gameMaster.getGame(gameToken);
		if (game == null) {
			throw new InvalidGameTokenException("Invalid game token: " + gameToken);
		}
		synchronized (game) {
			try {
				while (!(game.isPlayerTurn(gameToken) || game.hasWinner())) {
					game.wait();
				}
			}
			catch (InterruptedException exception) {
				throw new ServerException("Interrupted");
			}
			return gameStateFactory.createGameState(game, gameToken);
		}
	}
}
