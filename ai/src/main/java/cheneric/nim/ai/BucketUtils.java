package cheneric.nim.ai;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class BucketUtils {
	public static String toString(List<Integer> buckets) {
		return "{" + StringUtils.join(buckets, ", ") + "}";
	}

	private BucketUtils() {}
}
