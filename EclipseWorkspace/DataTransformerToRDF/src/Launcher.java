import services.Transformer;
import sparqlclient.SparqlClient;

public class Launcher {

	public static void main(String[] args) {
		System.out.println("Start Program ...");
		SparqlClient sparqlClient = new SparqlClient("localhost:3030/lieuFrance");
		SparqlClient dbpediaClient = new SparqlClient("dbpedia.org");
		Transformer transformer = new Transformer(sparqlClient, dbpediaClient,false);
		System.out.println("Start model loading");
		transformer.loadModel();
		System.out.println("End model loading");
		System.out.println("Start aligning informations with DBPedia");
		//transformer.doAlignStreet();
		System.out.println("End aligning informations with DBPedia");
		System.out.println("Start insert into sparql");
		transformer.convertModelToOntology();
		System.out.println("End insertion into sparql");
		System.out.println("End Program");
	}

}
