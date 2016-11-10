package uk.co.bluetrail.miro;

import junit.framework.TestCase;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;

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
}
