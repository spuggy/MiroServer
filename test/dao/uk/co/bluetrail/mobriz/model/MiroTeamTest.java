package uk.co.bluetrail.mobriz.model;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: RSpence1
 * Date: 19/07/2012
 * Time: 17:00
 * To change this template use File | Settings | File Templates.
 */
public class MiroTeamTest extends TestCase {



    public void testStripBadChars() {

        MiroTeam mt = null;

        mt = new MiroTeam();

        mt.setMiroTeamName(" \\ \\ \\ \\  \\ // / / / // / / / :: Arsenal");

        try {
        String name = mt.getMiroTeamNameFileName(".xhtml");

        File f = new File(name);


            f.createNewFile();
            assertTrue(f.exists());
        } catch (IOException e) {
           fail("Could not create file: " + e.getMessage());
        }


    }







}
