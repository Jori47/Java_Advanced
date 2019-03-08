package be.ap.javadv.innerclasses;

import java.util.HashSet;
import java.util.Set;

import be.ap.javadv.innerclasses.Course.LessonMaterial;

public class School {
	private String name;
	private Set<Department> departments = new HashSet<>();

	public class Department {
		private String name;
		private Set<Education> educations = new HashSet<>();

		public class Education {
			private String name;
			private Set<Lesson> lessons;
			private LectorCorps lectorCorps;
			private Set<Class> classes = new HashSet<>();

			public class Class extends PersonList<Student> {
				private String name;
				private Set<Group> groups = new HashSet<>();

				public class Group extends Class {
					public Group (String name){
						super(name);
					}

					@Override
					public String toString() {
						return String.format(
							"the Group %s (class %s) with (%d) students:\n(%s)",
							super.name, Class.this.name, this.personList.size(),
							this.personList);
					}
				} // Group

				public Class (String name){
					this.name = name;
				}

				public void addGroup (Group group){
					this.groups.add(group);
					super.addMembers(group.getMembers());
				}

				@Override
				public String toString() {
					return String.format(
						"the class %s of %s with (%d) students:\n(%s)", this.name,
						Education.this, this.personList.size(), this.personList);
				}

			} // School.Department.Education.Class

			public class LectorCorps extends PersonList<Lector>{
				public LectorCorps() {
					Education.this.lectorCorps = this;
				}

				@Override
				public String toString() {
					String lectorStringList = super.toString();
					return String.format(
						"the corps of %s consists of the following lectors:\n%s",
						Education.this, lectorStringList );
				}
			} // School.Department.Education.LectorCorps

			public abstract class Lesson {
				private String title;
				private Lector lector;
				private Class classOrGroup;
				private LessonMaterial lessonMaterial;

				public Lesson(String title, Lector lector, Class classOrGroup,
					LessonMaterial lessonMaterial) {
					
					this.title = title;
					this.lector = lector;
					this.classOrGroup = classOrGroup;
					this.lessonMaterial = lessonMaterial;
				}

				@Override
				public String toString() {
					return String.format(
						" with title %s is given by %s to %s. " +
						"The lector uses %d slide(s) for this lesson.",
						this.title, this.lector, this.classOrGroup,
						this.lessonMaterial.slides.size());
				}
			} // School.Department.Education.Lesson

			public class Lab extends Lesson {
				public Lab(String title, Lector lector, Class classOrGroup,
					LessonMaterial lessonMaterial) {

					super(title, lector, classOrGroup, lessonMaterial);
				}

				@Override
				public String toString() {
					return "the lab" + super.toString();
				}
			} // School.Department.Education.Lab

			public class Theory extends Lesson {
				public Theory(String title, Lector lector, Class classOrGroup,
					LessonMaterial lessonMaterial) {
					
					super(title, lector, classOrGroup, lessonMaterial);
				}

				@Override
				public String toString() {
					return "the theory lesson" + super.toString();
				}
			} // School.Department.Education.Theory

			public Education(String name) {
				this.name = name;
			}

			public Class addClass(String name){
				Class newClass = new Class (name);
				this.classes.add(newClass);

				return newClass;
			}

			@Override
			public String toString() {
				return String.format("education %s (dept %s of school %s)",
					this.name, Department.this.name, School.this.name );
			}
		} // School.Department.Education

		public Department(String name) {
			this.name = name;
		}

		public Education addEducation (String name){
			Education education = new Education(name);
			this.educations.add(education);

			return education;
		}

		@Override
		public String toString() {
			return String.format ("department %s of school %s", this.name,
				School.this.name);
		}
	} // School.Department

	public School(String name) {
		this.name = name;
	}

	public Department addDepartment (String name){
		Department department = new Department(name);
		this.departments.add(department);

		return department;
	}

	@Override
	public String toString() {
		return String.format("school '%s'.", this.name);
	}
} // School