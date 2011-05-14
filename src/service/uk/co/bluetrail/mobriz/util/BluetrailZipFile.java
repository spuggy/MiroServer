package uk.co.bluetrail.mobriz.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BluetrailZipFile {

	private ZipOutputStream out ;
	private byte[] buffer = new byte[18024];
	private String zipFileName ;
	
	public BluetrailZipFile(String zipFileName) {		
		this.zipFileName = zipFileName ; 
	}
	
	public void open() throws Exception{
		out =  new ZipOutputStream(new FileOutputStream(zipFileName));
		out.setLevel(Deflater.DEFAULT_COMPRESSION);
	}
	
	public void append(String fileName) throws Exception{
		
		// Associate a file input stream for the current file
		FileInputStream in = new FileInputStream(fileName);

		// Add ZIP entry to output stream.
		out.putNextEntry(new ZipEntry(fileName));

		// Transfer bytes from the current file to the ZIP file
		// out.write(buffer, 0, in.read(buffer));

		int len;
		while ((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}

		// Close the current entry
		out.closeEntry();

		// Close the current file input stream
		in.close();
	}
	
	public void close() throws Exception{
		out.close();
	}
	
	
}
