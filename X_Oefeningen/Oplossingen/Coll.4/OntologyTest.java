package be.ap.javadv.collections;

import java.time.LocalDate;

public class OntologyTest {
	public static void main(String[] args) {
		// ----------- <String, Link> ---------
		String[] s = { "tafel", "koe", "wei", "gras" };
		Link[] l = new Link[4];
		
		l[0] = new Link("staat op", LocalDate.of(2005, 2, 12));  
		l[1] = new Link("groeit in", LocalDate.of(2006, 11, 3));
		l[2] = new Link("eet", LocalDate.of(2007, 9, 23));
		l[3] = new Link("staat in", LocalDate.of(2008, 4, 25)); 
		
		Ontology<String, Link> ontStrings = new Ontology<>(s);
		
		ontStrings.makeLink(s[0], s[2], l[3]);
		ontStrings.makeLink(s[1], s[2], l[3]); 
		ontStrings.makeLink(s[1], s[0], l[0]);
		ontStrings.makeLink(s[1], s[3], l[2]);
		ontStrings.makeLink(s[3], s[2], l[1]);
		
		System.out.print(ontStrings);
		
		// ----------- <Human, Link> ---------

		Human lydia = new Human ("Lydia", "Baelen", 23 );
		Human smeag = new Human ("Smeagol", "Gollem", 600 );
		Human smurf = new Human ("Grote", "Smurf", 542 );
		Human piotr = new Human ("Piotr", "Wiwczarek", 54 );

		Human[] people = { lydia, smeag, smurf, piotr };
		
		Link[] b = new Link[4];

		b[0] = new Link("haat", LocalDate.of(2005, 2, 12));
		b[1] = new Link("is verliefd op", LocalDate.of(2006, 2, 14));
		b[2] = new Link("vergaderd met", LocalDate.of(2007, 9, 23));
		b[3] = new Link("is de vader van", LocalDate.of(2008, 4, 25)); 

		Ontology<Human, Link> ontHumans = new Ontology<>(people);

		ontHumans.makeLink(people[0], people[2], b[3]);
		ontHumans.makeLink(people[1], people[2], b[3]);
		ontHumans.makeLink(people[1], people[1], b[0]);
		ontHumans.makeLink(people[1], people[3], b[2]);
		ontHumans.makeLink(people[3], people[2], b[1]);

		System.out.print(ontHumans);
	}
}