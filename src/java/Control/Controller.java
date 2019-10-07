package Control;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import Model.*;

@ManagedBean
@SessionScoped
public class Controller implements Serializable {

    private String currentuser = null;
    private String username, password;
    private String newusername, newpassword;
    private String loginerror, registererror;

    private List<Blog> blogList = null;
    private Blog currentBlog = new Blog();
    private String blogTitle, blogContent;

    private List<Entry> entryList = null;
    private Entry currentEntry = new Entry();
    private String entryTitle, entryContent;

    private List<Comment> commentList = null;
    private String commentContent;

    private String search;
    private int entryCount;
    private List<Entry> searchedEntryList = null;

    private Connection con = null;
    private PreparedStatement sta = null;
    private ResultSet res = null;
    private final String URL = "jdbc:mysql://localhost:3306/project";
    private final String USER = "root";
    private final String PASS = "alperen";

    public String checkLogin() {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "SELECT * FROM User WHERE Username = ? AND Password = ?";
	    sta = con.prepareStatement(query);
	    sta.setString(1, username);
	    sta.setString(2, password);
	    res = sta.executeQuery();

	    if (res.next() && username.equalsIgnoreCase(res.getString("Username")) && password.equalsIgnoreCase(res.getString("Password"))) {
		currentuser = username;

		sta.close();
		res.close();
		con.close();

		return "user.xhtml";
	    } else {
		loginerror = "Username or password is invalid!";

		sta.close();
		res.close();
		con.close();

		return null;
	    }
	} catch (Exception e) {
	    return null;
	}
    }

    public String checkRegister() {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "INSERT INTO User VALUES(?, ?)";
	    sta = con.prepareStatement(query);
	    sta.setString(1, newusername);
	    sta.setString(2, newpassword);
	    sta.execute();

	    currentuser = newusername;

	    sta.close();
	    res.close();
	    con.close();

	    return "user.xhtml";
	} catch (Exception e) {
	    registererror = "This username is already taken!";
	    return null;
	}
    }

    public String logout() {

	currentuser = null;
	username = password = null;
	newusername = newpassword = null;

	return showAllBlogs();
    }

    public String addBlog() {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "INSERT INTO Blog VALUES(?, ?, ?)";
	    sta = con.prepareStatement(query);
	    sta.setString(1, currentuser);
	    sta.setString(2, blogTitle);
	    sta.setString(3, blogContent);
	    sta.execute();

	    blogTitle = blogContent = null;

	    sta.close();
	    res.close();
	    con.close();

	    return showAllBlogs();
	} catch (Exception e) {
	    return null;
	}
    }

    public String showAllBlogs() {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "SELECT * FROM Blog";
	    sta = con.prepareStatement(query);
	    res = sta.executeQuery();
	    blogList = null;

	    if (res.next()) {
		blogList = new ArrayList<Blog>();
	    }
	    res.beforeFirst();

	    while (res.next()) {
		Blog b = new Blog();
		b.setAuthor(res.getString("Author"));
		b.setTitle(res.getString("Title"));
		b.setContent(res.getString("Content"));
		blogList.add(b);
	    }

	    currentBlog.setAuthor("");
	    currentBlog.setTitle("");
	    currentBlog.setContent("");
	    currentEntry.setAuthor("");
	    currentEntry.setBlog("");
	    currentEntry.setTitle("");
	    currentEntry.setContent("");

	    sta.close();
	    res.close();
	    con.close();

	    return "user.xhtml";
	} catch (Exception e) {
	    return null;
	}
    }

    public String showMyBlogs() {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "SELECT * FROM Blog WHERE Author = ?";
	    sta = con.prepareStatement(query);
	    sta.setString(1, currentuser);
	    res = sta.executeQuery();
	    blogList = null;

	    if (res.next()) {
		blogList = new ArrayList<Blog>();
	    }
	    res.beforeFirst();

	    while (res.next()) {
		Blog b = new Blog();
		b.setAuthor(res.getString("Author"));
		b.setTitle(res.getString("Title"));
		b.setContent(res.getString("Content"));
		blogList.add(b);
	    }

	    currentBlog.setAuthor("");
	    currentBlog.setTitle("");
	    currentBlog.setContent("");
	    currentEntry.setAuthor("");
	    currentEntry.setBlog("");
	    currentEntry.setTitle("");
	    currentEntry.setContent("");

	    sta.close();
	    res.close();
	    con.close();

	    return "user.xhtml";
	} catch (Exception e) {
	    return null;
	}
    }

    public String deleteBlog(Blog b) {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "DELETE FROM Blog WHERE Title = ?";
	    sta = con.prepareStatement(query);
	    sta.setString(1, b.getTitle());
	    sta.executeUpdate();

	    query = "DELETE FROM Entry WHERE Blog = ?";
	    sta = con.prepareStatement(query);
	    sta.setString(1, b.getTitle());
	    sta.executeUpdate();

	    return showAllBlogs();
	} catch (Exception ex) {
	    return null;
	}
    }

    public String showBlog(Blog b) {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "SELECT * FROM Entry WHERE Blog = ?";
	    sta = con.prepareStatement(query);
	    sta.setString(1, b.getTitle());
	    res = sta.executeQuery();

	    entryList = null;
	    currentBlog.setAuthor(b.getAuthor());
	    currentBlog.setTitle(b.getTitle());
	    currentBlog.setContent(b.getContent());

	    if (res.next()) {
		entryList = new ArrayList<Entry>();
		currentEntry.setAuthor(res.getString("Author"));
		currentEntry.setBlog(res.getString("Blog"));
		currentEntry.setTitle(res.getString("Title"));
		currentEntry.setContent(res.getString("Content"));
	    }
	    res.beforeFirst();

	    while (res.next()) {
		Entry e = new Entry();
		e.setAuthor(res.getString("Author"));
		e.setBlog(res.getString("Blog"));
		e.setTitle(res.getString("Title"));
		e.setContent(res.getString("Content"));
		entryList.add(e);
	    }

	    listComments();

	    return "blog.xhtml";
	} catch (Exception e) {
	    return null;
	}
    }

    public void listComments() {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "SELECT * FROM Comment WHERE Entry = ? AND Confirm = 1";
	    sta = con.prepareStatement(query);
	    sta.setString(1, currentEntry.getTitle());
	    res = sta.executeQuery();
	    commentList = null;

	    if (res.next()) {
		commentList = new ArrayList<Comment>();
	    }
	    res.beforeFirst();

	    while (res.next()) {
		Comment c = new Comment();
		c.setAuthor(res.getString("Author"));
		c.setEntry(res.getString("Entry"));
		c.setContent(res.getString("Content"));
		c.setConfirm(res.getInt("Confirm"));
		commentList.add(c);
	    }

	    sta.close();
	    res.close();
	    con.close();
	} catch (Exception e) {
	}
    }

    public String listWaitingComments() {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "SELECT * FROM Comment WHERE Confirm = 0 AND Entry IN(SELECT Title FROM Entry WHERE Author = ?)";
	    sta = con.prepareStatement(query);
	    sta.setString(1, currentuser);
	    res = sta.executeQuery();
	    commentList = null;

	    if (res.next()) {
		commentList = new ArrayList<Comment>();
	    }
	    res.beforeFirst();

	    while (res.next()) {
		Comment c = new Comment();
		c.setAuthor(res.getString("Author"));
		c.setEntry(res.getString("Entry"));
		c.setContent(res.getString("Content"));
		c.setConfirm(res.getInt("Confirm"));
		commentList.add(c);
	    }

	    sta.close();
	    res.close();
	    con.close();

	    return "waitingcomments.xhtml";
	} catch (Exception e) {
	    return null;
	}
    }

    public String showEntry(Entry e) {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "SELECT * FROM Entry WHERE Title = ?";
	    sta = con.prepareStatement(query);
	    sta.setString(1, e.getTitle());
	    res = sta.executeQuery();
	    res.beforeFirst();

	    while (res.next()) {
		currentEntry.setAuthor(res.getString("Author"));
		currentEntry.setBlog(res.getString("Blog"));
		currentEntry.setTitle(res.getString("Title"));
		currentEntry.setContent(res.getString("Content"));
	    }

	    listComments();

	    sta.close();
	    res.close();
	    con.close();

	    return "blog.xhtml";
	} catch (Exception ex) {
	    return null;
	}
    }

    public String addEntry() {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "INSERT INTO Entry VALUES(?, ?, ?, ?)";
	    sta = con.prepareStatement(query);
	    sta.setString(1, currentuser);
	    sta.setString(2, currentBlog.getTitle());
	    sta.setString(3, entryTitle);
	    sta.setString(4, entryContent);
	    sta.execute();

	    entryTitle = entryContent = null;

	    sta.close();
	    res.close();
	    con.close();

	    return showBlog(currentBlog);
	} catch (Exception e) {
	    return null;
	}
    }

    public String deleteEntry() {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "DELETE FROM Entry WHERE Title = ?";
	    sta = con.prepareStatement(query);
	    sta.setString(1, currentEntry.getTitle());
	    sta.executeUpdate();

	    query = "DELETE FROM Comment WHERE Entry = ?";
	    sta = con.prepareStatement(query);
	    sta.setString(1, currentEntry.getTitle());
	    sta.executeUpdate();

	    return showBlog(currentBlog);
	} catch (Exception ex) {
	    return null;
	}
    }

    public String search() {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "SELECT * FROM Entry WHERE Content LIKE ? ORDER BY Blog";
	    sta = con.prepareStatement(query);
	    sta.setString(1, '%' + search + '%');
	    res = sta.executeQuery();
	    searchedEntryList = null;
	    entryCount = 0;

	    if (res.next()) {
		searchedEntryList = new ArrayList<Entry>();
	    }
	    res.beforeFirst();

	    while (res.next()) {
		Entry e = new Entry();
		e.setAuthor(res.getString("Author"));
		e.setBlog(res.getString("Blog"));
		e.setTitle(res.getString("Title"));
		e.setContent(res.getString("Content"));
		searchedEntryList.add(e);
		entryCount++;
	    }

	    search = null;

	    sta.close();
	    res.close();
	    con.close();

	    return "search.xhtml";
	} catch (Exception e) {
	    return null;
	}
    }

    public String showSearchedEntry(Entry e) {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "SELECT * FROM Entry WHERE Blog = ?";
	    sta = con.prepareStatement(query);
	    sta.setString(1, e.getBlog());
	    res = sta.executeQuery();
	    res.beforeFirst();
	    currentBlog.setTitle(e.getBlog());
	    entryList = null;

	    if (res.next()) {
		entryList = new ArrayList<Entry>();
	    }
	    res.beforeFirst();

	    while (res.next()) {
		Entry ent = new Entry();
		ent.setAuthor(res.getString("Author"));
		ent.setBlog(res.getString("Blog"));
		ent.setTitle(res.getString("Title"));
		ent.setContent(res.getString("Content"));
		entryList.add(ent);
	    }

	    currentEntry.setAuthor(e.getAuthor());
	    currentEntry.setBlog(e.getBlog());
	    currentEntry.setTitle(e.getTitle());
	    currentEntry.setContent(e.getContent());

	    listComments();

	    return "blog.xhtml";
	} catch (Exception ex) {
	    return null;
	}
    }

    public String addComment() {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "INSERT INTO Comment VALUES(?, ?, ?, 0)";
	    sta = con.prepareStatement(query);
	    sta.setString(1, currentuser);
	    sta.setString(2, currentEntry.getTitle());
	    sta.setString(3, commentContent);
	    sta.execute();

	    commentContent = null;

	    sta.close();
	    res.close();
	    con.close();

	    return showBlog(currentBlog);
	} catch (Exception e) {
	    return null;
	}
    }

    public String confirmComment(Comment c) {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "UPDATE Comment SET Confirm = 1 WHERE Content = ?";
	    sta = con.prepareStatement(query);
	    sta.setString(1, c.getContent());
	    sta.executeUpdate();

	    sta.close();
	    res.close();
	    con.close();

	    return listWaitingComments();
	} catch (Exception e) {
	    return null;
	}
    }

    public String deleteWaitingComment(Comment c) {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "DELETE FROM Comment WHERE Content = ?";
	    sta = con.prepareStatement(query);
	    sta.setString(1, c.getContent());
	    sta.executeUpdate();

	    return listWaitingComments();
	} catch (Exception ex) {
	    return null;
	}
    }

    public String deleteComment(Comment c) {

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    con = DriverManager.getConnection(URL, USER, PASS);

	    String query = "DELETE FROM Comment WHERE Content = ?";
	    sta = con.prepareStatement(query);
	    sta.setString(1, c.getContent());
	    sta.executeUpdate();

	    return showEntry(currentEntry);
	} catch (Exception ex) {
	    return null;
	}
    }

    public boolean canDeleteBlog(Blog b) {
	if (currentuser != null && currentuser.equalsIgnoreCase(b.getAuthor())) {
	    return true;
	} else {
	    return false;
	}
    }

    public boolean canDeleteEntry() {
	if (currentuser != null && currentuser.equalsIgnoreCase(currentEntry.getAuthor())
		|| (currentuser != null && currentBlog.getAuthor() != "" && currentuser.equalsIgnoreCase(currentBlog.getAuthor()))) {
	    return true;
	} else {
	    return false;
	}
    }

    public boolean canDeleteComment(Comment c) {
	if ((currentuser != null && currentuser.equalsIgnoreCase(c.getAuthor()))
		|| (currentuser != null && currentuser.equalsIgnoreCase(currentEntry.getAuthor()))) {
	    return true;
	} else {
	    return false;
	}
    }

    public String getCurrentuser() {
	return currentuser;
    }

    public void setCurrentuser(String currentuser) {
	this.currentuser = currentuser;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getNewusername() {
	return newusername;
    }

    public void setNewusername(String newusername) {
	this.newusername = newusername;
    }

    public String getNewpassword() {
	return newpassword;
    }

    public void setNewpassword(String newpassword) {
	this.newpassword = newpassword;
    }

    public String getLoginerror() {
	return loginerror;
    }

    public void setLoginerror(String loginerror) {
	this.loginerror = loginerror;
    }

    public String getRegistererror() {
	return registererror;
    }

    public void setRegistererror(String registererror) {
	this.registererror = registererror;
    }

    public List<Blog> getBlogList() {
	return blogList;
    }

    public void setBlogList(List<Blog> blogList) {
	this.blogList = blogList;
    }

    public Blog getCurrentBlog() {
	return currentBlog;
    }

    public void setCurrentBlog(Blog currentBlog) {
	this.currentBlog = currentBlog;
    }

    public String getBlogTitle() {
	return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
	this.blogTitle = blogTitle;
    }

    public String getBlogContent() {
	return blogContent;
    }

    public void setBlogContent(String blogContent) {
	this.blogContent = blogContent;
    }

    public List<Entry> getEntryList() {
	return entryList;
    }

    public void setEntryList(List<Entry> entryList) {
	this.entryList = entryList;
    }

    public Entry getCurrentEntry() {
	return currentEntry;
    }

    public void setCurrentEntry(Entry currentEntry) {
	this.currentEntry = currentEntry;
    }

    public String getEntryTitle() {
	return entryTitle;
    }

    public void setEntryTitle(String entryTitle) {
	this.entryTitle = entryTitle;
    }

    public String getEntryContent() {
	return entryContent;
    }

    public void setEntryContent(String entryContent) {
	this.entryContent = entryContent;
    }

    public List<Comment> getCommentList() {
	return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
	this.commentList = commentList;
    }

    public String getCommentContent() {
	return commentContent;
    }

    public void setCommentContent(String commentContent) {
	this.commentContent = commentContent;
    }

    public String getSearch() {
	return search;
    }

    public void setSearch(String search) {
	this.search = search;
    }

    public int getEntryCount() {
	return entryCount;
    }

    public void setEntryCount(int entryCount) {
	this.entryCount = entryCount;
    }

    public List<Entry> getSearchedEntryList() {
	return searchedEntryList;
    }

    public void setSearchedEntryList(List<Entry> searchedEntryList) {
	this.searchedEntryList = searchedEntryList;
    }
}
