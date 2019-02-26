package be.ap.javadv.collections;

public class ProductClass {
	private String name;

	public class Product {
		private String name;
		private Price price;

		public class Price {
			private double value;
			public Price(double value) {
				this.value = value;
			}
			public double getValue(){
				return this.value;
			}
		} // Price

		public Product(String name, double price) {
			this.name = name;
			this.price = new Price (price);
		}

		public Price getPrice (){
			return this.price;
		}

		@Override
		public String toString() {
			return String.format(this.name);
		}
	} // Product

	public ProductClass(String name){
		this.name = name;
	}
} //ProductCass