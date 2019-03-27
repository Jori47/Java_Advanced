package be.ap.javadv.hibernate.hib1.domain;

import java.io.Serializable;

public class Allegiance implements Serializable {
	private String house;
	private String character;

	public Allegiance() {}
	public Allegiance(String house, String character) {
		this.setHouse(house);
		this.setCharacter(character);
	}

	public String getCharacter() {
		return this.character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public String getHouse() {
		return this.house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	@Override
	public int hashCode() {
		return getHouse().hashCode() + getCharacter().hashCode();
	}
}