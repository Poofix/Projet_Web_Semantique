package model.model.builder;

public class LieuBuilder {
	public int id;
	public String adresse;
	public String ville;
	public String codePostal;

	public LieuBuilder(int id, String adr, String ville, String c) {
		this.id = id;
		this.adresse = adr;
		this.ville = ville;
		this.codePostal = c;
	}
}
