package database;

public class Indirizzo {
	private String via;
	private String cap;
	private String citta;
	
	public Indirizzo(String via, String cap, String citta) {
		this.via = via;
		this.cap = cap;
		this.citta = citta;
	}
	
	public void setVia(String via) {
		this.via = via;
	}
	
	public void setCap(String cap) {
		this.cap = cap;
	}
	
	public void setCitta(String citta) {
		this.citta = citta;
	}
	
	public String getVia() {
		return via;
	}

	public String getCap() {
		return cap;
	}

	public String getCitta() {
		return citta;
	}

	public String toString() {
		return String.format("%s|%s|%s", getVia(), getCap(), getCitta());
	}
	
	public static Indirizzo fromString(String ind) {
		String [] pieces = ind.split("\\|");
		return new Indirizzo(pieces[0], pieces[1], pieces[2]);
	}
	
}
