package model;

public class Triplet {
	public String s, p, o;

	public Triplet(String s, String p, String o) {
		this.s = s;
		this.p = p;
		this.o = o;
	}

	@Override
	public String toString() {
		return s + " " + p + " " + o + ".";
	}

}
