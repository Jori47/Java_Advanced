package be.ap.javadv.hibernate.hib1.domain;

import java.util.Objects;

public class House {
	private String name;

	public House() {}

	public House(String name) {
		this.setName (name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash (this.name);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;

		final House other = (House) obj;
		return this.getName().equals(other.getName());
	}
}