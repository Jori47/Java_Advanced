package be.ap.javadv.collections;

import java.util.HashMap;

import be.ap.javadv.collections.Customer.Address;
import be.ap.javadv.collections.ProductClass.Product;

public class Order {
	public static void main(String[] args) {
		Customer customer = new Customer("Jan Peeters");
		Customer.Address address = customer.new Address("Berkenlaan 23", "9262 Zwavelgem");

		ProductClass groenten = new ProductClass("Groenten");
		ProductClass.Product tomaat = groenten.new Product("tomaat", 1.12);
		ProductClass.Product prei = groenten.new Product("prei", .76);

		ProductClass zuivel = new ProductClass("zuivel");
		ProductClass.Product melk = zuivel.new Product("melk", .34);
		ProductClass.Product yoghurt = zuivel.new Product("yoghurt", 2.91);

		HashMap <Product, Integer> productAmounts = new HashMap<>();

		productAmounts.put (tomaat, 12);
		productAmounts.put (prei, 2);
		productAmounts.put (melk, 6);
		productAmounts.put (yoghurt, 10);

		Address.Invoice invoice = address.new Invoice(productAmounts);
		System.out.println(invoice);
	}	
}