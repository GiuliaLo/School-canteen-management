package file;

public final class Filenames {

	//public static final String root = "../";
	private static String root = "";
	
	public static String filenameStudenti() { return root + "Studenti.txt"; }
	public static String filenameDocenti() { return root + "Docenti.txt"; }
	public static String filenamePiatti() { return root + "Piatti.txt"; }

	public static void setRoot(String s) {
		if (s.charAt(s.length()-1) != '/')
			root = s + '/';
		else
			root = s;
	}
}
