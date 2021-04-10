package com.nre.mycollector.utils;

public class Pair<T, U> {
	public final T one;
	public final U two;

	public Pair(T one, U two) {
		this.one = one;
		this.two = two;
	}

	@Override
	public String toString() {
		return "Pair [" + one + ", " + two + "]";
	}

}
