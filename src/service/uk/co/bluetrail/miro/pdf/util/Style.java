package uk.co.bluetrail.miro.pdf.util;

import java.util.HashMap;
import java.util.Map;

public class Style {

    private Map<String,String> styleElements ;
    private String[] validElements = new String[] {"margin-top", "margin-bottom", "font-family"};

    
    public Style(String styleStr) {
        
        styleElements = new HashMap<String, String>();

        try {

            String bits[] = styleStr.split(";");
            for(int i = 0 ; i < bits.length ; i++) {
                String valuePair[] = bits[i].split(":");
                if(isValidElement(valuePair)) {
                    styleElements.put(valuePair[0].trim().toLowerCase(), valuePair[1].trim().toLowerCase());
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
            

        }

    }

    private boolean isValidElement(String[] valuePair) {

        if(valuePair == null || valuePair.length < 2) {
            return false;
        }

        boolean validElement = false ;
        for(int i = 0; i < validElements.length; i++) {
            if(valuePair[0].trim().toLowerCase().equals(validElements[i])) {
                return true;
            }
        }

        return validElement;
        
    }

    public String getStringValue(String key) {

        if(styleElements != null && key != null) {
            return styleElements.get(key.toLowerCase());
        } else {
            return null;
        }
    }

    public Integer getIntegerValue(String key) {

        String strValue = getStringValue(key);
        if(strValue!=null) {
            try {
                return new Integer(Integer.parseInt(strValue));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }
}
