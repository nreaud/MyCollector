package com.nre.mycollector.model;

public class MangaWebSite {
	private String name;
	private String pathState;
	private String url;

	public MangaWebSite() {
	}

	public MangaWebSite(String name, String pathState, String url) {
		this.name = name;
		this.pathState = pathState;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPathState() {
		return pathState;
	}

	public void setPathState(String pathState) {
		this.pathState = pathState;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "MangaWebSite [name=" + name + ", pathState=" + pathState + ", url=" + url + "]";
	}

}
