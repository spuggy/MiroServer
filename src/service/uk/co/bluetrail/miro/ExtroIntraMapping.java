package uk.co.bluetrail.miro;

import java.util.HashMap;

/**
 * Created by richard on 14/09/2014.
 */
public class ExtroIntraMapping {

    static HashMap<String,String> extroIntraMapping ;

    static String get(String key) {

        if(ExtroIntraMapping.extroIntraMapping==null) {
            ExtroIntraMapping.extroIntraMapping = new HashMap<String, String>();
            ExtroIntraMapping.init();
        }

        return ExtroIntraMapping.extroIntraMapping.get(key);


    }

    private static void init() {
        extroIntraMapping.put("DEAHEX","NHEX");
        extroIntraMapping.put("DEAMEX","NMEX");
        extroIntraMapping.put("DEALEX","NLEX");
        extroIntraMapping.put("DEAHIN","NHIN");
        extroIntraMapping.put("DEAMIN","NMIN");
        extroIntraMapping.put("DEALIN","NLIN");
        extroIntraMapping.put("DEOHEX","NHEX");
        extroIntraMapping.put("DEOMEX","NMEX");
        extroIntraMapping.put("DEOLEX","NLEX");
        extroIntraMapping.put("DEOHIN","NHIN");
        extroIntraMapping.put("DEOMIN","NMIN");
        extroIntraMapping.put("DEOLIN","NLIN");
        extroIntraMapping.put("DOAHEX","THEX");
        extroIntraMapping.put("DOAMEX","TMEX");
        extroIntraMapping.put("DOALEX","TLEX");
        extroIntraMapping.put("DOAHIN","THIN");
        extroIntraMapping.put("DOAMIN","TMIN");
        extroIntraMapping.put("DOALIN","TLIN");
        extroIntraMapping.put("DOEHEX","NHEX");
        extroIntraMapping.put("DOEMEX","NMEX");
        extroIntraMapping.put("DOELEX","NLEX");
        extroIntraMapping.put("DOEHIN","NHIN");
        extroIntraMapping.put("DOEMIN","NMIN");
        extroIntraMapping.put("DOELIN","NLIN");
        extroIntraMapping.put("DAOHEX","THEX");
        extroIntraMapping.put("DAOMEX","TMEX");
        extroIntraMapping.put("DAOLEX","TLEX");
        extroIntraMapping.put("DAOHIN","THIN");
        extroIntraMapping.put("DAOMIN","TMIN");
        extroIntraMapping.put("DAOLIN","TLIN");
        extroIntraMapping.put("DAEHEX","THEX");
        extroIntraMapping.put("DAEMEX","TMEX");
        extroIntraMapping.put("DAELEX","TLEX");
        extroIntraMapping.put("DAEHIN","THIN");
        extroIntraMapping.put("DAEMIN","TMIN");
        extroIntraMapping.put("DAELIN","TLIN");
        extroIntraMapping.put("EDOHEX","NHEX");
        extroIntraMapping.put("EDOMEX","NMEX");
        extroIntraMapping.put("EDOLEX","NLEX");
        extroIntraMapping.put("EDOHIN","NHIN");
        extroIntraMapping.put("EDOMIN","NMIN");
        extroIntraMapping.put("EDOLIN","NLIN");
        extroIntraMapping.put("EDAHEX","NHEX");
        extroIntraMapping.put("EDAMEX","NMEX");
        extroIntraMapping.put("EDALEX","NLEX");
        extroIntraMapping.put("EDAHIN","NHIN");
        extroIntraMapping.put("EDAMIN","NMIN");
        extroIntraMapping.put("EDALIN","NLIN");
        extroIntraMapping.put("EODHEX","FHEX");
        extroIntraMapping.put("EODMEX","FMEX");
        extroIntraMapping.put("EODLEX","FLEX");
        extroIntraMapping.put("EODHIN","FHIN");
        extroIntraMapping.put("EODMIN","FMIN");
        extroIntraMapping.put("EODLIN","FLIN");
        extroIntraMapping.put("EOAHEX","FHEX");
        extroIntraMapping.put("EOAMEX","FMEX");
        extroIntraMapping.put("EOALEX","FLEX");
        extroIntraMapping.put("EOAHIN","FHIN");
        extroIntraMapping.put("EOAMIN","FMIN");
        extroIntraMapping.put("EOALIN","FLIN");
        extroIntraMapping.put("EADHEX","NHEX");
        extroIntraMapping.put("EADMEX","NMEX");
        extroIntraMapping.put("EADLEX","NLEX");
        extroIntraMapping.put("EADHIN","NHIN");
        extroIntraMapping.put("EADMIN","NMIN");
        extroIntraMapping.put("EADLIN","NLIN");
        extroIntraMapping.put("EAOHEX","FHEX");
        extroIntraMapping.put("EAOMEX","FMEX");
        extroIntraMapping.put("EAOLEX","FLEX");
        extroIntraMapping.put("EAOHIN","FHIN");
        extroIntraMapping.put("EAOMIN","FMIN");
        extroIntraMapping.put("EAOLIN","FLIN");
        extroIntraMapping.put("ODEHEX","FHEX");
        extroIntraMapping.put("ODEMEX","FMEX");
        extroIntraMapping.put("ODELEX","FLEX");
        extroIntraMapping.put("ODEHIN","FHIN");
        extroIntraMapping.put("ODEMIN","FMIN");
        extroIntraMapping.put("ODELIN","FLIN");
        extroIntraMapping.put("ODAHEX","SHEX");
        extroIntraMapping.put("ODAMEX","SMEX");
        extroIntraMapping.put("ODALEX","SLEX");
        extroIntraMapping.put("ODAHIN","SHIN");
        extroIntraMapping.put("ODAMIN","SMIN");
        extroIntraMapping.put("ODALIN","SLIN");
        extroIntraMapping.put("OEDHEX","FHEX");
        extroIntraMapping.put("OEDMEX","FMEX");
        extroIntraMapping.put("OEDLEX","FLEX");
        extroIntraMapping.put("OEDHIN","FHIN");
        extroIntraMapping.put("OEDMIN","FMIN");
        extroIntraMapping.put("OEDLIN","FLIN");
        extroIntraMapping.put("OEAHEX","FHEX");
        extroIntraMapping.put("OEAMEX","FMEX");
        extroIntraMapping.put("OEALEX","FLEX");
        extroIntraMapping.put("OEAHIN","FHIN");
        extroIntraMapping.put("OEAMIN","FMIN");
        extroIntraMapping.put("OEALIN","FLIN");
        extroIntraMapping.put("OADHEX","SHEX");
        extroIntraMapping.put("OADMEX","SMEX");
        extroIntraMapping.put("OADLEX","SLEX");
        extroIntraMapping.put("OADHIN","SHIN");
        extroIntraMapping.put("OADMIN","SMIN");
        extroIntraMapping.put("OADLIN","SLIN");
        extroIntraMapping.put("OAEHEX","SHEX");
        extroIntraMapping.put("OAEMEX","SMEX");
        extroIntraMapping.put("OAELEX","SLEX");
        extroIntraMapping.put("OAEHIN","SHIN");
        extroIntraMapping.put("OAEMIN","SMIN");
        extroIntraMapping.put("OAELIN","SLIN");
        extroIntraMapping.put("ADEHEX","THEX");
        extroIntraMapping.put("ADEMEX","TMEX");
        extroIntraMapping.put("ADELEX","TLEX");
        extroIntraMapping.put("ADEHIN","THIN");
        extroIntraMapping.put("ADEMIN","TMIN");
        extroIntraMapping.put("ADELIN","TLIN");
        extroIntraMapping.put("ADOHEX","THEX");
        extroIntraMapping.put("ADOMEX","TMEX");
        extroIntraMapping.put("ADOLEX","TLEX");
        extroIntraMapping.put("ADOHIN","THIN");
        extroIntraMapping.put("ADOMIN","TMIN");
        extroIntraMapping.put("ADOLIN","TLIN");
        extroIntraMapping.put("AEDHEX","THEX");
        extroIntraMapping.put("AEDMEX","TMEX");
        extroIntraMapping.put("AEDLEX","TLEX");
        extroIntraMapping.put("AEDHIN","THIN");
        extroIntraMapping.put("AEDMIN","TMIN");
        extroIntraMapping.put("AEDLIN","TLIN");
        extroIntraMapping.put("AEOHEX","SHEX");
        extroIntraMapping.put("AEOMEX","SMEX");
        extroIntraMapping.put("AEOLEX","SLEX");
        extroIntraMapping.put("AEOHIN","SHIN");
        extroIntraMapping.put("AEOMIN","SMIN");
        extroIntraMapping.put("AEOLIN","SLIN");
        extroIntraMapping.put("AODHEX","SHEX");
        extroIntraMapping.put("AODMEX","SMEX");
        extroIntraMapping.put("AODLEX","SLEX");
        extroIntraMapping.put("AODHIN","SHIN");
        extroIntraMapping.put("AODMIN","SMIN");
        extroIntraMapping.put("AODLIN","SLIN");
        extroIntraMapping.put("AOEHEX","SHEX");
        extroIntraMapping.put("AOEMEX","SMEX");
        extroIntraMapping.put("AOELEX","SLEX");
        extroIntraMapping.put("AOEHIN","SHIN");
        extroIntraMapping.put("AOEMIN","SMIN");
        extroIntraMapping.put("AOELIN","SLIN");

    }


}
