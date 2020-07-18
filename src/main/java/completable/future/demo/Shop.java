package completable.future.demo;

import java.util.concurrent.CompletableFuture;

import completable.future.demo.DiscountService.DiscountCode;

public class Shop {

	private String mName;

	public Shop(String name) {
		mName = name;
	}

	public CompletableFuture<String> getPriceAsync(String product) {
		return CompletableFuture.supplyAsync(() -> getPrice(product));
	}

	public String getPrice(String product) {
		double price = determinePrice(product);
		DiscountCode discountCode = determineDiscountCode(product);
		Currency currency = Currency.valueOf("USD");
		CommonUtils.randomDelay();
		return String.format("%s:%.2f:%s:%s", mName, price, discountCode, currency);
	}

	// Deterministically generate a price based on shop name and product name
	private double determinePrice(String product) {
		if (product.equals("NA"))
			throw new IllegalArgumentException("Product not available");
		// Generate price between 0 and 199.99
		int hash = (mName + product).hashCode();
		int dollars = hash % 200;
		int cents = (hash / 7) % 100;
		return Math.abs(dollars + cents / 100.0);
	}

	// Deterministically pick a discount code based on shop name and product name
	private DiscountCode determineDiscountCode(String product) {
		DiscountCode[] codes = DiscountCode.values();
		return codes[Math.abs((mName + product).hashCode()) % codes.length];
	}

	public String getName() {
		return mName;
	}

}