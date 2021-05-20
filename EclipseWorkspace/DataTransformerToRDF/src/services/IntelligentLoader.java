package services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import model.model.builder.FilmBuilder;

public class IntelligentLoader {

	public String RepertoirePath;
	private String API_KEY;
	private OMDBProxy proxy;

	public IntelligentLoader(String repertoirePath, String apikey) {
		RepertoirePath = repertoirePath;
		proxy = new OMDBProxy();
		API_KEY = apikey;
	}

	public String searchRequestExist(int id) {
		File aFile = new File(RepertoirePath + "/id/"+id+".txt");
		if (aFile.exists()) {
			return RepertoirePath + "/id/"+id+".txt";
		}
		return null;
	}

	public String searchRequestExist(String titre) {
		String formalizedTitle = Utils.normalize(titre);
		File aFile = new File(RepertoirePath + "/titre/"+formalizedTitle+".txt");
		if (aFile.exists()) {
			return RepertoirePath + "/titre/"+formalizedTitle+".txt";
		}
		return null;
	}

	private void saveJson(org.json.JSONObject responseJSON, int id) {
		PrintWriter fl;
		try {
			fl = new PrintWriter(RepertoirePath + "/id/" + id+".txt");
			fl.write(responseJSON.toString());
			fl.flush();
			fl.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveJson(org.json.JSONObject responseJSON, String titre) {
		FileWriter fl;
		try {
			fl = new FileWriter(RepertoirePath + "/titre/" + titre+".txt"); //TODO : use normalizer
			fl.write(responseJSON.toString());
			fl.flush();
			fl.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public HashMap<String, String> makeIntelligentCallOMDB(FilmBuilder film) {

		HashMap<String, String> response = new HashMap<String, String>();
		org.json.JSONObject responseJSON = null;
		if (film.imdbId != -1) {
			String fileName = searchRequestExist(film.imdbId);
			if (fileName != null) {
				//InputStream is = IntelligentLoader.class.getResourceAsStream(fileName);
				JSONTokener tokener = null;
				try {
					tokener = new JSONTokener(new FileInputStream(fileName));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				responseJSON = new JSONObject(tokener); 
			} else {
				System.out.println("APPEL REST");
				responseJSON = proxy.getMovieInfosById(API_KEY, String.valueOf(film.imdbId));
				saveJson(responseJSON, film.imdbId);
			}
		} else { // On passe par le titre ici
			String fileName = searchRequestExist(film.titre);
			if (fileName != null) {
				//InputStream is = IntelligentLoader.class.getResourceAsStream(fileName);
				JSONTokener tokener = null;
				try {
					tokener = new JSONTokener(new FileInputStream(fileName));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				responseJSON = new JSONObject(tokener); 
			} else {
				System.out.println("APPEL REST");
				responseJSON = proxy.getMovieInfosByTitle(API_KEY, film.titre);
				saveJson(responseJSON, Utils.normalize(film.titre));
			}
		}

		for (String key : responseJSON.keySet()) {
			String val = "";
			try {
				val = responseJSON.getString(key);
				response.put(key, val);
			} catch (org.json.JSONException ex) {
				continue; // On passe
			}

		}
		return response;
	}

}
