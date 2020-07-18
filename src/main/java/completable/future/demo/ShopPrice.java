package completable.future.demo;

public class ShopPrice {
	private String shopName;
	private double price;
	private Currency currency;

	public ShopPrice(String shopName, double price, Currency currency) {
		this.shopName = shopName;
		this.price = price;
		this.currency = currency;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public void convertCurrency(double rate, Currency toCurrency) {
		this.price = price * rate;
		this.currency = toCurrency;
	}

	@Override
	public String toString() {
		return String.format("%s: %.2f %s", this.shopName, this.price, this.currency.name());
	}
}
