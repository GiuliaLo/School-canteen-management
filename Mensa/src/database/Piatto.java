package database;

import java.util.ArrayList;

public class Piatto {
	private int key;
	private TipoPiatto tipo;
	private String nome;
	private ArrayList<Allergene> allergeni;
	
	public enum TipoPiatto {
		primo, secondo, dolce, frutta;
	}
	
	protected Piatto(int key, TipoPiatto tipo, String nome, ArrayList<Allergene> allergeni) {
		this.key = key;
		this.tipo = tipo;
		this.nome = nome;
		this.allergeni = allergeni;
	}
	
	public int getKey() {
		return key;
	}
	
	public TipoPiatto getTipo() {
		return tipo;
	}

	public String getTipoStr() {
		return tipo.toString();
	}

	public String getNome() {
		return nome;
	}

	public ArrayList<Allergene> getAllergeni() {
		return allergeni;
	}
	
	@Override
	public String toString() {
		String allStr;
		if (getAllergeni() == null)
			allStr = "[]";
		else
			allStr = getAllergeni().toString();
		return String.format("%d\t%s\t%s\t%s", 
	         key, getTipo(), getNome(), allStr);
	}
	
	public static Piatto fromString(String info) {
		String[] str = info.split("\\t");
		int key = Integer.parseInt(str[0].trim());
		TipoPiatto t = TipoPiatto.valueOf(str[1].trim());
		String n = str[2].trim();
		ArrayList<Allergene> a = null;
		if (str.length > 3 && !str[3].equals("[]")) {
			a = new ArrayList<>();
			String[] all = str[3].substring(1, str[3].length()-1).split(",");	//substring toglie le []
			for (int i=0; i<all.length; i++) {
				try {
					a.add(Allergene.valueOf(all[i].trim()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Piatto piatto = new Piatto(key, t, n, a);
		return piatto;
	}
}