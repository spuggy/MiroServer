package uk.co.bluetrail.miro;

import com.lowagie.text.pdf.PdfReader;

import java.io.File;

public class MiroReportLeadership01Test extends  MiroReport11Test {

    public void testGenerateLeadership01() {

        MiroReport miroReport = getMiroReport(this.baseDirPath);

        MiroResponse miroResponse = new MiroResponse();

        initTestData(miroResponse);

        try {

            miroReport.generateReportLeadership01(miroResponse);

            String fileName = this.baseDirPath + "/out/Roger_Test_1_lship.pdf";

            File f = new File(fileName);

            if(!f.exists()) {
                fail("pdf not created for " + fileName);
            }

            PdfReader pdfReader = new PdfReader(fileName);
            int numOfPages = pdfReader.getNumberOfPages();

            assertEquals(3,numOfPages);
            

        } catch (Exception e) {
            e.printStackTrace();
            fail("failed with " + e.getMessage());
        }

    }




}
