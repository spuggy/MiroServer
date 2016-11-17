package uk.co.bluetrail.miro;

import junit.framework.TestCase;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.util.Random;

/**
 * Created by richard on 10/11/2016.
 */
public class UtilsTest extends TestCase {

    public boolean isValidEmailAddress(String email) {

        if(email==null) {
            return false;
        }

        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public void testMailValidation() {

        assertEquals(true,  isValidEmailAddress("richard.spence@gmail.com"));
        assertEquals(false, isValidEmailAddress(null));
        assertEquals(false, isValidEmailAddress("fuck"));

    }

    public void testRandon() {

        Random rand = new Random();

        int max = 2;
        int min = 0;

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        assertTrue((randomNum == 0 || randomNum == 1|| randomNum == 2));

    }
}
