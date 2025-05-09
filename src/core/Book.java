package core;

import java.util.Date;

public class Book {
	private Integer id;
	private Integer authorId;
	private String name;
	private Date releaseDate;
	private String description;
	private double avgScore;
	
	public Book(int id, int authorId, String name, Date releaseDate, String description) {
		this.id = id;
		this.authorId = authorId;
		this.name = name;
		this.releaseDate = releaseDate;
		this.description = description;
	}
	
	public Book(int authorId, String name, Date releaseDate, String description) {
		this.authorId = authorId;
		this.name = name;
		this.releaseDate = releaseDate;
		this.description = description;
	}
	
	public Book(int authorId, String name, Date releaseDate, String description, double avg) {
		this.authorId = authorId;
		this.name = name;
		this.releaseDate = releaseDate;
		this.description = description;
		this.avgScore = avg;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Integer authorId) {
		this.authorId = authorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(double avgScore) {
		this.avgScore = avgScore;
	}
	
}
