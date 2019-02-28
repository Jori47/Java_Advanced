package be.ap.javadv.annotations;

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;

@Migration ( Group.JAVA_CORE_TEAM )
@Migration ( Group.JAVA_CORE_TEAM, phase=3, allowedExceptions =
	{IllegalClassFormatException.class, IOException.class},
	comment="Could be moved to phase 2 if we decide to implement EARTA.")
public class MigrationTest {
	public static void main(String[] args) {
		System.out.println("Hello World");
	}
}