# JAVA Advanced (31089/1607/1819/1/72)
## Lambda Expressies
### Inleiding

Een lambda expressie (*lambda* of *Lambda expression*) is een instantie van een functionele interface (*functional interface*), i.e. een interface met slechts één abstracte methode, die zich gedraagt als een anonieme functie:

 ```java
@FunctionalInterface
interface StringToIntMapper {
	int map(String str);
}

StringToIntMapper mapper = (String str) -> str.length();
```

De compiler controleert dat de in- and outs van de lambda overeenkomt met die van de `map` methode van de `StringToIntMapper` interface.
De speciale verkorte schrijfwijze, die symbool staat voor de instantiëring van de functionele interface, kan als volgt worden samengevat:

```java
(<param list>) -> {<lambda body>}
```

 Voorbeelden van lambda expressies zijn:

 ```java
 // Incrementeer
x -> x + 1

 // Maximum van twee gehele getallen
(int x, int y) -> {
	int max = x > y ? x : y;
	return max;
}
 ```

De eerste van de expressies hierboven staat bekend als een impliciet lambda (*implicit lambda* of voluit *implicitly-typed lambda expression*), de tweede is een expliciete lambda (*explicit lambda expression*). Merk op dat er verkorter schrijfwijzen bestaan waarin haakjes niet meer nodig zijn en dat de compiler uit de context zelf een heleboel probeert af te leiden zoals het return type (*target typing*), parameter types en dergelijke. Het zal een CT foutmelding geven wanneer het die informatie niet uit de context kan afleiden.

[*Verdieping*] Een lambda body met een typering dat afhangt van de context noemt men een *poly expression* en de context noemt men de *poly context*. Een expressie waarbij het type gekend is noemt men een *standalone expression*.

### Generieke functionele interface

Hieronder een voorbeeld:

```java
package be.ap.javadv.lambdas;

@FunctionalInterface
public interface Mapper<T> {
	int map(T source);
	public static <U> int[] mapToInt(U[] list, Mapper<? super U> mapper) {
		int[] mappedValues = new int[list.length];

		for (int i = 0; i < list.length; i++) {
			mappedValues[i] = mapper.map(list[i]);
		}

		return mappedValues;
	}
}
```
```java
public class MapperTest {
	public static void main(String[] args) {
		System.out.println("Mapping names to their lengths:");
		String[] names = {"David", "Li", "Doug"};
		int[] lengthMapping = Mapper.mapToInt(names, (String name) -> name.length());
		printMapping(names, lengthMapping);
		System.out.println("\nMapping integers to their squares:");
		Integer[] numbers = {7, 3, 67};
		int[] countMapping = Mapper.mapToInt(numbers, (Integer n) -> n * n);
		printMapping(numbers, countMapping);
	}
	public static void printMapping(Object[] from, int[] to) {
		for(int i = 0; i < from.length; i++) {
			System.out.println(from[i] + " mapped to " + to[i]);
		}
	}
}
```

<div style="color:orange;">

#### Deep dive

Laten we het bovenstaande code eens grondig onderzoeken. We beginnen met de opdracht

```java
int[] lengthMapping = Mapper.mapToInt(&hellip;);
```

Een mapper dient om objecten van type `A` om te zetten naar objecten van type `B`. We willen niet enkel zo een mapper definiëren maar ook meteen uitvoeren (EN: to *apply* the lambda). Als we deze code volgen staat er in feite dat er een statische methode `mapToInt` op interface `Mapper` wordt uitgevoerd en het resultaat in `lengthMapping` wordt geplaatst. We weten trouwens dat het om een statische methode moet zijn omdat we deze vanaf de interface wordt aanroepen (`Mapper` met hoofdletter!) en niet van een object dat de interface implementeert. Wacht eens even&hellip; een statische methode op een interface? Ja, het kan! Vanaf Java 8 kan je een statische methode op een interface hebben als 1. deze is uitgeschreven is (i.e. niet abstract is) en public staat!

We willen nu de actuele parameters voor de methode opgeven, maar wat is de signatuur (*signature*) van `mapToInt`? Alvorens we dat onderzoeken moeten we een kijkje namen naar de interface:

```java
@FunctionalInterface
public interface Mapper<T> {
	int map(T source);
}
```

Om te beginnen zien we dat de interface generiek is. Een Functionele interface heeft per definitie slechts één (abstracte) methode en hier is dat `map`. Daarmee weten we dat onze lambda expressie een mapper is van `T` naar `int`, waarbij `T` dus zelf in te vullen is. Het kan dus van `String` -> `int` omzetten of bijvoorbeeld van `Person` -> `int` omzetten. OK, nu terug naar de `mapToInt` methode:

```java
public static <U> int[] mapToInt(U[] list, Mapper<? super U> mapper) {
	...
}
```

Wat? Misschien even vereenvoudigen:

```java
... mapToInt( ...[] list, Mapper ... mapper) { ... }
```

De methode heeft dus 2 formele parameters. De eerste is een array (`[]`), de tweede een instantiëring van de interface. Waarom een array aanvaarden eigenlijk? Omdat we, zo betaamt een goede mapper, tegelijkertijd veel objecten willen kunnen mappen. Bij een `Person` -> `int` mapper, willen we bijvoorbeeld `Person[] randomDudes` kunnen doorgeven. De tweede parameter is dus een `Mapper` naar keuze. Dit is de lambda expressie, de anonieme functie zo je wil. Hier zijn wat fictieve voorbeelden voor deze parameter:

```java
randomDude -> randomDude.friends.size()
randomDude -> randomDude.age()
randomDude -> randomDude.score * 200
```

Maar de eerste parameter is een `Array` van het type `U` (`U[]`) en de tweede is van het type `Mapper<? super U>`. `U` is dus opnieuw een generiek type dat ook vooraan in de declaratie van de methode staat (`public static <U> ...`). Dit type kan, maar moet niet per sé gelijk zijn aan `T`. Waarom is er een tweede generiek type? Het antwoord is dat we een mapper zoals bijvoorbeeld `mapper<Person>` naast `Person[] randomDudes` ook willen kunnen 'voeden' met zoiets als `Student[] randomStudents` of `Lector[] randomLectors`. Dus in feite kunnen we zeggen dat `<? super U>` overeenkomt met `T`. Omdat de methode statisch is, kun je echter niet vanuit de methode signatuur verwijzen naar `T`, anders had je `<U>` kunnen vervangen door `<U extends T>` en `<? super U>` door `<T>`.

Tenslotte kijken we naar de inhoud van `mapToInt`:

```java
int[] mappedValues = new int[list.length];

for (int i = 0; i < list.length; i++) {
	mappedValues[i] = mapper.map(list[i]);
}

return mappedValues;
```

Hier wordt er elk element van de input array `list` aan de mapping onderworpen en het resultaat in een nieuwe array `mappedValues` geplaatst. 
</div>

### Doorsnede types (*Intersection types*)

Hier is een voorbeeld:

```java
public interface Sensitive {}

Sensitive sen = (x, y) -> x + y; // NOK
Sensitive sen = (Sensitive & Adder) (x, y) -> x + y; // OK
```

Zo maak je een lambda serialiseerbaar:

```java
Serializable ser = (Serializable & Adder) (x, y) -> x + y;
```

### Veelgebruikte functionele interfaces

Functionele interface kan je zelf schrijven, maar Java heeft er al een heleboel voor jou klaagezet, hier zijn de meest populaire:

|Naam|Methode|Omschrijving|
|-|-|-|
|Function<T,R>|R apply(T t)|Generieke functie van T->R|
|BiFunction<T,U,R>|R apply(T t, U u)|Functie van {T,U}->R|
|Predicate<T>|boolean test(T t)|Functie om iets te testen, geeft `true` of `false` terug|
|BiPredicate<T,U>|boolean test(T t, U u)|`Predicate` met twee argumenten|
|Consumer<T>|void accept(T t)|Een operatie dat *side effects* vertoont maar niets retourneert|
|BiConsumer<T,U>|void accept(T t, U u)|Consumer met twee argumenten|
|Supplier<T>|T get()|Omgekeerde van een consumer, geen argumenten maar geeft wel iets terug|
|UnaryOperator<T>|T apply(T t)|Is een Function<T,T>|
|BinaryOperator<T>|T apply(T t1, T t2)|Is een BiFunction<T,T,T>|

Sommige uit bovenstaande tabel bestaan in andere versies. Bijvoorbeeld, `IntConsumer` is gespecialiseerd in het afhandelen van gehele getallen. Zoek bijvoorbeeld de termen `Function` en `IntFunction` op in de documentatie.

Hier is nog een voorbeeld van hoe je zulk een functionele interface implementeert:

```java
public class NotesHandler{
	public static <T> void forEach(List<T> list, Consumer<? super T> action) {
		for(T item : list) {
			action.accept(item);
		}
	}
}
```

De methode `forEach` ontvangt een lijst van een voorgedefinieerd type en voert een code uit op elke element van deze lijst. Je roept deze methode aan op deze wijze:

```java
List<Nota> notas = new ArrayList<>();
// Nota's toevoegen
NotesHandler.forEach(notas, nota -> System.out.println(nota));
```

### Chaining

Dankzij een `andThen` methode kan men functies aan elkaar koppelen (*chaining* of *piping*):

```java
// Square the input, add one to the result, and square the result
Function<Long, Long> chainedFunction = 
	((Function<Long, Long>)(x -> x * x))
	.andThen(x -> x + 1)
	.andThen(x -> x * x);
System.out.println(chainedFunction.apply(3L));
```
of nog:

```java
// Create some predicates
Predicate<Integer> divisibleByThree = x -> x % 3 == 0;
Predicate<Integer> divisibleByFive = x -> x % 5 == 0;

Predicate<Integer> divisibleByThreeOrFive = divisibleByThree.or(divisibleByFive);

System.out.println("divisibleByThreeOrFive: " + divisibleByThreeOrFive.test(num));
```

> Oef Lambda.1

### Methode referenties

Een methode referentie (*method reference*) is een tot lambda expressie geconverteerde methode.

```java
// Zonder methode referentie
ToIntFunction<String> lengthFunction = str -> str.length();
Function<Integer, String> func1 = x -> Integer.toBinaryString(x);
BiFunction<Integer, Integer, Integer> func1 = (x, y) -> Integer.sum(x, y);
BiFunction<String,Double, Item> func3 = (name, price) -> new Item(name, price);

// Met methode referentie
ToIntFunction<String> lengthFunction = String::length;
Function<Integer, String> func2 = Integer::toBinaryString;
BiFunction<Integer, Integer, Integer> func2 = Integer::sum;
BiFunction<String,Double, Item> func3 = Item::new;
```

Er zijn meerdere vormen mogelijk (Tabel 5.3 p. 150 $^2$):

|Syntax|Omschrijving|
|-|-|
|TypeName::staticMethod|A method reference to a static method of a class, an interface, or an enum|
|objectRef::instance|Method A method reference to an instance method of the specified object|
|ClassName::instanceMethod|A method reference to an instance method of an arbitrary object of the specified class|
|TypeName.super::instanceMethod|A method reference to an instance method of the supertype of a particular object|
|ClassName::new|A constructor reference to the constructor of the specified class|
|ArrayTypeName::new|An array constructor reference to the constructor of the specified array type|

> Oef Lambda.2

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