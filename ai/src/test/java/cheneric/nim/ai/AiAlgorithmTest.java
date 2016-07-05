package cheneric.nim.ai;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link AiAlgorithm} unit tests.
 */
public class AiAlgorithmTest {
	AiAlgorithm aiAlgorithm;

	@Before
	public void setUp() {
		aiAlgorithm = new AiAlgorithm();
	}

	/**
	 * Tests {@link AiAlgorithm#getTokenSum(java.util.List)}.
	 */
	@Test
	public void testGetAnrunSum() {
		assertEquals(0, aiAlgorithm.getTokenSum(new ArrayList<Integer>()));
		assertEquals(0, aiAlgorithm.getTokenSum(Arrays.asList(0)));
		assertEquals(0, aiAlgorithm.getTokenSum(Arrays.asList(0, 0)));
		assertEquals(1, aiAlgorithm.getTokenSum(Arrays.asList(0, 1)));
		assertEquals(0, aiAlgorithm.getTokenSum(Arrays.asList(1, 0, 1)));
		assertEquals(0, aiAlgorithm.getTokenSum(Arrays.asList(0, 1, 1)));
		assertEquals(0, aiAlgorithm.getTokenSum(Arrays.asList(1, 1, 0)));
		assertEquals(0, aiAlgorithm.getTokenSum(Arrays.asList(2, 2, 0, 0)));
		assertEquals(0, aiAlgorithm.getTokenSum(Arrays.asList(3, 0, 3, 0)));
		assertEquals(0, aiAlgorithm.getTokenSum(Arrays.asList(1, 2, 3)));
		assertEquals(2, aiAlgorithm.getTokenSum(Arrays.asList(3, 4, 5)));
		assertEquals(4, aiAlgorithm.getTokenSum(Arrays.asList(3, 4, 5, 6)));
		assertEquals(5, aiAlgorithm.getTokenSum(Arrays.asList(3, 4, 5, 7)));
	}

	/**
	 * Tests {@link AiAlgorithm#getMove(java.util.List)}.
	 */
	@Test
	public void testGetMove() {
		assertEquals(new Move(1, 2), aiAlgorithm.getMove(Arrays.asList(1, 3)));
		assertEquals(new Move(0, 2), aiAlgorithm.getMove(Arrays.asList(3, 1)));
		assertEquals(new Move(1, 4), aiAlgorithm.getMove(Arrays.asList(4, 8)));
		assertEquals(new Move(0, 4), aiAlgorithm.getMove(Arrays.asList(8, 4)));
		assertEquals(new Move(0, 2), aiAlgorithm.getMove(Arrays.asList(8, 4, 2)));
	}
}
