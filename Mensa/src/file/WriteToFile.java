package file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import database.*;

public class WriteToFile {
   
   public static void appendToFile(String filename, String content) throws Exception {
   	BufferedWriter bufwriter = new BufferedWriter(new FileWriter(filename, true));	//append
      try {
	      bufwriter.write(content + "\n");
	   } finally {
	      bufwriter.close();   
   	}
   }
   
   private static void createFile(String filename, String content) throws Exception {
      BufferedWriter bufwriter = new BufferedWriter(new FileWriter(filename));	//sovrascrive se file esiste
      try {
      	bufwriter.write(content);
      } finally {
      	bufwriter.close();
      }
   }
   
   public static synchronized void salvaStud(Studente stud) throws Exception {
   	salvaPers(stud, Filenames.filenameStudenti());
   }
   
   public static synchronized void salvaDoc(Docente doc) throws Exception {
   	salvaPers(doc, Filenames.filenameDocenti());
   }
   
   public static void salvaPers(Persona pers, String filename) throws Exception {
   	String content = pers.toString();
   	appendToFile(filename, content);
   }
   
   
   // per modifica e cancellazione riscrivo sempre tutto il file
	public static synchronized void salvaStudenti(ArrayList<Studente> list) throws Exception {
		salvaPersone(list, Filenames.filenameStudenti());
	}
	
	public static synchronized void salvaDocenti(ArrayList<Docente> list) throws Exception {
		salvaPersone(list, Filenames.filenameDocenti());
	}
	
	private static <P extends Persona> void salvaPersone(ArrayList<P> list, String filename) throws Exception {
		String content = "";
		
		if (list.size() != 0) {
			Iterator<P> iter = list.iterator();
			while(iter.hasNext())
				content += iter.next() + "\n";
		}
		
		createFile(filename, content);
	}
   
}