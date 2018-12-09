package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.naming.CommunicationException;

public class Network {
	private PrintWriter out;
	private BufferedReader in;
	
	private Socket socket;
	
	private static ServerSocket serverSocket;
	Server server = null;
	
	public Network(Server server, int port) throws Exception {
		this.server = server;
		if (serverSocket == null)
			serverSocket = new ServerSocket(port);
		//while (true) {
		System.out.println("In attesa di connessione...");
		socket = serverSocket.accept();
		System.out.println("Un client si è connesso. "
				+ "Il suo IP è: " + socket.getInetAddress().getHostAddress());
		initStream();
		//}
	}
	
	public static void closeServer() {
		if ( serverSocket != null && ! serverSocket.isClosed() )
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		serverSocket = null;
	}
	
	public Network(InetAddress inetaddr, int port) throws Exception {
		socket = new Socket(inetaddr, port);
		initStream();
	}
	public Network(String inetaddr, int port) throws Exception {
		socket = new Socket(inetaddr, port);
		initStream();
	}
	
	private void initStream() throws IOException {
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	//SERVER
	
	/*
	 * Riceve la richiesta dal client e la manda al server (manage).
	 * La risposta, che può essere una stringa, arraylist di stringhe o arraylist di arraylist di stringhe, viene rimandata al client
	 */
	public void serve() throws Exception {

		String request = null;
		
		try {
			
   		request = in.readLine();
			
			Object answer;
			answer = server.manage(request);
			
			if (answer instanceof String) {
				String sa = (String)answer;
				out.println(sa);
			} else if (answer instanceof ArrayList<?>) {
				ArrayList<?> aa = (ArrayList<?>)answer;
				
				if (aa.size() == 0 || aa.get(0) instanceof String) {
				// ArrayList di Stringhe
					sendArrayList(aa);
				} else if (aa.get(0) instanceof ArrayList<?>) {
					// ArrayList di ArrayList di Stringhe
					out.println(aa.size());
					for (Object l : aa) {
						if (l instanceof ArrayList<?>) {
							sendArrayList((ArrayList<?>)l);
						}
					}
				} else {
					throw new IllegalArgumentException("Invalid answer type: " + answer.getClass());
				}
			} else {
				throw new IllegalArgumentException("Invalid answer type: " + answer.getClass());
			}
   	
      } catch (Exception e) {
      	if (request == null)
      		throw new CommunicationException("Il client ha chiuso la connessione");
      	out.println();
      	out.println(e.getMessage());
      	e.printStackTrace();
		}
	}
	
	private void sendArrayList(ArrayList<?> list) {
		out.println(list.size());
		for (Object s : list)
			if (s instanceof String)
				out.println((String)s);
	}
	
	
	
	//funzioni chiamate da CLIENT
	public ArrayList<String> readList() throws Exception {
		String res = in.readLine();
		if (res.isEmpty())	//"" = errore
			throw new Exception(in.readLine());
		
		int n = Integer.parseInt(res);
		ArrayList<String> list = new ArrayList<String>(n);
		for (int i = 0; i < n; i++) 
			list.add(i, in.readLine());
		
		return list;
	}
	
	public String readLine() throws IOException {
		return in.readLine();
	}
	
	public void writeLine(String str) {
		out.println(str);
	}

	public ArrayList<ArrayList<String>> readListOfList() throws Exception {
		String res = in.readLine();
		if (res.isEmpty())
			throw new Exception(in.readLine());
		
		int n = Integer.parseInt(res);
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>(n);
		for (int i = 0; i < n; i++) 
			list.add(i, readList());
		
		return list;
	}

	public void close() {
		try{
			socket.close();
		} catch (Exception e) {
			System.err.println("Errore nella chiusura della connessione");
			e.printStackTrace();
		}
	}
}
