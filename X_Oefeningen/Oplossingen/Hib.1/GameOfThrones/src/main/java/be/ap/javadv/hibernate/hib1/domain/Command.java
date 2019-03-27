package be.ap.javadv.hibernate.hib1.domain;

import java.io.Serializable;

public class Command implements Serializable{
	private int side;
	private String character;

	public Command() {}
	public Command(int side, String character) {
		this.setSide(side);
		this.setCharacter(character);
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(getSide()).hashCode() + getCharacter().hashCode();
	}
}