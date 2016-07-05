package cheneric.nim.server.api;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.thrift.TException;

import cheneric.nim.api.Game;
import cheneric.nim.api.GameState;
import cheneric.nim.api.InvalidBucketException;
import cheneric.nim.api.InvalidGameTokenException;
import cheneric.nim.api.InvalidRemoveCountException;
import cheneric.nim.api.OutOfTurnException;
import cheneric.nim.api.ServerException;
import cheneric.nim.api.Nim;
import cheneric.nim.server.game.GameMaster;
import cheneric.nim.server.game.Player;

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
			final cheneric.nim.server.game.Game game = gameMaster.startGame(player);
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
		final cheneric.nim.server.game.Game game = gameMaster.getGame(gameToken);
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
		final cheneric.nim.server.game.Game game = gameMaster.getGame(gameToken);
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
