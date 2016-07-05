package cheneric.nim.server.api;

import javax.inject.Inject;
import javax.inject.Singleton;

import cheneric.nim.api.Game;

@Singleton
class GameFactory {

	@Inject
	GameStateFactory gameStateFactory;

	@Inject
	GameFactory() {}

	Game createGame(cheneric.nim.server.game.Game game, String gameToken) {
		return new Game(gameToken,
			game.getOpponent(gameToken).getName(),
			gameStateFactory.createGameState(game, gameToken));
	}
}
