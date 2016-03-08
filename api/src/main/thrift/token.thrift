namespace java chen.eric.token.api

exception InvalidGameTokenException {
	1:string message
}

exception InvalidBucketException {
	1:string message
}

exception InvalidRemoveCountException {
	1:string message
}

exception OutOfTurnException {
	1:string message
}

exception ServerException {
	1:string message
}

enum Player {
	SELF,
	OPPONENT
}

struct GameState {
	1:list<i32> buckets,
	2:Player turn,
	3:optional Player winner
}

struct Game {
	1:string gameToken,
	2:string opponentName,
	3:GameState gameState
}

service TokenGame {
	Game autoMatch(1:string playerName) throws (1:ServerException serverException),
	GameState move(1:string gameToken, 
		2:i32 bucketIndex, 
		3:i32 removeCount) 
		throws (1:InvalidGameTokenException invalidGameTokenException,
			2:InvalidBucketException invalidBucketException,
			3:InvalidRemoveCountException invalidRemoveCountException,
			4:OutOfTurnException outOfTurnException,
			5:ServerException serverException),
	GameState awaitMove(1:string gameToken) 
		throws (1:InvalidGameTokenException invalidGameTokenException,
			2:ServerException serverException)
}
