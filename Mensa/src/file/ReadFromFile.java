package file;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import database.Docente;
import database.Piatto;
import database.Studente;

public class ReadFromFile {
	
	public static ArrayList<Piatto> readPiatti() {
		ArrayList<Piatto> listaPiatti = new ArrayList<>();
		Path path = Paths.get(Filenames.filenamePiatti());
		
		try (
			Scanner input = new Scanner(path);
		) {
			
			while (input.hasNextLine()) {
				String line = input.nextLine();
				listaPiatti.add(Piatto.fromString(line));
			}
			
		} catch (IOException e) {
			System.err.println("Errore apertura file: " + path.toAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaPiatti;
	}
	
	public static ArrayList<Studente> readStudenti() {
		ArrayList<Studente> listaStudenti = new ArrayList<>();
		
		try (
			Scanner input = new Scanner(Paths.get(Filenames.filenameStudenti()));
		) {
			while (input.hasNextLine()) {
				String line = input.nextLine();
				listaStudenti.add(Studente.fromString(line));
			}
		} catch (NoSuchFileException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listaStudenti;
	}
	
	public static ArrayList<Docente> readDocenti() {
		ArrayList<Docente> listaDocenti = new ArrayList<>();
		
		try (
			Scanner input = new Scanner(Paths.get(Filenames.filenameDocenti()));
		) {
			while (input.hasNextLine()) {
				String line = input.nextLine();
				listaDocenti.add(Docente.fromString(line));
			}
			
		} catch (NoSuchFileException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listaDocenti;
	}

}
