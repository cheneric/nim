package chen.eric.nim.server.game;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chen.eric.nim.api.InvalidBucketException;
import chen.eric.nim.api.InvalidGameTokenException;
import chen.eric.nim.api.InvalidRemoveCountException;
import chen.eric.nim.api.OutOfTurnException;

public class Game {
	private static final Logger log = LoggerFactory.getLogger(Game.class);

	private final Player[] players = new Player[2];
	private final List<Integer> buckets;
	private int playerTurn;

	// cache of buckets sum
	private int tokenCount;

	Game(Player player1, Player player2, List<Integer> buckets) {
		players[0] = player1;
		players[1] = player2;
		this.buckets = buckets;
		tokenCount = countTokens(buckets);
	}

	int countTokens(List<Integer> buckets) {
		int totalCount = 0;
		for (final int bucketCount : buckets) {
			totalCount += bucketCount;
		}
		return totalCount;
	}

	public List<Integer> getBuckets() {
		return buckets;
	}

	Player[] getPlayers() {
		return players;
	}

	Player getCurrentPlayer() {
		return players[playerTurn];
	}

	Player getWaitingPlayer() {
		return players[(playerTurn + 1) % 2];
	}

	public Player getOpponent(String gameToken) {
		final Player currentPlayer = getCurrentPlayer();
		return (currentPlayer.getGameToken().equals(gameToken) ? getWaitingPlayer() : currentPlayer);
	}

	public Player move(String gameToken, int bucketIndex, int removeCount) 
		throws InvalidGameTokenException, InvalidBucketException, 
			InvalidRemoveCountException, OutOfTurnException
	{
		if (getCurrentPlayer().getGameToken().equals(gameToken)) {
			if (bucketIndex >= buckets.size()) {
				log.debug("Invalid bucket index " + bucketIndex + " in " + this);
				throw new InvalidBucketException("Invalid bucket: " + bucketIndex);
			}
			else {
				if (removeCount < 1) {
					log.debug("Invalid remove count " + removeCount  + " < 1 in " + this);
					throw new InvalidRemoveCountException("Remove count cannot be < 1 : " + removeCount);
				}
				else {
					final int numberOfTokens = buckets.get(bucketIndex);
					if (removeCount > numberOfTokens) {
						log.debug("Invalid remove count " + removeCount + " > number of tokens " + numberOfTokens + " in bucket " + bucketIndex + " in " + this);
						throw new InvalidRemoveCountException("Remove count " + removeCount + " > number of tokens " + numberOfTokens + " in bucket " + bucketIndex);
					}
					else {
						removeFromBucket(bucketIndex, removeCount);
						final Player winner = getWinner();
						if (winner == null) {
							nextPlayerTurn();
						}
						return winner;
					}
				}
			}
		}
		else if (getWaitingPlayer().getGameToken().equals(gameToken)) {
			log.debug(getWaitingPlayer() + " attempted to play out of turn in " + this);
			throw new OutOfTurnException("It's not your turn");
		}
		else {
			log.warn("Unexpected game token " + gameToken + " in " + this);
			throw new InvalidGameTokenException("Invalid game token: " + gameToken);
		}
	}

	Player getWinner() {
		return (hasWinner() ? getCurrentPlayer() : null);
	}

	public boolean hasWinner() {
		return tokenCount == 0;
	}

	public boolean isWinner(String gameToken) {
		final Player winner = getWinner();
		return (winner != null && winner.getGameToken().equals(gameToken));
	}

	void removeFromBucket(int bucketIndex, int removeCount) {
		log.debug("Removing " + removeCount + " tokens from bucket " + bucketIndex + " in " + this);
		buckets.set(bucketIndex, buckets.get(bucketIndex) - removeCount);
		tokenCount -= removeCount;
	}

	public boolean isPlayerTurn(String gameToken) {
		return getCurrentPlayer().getGameToken().equals(gameToken);
	}

	void nextPlayerTurn() {
		playerTurn = (playerTurn + 1) % 2;
	}

	@Override
	public String toString() {
		return "{Game: players = " + StringUtils.join(players, ',') 
			+ "; playerTurn = " + playerTurn 
			+ "; tokenCount = " + tokenCount 
			+ "; buckets = " + StringUtils.join(buckets, ",")
			+ "}";
	}
}
