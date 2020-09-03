package com.nre.mycollector.model;

public class MangaDTO {
	private String img;
	private Manga manga;
	private String name;

	public MangaDTO() {

	}

	public MangaDTO(String img, Manga manga, String name) {
		this.img = img;
		this.manga = manga;
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Manga getManga() {
		return manga;
	}

	public void setManga(Manga manga) {
		this.manga = manga;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "MangaDTO [img=" + img + ", manga=" + manga + ", name=" + name + "]";
	}

}
