package cheneric.nim.server.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class GameMaster {
	private static final Logger log = LoggerFactory.getLogger(GameMaster.class);
	private final Random random = new Random();
	private final Map<String,Game> games = new ConcurrentHashMap<String,Game>();

	@Inject
	PlayerQueue playerQueue;

	@Inject
	GameMaster() {}

	public Player createPlayer(String name) {
		return new Player(name);
	}

	public Game getGame(String gameToken) {
		return games.get(gameToken);
	}

	public Game startGame(Player player) throws InterruptedException {
		log.debug("Attempting to start game for " + player);
		Game game;
		final String gameToken = player.getGameToken();
		final Player opponent = playerQueue.getOrAwaitOpponent(player);
		if (opponent == null) {
			log.debug("Waiting for opponent: " + player);
			synchronized (player) {
				while ((game = getGame(gameToken)) == null) {
					player.wait();
				}
			}
		}
		else {
			game = startGame(player, opponent);
			final Player waitingPlayer = game.getOpponent(gameToken);
			synchronized (waitingPlayer) {
				waitingPlayer.notify();
			}
		}
		return game;
	}

	Game startGame(Player player1, Player player2) {
		final Game game = randomGame(player1, player2);
		log.debug("Started new game: " + game);
		for (final Player player : game.getPlayers()) {
			final String gameToken = player.getGameToken();
			log.debug("Storing game token " + gameToken + " --> " + game);
			games.put(gameToken, game);
		}
		return game;
	}

	Game randomGame(Player player1, Player player2) {
		// 2-10 buckets
		final int numberOfBuckets = random.nextInt(9) + 2;
		final List<Integer> buckets = new ArrayList<Integer>(numberOfBuckets);
		for (int count = 0; count < numberOfBuckets; count++) {
			// 1-10  tokens per bucket
			final int numberOfTokens = random.nextInt(10) + 1;
			buckets.add(numberOfTokens);
		}
		// randomize start player
		return (random.nextBoolean() ? 
			new Game(player1, player2, buckets) : 
			new Game(player2, player1, buckets));
	}

	void endGame(Game game) {
		for (final Player player : game.getPlayers()) {
			final String gameToken = player.getGameToken();
			log.debug("Removing game token: " + gameToken);
			games.remove(gameToken);
		}
	}
}