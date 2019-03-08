package be.ap.javadv.innerclasses;

import java.util.HashSet;
import java.util.Set;

public class Course {
	private Set<LessonMaterial> lessonmaterials = new HashSet<>();

	public class LessonMaterial {
		public Set<Slide> slides = new HashSet<>();

		public class Slide {
			private String title;
			private String content;

			public Slide(String title, String content) {
				this.title = title;
				this.content = content;

				LessonMaterial.this.slides.add (this);
			}

			@Override
			public String toString() {
				return String.format("the slide '%s' has content:\n\t%s",
					this.title, this.content);
			}
		} // Slide

		public LessonMaterial() {
			Course.this.lessonmaterials.add (this);
		}

		@Override
		public String toString() {
			String string = String.format(
				"this material consists of a total of %d slides:",
				this.slides.size());
			
			for (Slide slide : this.slides) {
				string += "\n" + slide;
			}

			return string;
		}
	}
} // Course