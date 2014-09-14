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
        extroIntraMapping.put("AEDLEX","FLEX");
        extroIntraMapping.put("AEDMEX","SMEX");
        extroIntraMapping.put("AEDHEX","THEX");
        extroIntraMapping.put("AEDLIN","TLIN");
        extroIntraMapping.put("AEDMIN","SMIN");
        extroIntraMapping.put("AEDHIN","SHIN");
        extroIntraMapping.put("AEOLEX","SLEX");
        extroIntraMapping.put("AEOMEX","SMEX");
        extroIntraMapping.put("AEOHEX","FHEX");
        extroIntraMapping.put("AEOLIN","SLIN");
        extroIntraMapping.put("AEOMIN","SMIN");
        extroIntraMapping.put("AEOHIN","SHIN");
        extroIntraMapping.put("ADELEX","TLEX");
        extroIntraMapping.put("ADEMEX","FMEX");
        extroIntraMapping.put("ADEHEX","FHEX");
        extroIntraMapping.put("ADELIN","TLIN");
        extroIntraMapping.put("ADEMIN","NMIN");
        extroIntraMapping.put("ADEHIN","THIN");
        extroIntraMapping.put("ADOLEX","SLEX");
        extroIntraMapping.put("ADOMEX","NMEX");
        extroIntraMapping.put("ADOHEX","THEX");
        extroIntraMapping.put("ADOLIN","SLIN");
        extroIntraMapping.put("ADOMIN","TMIN");
        extroIntraMapping.put("ADOHIN","THIN");
        extroIntraMapping.put("AOELEX","SLEX");
        extroIntraMapping.put("AOEMEX","NMEX");
        extroIntraMapping.put("AOEHEX","FHEX");
        extroIntraMapping.put("AOELIN","SLIN");
        extroIntraMapping.put("AOEMIN","FMIN");
        extroIntraMapping.put("AOEHIN","THIN");
        extroIntraMapping.put("AODLEX","SLEX");
        extroIntraMapping.put("AODMEX","NMEX");
        extroIntraMapping.put("AODHEX","SHEX");
        extroIntraMapping.put("AODLIN","TLIN");
        extroIntraMapping.put("AODMIN","FMIN");
        extroIntraMapping.put("AODHIN","FHIN");
        extroIntraMapping.put("EADLEX","SLEX");
        extroIntraMapping.put("EADMEX","SMEX");
        extroIntraMapping.put("EADHEX","FHEX");
        extroIntraMapping.put("EADLIN","FLIN");
        extroIntraMapping.put("EADMIN","FMIN");
        extroIntraMapping.put("EADHIN","NHIN");
        extroIntraMapping.put("EAOLEX","TLEX");
        extroIntraMapping.put("EAOMEX","FMEX");
        extroIntraMapping.put("EAOHEX","SHEX");
        extroIntraMapping.put("EAOLIN","NLIN");
        extroIntraMapping.put("EAOMIN","SMIN");
        extroIntraMapping.put("EAOHIN","SHIN");
        extroIntraMapping.put("EDALEX","SLEX");
        extroIntraMapping.put("EDAMEX","NMEX");
        extroIntraMapping.put("EDAHEX","NHEX");
        extroIntraMapping.put("EDALIN","NLIN");
        extroIntraMapping.put("EDAMIN","FMIN");
        extroIntraMapping.put("EDAHIN","FHIN");
        extroIntraMapping.put("EDOLEX","SLEX");
        extroIntraMapping.put("EDOMEX","FMEX");
        extroIntraMapping.put("EDOHEX","THEX");
        extroIntraMapping.put("EDOLIN","TLIN");
        extroIntraMapping.put("EDOMIN","FMIN");
        extroIntraMapping.put("EDOHIN","THIN");
        extroIntraMapping.put("EOALEX","FLEX");
        extroIntraMapping.put("EOAMEX","NMEX");
        extroIntraMapping.put("EOAHEX","FHEX");
        extroIntraMapping.put("EOALIN","SLIN");
        extroIntraMapping.put("EOAMIN","TMIN");
        extroIntraMapping.put("EOAHIN","SHIN");
        extroIntraMapping.put("EODLEX","FLEX");
        extroIntraMapping.put("EODMEX","SMEX");
        extroIntraMapping.put("EODHEX","NHEX");
        extroIntraMapping.put("EODLIN","FLIN");
        extroIntraMapping.put("EODMIN","SMIN");
        extroIntraMapping.put("EODHIN","NHIN");
        extroIntraMapping.put("DAELEX","SLEX");
        extroIntraMapping.put("DAEMEX","TMEX");
        extroIntraMapping.put("DAEHEX","SHEX");
        extroIntraMapping.put("DAELIN","NLIN");
        extroIntraMapping.put("DAEMIN","FMIN");
        extroIntraMapping.put("DAEHIN","FHIN");
        extroIntraMapping.put("DAOLEX","NLEX");
        extroIntraMapping.put("DAOMEX","SMEX");
        extroIntraMapping.put("DAOHEX","THEX");
        extroIntraMapping.put("DAOLIN","SLIN");
        extroIntraMapping.put("DAOMIN","TMIN");
        extroIntraMapping.put("DAOHIN","THIN");
        extroIntraMapping.put("DEALEX","TLEX");
        extroIntraMapping.put("DEAMEX","NMEX");
        extroIntraMapping.put("DEAHEX","NHEX");
        extroIntraMapping.put("DEALIN","TLIN");
        extroIntraMapping.put("DEAMIN","TMIN");
        extroIntraMapping.put("DEAHIN","THIN");
        extroIntraMapping.put("DEOLEX","TLEX");
        extroIntraMapping.put("DEOMEX","FMEX");
        extroIntraMapping.put("DEOHEX","THEX");
        extroIntraMapping.put("DEOLIN","NLIN");
        extroIntraMapping.put("DEOMIN","SMIN");
        extroIntraMapping.put("DEOHIN","FHIN");
        extroIntraMapping.put("DOALEX","TLEX");
        extroIntraMapping.put("DOAMEX","NMEX");
        extroIntraMapping.put("DOAHEX","THEX");
        extroIntraMapping.put("DOALIN","NLIN");
        extroIntraMapping.put("DOAMIN","TMIN");
        extroIntraMapping.put("DOAHIN","FHIN");
        extroIntraMapping.put("DOELEX","TLEX");
        extroIntraMapping.put("DOEMEX","FMEX");
        extroIntraMapping.put("DOEHEX","THEX");
        extroIntraMapping.put("DOELIN","FLIN");
        extroIntraMapping.put("DOEMIN","FMIN");
        extroIntraMapping.put("DOEHIN","NHIN");
        extroIntraMapping.put("OAELEX","FLEX");
        extroIntraMapping.put("OAEMEX","NMEX");
        extroIntraMapping.put("OAEHEX","FHEX");
        extroIntraMapping.put("OAELIN","TLIN");
        extroIntraMapping.put("OAEMIN","FMIN");
        extroIntraMapping.put("OAEHIN","FHIN");
        extroIntraMapping.put("OADLEX","SLEX");
        extroIntraMapping.put("OADMEX","NMEX");
        extroIntraMapping.put("OADHEX","THEX");
        extroIntraMapping.put("OADLIN","NLIN");
        extroIntraMapping.put("OADMIN","NMIN");
        extroIntraMapping.put("OADHIN","FHIN");
        extroIntraMapping.put("OEALEX","NLEX");
        extroIntraMapping.put("OEAMEX","NMEX");
        extroIntraMapping.put("OEAHEX","THEX");
        extroIntraMapping.put("OEALIN","TLIN");
        extroIntraMapping.put("OEAMIN","NMIN");
        extroIntraMapping.put("OEAHIN","SHIN");
        extroIntraMapping.put("OEDLEX","FLEX");
        extroIntraMapping.put("OEDMEX","SMEX");
        extroIntraMapping.put("OEDHEX","FHEX");
        extroIntraMapping.put("OEDLIN","TLIN");
        extroIntraMapping.put("OEDMIN","NMIN");
        extroIntraMapping.put("OEDHIN","SHIN");
        extroIntraMapping.put("ODALEX","TLEX");
        extroIntraMapping.put("ODAMEX","TMEX");
        extroIntraMapping.put("ODAHEX","SHEX");
        extroIntraMapping.put("ODALIN","FLIN");
        extroIntraMapping.put("ODAMIN","FMIN");
        extroIntraMapping.put("ODAHIN","NHIN");
        extroIntraMapping.put("ODELEX","NLEX");
        extroIntraMapping.put("ODEMEX","NMEX");
        extroIntraMapping.put("ODEHEX","FHEX");
        extroIntraMapping.put("ODELIN","FLIN");
        extroIntraMapping.put("ODEMIN","SMIN");
        extroIntraMapping.put("ODEHIN","NHIN");

    }


}
