package chen.eric.nim.server.api;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.thrift.TException;

import chen.eric.nim.api.Game;
import chen.eric.nim.api.GameState;
import chen.eric.nim.api.InvalidBucketException;
import chen.eric.nim.api.InvalidGameTokenException;
import chen.eric.nim.api.InvalidRemoveCountException;
import chen.eric.nim.api.OutOfTurnException;
import chen.eric.nim.api.ServerException;
import chen.eric.nim.api.Nim;
import chen.eric.nim.server.game.GameMaster;
import chen.eric.nim.server.game.Player;

@Singleton
public class ApiHandler implements Nim.Iface {

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
			final chen.eric.nim.server.game.Game game = gameMaster.startGame(player);
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
		final chen.eric.nim.server.game.Game game = gameMaster.getGame(gameToken);
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
		final chen.eric.nim.server.game.Game game = gameMaster.getGame(gameToken);
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
