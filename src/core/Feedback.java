package core;

import java.time.LocalDateTime;

public class Feedback {
	private Integer id;
	private Integer readerId;
	private Integer bookId;
	private int score;
	private LocalDateTime reviewDate;
	private String comment;
	
	public Feedback(Integer id, Integer readerId, Integer bookId, int score, LocalDateTime reviewDate, String comment) {
		this.id = id;
		this.readerId = readerId;
		this.bookId = bookId;
		this.score = score;
		this.reviewDate = reviewDate;
		this.comment = comment;
	}
	
	public Feedback(Integer readerId, Integer bookId, int score, LocalDateTime reviewDate, String comment) {
		this.readerId = readerId;
		this.bookId = bookId;
		this.score = score;
		this.reviewDate = reviewDate;
		this.comment = comment;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReaderId() {
		return readerId;
	}

	public void setReaderId(Integer readerId) {
		this.readerId = readerId;
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public LocalDateTime getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(LocalDateTime reviewDate) {
		this.reviewDate = reviewDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
