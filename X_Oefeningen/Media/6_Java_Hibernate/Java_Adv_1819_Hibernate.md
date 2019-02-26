# JAVA Advanced (31089/1607/1819/1/72)
## Hibernate
### Inleiding

<img alt="Hibernate logo" width="200px" src="http://hibernate.org/images/hibernate-logo.svg">

Hibernate is een ORM tool waarvan de ontwikkeling begon in 2001 en later gestuurd werd door [Red Hat](https://nl.wikipedia.org/wiki/Red_Hat). Het dient om een relationele databank te koppelen met klassen waarop rechtreeks de business logica geprogrammeerd kan worden. Het framework verzorgt de data typering, data toegangsfuncties (getters) en query management.

Merk op dat we hier geen gebruik gaan maken van de JPA (een soort interface om van ORM te kunnen switchen). Er wel nog steeds nood aan een persistent object dat als interface zal dienen tussen Hibernate en de toepassing. De figuur hieronder laat zien hoe dat Hibernate op zijn beurt via JDBC geïnterfaced is met een databank.

![Hibernate architecture](Media/Hibernate_architecture.png)

Volg onderstaande stappen om een Hibernate toepassing klaar te zetten. Na deze stappen gaan we iets dieper in op de het gebruik van de toepassing.

### Installatie Maven

Installeer indien nodig [Maven](http://maven.apache.org/) en de VS Code extensie `Maven for Java`. Als je Maven nog niet kent, kan je een Maven archetype uitproberen met de VS Code opdracht  `Maven: Generate from Maven archetype`.

![Maven archetype](Media/Maven_Archetype.jpg)

Om de Maven commando's uit te voeren gebruik je de opdracht `Maven Execute Commands` of gebruik je de rechter muisknop op het juiste project in de `MAVEN PROJECTS` sidebar.

### Stel dependencies in via Maven

Kopieer je de artefacten [hibernate-core](https://search.maven.org/artifact/org.hibernate/hibernate-core/5.4.1.Final/jar) uit de [Maven Central Repository](http://repo.maven.apache.org/maven2/) en [mssql-jdbc](https://docs.microsoft.com/en-us/sql/connect/jdbc/download-microsoft-jdbc-driver-for-sql-server?view=sql-server-2017) van Microsoft naar jouw `pom.xml` bestand. Druk indien nodig op de ververs-knop op de `JAVA DEPENDENCIES` sidebar:

![Refresh Dependencies](Media/Refresh_Dependencie.jpg)

Controleer bij problemen jouw `.classpath` bestand in de project root folder om na te gaan dat deze automatisch werd aangevuld.

### Aanmaken van een databank

De data die we voor deze demo gebruiken is afkomstig van een Cannabis dataset van Kaggle ([Cannabis Lineage, Growth, and Psych. Effects](https://www.kaggle.com/tictactouka/cannabis)). Hier maken we gebruik van een HSQL databank en de DBeaver manager, maar je bent vrij om andere databanken te gebruiken. Hier is de gebruikte artefact voor HSQL:

```xml
<dependency>
	<groupId>org.hsqldb</groupId>
	<artifactId>hsqldb</artifactId>
	<version>2.4.1</version>
</dependency>
```

Hieronder is het schema (MSSQL):

![MS_SQL](Media/DB_MSSQL.jpg)

Begin met enkel de `Breeder` tabel aan te maken. Zorg ervoor dat de id self-incrementing is. De rest van het data model kan je later aanvullen.

### Persistente Entiteiten

Maak een persistente Java klasse `Breeder` aan (zie figuur met architectuur hierboven) die de overeenkomstige entiteit in het data domein moet voorstellen. Gebruik dezelfde naamgeving en data typering als op de databank:

```java
package be.ap.javadv.hibernate.domain;

public class Breeder {
	private int id;
	private String name;
	private String logo;

	// Een constructor zonder argumenten is noodzakelijk 
	public Breeder() {
		super();
	}

	public int getId() {
		return id;
	}

	private void setId(int id) {
		this.id = id;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
}
```

Zoals de commentaar al aangeeft, moet je er voor zorgen dat de constructor argument-loos blijft. Dit heeft natuurlijk alles te maken met het feit dat het Hibernate framework (via *reflection*) zelf deze klasse zal instantiëren.

### Mapping

Voor de entiteit `Breeder` moet je nu nog een mapper schrijven. In dit voorbeeld gaan we ervan uit dat je inderdaad de naamgeving tussen Java en de databank heb weten synchroniseren. De mapper is hier vrij eenvoudig en ziet er voorlopig zo uit:

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
	"-//Hibernate/Hibernate Mapping DTD 3.0//EN"
	"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping package="be.ap.javadv.hibernate.domain">
	<class name="Breeder" table="Breeder">
		<id name="id" column="id">
			<generator class="native"/>
		</id>
		<property name="name" column="name"/>
		<property name="logo" column="logo"/>
	</class>
</hibernate-mapping>
```

De bovenstaande code bewaar je in het bestand `Breeder.hbm.xml` en plaatst dit in de folder samen met `Breeder.java`. Merk op dat de mapping zowel de Java klasse koppelt aan de tabel als de individuele velden. Je kan heel wat toevoegen (vnl data typering) en je kan hier en daar ook dingen weglaten (zoals de `column` attributen indien gelijknamig) en dan zal het Hibernate framework zelf proberen het nodige aan te vullen.

### Het Hibernate configuratie bestand

Maak het Hibernate configuratie bestand aan. Nu moet de databank nog gekoppeld worden. Dit doe je door een bestand te creëren met de naam `hibernate.cfg.xml` in de `src/main/resources` folder met de volgende inhoud:

```xml
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
	<session-factory>
		<property name="dialect">org.hibernate.dialect.HSQLDialect</property>
		<property name="hibernate.connection.driver_class">org.hsqldb.jdbcDriver</property>
		<property name="hibernate.connection.url">jdbc:hsqldb:hsql://localhost/testdb</property>
		<property name="hibernate.connection.username">sa</property>
		<property name="hibernate.connection.password"></property>
		<property name="current_session_context_class">thread</property>
		<property name="connection.pool_size">1</property>
		<property name="cache.provider_class">org.hibernate.cache.internal.NoCacheProvider</property>
		<property name="show_sql">true</property>
		<property name="hbm2ddl.auto">update</property>
		<mapping resource="be/ap/javadv/hibernate/domain/Breeder.hbm.xml"/>
	</session-factory>
</hibernate-configuration>
```

Je vind hier inderdaad de nodige informatie om te verbinden met de databank via de JDBC connector. De laatste lijn binnen het <session-factory> element verwijst naar de mapping die we daarnet hebben gedefinieerd (je kan ook met de `class` attribuut werken ipv de `resource` attribuut).

### Entry point

Maak een entry point voor de toepassing. Hierin zal ook een `createAndStoreBreeder` methode worden aangemaakt en zal er een nieuwe sessie en transactie naar de databank gestart worden op basis van de eerder gedefinieerde configuratie in `hibernate.cfg.xml`.

```java
package be.ap.javadv;

import org.hibernate.Session;

import be.ap.javadv.hibernate.domain.Breeder;
import be.ap.javadv.hibernate.utils.HibernateUtil;

public class Program {
	public static void main(String[] args) {
		Program prg = new Program();

		if (args[0].equals("store")) {
			prg.createAndStoreBreeder("Breeder Name", "Breeder logo");
		}

		HibernateUtil.getSessionFactory().close();
	}

	private void createAndStoreBreeder(String name, String logo) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Breeder breeder = new Breeder();
		breeder.setName(name);
		breeder.setLogo(logo);

		session.save(breeder);

		session.getTransaction().commit();
	}
}
```

Deze code hangt af van een zelf-geschreven session factory:

```java
package be.ap.javadv.hibernate.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			return new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
```

Nu kan je het project compileren. Starten kan je ook manueel:

```sh
mvn exec:java -Dexec.mainClass="be.ap.javadv.Program" -Dexec.args="store"
```

Als alles goed gaat, zie je iets als het volgende ergens onderaan in de output:

```sh
Hibernate: insert into Breeder (id, name, logo) values (default, ?, ?)
```

en zie je een rij toegevoegd in de databank.

### HQL

Merk op dat je in bovenstaande code geen SQL hebt moeten schrijven. De methode `save` op `session` heeft er in de achtergrond voor gezorgd dat de juiste query werd aangeroepen. Deze methode gaat niet altijd lukken. Soms moet je SQL gebruiken. Om toch de queries te vereenvoudigen (SQL-ofobie?) heeft Hibernate een soort tussentaal ontwikkeld: HQL. Hieronder zie je de implementatie voor het opsommen van de Breeders in de databank (zonder filter):

```java
if (args[0].equals("store")) {
	prg.createAndStoreBreeder("Breeder Name", "Breeder logo");
} else if (args[0].equals("list")) {
	List<Breeder> breeders = prg.listBreeders();
	for (Breeder breeder : breeders) {
		System.out.println(breeder);
	}
}
// [...]
private List<Breeder> listBreeders() {
	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	session.beginTransaction();
	List<Breeder> result = session.createQuery("from Breeder").list();
	session.getTransaction().commit();
	return result;
}
```

Te starten met de opdracht:

```sh
mvn exec:java -Dexec.mainClass="be.ap.javadv.Program" -Dexec.args="list"
```

Je hebt nu genoeg info om de persistente klassen af te werken en individuele tabellen te query-en.

### Associaties

### Querying
### Java-Realisme

Als je goed kijkt naar het architectuurschema dat we eerder introduceerden, zie je een aantal eigenaardigheden:

![Hibernate architecture](Media/Hibernate_architecture.png)

Denk eraan dat het doel is om Java applicatie te koppelen aan een databank. Als je twee dingen aan elkaar wil koppelen, heb je een interface nodig. Maar in plaats daarvan staat hier een volledig uitgebouwd framework tussen (Hibernate). Handig, toch? Behalve dat je nu 2 interfaces nodig hebt (oranje in bovenstaande figuur). En met interfaces moet je zuinig zijn. Ze bieden meer flexibiliteit maar dat gaat bijna altijd ten koste van onderhoudbaarheid.

Bovendien zie je daar configuratie en mapping staan. Bijna altijd als je deze woorden ziet staan, wees dan op je hoede want je bent het domein van OOP aan het verlaten. Configuratie is alles wat OOP niet is. Mapping is niet veel beter. De hele bedoeling van objecten is het niet nodig hebben van mappings. Je kan het niet vermijden als je vertrekt van een set tekst bestanden, maar eens in een databank moet het OO karakter gehandhaafd. Mappings en serialisaties en dergelijke mogen eigenlijk niet tot het domein van de software ontwikkelaar behoren.

![Hibernate architecture](Media/Trends.jpg)

Nieuwe frameworks komen snel.... en in het begin gaat alles goed.  Na een tijdje beginnen de eerste scheurtjes in het framework op te vallen en voor je het weet ben je in een programeerhel terecht gekomen waar geen ontsnappen meer aan is. De evolutie van "Waw, wat een cool framework!" naar "Hoe hebben we daar ooit mee kunnen en willen werken?" gaat steeds sneller en je kan maar best op de uitkijk staan wanneer iemand met een "Waw" komt pronken. Werp dan een genuanceerd op het nieuwe framework! Dat is Hibernate nu ook vergaan:

### Termen

| Term | Betekenis                  |
|------|----------------------------|
| CT   | Compile-time               |
| GC   | Garbage Collector          |
| FIFO | First in, first out        |
| HQL  | Hibernate Query Language   |
| JDBC | Java DataBase Connectivity |
| JPA  | Java Persistence API       |
| JVM  | Java Virtual Machine       |
| LIFO | Last in, first out         |
| OO   | Object georiënteerd        |
| ORM  | Object-relational mapping  |

### On-line

- [Deze video reeks](https://www.youtube.com/watch?v=0hm3QidFwYY&list=PLEAQNNR8IlB7fNkRsUgzrR346i-UqE5CG) is een goed begin. Het gaat een beetje traag, maar het wordt goed uitgelegd en de spreker is aangenaam om naar te luisteren.
- [Een interessant artikel](https://www.yegor256.com/2016/04/12/java-annotations-are-evil.html) over het gebruik van annotaties.
- [Officiele Hibernate](https://docs.jboss.org/hibernate/orm/4.0/manual/en-US/html/tutorial.html#tutorial-associations) tutorial.
- Hier vind je nogal kritische blik op Hibernate:

<iframe src="https://player.vimeo.com/video/28885655?color=ff9933&portrait=0" width="640" height="204" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>

### Referenties

|              | Literatuur                                                                                                                                                      |
|--------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| <sup>1</sup> | Sharan K (2014) Beginning Java 8 Fundamentals: Language Syntax, Arrays, Data Types, Objects, and Regular Expressions. Apress. ISBN: 978-1-4302-6653-2.          |
| <sup>2</sup> | Sharan K (2014) Beginning Java 8 Language Features: Lambda Expressions, Inner Classes, Threads, I/O, Collections, and Streams. Apress. ISBN: 978-1-4302-6659-4. |
| <sup>3</sup> | Sharan K (2014) Beginning Java 8 APIs, Extensions and Libraries: Swing, JavaFX, JavaScript, JDBC and Network Programming APIs. Apress. ISBN: 978-1-4302-6662-4. |