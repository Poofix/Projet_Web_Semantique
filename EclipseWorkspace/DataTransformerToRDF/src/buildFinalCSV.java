import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import model.model.Film;
import model.model.Genre;
import model.model.Lieu;
import model.model.Realisateur;
import model.model.builder.FilmBuilder;
import model.model.builder.GenreBuilder;
import model.model.builder.LieuBuilder;
import model.model.builder.RealisateurBuilder;
import services.OMDBProxy;

public class buildFinalCSV {
	
	//Map<String, Genre> 			dictGenres;
	//Map<String, Realisateur> 	dictRealisateurs;
	//Map<String, Lieu> 			dictLieux;
	Map<String, FilmBuilder> 	dictFilm;
	Map<String, LieuBuilder> 	dictLieu;
	
	final static String API_KEY = "b82b2479";
	
	static int LAST_ID_MOVIE 		= 0;
	static int LAST_ID_LIEU 		= 0;
	static int LAST_ID_REALISATEUR  = 0;
	static int LAST_ID_GENRE 		= 0;
	
	final static byte INDEX_FI_key_film 		= 0;
	final static byte INDEX_FI_TITRE 			= 1;
	final static byte INDEX_FI_AUTEUR 			= 2;
	final static byte INDEX_FI_nbEmprunt 		= 3;
	final static byte INDEX_FI_annee_tournage	= 4;
	final static byte INDEX_FI_type_tournage 	= 5;
	final static byte INDEX_FI_imdbid			= 6;
	final static byte INDEX_FI_year 			= 7;
	final static byte INDEX_FI_rating		 	= 8;
	
	final static byte INDEX_Lieu_key_lieu 		= 0;
	final static byte INDEX_Lieu_id_lieu 		= 1;
	final static byte INDEX_Lieu_adresse_lieu 	= 2;
	final static byte INDEX_Lieu_ardt_lieu 		= 3;
	final static byte INDEX_Lieu_coord_x		= 4;
	final static byte INDEX_Lieu_coord_y 		= 5;
	final static byte INDEX_Lieu_geo_shape		= 6;
	final static byte INDEX_Lieu_geo_point_2d 	= 7;
		
	public static void main(String[] args) {
		//dictGenres = new HashMap<String, Genre>();
		//dictRealisateurs = new HashMap<String, Realisateur>();
		//dictLieux = new HashMap<String, Lieu>();
		Map<String, FilmBuilder> 		dictFilm 	= new HashMap<String, FilmBuilder>();
		Map<String, LieuBuilder> 		dictLieu 	= new HashMap<String, LieuBuilder>();
		Map<String, RealisateurBuilder> dictReal	= new HashMap<String, RealisateurBuilder>();
		Map<String, GenreBuilder> 		dictGenre 	= new HashMap<String, GenreBuilder>();
		OMDBProxy proxy = new OMDBProxy();
		
		firstLoad(System.getProperty("user.dir") + "/src/datas/talendOutput/FilmsIncomplete.csv", dictFilm);
		
		// Print MOVIE first passes : 
		/*for(String aMovieKey : dictFilm.keySet()) {
			System.out.println("ID : " + aMovieKey);
			FilmBuilder aMovie = dictFilm.get(aMovieKey);
			System.out.println(aMovie.imdbId + " ; " + aMovie.titre + " ; " + aMovie.anneeSortie + " ; " + aMovie.note);
		}*/
		
		secondLoad(System.getProperty("user.dir") + "/src/datas/talendOutput/Lieux.csv", dictLieu);
		/*for(String aLieuKey : dictLieu.keySet()) {
			System.out.println("ID : " + aLieuKey);
			LieuBuilder aLieu = dictLieu.get(aLieuKey);
			System.out.println(aLieu.id + " ; " + aLieu.adresse + " ; " + aLieu.codePostal);
		}*/
		
		// Merge des lieux dans Movies :
		mergeLieuToMovies(System.getProperty("user.dir") + "/src/datas/talendOutput/asso_film_lieu.csv", dictFilm, dictLieu);
		/*for(String aMovieKey : dictFilm.keySet()) {
			FilmBuilder aMovie = dictFilm.get(aMovieKey);
			if (aMovie.lieuxDeTournages.size() > 0) {
				System.out.println(aMovie.titre + " ---------> a un lieu");
			}
			
		}*/
		
		// Ajout des réalisateurs + merges dans les films :
		addAndMergeRealisateur(System.getProperty("user.dir") + "/src/datas/talendOutput/asso_film_lieu.csv", dictFilm, dictReal);
		/*for(String aMovieKey : dictFilm.keySet()) {
			FilmBuilder aMovie = dictFilm.get(aMovieKey);
			if (!aMovie.realisateur.equals(null)) {
				System.out.println(aMovie.titre + " ---------> a un réalisateur");
			}
			
		}*/
		
		// DEUXIEME PASSES :)
		// -----------------------------------------------------------------------------------------------------------------------
		LAST_ID_MOVIE = dictFilm.size();
		LAST_ID_REALISATEUR = dictReal.size();
		LAST_ID_LIEU = dictLieu.size();
		LAST_ID_GENRE = 0;
		
		
		// Itération des films et requêtage
		for(String aMovieKey : dictFilm.keySet()) {
			FilmBuilder aMovie = dictFilm.get(aMovieKey);
			if (aMovie.anneeSortie.equals("") || aMovie.genreDominant == null || aMovie.realisateur == null || aMovie.note == -1.0f) {
				
				HashMap<String, String> response = null;
				
				if (aMovie.imdbId != -1) {
					response = proxy.getMovieInfosById(API_KEY, String.valueOf(aMovie.imdbId));
				} else { // On passe par le titre ici
					response = proxy.getMovieInfosByTitle(API_KEY, aMovie.titre);
				}
				
				if (response != null && response.size() > 2) { // >~ 2 : Pas d'erreur
					// TODO : Compléter : 
					
					// Exemple de requête : http://www.omdbapi.com/?y=&plot=short&r=json&apikey=b82b2479&i=tt0046066
					
					/*
					 * {"Title":"Mesa of Lost Women","Year":"1953","Rated":"Approved","Released":"17 Jun 1953","Runtime":"70 min","Genre":"Horror, Sci-Fi","Director":"Ron Ormond, Herbert Tevos","Writer":"Herbert Tevos (written for the screen by)","Actors":"Jackie Coogan, Allan Nixon, Richard Travis, Lyle Talbot","Plot":"A mad scientist named Arana is creating giant spiders and dwarfs in his lab on Zarpa Mesa in Mexico. He wants to create a master race of superwomen by injecting his female subjects with spider venom.","Language":"English","Country":"USA","Awards":"N/A","Poster":"https://m.media-amazon.com/images/M/MV5BZWNjNzFhYmYtNzFmZi00MjY4LTk3MDMtZWVjNjZlZWZlMjI0XkEyXkFqcGdeQXVyMTQ2MjQyNDc@._V1_SX300.jpg","Ratings":[{"Source":"Internet Movie Database","Value":"2.8/10"},{"Source":"Rotten Tomatoes","Value":"17%"}],"Metascore":"N/A","imdbRating":"2.8","imdbVotes":"1,440","imdbID":"tt0046066","Type":"movie","DVD":"27 Jul 2016","BoxOffice":"N/A","Production":"Ron Ormond Productions","Website":"N/A","Response":"True"}
					 * 
					 */
					
					// TRAITEMENT MOVIE : -----------------------
					// -> Si pas ANNEE SORTIE
					if (aMovie.anneeSortie.equals("")) {
						aMovie.anneeSortie = response.get("Year");
					}
					
					// -> Si pas REALISATEUR
					if (aMovie.realisateur == null) {
						String nomRealisateur = response.get("Director");
						
						if (!dictReal.containsKey(nomRealisateur)) {
							RealisateurBuilder newRealisateur = new RealisateurBuilder(LAST_ID_REALISATEUR, nomRealisateur);
							aMovie.realisateur = newRealisateur;
							
							dictReal.put(nomRealisateur, newRealisateur);
							LAST_ID_REALISATEUR++;
						} else {
							aMovie.realisateur = dictReal.get(response.get("Director"));
						}
					}
					
					// -> Si pas GENRE :
					if (aMovie.genreDominant == null) {
						String[] genres = response.get("Genre").split(", ");
						if (genres.length > 0) {
							for (String aGenre : genres) {
								if (!dictGenre.containsKey(aGenre)) {
									dictGenre.put(aGenre, new GenreBuilder(LAST_ID_GENRE, aGenre));
									LAST_ID_GENRE++;
								}
							}
							aMovie.genreDominant = dictGenre.get(genres[0]);
						}
					}
					
					// -> Si pas NOTE :
					if (aMovie.note == -1.0f) {
						aMovie.note = Float.parseFloat(response.get("imdbRating"));
					}
					
				} else {
					System.out.println("ERROR : " + response.get("Error"));
				}
				
				System.out.println(aMovie);
			}
		}
	}


	/**
	 * Cette fonction permet de charger le fichier "FilmsIncomplete" et de remplir les champs associés
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
					
					int id              	= -1;
					String titre         	= "";
					// NB EMPRUNT ?
					RealisateurBuilder rea 	= null;
					String anneeTournage 	= "";
					int imdbId           	= -1;
					// YEAR = anneeTournage
					// RATING ?
					
					try {
						id            = Integer.valueOf(nextLine[INDEX_FI_key_film]);
						titre         = nextLine[INDEX_FI_TITRE];
						rea           = new RealisateurBuilder(nextLine[INDEX_FI_AUTEUR]);
						//NB EMPRUNT
						
						imdbId        = (nextLine[INDEX_FI_imdbid].isEmpty() ? -1 : Integer.valueOf(nextLine[INDEX_FI_imdbid]));
						
						// YEAR / ANNEE TOURNAGE
						if (nextLine.length >= INDEX_FI_year) {
							anneeTournage = nextLine[INDEX_FI_year];
						} else if (nextLine.length >= INDEX_FI_annee_tournage) {
							anneeTournage = nextLine[INDEX_FI_annee_tournage];
						} else {
							anneeTournage = "0";
						}

					} catch(java.lang.ArrayIndexOutOfBoundsException e) {
						// ERROR QUE NOUS IGNORONS :)
					}/* catch(Exception ex) {
						System.err.println(ex.getCause());
						return;
					}*/
					
					dico.put(
						nextLine[INDEX_FI_key_film],
						new FilmBuilder(
							id,
							imdbId,
							titre,
							anneeTournage,
							rea
						));
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
					
					int key_lieu        = -1;
					String id_lieu      = "";
					String adresse_lieu = "";
					String ardt_lieu 	= "";
					
					try {
						key_lieu        = Integer.valueOf(nextLine[INDEX_Lieu_key_lieu]);
						id_lieu         = nextLine[INDEX_Lieu_id_lieu];
						adresse_lieu    = nextLine[INDEX_Lieu_adresse_lieu];
						ardt_lieu       = nextLine[INDEX_Lieu_ardt_lieu];


					} catch(java.lang.ArrayIndexOutOfBoundsException e) {
						// ERROR QUE NOUS IGNORONS :)
					}/* catch(Exception ex) {
						System.err.println(ex.getCause());
						return;
					}*/
					
					dico.put(
						nextLine[INDEX_Lieu_key_lieu],
						new LieuBuilder(
							key_lieu,
							//id_lieu,
							adresse_lieu,
							ardt_lieu
						));
					
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
	
	private static void mergeLieuToMovies(String csvFilePath, Map<String, FilmBuilder> movies, Map<String, LieuBuilder> lieux) {
		
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
	
	private static void addAndMergeRealisateur(String csvFilePath, Map<String, FilmBuilder> movies, Map<String, RealisateurBuilder> realisateurs) {
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
					
					int id 					= -1;
					String movieId 			= "";
					String realisateurName 	= "";
					
					if (nextLine.length == 3) {
						id 				= Integer.parseInt(nextLine[0]);
						movieId 		= nextLine[1];
						realisateurName = nextLine[2];
					} else {
						id 				= Integer.parseInt(nextLine[0]);
						//movieId 		= nextLine[1];
						realisateurName = nextLine[1];
					}
					
					
					RealisateurBuilder aRealisateur = new RealisateurBuilder(id, realisateurName);
					
					realisateurs.put(
							nextLine[0],
							new RealisateurBuilder(id, realisateurName));
					
					FilmBuilder thisMovie = movies.get(movieId);
					if (thisMovie == null) {
						System.err.println("Le film " + movieId + " n'existe pas ... Le realisateur ne peut pas être ajouté...");
						continue;
					} else {
						thisMovie.realisateur = aRealisateur;
						// TODO : Problème ici : Si plusieurs réalisateurs ... ?
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
