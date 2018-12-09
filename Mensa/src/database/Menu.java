package database;

public class Menu {
	private Piatto primo;
	private Piatto secondo;
	private Piatto dessert;
	
	public Menu() {
		
	}
	
	public Menu(Piatto primo, Piatto secondo, Piatto dessert) {
		this.primo = primo;
		this.secondo = secondo;
		this.dessert = dessert;	
	}

	public Piatto getPrimo() {
		return primo;
	}

	public void setPrimo(Piatto primo) {
		this.primo = primo;
	}

	public Piatto getSecondo() {
		return secondo;
	}

	public void setSecondo(Piatto secondo) {
		this.secondo = secondo;
	}

	public Piatto getDessert() {
		return dessert;
	}

	public void setDessert(Piatto dessert) {
		this.dessert = dessert;
	}
	
	public String toString() {
		return String.format("%s&%s&%s", getPrimo().toString(), getSecondo().toString(), getDessert().toString());
	}
	
	public static Menu fromString(String info) {
		String[] str = info.split("&");
		Piatto p = Piatto.fromString(str[0].trim());
		Piatto s = Piatto.fromString(str[1].trim());
		Piatto d = Piatto.fromString(str[2].trim());
		
		Menu menu = new Menu(p, s, d);
		return menu;
	}
}
