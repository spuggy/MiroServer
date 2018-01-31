package uk.co.bluetrail.miro;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import uk.co.bluetrail.miro.pdf.util.Context;

import java.awt.*;

public class MiroReportPDFGeneratorContext {


    public static Context leaderShipContext() {
        Context context = MiroReportPDFGeneratorContext.defaultContext();

        try {

            Font pLarge = context.getFont("PLARGE") ;
            Font pLargeBold = context.getFont("PLARGEBOLD");

            context.addFont("P",pLarge);
            context.addFont("PBOLD",pLargeBold);


        } catch (Exception e) {
           throw new uk.co.bluetrail.miro.pdf.util.MiroException(e.getMessage());
        }

        return context;

    }

    public static Context defaultContext() {

        Context context = new Context("/miro-reports");

        Font h1Font = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 44, Font.NORMAL);
        Font h3Font = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 16, Font.NORMAL);
        Font h4Font = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 13, Font.NORMAL);
        Font h2Font = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 12, Font.NORMAL);
        Font firstPageFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINRegular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 14, Font.NORMAL);
        Font firstPageFontBold = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINRegular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 14, Font.BOLD);
        Font pFontBold = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 75 Bold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 10, Font.NORMAL);
        Font pFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 10, Font.NORMAL);
        Font pFontSmall = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 8, Font.NORMAL);
        Font pFontLargeBold = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 75 Bold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 11, Font.BOLD);
        Font pFontLarge = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 11, Font.NORMAL);
        Font frontBannerFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 16, Font.NORMAL, Color.white);
        Font frontNameFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 34, Font.NORMAL, Color.black);
        Font frontCompanyFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINRegular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 16, Font.NORMAL);
        Font footerFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 8, Font.NORMAL);
        Font tocCompanyFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 16, Font.NORMAL);
        Font legTextFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 9, Font.NORMAL);
        Font legSubTextFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINRegular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 7, Font.NORMAL);
        Font barTextFont = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 10, Font.NORMAL);
        Font legTextFontSmall = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINBold.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 7, Font.NORMAL);
        Font legSubTextFontSmall = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "DINRegular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 5, Font.NORMAL);
        Font pFontSmallItalics = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 7, Font.ITALIC);
        Font pFontItalics = FontFactory.getFont(context.filePath + "/miro2/fonts/" + "Linotype - Helvetica LT 55 Roman.ttf", BaseFont.CP1252, BaseFont.EMBEDDED, 10, Font.ITALIC);

        try {

            context.addFont("H1", h1Font);
            context.addFont("H3", h3Font);
            context.addFont("H4", h4Font);
            context.addFont("H2", h2Font);
            context.addFont("P", pFont);
            context.addFont("PSMALL", pFont);
            context.addFont("PBOLD", pFontBold);
            context.addFont("PLARGE", pFontLarge);
            context.addFont("PLARGEBOLD", pFontLargeBold);
            context.addFont("FRONTBANNER", frontBannerFont);
            context.addColor("MIROBLUE", MiroConstants.getInstance().miroBlue);
            context.addColor("MIRORED", MiroConstants.getInstance().miroRed);
            context.addColor("MIROGREEN", MiroConstants.getInstance().miroGreen);
            context.addColor("MIROYELLOW", MiroConstants.getInstance().miroYellow);
            context.addColor("TITLE_BG_GREY", MiroConstants.getInstance().titleBgGrey);
            context.addFont("FRONTNAMEFONT", frontNameFont);
            context.addFont("FRONTCOMPANYFONT", frontCompanyFont);
            context.addFont("FOOTERFONT", footerFont);
            context.addFont("BARTEXTFONT", barTextFont);
            context.addFont("TOCCOMPANYFONT", tocCompanyFont);
            context.addFont("FIRSTPAGEFONT", firstPageFont);
            context.addFont("FIRSTPAGEFONTBOLD", firstPageFontBold);
            context.addFont("LEGTEXTFONT", legTextFont);
            context.addFont("LEGSUBTEXTFONT", legSubTextFont);
            context.addFont("LEGTEXTFONTSMALL", legTextFontSmall);
            context.addFont("LEGSUBTEXTFONTSMALL", legSubTextFontSmall);
            context.addFont("SMALLITALICS", pFontSmallItalics);
            context.addFont("ITALICS", pFontItalics);



        } catch (Exception e) {
            throw new uk.co.bluetrail.miro.pdf.util.MiroException(e.getMessage());
        }


        return context;
    }


}
