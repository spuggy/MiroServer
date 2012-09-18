package uk.co.bluetrail.miro;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: richard
 * Date: 18/09/2012
 * Time: 17:48
 * To change this template use File | Settings | File Templates.
 */
public class MiroXSLFileGenerator {



    public static void generate(File baseDir, String input, HashMap<String,String> strings ) throws IOException {

        String inputFileName =  baseDir.getAbsoluteFile() + File.separator + "xhtml" + File.separator+ input;
        String outputFileName =  baseDir.getAbsoluteFile() + File.separator + "out" + File.separator+ input;


        FileInputStream fstream = new FileInputStream(inputFileName);
        FileOutputStream ostream = new FileOutputStream(outputFileName);

        // Get the object of DataInputStream
        DataInputStream in = new DataInputStream(fstream);
        DataOutputStream out = new DataOutputStream(ostream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        //Read File Line By Line
        while ((strLine = br.readLine()) != null)   {
            // Print the content on the console
            out.writeBytes(replaceStrings(strLine,strings));
            out.writeBytes("\n")     ;
        }
        //Close the input stream
        in.close();


    }

    private static String replaceStrings(String strLine,HashMap<String,String> strings) {

        Iterator keys = strings.keySet().iterator();

        String s = "";
        while(keys.hasNext()) {
            s = (String) keys.next();
            if(strLine.indexOf(s)==-1) {
                //ignore
            } else {
              return strings.get(s);   //returns here assumes on one per line
            }
        }

        return strLine;
    }




}





