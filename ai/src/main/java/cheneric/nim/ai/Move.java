package cheneric.nim.ai;

public class Move {
	private final int bucketIndex;
	private final int removeTokenCount;

	public Move(int bucketIndex, int removeTokenCount) {
		this.bucketIndex = bucketIndex;
		this.removeTokenCount = removeTokenCount;
	}

	public int getBucketIndex() {
		return bucketIndex;
	}

	public int getRemoveTokenCount() {
		return removeTokenCount;
	}

	@Override
	public boolean equals(Object object) {
		return object != null 
			&& object instanceof Move
			&& equals((Move)object);
	}
	
	public boolean equals(Move move) {
		return move != null
			&& bucketIndex == move.bucketIndex
			&& removeTokenCount == move.removeTokenCount;
	}
	@Override
	public String toString() {
		return "{Move: bucketIndex = " + bucketIndex + ", removeTokenCount = " + removeTokenCount + "}";
	}
}
