package be.ap.javadv.javafx;

public class Podcast {
	private String image;
	private String title;
	private String url;

	public Podcast(String title, String image, String url) {
		this.setTitle(title);
		this.image = image;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}