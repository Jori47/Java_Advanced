package be.ap.javadv.collections;

public class Human {
	private String firstName;
	private String lastName;
	private int age;

	public Human(String firstName, String lastName, int age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}

	@Override
	public String toString() {
		return String.format ("%s %s (%s years old)", 
			this.firstName, this.lastName, this.age);
	}
}