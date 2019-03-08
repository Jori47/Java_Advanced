package be.ap.javadv.innerclasses;

public class Person {
	private String name;

	public Person(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
