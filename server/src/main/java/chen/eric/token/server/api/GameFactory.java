package chen.eric.token.server.api;

import javax.inject.Inject;
import javax.inject.Singleton;

import chen.eric.token.api.Game;

@Singleton
class GameFactory {

	@Inject
	GameStateFactory gameStateFactory;

	@Inject
	GameFactory() {}

	Game createGame(chen.eric.token.server.game.Game game, String gameToken) {
		return new Game(gameToken,
			game.getOpponent(gameToken).getName(),
			gameStateFactory.createGameState(game, gameToken));
	}
}
