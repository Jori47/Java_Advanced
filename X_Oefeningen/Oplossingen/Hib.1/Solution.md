# Solution of Hib.1
## Step 1 - Create new Hibernate project

Create new Maven project with the name `Game_Of_Thrones` and use this name of the `artifactId` and the `groupId` `be.ap.javadv`. Set the version of the Maven compiler:

```xml
<maven.compiler.source>1.8</maven.compiler.source>
<maven.compiler.target>1.8</maven.compiler.target>
```

and set these two dependencies for Hibernate and for the Microsoft SQL server connector:

```xml
<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-core</artifactId>
	<version>5.4.1.Final</version>
</dependency>
<dependency>
	<groupId>com.microsoft.sqlserver</groupId>
	<artifactId>mssql-jdbc</artifactId>
	<version>6.1.0.jre8</version>
</dependency>
```

Make sure you have the folder structure `be/ap/javadv/hibernate/hib1` under the `java` folder and declare the main method in `Program.java`. Now, compile and execute you project with this line:

```sh
mvn exec:java -Dexec.mainClass="be.ap.javadv.hibernate.hib1.Program"
```

## Step 2 - Set up Hibernate

Create a file `hibernate.cfg.xml` and place under the `/main/resources` folder. Add the following content:

```xml
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN" "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
	<session-factory>
		<property name="dialect">org.hibernate.dialect.SQLServerDialect</property>
		<property name="hibernate.connection.driver_class">com.microsoft.sqlserver.jdbc.SQLServerDriver</property>
		<property name="hibernate.connection.url">jdbc:sqlserver://localhost</property>
		<property name="hibernate.connection.DatabaseName">GAMEOFTHRONES</property>
		<property name="hibernate.connection.username">YOUR_USER_HERE</property>
		<property name="hibernate.connection.password">YOUR_PASSWORD_HERE</property>

		<property name="current_session_context_class">thread</property>
		<property name="connection.pool_size">1</property>
		<property name="cache.provider_class">org.hibernate.cache.internal.NoCacheProvider</property>
		<property name="show_sql">true</property>

		<mapping resource="be/ap/javadv/hibernate/hib1/domain/House.hbm.xml"/>
	</session-factory>
</hibernate-configuration>
```

## Step 3 - Create Entities

In the folder `hib1/domain` you add `House.java`:

```java
package be.ap.javadv.hibernate.hib1.domain;

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
}
```

and the corresponding mapper (`House.hbm.xml`):

```xml
<?xml version="1.0"?>
<!DOCTYPE hibernate-mapping PUBLIC
	"-//Hibernate/Hibernate Mapping DTD 3.0//EN"
	"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping package="be.ap.javadv.hibernate.domain">
	<class name="House">
		<id name="name"/>
	</class>
</hibernate-mapping>
```

## Step 4 - Prepare the database

Prepare the database as indicated in the exercise using the following script

```sql
USE MASTER 
DROP DATABASE IF EXISTS GAMEOFTHRONES
CREATE DATABASE GAMEOFTHRONES
GO

USE GAMEOFTHRONES

CREATE TABLE HOUSE(
	[NAME] VARCHAR(255) NOT NULL,
	CONSTRAINT PK_HOUSE PRIMARY KEY ([NAME]))
CREATE TABLE BATTLE(
	[NAME] VARCHAR(255) NOT NULL,
	[YEAR] INT NULL,
	[TYPE] VARCHAR(14) NULL,
	CONSTRAINT PK_BATTLE PRIMARY KEY ([NAME]))
CREATE TABLE CHARACTER(
	[NAME] VARCHAR(255) NOT NULL,
	DEATH_YEAR INT NULL,
	GENDER CHAR(1) NULL,
	NOBILITY BIT NULL,
	CONSTRAINT PK_CHARACTER PRIMARY KEY ([NAME]))
CREATE TABLE ALLEGIANCE(
	HOUSE VARCHAR(255) NOT NULL,
	[CHARACTER] VARCHAR(255) NOT NULL,
	CONSTRAINT PK_ALLEGIANCE PRIMARY KEY (HOUSE, [CHARACTER]))
CREATE TABLE SIDE(
	ID INT NOT NULL,
	BATTLE VARCHAR(255) NOT NULL,
	HOUSE VARCHAR(255) NOT NULL,
	[TYPE] CHAR(3) NULL,
	OUTCOME VARCHAR(4) NULL,
	CONSTRAINT PK_SIDE PRIMARY KEY (ID))
CREATE TABLE COMMAND(
	SIDE INT NOT NULL,
	[CHARACTER] VARCHAR(255) NOT NULL,
	CONSTRAINT PK_COMMAND PRIMARY KEY (SIDE, [CHARACTER]))
GO

ALTER TABLE ALLEGIANCE ADD CONSTRAINT FK_ALLEGIANCE_CHARACTER FOREIGN KEY([CHARACTER])
	REFERENCES [CHARACTER] ([NAME])
ALTER TABLE ALLEGIANCE ADD CONSTRAINT FK_ALLEGIANCE_HOUSE FOREIGN KEY(HOUSE)
	REFERENCES HOUSE ([NAME])
ALTER TABLE SIDE ADD CONSTRAINT FK_SIDE_BATTLE FOREIGN KEY(BATTLE)
	REFERENCES BATTLE ([NAME])
ALTER TABLE SIDE ADD CONSTRAINT FK_SIDE_HOUSE FOREIGN KEY(HOUSE)
	REFERENCES HOUSE ([NAME])
ALTER TABLE COMMAND ADD CONSTRAINT FK_COMMAND_SIDE FOREIGN KEY(SIDE)
	REFERENCES SIDE (ID)
ALTER TABLE COMMAND ADD CONSTRAINT FK_COMMAND_CHARACTER FOREIGN KEY([CHARACTER])
	REFERENCES [CHARACTER] ([NAME])
GO
```

## Step 5 - Test Database Connectivity

In the `hib1/utils` folder, add the `HibernateUtil` class:

```java
package be.ap.javadv.hibernate.hib1.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
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

In the main method, try out the following:

```java
Session session = HibernateUtil.getSessionFactory().getCurrentSession();
session.beginTransaction();

House house = new House("test");

session.save(house);
session.getTransaction().commit();
```

Check that the house has been added to the database.

## Step 6 - Get the data

Download the "Game of Thrones" dataset from [Kaggle](https://www.kaggle.com/datasets). Keep the site open, you'll need to access the 'dictionary', the metadata such as the description of the columns. Extract the`battles.csv` and `character-deaths.csv` in the folder `be/ap/javadv/hibernate/hib1/data`.

## Step 7 - Write a DataReader class

First add the dependency for a CSV reader:

```xml
<dependency>
	<groupId>com.opencsv</groupId>
	<artifactId>opencsv</artifactId>
	<version>3.8</version>
</dependency>
```

And here is the class:

```java
package be.ap.javadv.hibernate.hib1.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;

public class DataReader {
	CSVReader battleReader;
	CSVReader characterReader;

	private CSVReader getFile(String fileName) throws FileNotFoundException {
		String fullName = getClass().getResource(fileName).getPath();
		return new CSVReader(new FileReader(fullName));
	}

	public DataReader() {
		try {
			this.battleReader = getFile("battles.csv");
			this.characterReader = getFile("character-deaths.csv");
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}

	public String[] NextBattle() throws IOException {
		return this.battleReader.readNext();
	}

	public String[] NextCharacter() throws IOException {
		return this.characterReader.readNext();
	}
}
```

You can easily test the above code with something like this:

```java
try {
	DataReader dataReader = new DataReader();
	String[] battleHeader = dataReader.NextBattle();
	String[] battle = battleHeader;

	while (battle != null) {
		for (int index = 0; index < battle.length; index++) {
			System.out.print(battleHeader[index] + ": " + battle[index] + "|");
		}
		System.out.println();
		battle = dataReader.NextBattle();
	}
} catch (IOException e) {
	System.out.println(e);
}
```

## Step 7 - Parse data

Create the other entities. As the data is limited in size we will first generate collections of our entities and then loop over the collection to insert them. By using sets you avoid to have to check wether an element already exists, but remember to override the `hashCode` and `equals` methods:

```java
Set<House> houses = new HashSet<>();

try {
	// Based on string array of fields charDat for current record
	//  and on the header names in charHeader, create entity from
	//  data extracted from record.
	House house = new House(charDat[i(charHeader, "Allegiances")]);

	// Some inconsistencies in de DB regarding naming of Houses
	house.setName(house.getName().replace("House ", ""));
	houses.add(house);
} catch (IOException e) {
	System.err.println(e);
}

// Now  we insert into DB	
Session session = HibernateUtil.getSessionFactory().getCurrentSession();
session.beginTransaction();
for (House house : houses) session.save(house);
session.getTransaction().commit();
```

where `i` is a small utility function to check the index of a column based on its name.

## The Queries

The queries to solve the exercises were clearly quite complex. Of course there are many ways to solve this but the simplest is probably this: Write a HQL query directly. The advantage is that no extra relations need to be configured as, in this case, we rely on the DB to check all constraints:

```java
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
```
 The output should be something similar to this:

```
------------
QUESTION 1
Arys Oakheart: 9
Falyse Stokeworth: 9
Gregor Clegane: 9
Gyles Rosby: 9
Joffrey Baratheon: 9
Kevan Lannister: 9
Pycelle: 9
Senelle: 9
Shae: 9
Tanda Stokeworth: 9

[...]
------------
QUESTION 2
ambush: 13
pitched battle: 8
siege: 5
```

That's it! Hope you learned one ar two things from this exercise.