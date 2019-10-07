package Model;

public class Entry {

    private String author;
    private String blog;
    private String title;
    private String content;

    public Entry() {

    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public String getBlog() {
	return blog;
    }

    public void setBlog(String blog) {
	this.blog = blog;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }
}
