/**
 * 
 */
package uk.co.bluetrail.mobriz.webapp.util;

/**
 * @author Richard Spence
 *
 */
	import java.io.DataInputStream;
	import java.io.DataOutputStream;
	import java.io.IOException;
	import java.io.InputStream;
import java.io.InputStreamReader;

	public class FileUploadReader {

		private InputStream dis = null;	
		private InputStreamReader reader = null;
		
		private static final int BUFFER_SIZE = 	8192; // Size of the input buffer
		private final char[] buffer = new char[BUFFER_SIZE]; // The input buffer
		
		private int charsRead = 0;
		private int charsLeft; // Number of characters left in the buffer
		private int nextChar; // Index of the next character in the buffer	
		private boolean eof = false;
		
		private static final char RECORD_END = '\n';
		
		public FileUploadReader(InputStream stream){
			this.dis = stream;
		}

		
	public String getNextLine() throws IOException {

			
			if(eof) {
				return null;
			}
			
			char c;
			StringBuffer sb = new StringBuffer(100);
				
				c = getNextChar(); //read past the start char
					
				int code ;
				
				while (c != RECORD_END && !eof) {
					code = (int) c ;
					if(c != 13 && c != 10 ) {
						sb.append(c);
					}
					
					c = getNextChar();
				} 
				
				
				
				
				
				return sb.toString();
				
				
			
			
			 
		}

		public char getNextChar() throws IOException {
			
			
			
			if(eof) {
				return (char) 0;
			}
			
			
			if (charsLeft == 0) {

				if(reader == null) {
					 reader = new InputStreamReader(dis);
				}
				
				charsLeft = reader.read(buffer, 0, BUFFER_SIZE);

				if (charsLeft < 0) {
			
					eof = true;
					return (char) 0;
				}
				charsRead = charsRead + charsLeft; // keep track of the number of
													// chars
				nextChar = 0;

			}
			charsLeft--;

			
			return buffer[nextChar++];
		}
		
	}


