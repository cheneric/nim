package chen.eric.token.ai;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chen.eric.token.api.Game;
import chen.eric.token.api.GameState;
import chen.eric.token.api.Player;
import chen.eric.token.api.TokenGame;

@Singleton
class AiPlayer {
	private static final Logger log = LoggerFactory.getLogger(Game.class);

	@Inject
	AiAlgorithm aiAlgorithm;

	@Inject
	TokenGame.Iface apiClient;

	@Inject
	TTransport transport;

	@Inject
	AiPlayer() {}

	public void run(String playerName) throws TException, IOException {
		try {
			log.debug("Connecting to game server as " + playerName);
			transport.open();

			while (true) {
				log.debug("Automatching to a game");
				final chen.eric.token.api.Game game = apiClient.autoMatch(playerName);

				// game loop
				final String gameToken = game.getGameToken();
				log.debug("Game token: " + gameToken);
				GameState gameState = game.getGameState();
				do {
					logGameState(gameState);
					if (gameState.getTurn() == Player.SELF) {
						log.debug("My turn");
						final Move move = aiAlgorithm.getMove(gameState.getBuckets());
						gameState = apiClient.move(gameToken, move.getBucketIndex(), move.getRemoveTokenCount());
					}
					else {
						log.debug("Waiting for other player's turn");
						gameState = apiClient.awaitMove(gameToken);
					}
				}
				while (gameState.getWinner() == null);

				// there is a winner
				logGameState(gameState);
				logWinner(gameState.getWinner());
			}

		}
		finally {
			if (transport != null) {
				log.info("Closing connection");
				transport.close();
			}

		}
	}

	void logGameState(GameState gameState) {
		log.debug("{GameState: turn = " + gameState.getTurn() 
			+ "; buckets = " + BucketUtils.toString(gameState.getBuckets()));
	}

	void logWinner(Player winner) {
		log.debug(winner == Player.SELF ? "You win!" : "Your opponent won.");
	}

}
