package completable.future.demo;

import static completable.future.demo.TestHelpers.getTime;
import static completable.future.demo.TestHelpers.say;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.junit.Test;

public class BestPriceFinderTestAllShops {
	private BestPriceFinder mFinder = new BestPriceFinder();
	private String mProduct = "BestProduct"; // "NA"

	@Test
	public void runFindAllPricesAsync() {
		say("> Calling findAllPricesAsync");
		long startTime = getTime();
		Stream<CompletableFuture<ShopPrice>> fut = mFinder.findAllPricesAsync(mProduct);
		say("< findAllPricesAsync returns after " + (getTime() - startTime) + " milliseconds");

		CompletableFuture[] futArr = fut
				.map(f -> f.thenAccept(price -> System.out
						.println(String.format("%s (%d milliseconds)", price, (getTime() - startTime)))))
				.toArray(size -> new CompletableFuture[size]);
		CompletableFuture.allOf(futArr).join();
		say("All shops returned results after " + (getTime() - startTime) + " milliseconds");
	}

}
