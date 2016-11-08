package gui;

import java.io.*;

/** This class enables reading and writing text from/to a file.
 * @author MPatashnik 
 */
public class TextIO {
	
	/** Constructor: It prevents instantiation of TextIO */
	private TextIO() {}
	
	/** Write s to text file f. 
     * Throw anIOException if there is a problem with the file.*/
	public static void write(String f, String s) throws IOException {
		write(new File(f), s);
	}

	/** Write s to text file f.
	 * Throw anIOException if there is a problem with the file. */
	public static void write(File f, String s) throws IOException {
		FileWriter fr= new FileWriter(f);
		BufferedWriter br= new BufferedWriter(fr);

		br.write(s);

		br.flush(); 
		br.close();
	}
	
	/** Read text file f and return it as a String, with newlines as necessary.
     * Throw anIOException if there is a problem with the file. */
	public static String read(File f) throws IOException {
		FileReader fr= null;
		BufferedReader br= null;
		try {
			fr= new FileReader(f);
			br= new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String s= "";
		String line= br.readLine();
		// inv: all input lines preceding line have been placed in s, preceded by '\n'
		while (line != null) {
		    s= s + "\n" + line;
		    line= br.readLine();
		}
		
		br.close();
		return s.substring(1); //Cut off preceding newline character
	}
	
	/** Read text file f and return it with one line in each array element.
     * Throw anIOException if there is a problem with the file. */
	public static String[] readToArray(File f) throws IOException {
		return read(f).split("\\n");
	}
}