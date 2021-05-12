import services.Transformer;

public class Launcher {

	public static void main(String[] args) {
		System.out.println("Starting ...");
		Transformer transformer = new Transformer();
		System.out.println("Start model loading");
		transformer.loadModel();
		System.out.println("End model loading");
		System.out.println("Start insert into sparql");
		transformer.convertModelToOntology();
		System.out.println("End insertion into sparql");
	}

}
