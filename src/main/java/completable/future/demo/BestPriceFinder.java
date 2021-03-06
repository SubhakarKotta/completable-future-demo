package completable.future.demo;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class BestPriceFinder {

	private final List<Shop> mShops = Arrays.asList(new Shop("BestShop"), new Shop("WeRipYouOff"),
			new Shop("Abriss GmbH"), new Shop("IKEA"));

	// One thread for each shop (results in total exec time = exec time of 1 shop)
	private final Executor mExec = Executors.newFixedThreadPool(Math.min(mShops.size(), 100), runnable -> {
		Thread t = new Thread(runnable);
		t.setDaemon(true);
		return t;
	});

	public ShopPrice findPrice(String shop, String product) {
		String priceInfo = (new Shop(shop)).getPrice(product);
		ShopPrice price = DiscountService.applyDiscount(Quote.parse(priceInfo));
		price.convertCurrency(ExchangeService.getUSDToEUR(), Currency.EUR);
		return price;
	}

	public Future<ShopPrice> findPriceAsync(String shop, String product) {
		return supplyAsync(() -> (new Shop(shop)).getPrice(product)).thenApply(Quote::parse)
				.thenCompose(q -> supplyAsync(() -> DiscountService.applyDiscount(q)))
				.thenCombine(supplyAsync(() -> ExchangeService.getUSDToEUR()), (price, rate) -> {
					price.convertCurrency(rate, Currency.EUR);
					return price;
				});
	}

	// This solution uses a synchronous API in an asynchronous (non-blocking) way
	public Stream<CompletableFuture<ShopPrice>> findAllPricesAsync(String product) {
		return mShops.stream().map(shop -> supplyAsync(() -> shop.getPrice(product), mExec))
				.map(f -> f.thenApply(Quote::parse))
				.map(f -> f.thenCompose(quote -> supplyAsync(() -> DiscountService.applyDiscount(quote), mExec)))
				.map(f -> f.thenCombine(supplyAsync(ExchangeService::getUSDToEUR, mExec), (price, rate) -> {
					price.convertCurrency(rate, Currency.EUR);
					return price;
				}));
	}

}