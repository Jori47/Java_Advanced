package be.ap.javadv.hibernate.hib1.domain;

public class Battle {
	private String name;
	private Integer year;
	private String type;

	public Battle() {}

	public Battle(String name, String year, String type) {
		this.setName(name);
		this.setYear(year);
		this.setType(type);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setYear(String year) {
		this.year = year.equals("") ? null : Integer.valueOf(year);
	}
}