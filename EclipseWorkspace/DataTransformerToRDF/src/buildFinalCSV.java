import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.openjena.atlas.lib.Pair;

import model.model.Film;
import model.model.Genre;
import model.model.Lieu;
import model.model.Realisateur;
import model.model.builder.FilmBuilder;
import model.model.builder.GenreBuilder;
import model.model.builder.LieuBuilder;
import model.model.builder.RealisateurBuilder;
import services.IntelligentLoader;
import services.OMDBProxy;
import services.Utils;

public class buildFinalCSV {

	final static String API_KEY = "b82b2479";

	static int LAST_ID_MOVIE = 0;
	static int LAST_ID_LIEU = 0;
	static int LAST_ID_REALISATEUR = 0;
	static int LAST_ID_GENRE = 0;

	final static byte INDEX_FI_key_film = 0;
	final static byte INDEX_FI_TITRE = 1;
	final static byte INDEX_FI_AUTEUR = 2;
	final static byte INDEX_FI_nbEmprunt = 3;
	final static byte INDEX_FI_annee_tournage = 4;
	final static byte INDEX_FI_type_tournage = 5;
	final static byte INDEX_FI_imdbid = 6;
	final static byte INDEX_FI_year = 7;
	final static byte INDEX_FI_rating = 8;

	final static byte INDEX_Lieu_key_lieu = 0;
	final static byte INDEX_Lieu_id_lieu = 1;
	final static byte INDEX_Lieu_adresse_lieu = 2;
	final static byte INDEX_Lieu_ardt_lieu = 3;
	final static byte INDEX_Lieu_coord_x = 4;
	final static byte INDEX_Lieu_coord_y = 5;
	final static byte INDEX_Lieu_geo_shape = 6;
	final static byte INDEX_Lieu_geo_point_2d = 7;

	public static void main(String[] args) {

		// dictGenres = new HashMap<String, Genre>();
		// dictRealisateurs = new HashMap<String, Realisateur>();
		// dictLieux = new HashMap<String, Lieu>();
		Map<String, FilmBuilder> dictFilm = new HashMap<String, FilmBuilder>();
		Map<String, LieuBuilder> dictLieu = new HashMap<String, LieuBuilder>();
		Map<String, RealisateurBuilder> dictReal = new HashMap<String, RealisateurBuilder>();
		Map<String, GenreBuilder> dictGenre = new HashMap<String, GenreBuilder>();
		IntelligentLoader intelligentLoader = new IntelligentLoader(System.getProperty("user.dir")+"/src/datas/tmpRequest/", API_KEY, 500);

		firstLoad(System.getProperty("user.dir") + "/src/datas/talendOutput/FilmsIncomplete.csv", dictFilm);

		// Print MOVIE first passes :
		/*
		 * for(String aMovieKey : dictFilm.keySet()) { System.out.println("ID : " +
		 * aMovieKey); FilmBuilder aMovie = dictFilm.get(aMovieKey);
		 * System.out.println(aMovie.imdbId + " ; " + aMovie.titre + " ; " +
		 * aMovie.anneeSortie + " ; " + aMovie.note); }
		 */

		secondLoad(System.getProperty("user.dir") + "/src/datas/talendOutput/Lieux.csv", dictLieu);
		/*
		 * for(String aLieuKey : dictLieu.keySet()) { System.out.println("ID : " +
		 * aLieuKey); LieuBuilder aLieu = dictLieu.get(aLieuKey);
		 * System.out.println(aLieu.id + " ; " + aLieu.adresse + " ; " +
		 * aLieu.codePostal); }
		 */

		// Merge des lieux dans Movies :
		mergeLieuToMovies(System.getProperty("user.dir") + "/src/datas/talendOutput/asso_film_lieu.csv", dictFilm,
				dictLieu);
		/*
		 * for(String aMovieKey : dictFilm.keySet()) { FilmBuilder aMovie =
		 * dictFilm.get(aMovieKey); if (aMovie.lieuxDeTournages.size() > 0) {
		 * System.out.println(aMovie.titre + " ---------> a un lieu"); }
		 * 
		 * }
		 */

		// Ajout des realisateurs + merges dans les films :
		addAndMergeRealisateur(System.getProperty("user.dir") + "/src/datas/talendOutput/out.csv", dictFilm,
				dictReal);
		/*
		 * for(String aMovieKey : dictFilm.keySet()) { FilmBuilder aMovie =
		 * dictFilm.get(aMovieKey); if (!aMovie.realisateur.equals(null)) {
		 * System.out.println(aMovie.titre + " ---------> a un realisateur"); }
		 * 
		 * }
		 */

		// DEUXIEME PASSES :)
		// -----------------------------------------------------------------------------------------------------------------------
		LAST_ID_MOVIE = dictFilm.size();
		LAST_ID_REALISATEUR = dictReal.size();
		LAST_ID_LIEU = dictLieu.size();
		LAST_ID_GENRE = 0;
		
		
		// Iteration des films et requetage
		for (String aMovieKey : dictFilm.keySet()) {
			FilmBuilder aMovie = dictFilm.get(aMovieKey);
			if (aMovie.anneeSortie.equals("") || aMovie.genres.size() > 0 || aMovie.realisateur == null || aMovie.note == -1.0f) {
				
				HashMap<String, String> response = null;
				
				response = intelligentLoader.makeIntelligentCallOMDB(aMovie, false);

				if (response != null && response.size() > 2) { // >~ 2 : Pas d'erreur
						// TODO : Complï¿½ter :

						// Exemple de requï¿½te :
						// http://www.omdbapi.com/?y=&plot=short&r=json&apikey=b82b2479&i=tt0046066

						/*
						 * {"Title":"Mesa of Lost Women","Year":"1953","Rated":"Approved",
						 * "Released":"17 Jun 1953","Runtime":"70 min","Genre":"Horror, Sci-Fi"
						 * ,"Director":"Ron Ormond, Herbert Tevos"
						 * ,"Writer":"Herbert Tevos (written for the screen by)"
						 * ,"Actors":"Jackie Coogan, Allan Nixon, Richard Travis, Lyle Talbot"
						 * ,"Plot":"A mad scientist named Arana is creating giant spiders and dwarfs in his lab on Zarpa Mesa in Mexico. He wants to create a master race of superwomen by injecting his female subjects with spider venom."
						 * ,"Language":"English","Country":"USA","Awards":"N/A","Poster":
						 * "https://m.media-amazon.com/images/M/MV5BZWNjNzFhYmYtNzFmZi00MjY4LTk3MDMtZWVjNjZlZWZlMjI0XkEyXkFqcGdeQXVyMTQ2MjQyNDc@._V1_SX300.jpg"
						 * ,"Ratings":[{"Source":"Internet Movie Database","Value":"2.8/10"},{
						 * "Source":"Rotten Tomatoes","Value":"17%"}],"Metascore":"N/A","imdbRating":
						 * "2.8","imdbVotes":"1,440","imdbID":"tt0046066","Type":"movie",
						 * "DVD":"27 Jul 2016","BoxOffice":"N/A","Production":"Ron Ormond Productions"
						 * ,"Website":"N/A","Response":"True"}
						 * 
						 */

					// TRAITEMENT MOVIE : -----------------------
					// -> Si pas ANNEE SORTIE
					if (aMovie.anneeSortie.equals("")) {
						aMovie.anneeSortie = Utils.normalizeAnnee(response.get("Year"));
					}
					
					// -> Si pas REALISATEUR
					if (aMovie.realisateur.id == -1) { // TODO : Etait -1
						String nomRealisateur = Utils.normalizeAuteur(response.get("Director").split(", ")[0]); // TODO : A voir selon le formalisme
						
						RealisateurBuilder unNouveauRealisateur = null;
						
						if (!dictReal.containsKey(nomRealisateur)) {
							unNouveauRealisateur = new RealisateurBuilder(LAST_ID_REALISATEUR, nomRealisateur);
							dictReal.put(nomRealisateur, unNouveauRealisateur);
							LAST_ID_REALISATEUR++;
						} else {
							unNouveauRealisateur = dictReal.get(nomRealisateur);
						}
						
						aMovie.realisateur = unNouveauRealisateur;
					}
					
					// -> Si pas GENRE :
					if (aMovie.genres.size() == 0) {
						String[] genres = response.get("Genre").split(", ");
						if (genres.length > 0) {
							for (String aGenre : genres) {
								
								// Certains genres ne sont pas renseignes et sont annotes "N/A", on ignore.
								if (aGenre.equals("N/A")) continue;
								
								GenreBuilder unNouveauGenre = null;
								
								
								if (!dictGenre.containsKey(aGenre)) {
									unNouveauGenre = new GenreBuilder(LAST_ID_GENRE, aGenre);
									dictGenre.put(aGenre, unNouveauGenre);
									LAST_ID_GENRE++;
								} else {
									unNouveauGenre = dictGenre.get(aGenre);
								}
								
								aMovie.genres.add(unNouveauGenre);
							}
						}
					}

					// -> Si pas NOTE :
					if (aMovie.note == -1.0f) {
						Object ranking = response.get("imdbRating");
						
						// Si IMDB à une note :
						if ((ranking != null) && (!ranking.equals("N/A"))) {
							aMovie.note = Float.parseFloat(response.get("imdbRating"));
						} else { // Dans le cas contraire, la note est par defaut mise à 0.0
							//aMovie.note = 0.0f;
						}
					}
					
					// Détermine si le film est d'origine Française :
					if (!aMovie.estFrancais) {
						boolean isFrench = false;
						
						// On vérifie avec la propriété "Country" de la requête IMDB
						String[] countries = response.get("Country").split(", ");
						for (String pays : countries) {
							if (pays.toLowerCase().equals("france")) {
								aMovie.estFrancais = true; // Si OK on annote que le film est FRANCAIS
								isFrench = true; // sert seulement pour la condition prochaine
								break;
							}
						}
						
						// Ici, on vérifie l'adresse de tournage dans le cas où "Country" n'est pas renseigné.
						if (!isFrench) {
							aMovie.updateEstFrancais();								
						}
					} // Fin IF
					
				} else {
					if (response != null) {
						System.out.println("ERROR : " + response.get("Error"));
					} else {
						//System.out.println("DOWNLOAD LIMIT REACHED !"); // TODO : Pourri les loGS
					}
				}

				System.out.println(aMovie);
			}
			
			// Pour le réalisateur courant : On cherche à mettre à jour le compteur de genre en fonction des films
			if (aMovie.realisateur != null) {
				for (GenreBuilder unGenre : aMovie.genres) {
					aMovie.realisateur.incementGenre(unGenre.label);
				}
			}
		}
		
		// On cherche quels sont les film autres que Français pour les supprimer...
		Pair<Map<String, FilmBuilder>, Map<String, RealisateurBuilder>> purged = purgeFilmeNonFrancais(dictFilm, dictReal);
		dictFilm = purged.getLeft();
		dictReal = purged.getRight();
		
		// On cherche le genre de prédilection de chaque Realisateur, puis mise à jour
		for (RealisateurBuilder realisateur : dictReal.values()) {
			String genreDePredilection = realisateur.getGenrePredilection();
			if (genreDePredilection != null) {
				realisateur.genreDePredilection = dictGenre.get(genreDePredilection);
			}
		}
		
		// Ecriture des CSVs
		writeOutPutFile(dictFilm, dictLieu, dictReal, dictGenre);
	}
	
	private static Pair<Map<String, FilmBuilder>, Map<String, RealisateurBuilder>> purgeFilmeNonFrancais(Map<String, FilmBuilder> dictFilm, Map<String, RealisateurBuilder> dictReal) {
		Map<String, FilmBuilder> filmsFrancais = new HashMap<String, FilmBuilder>();
		Map<String, RealisateurBuilder> realisateurConserves = new HashMap<String, RealisateurBuilder>();
		
		
		for (Map.Entry<String,FilmBuilder> e : dictFilm.entrySet()) {
			e.getValue().updateEstFrancais();
			if (e.getValue().estFrancais ) {
				filmsFrancais.put(e.getKey(), e.getValue());
				realisateurConserves.put(e.getValue().realisateur.id+"",e.getValue().realisateur); 
			}
		}
		
		return new Pair<Map<String, FilmBuilder>, Map<String, RealisateurBuilder>>(filmsFrancais, realisateurConserves);
	}

	private static void writeOutPutFile(Map<String, FilmBuilder> dictFilm2, Map<String, LieuBuilder> dictLieu2,
			Map<String, RealisateurBuilder> dictReal, Map<String, GenreBuilder> dictGenre) {
		writeOutputFilm(dictFilm2);
		writeOutputLieu(dictLieu2);
		writeOutputRealisateur(dictReal);
		writeOutputGenre(dictGenre);

	}

	private static void writeOutputGenre(Map<String, GenreBuilder> dictGenre) {
		List<String> col = new ArrayList<String>();
		col.add("id");
		col.add("label");
		List<List<String>> data = new ArrayList<List<String>>();
		for (GenreBuilder g : dictGenre.values()) {
			List<String> line = new ArrayList<String>();
			line.add(g.id + "");
			line.add(g.label);
			if (line.size() >= col.size()) {
				data.add(line);
			}
		}

		writeCsv(System.getProperty("user.dir") + "/src/datas/generated/genres.csv", col, data);
	}

	private static void writeOutputRealisateur(Map<String, RealisateurBuilder> dictReal) {
		List<String> col = new ArrayList<String>();
		col.add("id");
		col.add("nom");
		col.add("idgenre");
		List<List<String>> data = new ArrayList<List<String>>();
		for (RealisateurBuilder r : dictReal.values()) {
			List<String> line = new ArrayList<String>();
			line.add(r.id + "");
			line.add(r.nom);
			line.add((r.genreDePredilection != null) ? String.valueOf(r.genreDePredilection.id) : "" );

			if (line.size() >= col.size()) {
				data.add(line);
			}
		}

		writeCsv(System.getProperty("user.dir") + "/src/datas/generated/realisateurs.csv", col, data);
	}

	private static void writeOutputLieu(Map<String, LieuBuilder> dictLieu2) {
		List<String> col = new ArrayList<String>();
		col.add("id");
		col.add("adresse");
		col.add("ville");
		col.add("codePostal");
		List<List<String>> data = new ArrayList<List<String>>();
		for (LieuBuilder l : dictLieu2.values()) {
			List<String> line = new ArrayList<String>();
			line.add(l.id + "");
			line.add(l.adresse);
			line.add(l.ville);
			line.add(l.codePostal);
			if (line.size() >= col.size()) {
				data.add(line);
			}
		}

		writeCsv(System.getProperty("user.dir") + "/src/datas/generated/lieux.csv", col, data);
	}

	private static void writeOutputFilm(Map<String, FilmBuilder> dictFilm2) {
		List<String> col = new ArrayList<String>();
		col.add("id");
		col.add("titre");
		col.add("annee");
		col.add("genre");
		col.add("auteur");
		col.add("lieu");
		col.add("note");
		List<List<String>> data = new ArrayList<List<String>>();
		for (FilmBuilder f : dictFilm2.values()) {
			List<String> line = new ArrayList<String>();
			for (LieuBuilder l : f.lieuxDeTournages) {
				line.add(f.id + "");
				line.add(f.titre);
				line.add(f.anneeSortie != "" ? f.anneeSortie : "");
				line.add(f.genres.size() > 0 ? String.valueOf(f.genres.get(0).id) : "");
				line.add(f.realisateur.id + "");
				line.add(l.id + "");
				line.add(f.note + "");
			}

			if (line.size() >= col.size()) {
				data.add(line);
			}
		}

		writeCsv(System.getProperty("user.dir") + "/src/datas/generated/film.csv", col, data);
	}

	private static void writeCsv(String filePath, List<String> columnsList, List<List<String>> data) {

		try {
			if (columnsList.size() != data.get(0).size()) {
				System.out.println("ERREUR, differences de colonnes entre les datas et le hearder");
			} else {

				PrintWriter pw = null;
				try {
					pw = new PrintWriter(new File(filePath));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				StringBuilder builder = new StringBuilder();
				builder.append(String.join(";", columnsList));
				builder.append("\n");
				for (List<String> rowData : data) {
					builder.append(String.join(";", rowData));
					builder.append("\n");
				}
				pw.write(builder.toString());
				pw.close();
			}
		} catch (Exception ex) {
			// System.out.println(ex.getMessage());
		}

	}

	/**
	 * Cette fonction permet de charger le fichier "FilmsIncomplete" et de remplir
	 * les champs associes
	 * 
	 * @param csvFilePath
	 */
	private static void firstLoad(String csvFilePath, Map<String, FilmBuilder> dico) {
		BufferedReader fileReader = null;
		try {

			String line = "";
			fileReader = new BufferedReader(new FileReader(csvFilePath));

			if ((line = fileReader.readLine()) != null) {

				while ((line = fileReader.readLine()) != null) {
					String[] nextLine = line.split(";");

					int id = -1;
					String titre = "";
					// NB EMPRUNT ?
					RealisateurBuilder rea = null;
					String anneeTournage = "";
					int imdbId = -1;
					// YEAR = anneeTournage
					// RATING ?

					try {
						id = Integer.valueOf(nextLine[INDEX_FI_key_film]);
						titre = nextLine[INDEX_FI_TITRE];
						rea = new RealisateurBuilder(Utils.normalizeAuteur(nextLine[INDEX_FI_AUTEUR]));
						// NB EMPRUNT

						imdbId = (nextLine[INDEX_FI_imdbid].isEmpty() ? -1
								: Integer.valueOf(nextLine[INDEX_FI_imdbid]));

						// YEAR / ANNEE TOURNAGE
						if (nextLine.length >= INDEX_FI_year) {
							anneeTournage = nextLine[INDEX_FI_year];
						} else if (nextLine.length >= INDEX_FI_annee_tournage) {
							anneeTournage = nextLine[INDEX_FI_annee_tournage];
						} else {
							anneeTournage = "0";
						}

					} catch (java.lang.ArrayIndexOutOfBoundsException e) {
						// ERROR QUE NOUS IGNORONS :)
					} /*
						 * catch(Exception ex) { System.err.println(ex.getCause()); return; }
						 */

					dico.put(nextLine[INDEX_FI_key_film], new FilmBuilder(id, imdbId, titre, anneeTournage, rea));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static void secondLoad(String csvFilePath, Map<String, LieuBuilder> dico) {

		BufferedReader fileReader = null;
		try {

			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFilePath));
			// Skip first line
			if ((line = fileReader.readLine()) != null) {
				// Read the file line by line
				while ((line = fileReader.readLine()) != null) {
					String[] nextLine = line.split(";");

					int key_lieu = -1;
					String id_lieu = "";
					String adresse_lieu = "";
					String ville = "";
					String ardt_lieu = "";

					try {
						key_lieu = Integer.valueOf(nextLine[INDEX_Lieu_key_lieu]);
						id_lieu = nextLine[INDEX_Lieu_id_lieu];
						adresse_lieu = Utils.normalizeLieu(nextLine[INDEX_Lieu_adresse_lieu]);
						ardt_lieu = nextLine[INDEX_Lieu_ardt_lieu];
						ville = nextLine[8];

						/*
						 * if (adresse_lieu.split(", ").length > 1) { ville =
						 * adresse_lieu.split(", ")[1].split(" ")[1]; // TODO : nul ! }
						 */

					} catch (java.lang.ArrayIndexOutOfBoundsException e) {
						// ERROR QUE NOUS IGNORONS :)
					} /*
						 * catch(Exception ex) { System.err.println(ex.getCause()); return; }
						 */

					dico.put(nextLine[INDEX_Lieu_key_lieu], new LieuBuilder(key_lieu,
							// id_lieu,
							adresse_lieu, ville, ardt_lieu));

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static void mergeLieuToMovies(String csvFilePath, Map<String, FilmBuilder> movies,
			Map<String, LieuBuilder> lieux) {

		BufferedReader fileReader = null;
		try {

			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFilePath));
			// Skip first line
			if ((line = fileReader.readLine()) != null) {
				// Read the file line by line
				while ((line = fileReader.readLine()) != null) {
					String[] nextLine = line.split(";");

					String aMovie = nextLine[0];
					String aLieu = nextLine[1];

					FilmBuilder thisMovie = movies.get(aMovie);
					if (thisMovie == null) {
						System.err.println("Le film " + aMovie + " n'existe pas ...");
						continue;
					}

					LieuBuilder thisLieu = lieux.get(aLieu);
					if (thisLieu != null) {
						thisMovie.lieuxDeTournages.add(thisLieu);
					} else {
						System.err.println("Le lieu " + thisLieu + " n'existe pas ...");
						continue;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void addAndMergeRealisateur(String csvFilePath, Map<String, FilmBuilder> movies,
			Map<String, RealisateurBuilder> realisateurs) {
		int count = 1;
		BufferedReader fileReader = null;
		try {

			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFilePath));
			// Skip first line
			if ((line = fileReader.readLine()) != null) {
				// Read the file line by line

				while ((line = fileReader.readLine()) != null) {
					String[] nextLine = line.split(";");

					count++;

					int id = -1;
					String movieId = "";
					String realisateurName = "";

					id = Integer.parseInt(nextLine[0]);
					movieId = nextLine[1];
					realisateurName = Utils.normalizeAuteur(nextLine[2]);

					RealisateurBuilder aRealisateur = new RealisateurBuilder(id, realisateurName);

					realisateurs.put(nextLine[0], aRealisateur);

					FilmBuilder thisMovie = movies.get(movieId);
					if (thisMovie == null) {
						System.err.println("Le film \"" + movieId
								+ "\" n'existe pas ... Le realisateur est crï¿½ï¿½ mais pas associï¿½.");
						continue;
					} else {
						thisMovie.realisateur = aRealisateur;
						// TODO : Probleme ici : Si plusieurs realisateurs ... ?
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
