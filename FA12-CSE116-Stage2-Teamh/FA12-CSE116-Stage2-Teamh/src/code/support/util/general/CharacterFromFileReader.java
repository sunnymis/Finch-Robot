package code.support.util.general;

/**
 * This is the CharacterFromFileReader that was used from the lab write-ups 
 * of CSE116.  It is solely used for the I/O functionality of the Stage1Finch
 * class.  
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class CharacterFromFileReader implements Iterator<Character> {

	private final static int EOF_VALUE = -1; 

	private FileReader inputStream;
	private int lastRead;

	public CharacterFromFileReader(String path) {
		try {
			inputStream = new FileReader(path);
			read();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			finish();
		}
	}

	private void finish() {
		lastRead = EOF_VALUE;
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void read() {
		try {
			lastRead = inputStream.read();
		} catch (IOException e) {
			finish();
		}
	}

	@Override
	public boolean hasNext() {
		return lastRead != EOF_VALUE;
	}

	@Override
	public Character next() {
		char c = (char) lastRead;
		read();
		return c;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
