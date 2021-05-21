package services;

import java.text.Normalizer;

public class Utils {

	public static String removeAccent(String label) {
		String copy =new String(label);
		StringBuilder sb = new StringBuilder(copy.length());
		copy = Normalizer.normalize(copy, Normalizer.Form.NFD);
        for (char c : copy.toCharArray()) {
            if (c <= '\u007F') sb.append(c);
        }
		return  sb.toString(); 
	}
	
	
	public static String normalize(String label) {
		String result = new String (label);
		result = removeAccent(result);
		result = result.replaceAll("\\s+", "");
		result = result.replace("(", "");
		result = result.replace(")", "");
		result = result.replace("\\", "");
		result = result.replace("/", "");
		result = result.replace("'", "");
		result = result.replace(".", "");
		result = result.replace(",", "");
		result = result.replace("\"", "");
		result = result.replace("?", "");
		result = result.replace("!", "");
		result = result.toLowerCase();
		return  result; 
	}
	
	public static String normalizeAuteur(String label) {
		String result = new String (label);
		result = removeAccent(result);
		result = result.replace(",", "");
		result = result.toLowerCase();
		return  result; 
	}
	
	public static String normalizeLieu(String lieu) {
		String result = new String (lieu);
		result = result.replace("Â", "");
		result = removeAccent(result);
		result = result.toLowerCase();
		return  result; 
	}
	
	public static String normalizeAnnee(String annee) {
		String result = new String (annee);
		result = result.replace("â", "");
		result = result.replace("€", "");
		result = result.replace("“", "");
		
		// Dans certains cas : une annee est note comme ceci "20182019"... On garde les 4 derniers chiffres
		if (result.length() > 4) {
			result = result.substring(result.length()-4,result.length());
		}
		return result;
	}
}
