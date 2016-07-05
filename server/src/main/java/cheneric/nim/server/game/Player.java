package cheneric.nim.server.game;

import java.util.UUID;

public class Player {
	private final String gameToken;
	private final String name;

	Player(String name) {
		this.gameToken = UUID.randomUUID().toString();
		this.name = name;
	}

	public String getGameToken() {
		return gameToken;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "{Player name = " + name + "; gameToken = " + gameToken + "}";
	}
}