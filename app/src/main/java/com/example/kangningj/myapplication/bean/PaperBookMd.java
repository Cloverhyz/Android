package com.example.kangningj.myapplication.bean;

import java.io.Serializable;


public class PaperBookMd implements Serializable {

	/**
	 * 书籍模型
	 */
	private static final long serialVersionUID = 789456123L;
	private int paperBookId;
	private int number;
	private Long accountId;
	private String bookName;
	private Double bookPrice;
	private String bookPicPath;
	private String description;
	private Integer checkd = 0;

	public int getPaperBookId() {
		return paperBookId;
	}

	public void setPaperBookId(int paperBookId) {
		this.paperBookId = paperBookId;
	}
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public Double getBookPrice() {
		return bookPrice;
	}

	public void setBookPrice(Double bookPrice) {
		this.bookPrice = bookPrice;
	}
	public String getBookPicPath() {
		return bookPicPath;
	}

	public void setBookPicPath(String bookPicPath) {
		this.bookPicPath = bookPicPath;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}


	public Integer getCheckd() {
		return checkd;
	}

	public void setCheckd(Integer checkd) {
		this.checkd = checkd;
	}
}
