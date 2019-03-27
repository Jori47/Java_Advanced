package be.ap.javadv.hibernate.hib1;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.hibernate.Session;

import be.ap.javadv.hibernate.hib1.data.DataReader;
import be.ap.javadv.hibernate.hib1.domain.Allegiance;
import be.ap.javadv.hibernate.hib1.domain.Battle;
import be.ap.javadv.hibernate.hib1.domain.Charactr;
import be.ap.javadv.hibernate.hib1.domain.Command;
import be.ap.javadv.hibernate.hib1.domain.House;
import be.ap.javadv.hibernate.hib1.domain.Side;
import be.ap.javadv.hibernate.hib1.utils.HibernateUtil;

public class Program {
	public static void main(String[] args) {
		Program program = new Program();

		program.parseData();
		program.queryDB();

		HibernateUtil.getSessionFactory().close();
	}

	private void parseData() {
		Set<House> houses = new HashSet<>();
		Set<Charactr> characters = new HashSet<>();
		Set<Allegiance> allegiances = new HashSet<>();
		Set<Battle> battles = new HashSet<>();
		Set<Side> sides = new HashSet<>();
		Set<Command> commands = new HashSet<>();

		String[] battleSides = { "Attacker", "Defender" };

		try {
			DataReader dataReader = new DataReader();
			String[] batlHeader = dataReader.NextBattle();
			String[] charHeader = dataReader.NextCharacter();
			String[] batlDat = dataReader.NextBattle();
			String[] charDat = dataReader.NextCharacter();

			while (charDat != null) {
				String name = charDat[i(charHeader, "Name")];
				String death = charDat[i(charHeader, "Death Year")];
				boolean nobility = charDat[i(charHeader, "Nobility")].trim().equals("1");
				String gender = (charDat[i(charHeader, "Gender")].trim().equals("1")) ? "M" : "F";

				Charactr character = new Charactr(name, death, nobility, gender);
				House house = new House(charDat[i(charHeader, "Allegiances")]);
				house.setName(house.getName().replace("House ", ""));
				Allegiance allegiance = new Allegiance(house.getName(), character.getName());

				houses.add(house);
				characters.add(character);
				allegiances.add(allegiance);

				charDat = dataReader.NextCharacter();
			}

			while (batlDat != null) {
				Battle battle = new Battle(batlDat[i(batlHeader, "name")], batlDat[i(batlHeader, "year")],
						batlDat[i(batlHeader, "battle_type")]);
				String attOutc = batlDat[i(batlHeader, "attacker_outcome")];

				for (int index = 1; index < 5; index++) {
					for (String bSide : battleSides) {
						final String house = batlDat[i(batlHeader, bSide.toLowerCase() + "_" + index)].replace("House ",
								"");

						String outcome = "LOSE";
						String commander = batlDat[i(batlHeader, bSide.toLowerCase() + "_king")];

						if (bSide.equals("Attacker") && attOutc.equals("win"))
							outcome = "WIN";
						if (bSide.equals("Defender") && attOutc.equals("loss"))
							outcome = "WIN";

						if (house.length() > 0 && houses.stream().anyMatch(h -> h.getName().equals(house))) {
							Side side = new Side(house, bSide.substring(0, 3).toUpperCase(), outcome, battle.getName());
							sides.add(side);

							if (commander.length() > 0
									&& characters.stream().anyMatch(c -> c.getName().equals(commander))) {
								Command command = new Command(side.getId(), commander);
								commands.add(command);
							}
						}
					}
				}

				battles.add(battle);

				batlDat = dataReader.NextBattle();
			}
		} catch (IOException e) {
			System.err.println(e);
		}

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		for (House house : houses)
			session.save(house);
		for (Charactr character : characters)
			session.save(character);
		for (Allegiance alegiance : allegiances)
			session.save(alegiance);
		for (Battle battle : battles)
			session.save(battle);
		for (Side side : sides)
			session.save(side);
		for (Command command : commands)
			session.save(command);

		session.getTransaction().commit();
	}

	private int i(String[] array, String element) {
		return Arrays.asList(array).indexOf(element);
	}

	private void queryDB() {
		// Geef de top-10 terug van wie er het vaakst (via allegiance) aan de winnende
		// zijde heeft gestaan? Hou rekening met sterftejaar.
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		String query1 =
			"select c.name, count(s.outcome) from Charactr c " +
			"join Allegiance a on c.name = a.character " +
			"join House h on h.name = a.house " +
			"join Side s on s.house = h.name " +
			"join Battle b on b.name = s.battle " +
			"where s.outcome = 'WIN' " +
			"and coalesce(c.deathYear, 0) > coalesce (b.year, 0) " +
			"group by c.name " +
			"order by 2 desc";

		Stream<Object[]> result1 = session.createQuery( query1 )
			.setMaxResults(10).getResultStream();
		
		System.out.println("\n\n------------\nQUESTION 1");
		result1.forEach(c -> System.out.println(c[0] + ": " +c[1]));
		
		// Druk een tabelletje af met in de eerste kolom Battle type en in de tweede
		// kolom het aantal betrokken commanders
		String query2 =
		"select b.type, count(*) from Battle b " +
		"join Side s on s.battle = b.name " +
		"join Command c on c.side = s.id " +
		"group by b.type "; 
		
		Stream<Object[]> result2 = session.createQuery( query2 ).getResultStream();
		
		System.out.println("\n\n------------\nQUESTION 2");
		result2.forEach(c -> System.out.println(c[0] + ": " +c[1]));
		session.getTransaction().commit();
	}
}
