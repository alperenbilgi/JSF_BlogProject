package Model;

public class Comment {

    private String author;
    private String entry;
    private String content;
    private int confirm;

    public Comment() {

    }

    public String getAuthor() {
	return author;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public String getEntry() {
	return entry;
    }

    public void setEntry(String entry) {
	this.entry = entry;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    public int getConfirm() {
	return confirm;
    }

    public void setConfirm(int confirm) {
	this.confirm = confirm;
    }
}
