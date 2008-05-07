package pairwisetesting.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class TextFile extends ArrayList<String> {
	
	private static final long serialVersionUID = -20207102858888293L;

	/**
	 * Read a file as a single string
	 */
	public static String read(String fileName) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(fileName).getAbsoluteFile()));
			try {
				String s = null;
				while ((s = in.readLine()) != null) {
					sb.append(s).append("\n");
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}
	
	
	/**
	 * Write text to a single file
	 */
	public static void write(String fileName, String text) {
		try {
			File outFile = new File(fileName).getAbsoluteFile();
			outFile.getParentFile().mkdirs();
			PrintWriter out = new PrintWriter(outFile);
			
			try {
				out.print(text);
				//out.write(text);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Read a file, split by any regular expression
	 */
	public TextFile(String fileName, String splitter) {
		super(Arrays.asList(read(fileName).split(splitter)));
		// Regular expression split() often leaves an empty String at the first position
		if (get(0).equals("")) remove(0);
	}
	
	// Read by lines
	public TextFile(String fileName) {
		this(fileName, "\n");
	}
	
	public void write(String fileName) {
		try {
			File outFile = new File(fileName).getAbsoluteFile();
			outFile.getParentFile().mkdirs();
			PrintWriter out = new PrintWriter(outFile);
			
			try {
				for (String item : this)
					out.println(item);
					//out.write(item);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
