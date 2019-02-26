package be.ap.javadv.collections;

import java.time.LocalDate;

public class Link {
	private LocalDate startDate;
	private String description;

	public Link(String description, LocalDate startDate) {
		this.description = description;
		this.startDate = startDate;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}
}