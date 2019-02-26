package be.ap.javadv.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import be.ap.javadv.collections.ProductClass.Product;
import be.ap.javadv.collections.ProductClass.Product.Price;

public class Customer {
	private String name;

	public class Address {
		private String line1;
		private String line2;

		public class Invoice {
			private int number = 0;
			HashMap<Product, Integer> productAmounts;

			public Invoice(HashMap<Product, Integer> productAmounts) {
				this.number++;
				this.productAmounts = productAmounts;
			}

			@Override
			public String toString() {
				String productsString = "";
				double totalPrice = 0;
				
				Iterator<Entry<Product, Integer>> productsIterator =
					this.productAmounts.entrySet().iterator();
					
				while (productsIterator.hasNext()) {
					Entry<Product, Integer> productAmount = productsIterator.next();
					Price price = productAmount.getKey().getPrice();
					double priceValue = price.getValue();
					int amount = productAmount.getValue();

					totalPrice += priceValue;
					productsString += String.format("%s\t%d\t%7.2f€\n",
						productAmount.getKey(), amount, amount * priceValue);
				}

				return String.format("Invoice No %d\n%s\n\n%s\t\t--------\n\t\t%7.2f€",
					this.number, Address.this, productsString, totalPrice);
			}
		} // Invoice

		public Address(String line1, String line2) {
			this.line1 = line1;
			this.line2 = line2;
		}

		@Override
		public String toString() {
			return String.format("%s\n%s\n%s", Customer.this, this.line1, this.line2);
		}
	} // Address

	public Customer(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
} // Customer