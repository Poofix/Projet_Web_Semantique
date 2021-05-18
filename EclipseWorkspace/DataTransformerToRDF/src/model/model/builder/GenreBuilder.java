package model.model.builder;

public class GenreBuilder {

	// private static final AtomicInteger count = new AtomicInteger(0);
	// private String key ;

	public int id;
	public String label;

	public GenreBuilder(int id, String token) {
		this.id = id;
		this.label = token;
	}
}
