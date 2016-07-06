package quantum.utils;

import java.util.Random;

public class MyRandom extends Random{
	
	public MyRandom() {
	}

	public MyRandom(long seed) {
		super(seed);
	}
	
	public double rand () {
		return super.nextDouble();
	}
	
	/** Returns a random number between 0 (inclusive) and the specified value (inclusive). */
	public int rand (int range) {
		return super.nextInt(range + 1);
	}

	/** Returns a random number between start (inclusive) and end (inclusive). */
	public int rand (int start, int end) {
		return start + super.nextInt(end - start + 1);
	}
	
	/** Returns a random number between 0 (inclusive) and the specified value (inclusive). */
	public long rand (long range) {
		return (long)(super.nextDouble() * range);
	}

	/** Returns a random number between start (inclusive) and end (inclusive). */
	public long rand (long start, long end) {
		return start + (long)(super.nextDouble() * (end - start));
	}
	
	/** Returns a random number between 0 (inclusive) and the specified value (exclusive). */
	public float rand (float range) {
		return super.nextFloat() * range;
	}

	/** Returns a random number between start (inclusive) and end (exclusive). */
	public float rand (float start, float end) {
		return start + super.nextFloat() * (end - start);
	}
}
