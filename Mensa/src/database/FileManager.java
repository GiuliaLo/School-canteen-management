package database;

import java.util.ArrayList;

import file.ReadFromFile;
import file.WriteToFile;

public class FileManager implements DataAccess {
	
	private ArrayList<Studente> listaStud;
	private ArrayList<Docente> listaDoc;
	private ArrayList<Piatto> listaPiatti;
	private Menu menuDelGiorno;
	
	public FileManager() {
		listaStud = ReadFromFile.readStudenti();
		listaDoc = ReadFromFile.readDocenti();
		listaPiatti = ReadFromFile.readPiatti();
		
		menuDelGiorno = null;
	}
	
	public ArrayList<Studente> listaStud() {
		ArrayList<Studente> elencoStudenti = new ArrayList<>(listaStud);
		return elencoStudenti;
	}
	
	public ArrayList<Docente> listaDoc() {
		ArrayList<Docente> elencoDocenti = new ArrayList<>(listaDoc);
		return elencoDocenti;
	}
	
	public ArrayList<Piatto> listaPiatti() {
		ArrayList<Piatto> elencoPiatti = new ArrayList<>(listaPiatti);
		return elencoPiatti;
	}

	public int aggiungiStudente(String nome, String cognome, String telefono, String cellulare, Indirizzo indirizzo,
			ArrayList<String> note, ArrayList<Allergene> allergie) throws Exception {
		Studente s = new Studente(nome, cognome, telefono, cellulare, indirizzo, note, allergie);
		listaStud.add(s);
		WriteToFile.salvaStud(s);
		return s.getKey();
	}

	public int aggiungiDocente(String nome, String cognome, String cellulare, Indirizzo indirizzo,
			ArrayList<String> note, ArrayList<Allergene> allergie) throws Exception {
		Docente d = new Docente(nome, cognome, cellulare, indirizzo, note, allergie);
		listaDoc.add(d);
		WriteToFile.salvaDoc(d);
		return d.getKey();
	}

	public int cancellaStudente(int key) throws Exception {
		listaStud.removeIf((Studente stud) -> stud.getKey() == key);
		// riscrivo tutta la listaStud sul file
		WriteToFile.salvaStudenti(listaStud);
		return 0;
	}

	public int cancellaDocente(int key) throws Exception {
		listaDoc.removeIf((Docente doc) -> doc.getKey() == key);
		WriteToFile.salvaDocenti(listaDoc);
		return 0;
	}

	@Override
	public int modificaStudente(int key, String campo, Object info) throws Exception {
		Studente stud = getStudente(key);
		switch (campo) {
		case "nome":
			stud.setNome((String)info);
			break;
		case "cognome":
			stud.setCognome((String)info);
			break;
		case "telefono":
			stud.setTelefono((String)info);
			break;
		case "cellulare":
			stud.setCellulare((String)info);
			break;
		case "indirizzo":
			String ind = (String)info;
			stud.setIndirizzo(Indirizzo.fromString(ind));
			break;
		case "note_add":
			stud.addNota((String)info);
			break;
		case "note_del":
			stud.removeNota(Integer.parseInt((String)info));
			break;
		case "allergie_add":
			stud.addAllergia(Allergene.values()[Integer.parseInt((String)info) - 1]);
			break;
		case "allergie_del":
			stud.removeAllergia(Allergene.values()[Integer.parseInt((String)info) - 1]);
			break;
		}
		WriteToFile.salvaStudenti(listaStud);
		return 0;
	}

	@Override
	public int modificaDocente(int key, String campo, Object info) throws Exception {
		Docente doc = getDocente(key);
		switch (campo) {
		case "nome":
			doc.setNome((String)info);
			break;
		case "cognome":
			doc.setCognome((String)info);
			break;
		case "cellulare":
			doc.setCellulare((String)info);
			break;
		case "indirizzo":
			String ind = (String)info;
			doc.setIndirizzo(Indirizzo.fromString(ind));
			break;
		case "note_add":
			doc.addNota((String)info);
			break;
		case "note_del":
			doc.removeNota(Integer.parseInt((String)info));
			break;
		case "allergie_add":
			doc.addAllergia(Allergene.values()[Integer.parseInt((String)info) - 1]);
			break;
		case "allergie_del":
			doc.removeAllergia(Allergene.values()[Integer.parseInt((String)info) - 1]); // -1 perché nella enum si parte da 0
			break;
		}
		WriteToFile.salvaDocenti(listaDoc);
		return 0;
	}

	@Override
	public ArrayList<Studente> elencoStudenti() {
		return listaStud;
	}

	@Override
	public Studente getStudente(int id) {
		return listaStud.stream().filter((Studente stud) ->
			id==(stud.getKey())).findFirst().orElse(null);
	}

	@Override
	public ArrayList<Docente> elencoDocenti() {
		return listaDoc;
	}

	@Override
	public Docente getDocente(int id) {
		return listaDoc.stream().filter((Docente doc) ->
		id==(doc.getKey())).findFirst().orElse(null);
	}

	@Override
	public ArrayList<Piatto> elencoPiatti() {
		return listaPiatti;
	}

	@Override
	public ArrayList<Piatto> elencoPiatti(String tipo) {
		ArrayList<Piatto> piattiTipo = new ArrayList<>();
		for (Piatto p : listaPiatti) {
			if (tipo.equals("dessert")) {
				if (p.getTipoStr().equals("dolce") || p.getTipoStr().equals("frutta"))
					piattiTipo.add(p);
			}
			else {
				if (p.getTipoStr().equals(tipo))
					piattiTipo.add(p);
			}
		}
		return piattiTipo;
	}

	@Override
	public Piatto getPiatto(int id) {
		return listaPiatti.stream().filter((Piatto p) ->
		id==(p.getKey())).findFirst().orElse(null);
	}

	@Override
	public Menu generaMenu(int primo, int secondo, int dessert) {
		Piatto p = getPiatto(primo);
		Piatto s = getPiatto(secondo);
		Piatto d = getPiatto(dessert);
		menuDelGiorno = new Menu(p, s, d);
		return menuDelGiorno;
	}

	@Override
	public ArrayList<ArrayList<Persona>> getAllergici() {
		ArrayList<ArrayList<Persona>> listaAll = new ArrayList<>(3);
		ArrayList<Persona> listaPrimo = new ArrayList<Persona>();
		ArrayList<Persona> listaSecondo = new ArrayList<Persona>();
		ArrayList<Persona> listaDessert = new ArrayList<Persona>();
		
		Piatto pr = menuDelGiorno.getPrimo();
		ArrayList<Allergene> primo = null;
		if (!(pr.getAllergeni() == null)) {
			primo = pr.getAllergeni();
		}
		
		Piatto sec = menuDelGiorno.getSecondo();
		ArrayList<Allergene> secondo = null;
		if (!(sec.getAllergeni() == null)) {
			secondo = sec.getAllergeni();
		}
		
		Piatto des = menuDelGiorno.getDessert();
		ArrayList<Allergene> dessert = null;
		if (!(des.getAllergeni() == null)) {
			dessert = des.getAllergeni();
		}
				
		for (Studente pers : listaStud) {
			if (primo != null)
				for (Allergene all : primo) {
					if (pers.hasAllergia(all))
						listaPrimo.add(pers);
				}
			if (secondo != null)
				for (Allergene all : secondo) {
					if (pers.hasAllergia(all))
						listaSecondo.add(pers);
				}
			if (dessert != null)
				for (Allergene all : dessert) {
					if (pers.hasAllergia(all))
						listaDessert.add(pers);
				}
		}
		for (Docente pers : listaDoc) {
			if (primo != null)
				for (Allergene all : primo) {
					if (pers.hasAllergia(all))
						listaPrimo.add(pers);
				}
			if (secondo != null)
				for (Allergene all : secondo) {
					if (pers.hasAllergia(all))
						listaSecondo.add(pers);
				}
			if (dessert != null)
				for (Allergene all : dessert) {
					if (pers.hasAllergia(all))
						listaDessert.add(pers);
				}
		}
		
		listaAll.add(listaPrimo);
		listaAll.add(listaSecondo);
		listaAll.add(listaDessert);
		
		return listaAll;
	}

}
