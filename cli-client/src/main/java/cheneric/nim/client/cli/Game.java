package cheneric.nim.client.cli;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cheneric.nim.api.GameState;
import cheneric.nim.api.InvalidBucketException;
import cheneric.nim.api.InvalidRemoveCountException;
import cheneric.nim.api.Player;
import cheneric.nim.api.Nim;

@Singleton
public class Game {
	private static final Logger log = LoggerFactory.getLogger(Game.class);

	@Inject
	TTransport transport;

	@Inject
	Nim.Iface apiClient;

	@Inject
	Game() {}

	public void run(String playerName) throws TException, IOException {
		try {
			// connect to game server
			renderConnecting();
			transport.open();

			while (true) {
				final cheneric.nim.api.Game game = apiClient.autoMatch(playerName);
				renderNewGame(game);

				// game loop
				final String gameToken = game.getGameToken();
				GameState gameState = game.getGameState();
				do {
					renderGameState(gameState);
					if (gameState.getTurn() == Player.SELF) {
						boolean wasMoveSuccessful = false;
						while (!wasMoveSuccessful) {
							Integer bucketNumber = null;
							Integer tokenRemoveCount = null;
							try {
								bucketNumber = inputBucketNumber();
								tokenRemoveCount = inputTokenRemoveCount(bucketNumber);
								gameState = apiClient.move(gameToken, bucketNumber - 1, tokenRemoveCount);
								wasMoveSuccessful = true;
							}
							catch (InvalidBucketException exception) {
								renderInvalidBucketNumber(bucketNumber);
								log.debug("Invalid bucket number: " + bucketNumber, exception);
							}
							catch (InvalidRemoveCountException exception) {
								renderInvalidTokenRemoveCount(bucketNumber, tokenRemoveCount);
								log.debug("Invalid token remove count: " + tokenRemoveCount, exception);
							}
						}
					}
					else {
						renderWaitingForOtherTurn();
						gameState = apiClient.awaitMove(gameToken);
					}
				}
				while (gameState.getWinner() == null);

				// there is a winner
				renderGameState(gameState);
				renderWinner(gameState.getWinner());
			}
		}
		finally {
			if (transport != null) {
				log.info("Closing connection");
				transport.close();
			}

		}
	}

	int inputBucketNumber() {
		Integer integer = null;
		while (integer == null) {
			renderInputBucketNumber();
			integer = readInt();
		}
		return integer;
	}

	int inputTokenRemoveCount(int bucketNumber) {
		Integer integer = null;
		while (integer == null) {
			renderInputTokenRemoveCount(bucketNumber);
			integer = readInt();
		}
		return integer;
	}

	Integer readInt() {
		Integer integer = null;
		final Scanner scanner = new Scanner(System.in);
		try {
			integer = scanner.nextInt();
		}
		catch (NoSuchElementException exception) {
			log.debug("Error reading int", exception);
		}
		return integer;
	}

	void renderConnecting() {
		System.out.println("Connecting to game server.\nWaiting for opponent.");
	}

	void renderInputBucketNumber() {
		System.out.print("Enter the pile number you want to remove beans from: ");
	}

	void renderInputTokenRemoveCount(int bucketNumber) {
		System.out.print("Enter the number of beans you want to remove from pile " + bucketNumber + ": ");
	}

	void renderNewGame(cheneric.nim.api.Game game) {
		System.out.println("\nNew game started.  Your opponent is " + game.getOpponentName() + ".");
	}

	void renderGameState(GameState gameState) {
		final StringBuilder gameStateBuilder = new StringBuilder();
		int count = 0;
		for (final Integer numberOfTokens : gameState.getBuckets()) {
			if (count % 5 == 0) {
				gameStateBuilder.append('\n');
			}
			gameStateBuilder.append(count + 1)
				.append(':')
				.append(' ')
				.append(numberOfTokens)
				.append('\t');
			count++;
		}
		gameStateBuilder.append('\n');
		System.out.println(gameStateBuilder.toString());
	}

	void renderInvalidBucketNumber(Integer bucketNumber) {
		System.out.println("Invalid bucket number: " + bucketNumber);
	}

	void renderInvalidTokenRemoveCount(Integer bucketNumber, Integer tokenRemoveCount) {
		System.out.println("Invalid token remove count " + tokenRemoveCount + " for bucket number " + bucketNumber);
	}

	void renderWaitingForOtherTurn() {
		System.out.println("Waiting for other player's turn.");
	}

	void renderWinner(Player winner) {
		System.out.println(winner == Player.SELF ? "You win!" : "Your opponent won.");
	}
}
