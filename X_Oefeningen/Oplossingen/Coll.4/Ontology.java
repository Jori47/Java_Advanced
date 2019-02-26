package be.ap.javadv.collections;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Triplet;

public class Ontology<T, U extends Link> {
	private T[] objects;
	private List<Triplet<T, T, U>> links = new ArrayList<>();

	public Ontology(T[] objects) {
		this.objects = objects;
	}

	public void makeLink(T A, T B, U L) {
		this.links.add(Triplet.with(A, B, L));
	}

	@Override
	public String toString() {
		String ontString = "Ontologie van " + objects[0].getClass().getSimpleName() + ":\n";

		for (Triplet<T, T, U> link : this.links) {
			ontString += String.format("%s ——— %s ———> %s (since %s)\n", link.getValue0(),
					link.getValue2().getDescription(), link.getValue1(), link.getValue2().getStartDate());
		}

		ontString += "----------------------------\n";

		return ontString;
	}
}