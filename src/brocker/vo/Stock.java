package brocker.vo;

import java.io.Serializable;

public class Stock implements Serializable{
	 private String symbol;
	 private int price;
	 
	public Stock(String symbol, int price) {
		super();
		this.symbol = symbol;
		this.price = price;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "[주식명] " + symbol + " [주가] " + price;
	}
	
}
