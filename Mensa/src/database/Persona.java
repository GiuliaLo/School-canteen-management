package database;
import java.util.ArrayList;

public class Persona 
{
	/**
	 * Chiave dell'ultima persona creata
	 */
	private static int currentKey = 0;
	
	private final int key;
   private String nome;
   private String cognome;
   private String cellulare;
   private Indirizzo indirizzo;
   private ArrayList<String> note;
   private ArrayList<Allergene> allergie;

   public Persona() {
      this.key = ++currentKey;
   }
   /*
    * L’ID di studenti e docenti viene generato dal costruttore di Persona quindi è non ci saranno ID uguali neanche tra studenti e docenti
    * Dopo una cancellazione non viene riempito l'ID lasciato libero
    */
   
   //costruttore per dati da utente
   public Persona(String nome, String cognome, String cellulare,
   		Indirizzo indirizzo, ArrayList<String> note, ArrayList<Allergene> allergie) {
      
      this();
   	
      this.nome = nome;                                    
      this.cognome = cognome;
      this.cellulare = cellulare;
      this.indirizzo = indirizzo;
      this.note = note;
      this.allergie = allergie;
   }
   
   //costruttore per dati da file
   protected Persona(int key, String nome, String cognome, String cellulare,
   		Indirizzo indirizzo, ArrayList<String> note, ArrayList<Allergene> allergie) {
      
   	currentKey = Math.max(currentKey, key);
   	
   	this.key = key;
      this.nome = nome;                                    
      this.cognome = cognome;
      this.cellulare = cellulare;
      this.indirizzo = indirizzo;
      this.note = new ArrayList<String>();
      this.note = note;         
      this.allergie = allergie; 
   }

   public int getKey() {
   	return key;
   }
   
   public String getNome() {
      return nome;
   } 

   public String getCognome() {
      return cognome;
   } 

   public String getCellulare() {
   	return cellulare;
   } 
   
   public Indirizzo getIndirizzo() {
   	return indirizzo;
   }

   public ArrayList<String> getNote() {
   	return note;
   }
   
   public ArrayList<Allergene> getAllergie() {
   	return allergie;
   }
   
   public void setNome(String nome ) {
   	this.nome = nome;
   }
   
   public void setCognome(String cognome) {
   	this.cognome = cognome;
   }

   public void setCellulare(String cellulare) {
   	this.cellulare = cellulare;
   }
   
   public void setIndirizzo(Indirizzo indirizzo) {
   	this.indirizzo = indirizzo;
   }
   
   public void addNota(String str) {
   	if (note == null)
   		note = new ArrayList<>();
   	note.add(str);
   }
   
   public boolean removeNota(int index) {
   	if (index < note.size()) {
   		note.remove(index);
   		return true;
   	}
   	return false;
   }
   
   public boolean addAllergia(Allergene a) {
   	if (allergie == null)
   		allergie = new ArrayList<>();
   	if (allergie.contains(a)) 
   		return false;
   	allergie.add(a);
   	return true;
   }

   public boolean removeAllergia(Allergene a) {
   	if (! allergie.contains(a)) {
   		return false;
   	}
   	allergie.remove(a);
   	return true;
   }
   
   public boolean hasAllergia(Allergene a) {
   	if (allergie!=null && allergie.contains(a))
   		return true;
   	return false;
   }
   
   @Override
   public String toString() {
   	String strNote = null;
   	String strAllergie = null;
   	if (note != null)
   		strNote = note.toString();
   	else
   		strNote = "[]";
   	if (allergie != null)
   		strAllergie = allergie.toString();
   	else
   		strAllergie = "[]";
   	
      return String.format("%d\t%s\t%s\t%s\t%s\t%s\t%s", 
         getKey(), getNome(), getCognome(), getCellulare(), getIndirizzo().toString(), 
         strNote, strAllergie);
   }
   
   // !!!
   public static Persona fromString(String info) {
   	String[] str = info.split("\\t");
   	
   	int key = Integer.parseInt(str[0].trim());
   	String n = str[1].trim();
   	String c = str[2].trim();
   	String cell = str[3].trim();
   	Indirizzo ind = Indirizzo.fromString(str[4].trim());
   	ArrayList<String> note = new ArrayList<>();
   	if (!str[5].trim().isEmpty()) {  
   		String[] no = str[5].split(",");
   		for (int i=0; i<no.length; i++)
   			note.add(no[i].trim());
   	}
   	ArrayList<Allergene> a = new ArrayList<>();
   	if (!str[6].trim().isEmpty()) {
   		String[] all = str[6].split(",");
   		for (int i=0; i<all.length; i++)
   			a.add(Allergene.valueOf(all[i].trim()));
   	}
   	
   	Persona persona = new Persona(key, n, c, cell, ind, note, a);
   	return persona;
   }

}

