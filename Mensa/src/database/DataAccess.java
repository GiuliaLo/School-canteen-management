package database;

import java.util.ArrayList;

public interface DataAccess {

	public int aggiungiStudente(String nome, String cognome, String telefono, String cellulare,
			 Indirizzo indirizzo, ArrayList<String> note, ArrayList<Allergene> allergie) throws Exception;
	public int aggiungiDocente(String nome, String cognome, String cellulare,
			 Indirizzo indirizzo, ArrayList<String> note, ArrayList<Allergene> allergie) throws Exception;
	public int cancellaStudente(int key) throws Exception;
	public int cancellaDocente(int key) throws Exception;
	public int modificaStudente(int key, String campo, Object info) throws Exception;
	public int modificaDocente(int key, String campo, Object info) throws Exception;
	public ArrayList<Studente> elencoStudenti() throws Exception;
	public Studente getStudente(int id) throws Exception;
	public ArrayList<Docente> elencoDocenti() throws Exception;
	public Docente getDocente(int id) throws Exception;
	public ArrayList<Piatto> elencoPiatti() throws Exception;
	public ArrayList<Piatto> elencoPiatti(String tipo) throws Exception;
	public Piatto getPiatto(int id) throws Exception;
	public Menu generaMenu(int primo, int secondo, int dessert) throws Exception;
	public ArrayList<ArrayList<Persona>> getAllergici() throws Exception;

}
