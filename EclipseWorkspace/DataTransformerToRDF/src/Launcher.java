import services.Transformer;
import sparqlclient.SparqlClient;

public class Launcher {

	public static void main(String[] args) {
		System.out.println("Start Program ...");
		SparqlClient sparqlClient = new SparqlClient("localhost:3030/lieuFrance");
		Transformer transformer = new Transformer(sparqlClient,true);
		System.out.println("Start model loading");
		transformer.loadModel();
		System.out.println("End model loading");
		System.out.println("Start insert into sparql");
		transformer.convertModelToOntology();
		System.out.println("End insertion into sparql");
		System.out.println("End Program");
	}

}
