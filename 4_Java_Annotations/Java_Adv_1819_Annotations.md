# JAVA Advanced (31089/1607/1819/1/72)
## Annotations
### Inleiding

Een annotatie (*annotation*) is een soort notitie die aan elementen van een Java-programma gekoppeld kan worden. Een bekend voorbeeld inmiddels is de `@Override` annotatie:

```java
@Override
public String toString() {
	return titleList.toString();
}
```

Hier zijn er een aantal andere:

```java
@Deprecated
@Override
@SuppressWarnings
@FunctionalInterface
```

Het moet duidelijk zijn dat in principe annotaties alleen maar meta-informatie draagt maar aangezien Java platformen waaronder het Spring Boot framework op de correctheid van deze annotaties vertrouwen kunnen ze oprechtreeks een impact hebben op de werking van de code. Dit zijn de elementen waaraan een annotatie gekoppeld kan worden:

- pakket
- klasse
- interface
- veld
- lokale variabele
- methode
- parameter
- enumeratie
- type parameter
- annotatie

Overal waar een type gebruikt wordt, kan er een annotatie aan gekoppeld worden. Het is opmerkelijk dat annotaties zelf geannoteerd kunnen worden. Inderdaad Annotaties zijn ook types. Meer bepaald: annotaties zijn een speciale vorm van interface. Ze zijn speciaal in de zin dat ze verplicht en impliciet van de `java.lang.annotation.Annotation` interface overerven en van niets anders mogen overerven. Het is goed om deze interface eens op te zoeken in de documentatie.

### Declareren van een annotatie

Een nieuwe annotatie declareren doe je volgens dit schema:

```java
<toegankelijkheid> @interface <annotatie-type-naam> {
	// Annotatie inhoud hier
}
```

,waarbij de toegankelijkheid ofwel `public` of wel package-level is. Hier is een voorbeeld:

```java
package be.ap.javadv.annotaties;

public @interface Version {
	int major();
	int minor();
}
```

Je ziet dat de syntax hetzelfde is als voor een reguliere interface op het `@`-symbool na. De methoden mogen echter geen `throws exception` clausule hebben, geen argumenten hebben en mogen enkel deze typen teruggeven:

- Primitieve types
- java.lang.String
- java.lang.Class
- Een enumeratie type
- Een annotatie type
- Een 1-dimensionale array van bovenstaande types

Hier zie je hoe je `java.lang.Class` kan gebruiken:

```java
public @interface GoodOne {
	Class element1(); // Elk type klasse
	Class<Test> element2(); // Enkel Test type
	Class<? extends Test> element3(); // Test of een subklasse ervan
}
```

### Aanroepen van een annotatie

De bovenstaande annotatie `Version` kan als volgt aangeroepen worden:

```java
@Version (major=1, minor=0)
public class MyClass{
	@Version (major=1, minor=22)
	public void () myMethod ( @Version (major=1, minor=22) String myArg)
}
```

Merk op dat de annotatie in feite een verkorte vorm is voor het uitvoeren van volgende taken:

- Creëren van een klasse die de interface implementeert
- Instantiëren van die klasse
- Het zetten van de waarden voor de elementen (hier `minor` en `major`)
- Het koppelen van de annotatie-instantie met een Java element

### Standaardwaarden

Je kan opgeven dat een element van de annotatie een standaard waarde gebruik indien de gebruiker dat element niet opgeeft:

```java
public @interface Version {
	int major();
	int minor() default 0;
}

// Aanroepen
@Version(major=1) // minor = 0
```

### Verkorte schrijfwijze

Om een annotatie te maken zoals `@Override` waarbij geen haakjes nodig zijn of waarbij je de parameternaam niet hoeft op te geven, declareer je deze met allemaal elementen die een standaardwaarde hebben (voorbeeld A hieronder) of waarbij van de één elementen geen standaardwaarde heeft maar dan wel de vaste naam `value` draagt (voorbeeld B hieronder):

```java
// Voorbeeld A
public @interface Enabled {
	boolean status() default true;
}

// Gebruik
@Enabled
public class Test {}
```

```java
// Voorbeeld B
public @interface A {
	String value();
	int id() default 10;
}

// Gebruik
@A("Hello")
public class Test {}
```

### Fun with flags

Een annotatie die enkel als 'flag' dient noemt men een 'marker annotatie', kan je zo gebruiken:

```java
public @interface Marker {}

// Gebruik
@Marker
public class Test {}
```

### Meta-annotaties

Meta-annotaties zijn annotaties op annotaties. Hier is een voorbeeld van listing 1.6 op p.13 $^2$:

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TestCase {
	Class<? extends Throwable> willThrow() default DefaultException.class;
}
```

De `TestCase` interface dient om testscenario's uit te werken op bestaande Java code-elementen. De `DefaultException` klasse ziet er zo uit:

```java
public class DefaultException extends java.lang.Throwable {
	public DefaultException() {}

	public DefaultException(String msg) {
		super(msg);
	}
}
```

De `@Retention` meta-annotatie geeft aan wanneer het annotatie type beschikbaar wordt gemaakt, in dit geval is dat tijdens runtime. De `@Target` meta-annotatie beperkt het gebruik van de annotatie enkel voor bij methoden. 

Hier is een lijstje met de meest populaire meta-annotaties:

```
Retention
Target
Inherited
Documented
Repeatable
```

De `Inherited` annotatie is een marker meta-annotatie type dat aangeeft of subklassen van de gemarkeerde klassen ook de annotatie overerven. `Documented` stuurt de Javadoc tool aan en `Repeatable` geeft aan of de annotatie mag herhaald worden.

> Oef Ann.1

> Oef Ann.2

### Termen

| Term | Betekenis            |
|------|----------------------|
| CT   | Compile-time         |
| GC   | Garbage Collector    |
| FIFO | First in, first out  |
| JVM  | Java Virtual Machine |
| LIFO | Last in, first out   |
| OO   | Object georiënteerd  |

### Referenties

||Literatuur|
|-|-|
|<sup>1</sup>|Sharan K (2014) Beginning Java 8 Fundamentals: Language Syntax, Arrays, Data Types, Objects, and Regular Expressions. Apress. ISBN: 978-1-4302-6653-2.|
|<sup>2</sup>|Sharan K (2014) Beginning Java 8 Language Features: Lambda Expressions, Inner Classes, Threads, I/O, Collections, and Streams. Apress. ISBN: 978-1-4302-6659-4.|
|<sup>3</sup>|Sharan K (2014) Beginning Java 8 APIs, Extensions and Libraries: Swing, JavaFX, JavaScript, JDBC and Network Programming APIs. Apress. ISBN: 978-1-4302-6662-4.|