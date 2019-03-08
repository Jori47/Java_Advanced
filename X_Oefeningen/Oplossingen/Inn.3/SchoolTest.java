package be.ap.javadv.innerclasses;

import be.ap.javadv.innerclasses.Course.LessonMaterial;
import be.ap.javadv.innerclasses.School.Department;
import be.ap.javadv.innerclasses.School.Department.Education;
import be.ap.javadv.innerclasses.School.Department.Education.Class;
import be.ap.javadv.innerclasses.School.Department.Education.Lab;
import be.ap.javadv.innerclasses.School.Department.Education.LectorCorps;
import be.ap.javadv.innerclasses.School.Department.Education.Class.Group;

// Like all other exercizes, this one is very much dependent on real case scenario,
// so do not worry if your solution is different.

public class SchoolTest {
	public static void main(String[] args) {

		School school = new School("AP");
		Department wetTech = school.addDepartment("Wetenschap en Techniek");

		// Electronica ICT
		Education eICT = wetTech.addEducation("Electronica ICT");
		Lector lectorA = new Lector("Lector A");
		Lector lectorB = new Lector("Lector B");

		LectorCorps eICTCorps = eICT.new LectorCorps();

		eICTCorps.addMember(lectorA);
		eICTCorps.addMember(lectorB);

		Class eICT1 = eICT.addClass("EAICT1");
		Group eICT1A = eICT1.new Group("EAICT1 - A");
		Group eICT1B = eICT1.new Group("EAICT1 - B");

		eICT1A.addMembers(new String[] { "Peter", "Geraldine", "Tuur", "Fatima" });
		eICT1B.addMembers(new String[] { "Sylvie", "Marijke", "Tim", "Chen" });
		eICT1.addGroup(eICT1A);
		eICT1.addGroup(eICT1B);

		Course elektronica = new Course();
		LessonMaterial elek_Lab1_mat = elektronica.new LessonMaterial();
		elek_Lab1_mat.new Slide("Inleiding", "Elektronica is cool!");
		elek_Lab1_mat.new Slide("verdieping", "Elektronica is super cool!");

		LessonMaterial elek_The1_mat = elektronica.new LessonMaterial();
		elek_The1_mat.new Slide("Inleiding", "Elektronica theorie is pijnlijk!");

		Lab elek_Lab1 = eICT.new Lab("Electronics Lab 1",
			eICTCorps.getMember("Lector A"), eICT1, elek_Lab1_mat);
		Lab elek_The1 = eICT.new Lab("Electronics Theory 1",
			eICTCorps.getMember("Lector B"), eICT1A, elek_The1_mat);

		System.out.println(school);
		System.out.println(wetTech);
		System.out.println(eICT);
		System.out.println(eICTCorps);
		System.out.println(eICT1);
		System.out.println(eICT1A);
		System.out.println(elek_Lab1);
		System.out.println(elek_The1);
		System.out.println(elek_Lab1_mat);
	}
}