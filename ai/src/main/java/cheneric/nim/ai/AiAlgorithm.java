package cheneric.nim.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AiAlgorithm {
	private static final Logger log = LoggerFactory.getLogger(AiAlgorithm.class);
	private static final Random random = new Random();

	@Inject
	AiAlgorithm() {}

	public Move getMove(List<Integer> buckets) {
		Move move = null;
		final int tokenSum = getTokenSum(buckets);
		final String bucketsString = BucketUtils.toString(buckets);
		log.debug("Token sum " + tokenSum + " for buckets " + bucketsString);
		final List<Integer> nonEmptyBucketIndexes = new ArrayList<Integer>();
		int bucketIndex = 0;
		for (final Integer tokenCount : buckets) {
			if (tokenCount > 0) {
				// buckets with no tokens are not eligible for a move
				final int partialTokenSum = tokenSum ^ tokenCount;
				if (partialTokenSum < tokenCount) {
					log.debug("Found token sum target at index " + bucketIndex +  " for sum " + tokenSum + " of " + bucketsString );
					move = new Move(bucketIndex, tokenCount - partialTokenSum);
					break;
				}

				log.debug("Storing non-empty bucket index " + bucketIndex + " of " + bucketsString);
				nonEmptyBucketIndexes.add(bucketIndex);
			}
			bucketIndex++;
		}
		if (move == null) {
			log.debug("No token sum target for sum "  + tokenSum + " in " + bucketsString);
			bucketIndex = nonEmptyBucketIndexes.get(random.nextInt(nonEmptyBucketIndexes.size()));
			log.debug("Randomly selected non-empty bucket index " + bucketIndex);
			move = new Move(bucketIndex, 1);
		}
		log.debug(move.toString());
		return move;
	}

	int getTokenSum(List<Integer> integers) {
		int tokenSum = 0;
		for (final int integer : integers) {
			tokenSum ^= integer;
		}
		return tokenSum;
	}
}
