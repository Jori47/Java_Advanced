package be.ap.javadv.innerclasses;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonList<T extends Person> {
	public Set<T> personList = new HashSet<>();

	public PersonList() {
		super();
	}

	public PersonList(Set<T> personList) {
		this.personList=personList;
	}

	public PersonList(String[] names) {
		this.addMembers(names);
	}

	public Set<T> getMembers (){
		return this.personList;
	}

	public T getMember (String name){
		for (T member : this.personList) {
			if (member.toString().equals(name)){
				return member;
			}
		}
		return null;
	}

	public Set<T> addMembers (String[] names){
		Set<T> newMembers = new HashSet<>();

		for (String name : names) {
			Person person = new Person(name);
			T newMember = (T)person;
			newMembers.add(newMember);
			this.addMember(newMember);
		}

		return newMembers;
	}

	public void addMembers (Set<T> newMembers){
		this.personList.addAll(newMembers);
	}
	
	public void addMember (T newMember){
		this.personList.add(newMember);
	}

	@Override
	public String toString() {
		return this.personList
			.stream()
			.map(n -> n.toString())
			.collect(Collectors.joining(", "));
	}
}