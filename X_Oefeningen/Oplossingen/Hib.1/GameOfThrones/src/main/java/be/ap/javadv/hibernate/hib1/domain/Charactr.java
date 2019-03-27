package be.ap.javadv.hibernate.hib1.domain;

import java.util.Objects;

public class Charactr {
	private String name;
	private Integer deathYear;
	private boolean nobility;
	private String gender;

	public Charactr() {}

	public Charactr(String name, String deathYear, boolean nobility, String gender) { 
		this.setName(name);
		this.setGender(gender);
		this.setDeathYear(deathYear);
		this.setNobility(nobility);
	}

	public Integer getDeathYear() {
		return this.deathYear;
	}

	public void setDeathYear(String deathYear) {
		this.deathYear = deathYear.equals("") ? null : Integer.valueOf(deathYear);
	}

	public void setDeathYear(Integer deathYear) {
		this.deathYear = deathYear;
	}

	public boolean getNobility() {
		return this.nobility;
	}

	public void setNobility(boolean nobility) {
		this.nobility = nobility;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;

		final Charactr other = (Charactr) obj;
		return this.getName().equals(other.getName());
	}
}