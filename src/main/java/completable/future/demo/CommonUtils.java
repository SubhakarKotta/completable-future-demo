package completable.future.demo;

import java.util.Random;

public class CommonUtils {

	public static void delay(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void randomDelay() {
		delay(500 + (new Random()).nextInt(2000));
	}

}
