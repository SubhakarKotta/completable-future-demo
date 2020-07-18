package completable.future.demo;

import static completable.future.demo.TestHelpers.doSomethingElse;
import static completable.future.demo.TestHelpers.getTime;
import static completable.future.demo.TestHelpers.say;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class BestPriceFinderTestSingleShop {
	private BestPriceFinder mFinder = new BestPriceFinder();
	private String mProduct = "BestProduct"; // "NA"
	private String mShop = "BestShop";

	@Test
	public void runFindPrice() {
		say("> Calling findPrice");
		long startTime = getTime();
		ShopPrice price = mFinder.findPrice(mShop, mProduct);
		say("< findPrice returns after " + (getTime() - startTime) + " milliseconds");
		say("I have been blocked until now :(");
		say(price.toString());
	}

	@Test
	public void runFindPriceAsync() {
		say("> Calling findPriceAsync");
		long startTime = getTime();
		Future<ShopPrice> futurePrice = mFinder.findPriceAsync(mShop, mProduct);
		say("< findPriceAsync returns a Future after " + (getTime() - startTime) + " milliseconds");

		say("I can now dow anything I want :)");
		doSomethingElse();

		ShopPrice price;
		try {
			price = futurePrice.get(10, TimeUnit.SECONDS);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		say("The Future is ready after " + (getTime() - startTime) + " milliseconds");
		say(price.toString());
	}
}
