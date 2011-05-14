package uk.co.bluetrail.miro;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * A utility to convert text files to utf-8.
 */
public class FileEncoder {
	public static void textToUtf8(String srcFile, String destFile) {
		try {
			FileInputStream fis = new FileInputStream(srcFile);
			byte[] contents = new byte[fis.available()];
			fis.read(contents, 0, contents.length);
			String asString = new String(contents, "ISO8859_1");
			byte[] newBytes = asString.getBytes("UTF8");
			FileOutputStream fos = new FileOutputStream(destFile);
			fos.write(newBytes);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();    
		}
	}
}