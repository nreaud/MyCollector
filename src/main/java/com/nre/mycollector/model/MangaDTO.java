package com.nre.mycollector.model;

public class MangaDTO {

	private String imgUrl;
	private String name;

	public MangaDTO() {

	}

	public MangaDTO(String imgUrl, String name) {
		this.imgUrl = imgUrl;
		this.name = name;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MangaDTO [imgUrl=" + imgUrl + ", name=" + name + "]";
	}

}
