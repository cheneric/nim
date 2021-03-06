package cheneric.nim.server.api;

import javax.inject.Inject;
import javax.inject.Singleton;

import cheneric.nim.api.GameState;
import cheneric.nim.api.Player;
import cheneric.nim.server.game.Game;

@Singleton
class GameStateFactory {

	@Inject
	GameStateFactory() {}

	GameState createGameState(Game game, String gameToken) {
		final GameState gameState = new GameState();
		gameState.setBuckets(game.getBuckets());
		gameState.setTurn(game.isPlayerTurn(gameToken) ? Player.SELF : Player.OPPONENT);

		if (game.hasWinner()) {
			gameState.setWinner(game.isWinner(gameToken) ? Player.SELF : Player.OPPONENT);
		}
		return gameState;
	}
}
