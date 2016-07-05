package cheneric.nim.server.game;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PlayerQueue {
	private static final Logger log = LoggerFactory.getLogger(PlayerQueue.class);
	private final BlockingQueue<Player> players = new LinkedBlockingQueue<Player>();

	@Inject
	PlayerQueue() {}

	Player getOrAwaitOpponent(Player player) throws InterruptedException {
		Player opponent = null;
		synchronized (players) {
			opponent = players.poll();
			if (opponent == null) {
				log.debug("Waiting for opponent: " + player);
				players.put(player);
			}
			else {
				log.debug("Found opponent: " + player + " <> " + opponent);
			}
		}
		return opponent;
	}
}
