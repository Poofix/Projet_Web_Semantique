package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
		File dir = new File(RepertoirePath + "/id/"+id+".txt");
		if (dir.exists()) {
			return RepertoirePath + "/id/"+id+".txt";
		}
		return null;
	}

	public String searchRequestExist(String titre) {
		File dir = new File(RepertoirePath + "/titre/"+titre+".txt");
		if (dir.exists()) {
			return RepertoirePath + "/id/"+titre+".txt";
		}
		return null;
	}

	private void saveJson(org.json.JSONObject responseJSON, int id) {
		FileWriter fl;
		try {
			fl = new FileWriter(RepertoirePath + "/id/" + id+".txt");
			fl.write(responseJSON.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveJson(org.json.JSONObject responseJSON, String titre) {
		FileWriter fl;
		try {
			fl = new FileWriter(RepertoirePath + "/titre/" + titre+".txt");
			fl.write(responseJSON.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public HashMap<String, String> makeIntelligentCallOMDB(FilmBuilder film) {

		HashMap<String, String> response = null;
		org.json.JSONObject responseJSON = null;
		JSONParser parser = new JSONParser();
		if (film.imdbId != -1) {
			String fileName = searchRequestExist(film.imdbId);
			if (fileName != null) {
				try {
					responseJSON = (org.json.JSONObject) parser.parse(new FileReader(fileName));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				responseJSON = proxy.getMovieInfosById(API_KEY, String.valueOf(film.imdbId));
				saveJson(responseJSON, film.imdbId);
			}
		} else { // On passe par le titre ici
			String fileName = searchRequestExist(film.imdbId);
			if (fileName != null) {
				try {
					responseJSON = (org.json.JSONObject) parser
							.parse(new FileReader(fileName));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
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
