package be.ap.javadv.hibernate.hib1.domain;

public class Side {
	private static int counter=0;
	private int id;
	private String battle;
	private String house;
	private String type;
	private String outcome;

	public Side() {}

	public Side(String house, String type, String outcome, String battle) {
		this.setHouse(house);
		this.setBattle(battle);
		this.setType(type);
		this.setOutcome(outcome);
		this.setId(Side.counter++);
	}

	public String getBattle() {
		return this.battle;
	}

	public void setBattle(String battle) {
		this.battle = battle;
	}

	public String getOutcome() {
		return this.outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHouse() {
		return this.house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
}