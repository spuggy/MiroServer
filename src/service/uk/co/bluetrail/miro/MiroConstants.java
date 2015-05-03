package uk.co.bluetrail.miro;

import java.awt.*;

/**
 * Created by richard on 24/04/15.
 */
public class MiroConstants {

    private static MiroConstants instance;

    public Color miroYellow;
    public Color miroGreen;
    public Color miroBlue;
    public Color miroRed;

    private MiroConstants() {


        this.miroYellow = Color.decode("#fbc726");
        this.miroGreen = Color.decode("#42b449");
        this.miroBlue = Color.decode("#43add5");
        this.miroRed = Color.decode("#d53f35");

    }

    public static MiroConstants getInstance() {
        if (MiroConstants.instance == null) {
            MiroConstants.instance = new MiroConstants();
        }

        return MiroConstants.instance;
    }
}






