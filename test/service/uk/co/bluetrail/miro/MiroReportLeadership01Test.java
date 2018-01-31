package uk.co.bluetrail.miro;

import com.lowagie.text.pdf.PdfReader;

import java.io.File;

public class MiroReportLeadership01Test extends  MiroReport11Test {


    class StubMiroReport extends MiroReport {

        private String MBTIValue = "";

        public StubMiroReport(File baseDirectory, int engagedScore, int excessScore, int latentScore) {
            super(baseDirectory,engagedScore,excessScore,latentScore);
        }

        protected String getMBTIValue() {
           return this.MBTIValue;
        }

        public void setMBTIValue(String val) {
            this.MBTIValue  = val;
        }


    }


    public void testGenerateAllPermsLeadership() {

        
        StubMiroReport stubMiroReport = new StubMiroReport(new File(this.baseDirPath),this.engagedScore,this.excessScore,this.latentScore);

        MiroResponse miroResponse = new MiroResponse();

        initTestData(miroResponse);


        String[] mbtiValues = {"ENFJ",	"ENFP",	"ENTJ",	"ENTP",	"ESFJ",	"ESFP",	"ESTJ",	"ESTP",	"INFJ",	"INFP",	"INTJ",	"INTP",	"ISFJ",	"ISFP",	"ISTJ",	"ISTP"};


        for(String mbtiValue: mbtiValues) {

            try {

                stubMiroReport.setMBTIValue(mbtiValue);
                stubMiroReport.generateReportLeadership01(miroResponse);

                String fileName = this.baseDirPath + "/out/Roger_Test_1_lship.pdf";

                File f = new File(fileName);

                if (!f.exists()) {
                    fail("pdf not created for " + fileName);
                }

                PdfReader pdfReader = new PdfReader(fileName);
                int numOfPages = pdfReader.getNumberOfPages();

                assertEquals(6, numOfPages);

                String permfileName = this.baseDirPath + "/out/Roger_" + mbtiValue + "_Test_1_lship.pdf";

                File permfile = new File(permfileName);

                f.renameTo(permfile);


            } catch (Exception e) {
                e.printStackTrace();
                fail("failed with " + e.getMessage());
            }
        }

    }

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

            assertEquals(6,numOfPages);


        } catch (Exception e) {
            e.printStackTrace();
            fail("failed with " + e.getMessage());
        }

    }






}
