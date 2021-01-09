

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Properties;

public class Webserver implements Runnable {

	// here is the configuration part
	static final File WEB_ROOT = new File(".");
	static String default_file = "index.html";
	static String file_not_found = "404.html";
	static String method_not_supported = "not_supported.html";
	private static Webserver instance;
	static int run_in_thread;
	// port to listen connection
	static int port = 8080;
	private static Thread thread;

	// verbose mode
	static final boolean verbose = true;

	// Client Connection via Socket Class
	private Socket connect;
	static ServerSocket serverConnect;

	private Webserver() {
			
	}

	public static void start() {
		if (instance != null) {
			if (run_in_thread == 0) {
				run_in_thread = 1;
				thread = new Thread(instance);
				thread.start();
			} else {
				ServerMain.label.setText("Web server is already running!");
			}
			return;
		}
//property file section-----------------------------------------------------------------------------
		try (InputStream input = new FileInputStream("webserver.properties")) {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			port = Integer.parseInt(prop.getProperty("port", "8080"));
			method_not_supported = prop.getProperty("method_not_supported_page", "not_supported.html");
			file_not_found = prop.getProperty("error404_page", "404.html");
			default_file = prop.getProperty("standard_page", "index.html");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
//end of the property file section------------------------------------------------------------------
			
			instance = new Webserver();
			// we listen until user halts server execution
			if (verbose) {
				System.out.println("Connecton opened. (" + new Date() + ")");
			}

			// create dedicated thread to manage the client connection
			try {
				serverConnect = new ServerSocket(port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			thread = new Thread(instance);
			thread.start();

	}

	@Override
	public void run() {

		// we manage our particular client connection
		BufferedReader in = null;
		PrintWriter out = null;
		BufferedOutputStream dataOut = null;
		String fileRequested = null;

		try {
			
			
			
			
			System.out.println("Server started.\nListening for connections on port : " + port + " ...\n");
			ServerMain.label.setText("Webserver is running.");
			
			while(true) {
				connect = serverConnect.accept();
				// we read characters from the client via input stream on the socket
				in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
				// we get character output stream to client (for headers)
				out = new PrintWriter(connect.getOutputStream());
				// get binary output stream to client (for requested data)
				dataOut = new BufferedOutputStream(connect.getOutputStream());
				// get first line of the request from the client
				String input = in.readLine();
				// we parse the request with a string tokenizer
				StringTokenizer parse = new StringTokenizer(input);
				String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
				// we get file requested
				fileRequested = parse.nextToken().toLowerCase();

				// we support only GET and HEAD methods, we check
				if (!method.equals("GET") && !method.equals("HEAD")) {
					if (verbose) {
						System.out.println("501 Not Implemented : " + method + " method.");
					}

					// we return the not supported file to the client
					File file = new File(WEB_ROOT, method_not_supported);
					int fileLength = (int) file.length();
					String contentMimeType = "text/html";
					// read content to return to client
					byte[] fileData = readFileData(file, fileLength);

					// we send HTTP Headers with data to client
					out.println("HTTP/1.1 501 Not Implemented");
					out.println("Date: " + new Date());
					out.println("Content-type: " + contentMimeType);
					out.println("Content-length: " + fileLength);
					out.println(); // blank line between headers and content, very important !
					out.flush(); // flush character output stream buffer
					// file
					dataOut.write(fileData, 0, fileLength);
					dataOut.flush();

				} else {
					// GET or HEAD method
					if (fileRequested.endsWith("/")) {
						fileRequested += default_file;

					}

					File file = new File(WEB_ROOT, fileRequested);
					int fileLength = (int) file.length();
					String content = getContentType(fileRequested);

					if (method.equals("GET")) { // GET method so we return content
						byte[] fileData = readFileData(file, fileLength);

						// send HTTP Headers
						out.println("HTTP/1.1 200 OK");
						out.println("Date: " + new Date());
						out.println("Content-type: " + content);
						out.println("Content-length: " + fileLength);
						out.println(); // blank line between headers and content, very important !
						out.flush(); // flush character output stream buffer

						dataOut.write(fileData, 0, fileLength);
						dataOut.flush();
					}

					if (verbose) {
						System.out.println("File " + fileRequested + " of type " + content + " returned");
					}

				}

			}
		} catch (FileNotFoundException fnfe) {
			try {
				fileNotFound(out, dataOut, fileRequested);
			} catch (IOException ioe) {
				System.err.println("Error with file not found exception : " + ioe.getMessage());
			}

		} catch (IOException ioe) {
			System.err.println("Server error : " + ioe);
		} finally {
			try {
				in.close();
				out.close();
				dataOut.close();
				connect.close(); // we close socket connection
			} catch (Exception e) {
				System.err.println("Error closing stream : " + e.getMessage());
			}

			if (verbose) {
				System.out.println("Connection closed.\n");
			}
		}

	}

	private byte[] readFileData(File file, int fileLength) throws IOException {
		FileInputStream fileIn = null;
		byte[] fileData = new byte[fileLength];

		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null)
				fileIn.close();
		}

		return fileData;
	}

	// return supported MIME Types
	private String getContentType(String fileRequested) {
		if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html"))
			return "text/html";
		else
			return "text/plain";
	}

	private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
		File file = new File(WEB_ROOT, file_not_found);
		int fileLength = (int) file.length();
		String content = "text/html";
		byte[] fileData = readFileData(file, fileLength);

		out.println("HTTP/1.1 404 File Not Found");
		out.println("Date: " + new Date());
		out.println("Content-type: " + content);
		out.println("Content-length: " + fileLength);
		out.println(); // blank line between headers and content, very important !
		out.flush(); // flush character output stream buffer

		dataOut.write(fileData, 0, fileLength);
		dataOut.flush();

		if (verbose) {
			System.out.println("File " + fileRequested + " not found");
		}
	}

	public static Webserver getWebserver() {
		return instance;
	}

	public static void stop() throws IllegalStateException{
		if (getWebserver() == null) {
			throw new IllegalStateException("The web server hasn't been instanced yet!");
		}
		run_in_thread = 0;
		thread.stop();
		ServerMain.label.setText("Web server has been stopped");
	}
}