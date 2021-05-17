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
		result = result.toLowerCase();
		return  result; 
	}
}
