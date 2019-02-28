package be.ap.javadv.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Repeatable(Migrations.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Migration {
	Group value();
	int phase() default 1;
	Class<? extends Throwable>[] allowedExceptions() default {Exception.class};

	String comment() default "";
}

// Zorg er verder voor dat dit type tijdens runtime gelezen kan worden en dat
// het enkel op methoden en klassen kan gebruikt worden. Probeer het dan eens
// uit op een ander element en observeer de melding die er gegeven wordt.