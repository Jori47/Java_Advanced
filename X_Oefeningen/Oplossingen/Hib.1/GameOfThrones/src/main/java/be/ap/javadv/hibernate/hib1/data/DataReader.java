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
