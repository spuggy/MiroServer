package uk.co.bluetrail.miro;

import junit.framework.TestCase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MiroPieChartGeneratorTest extends TestCase {
	
	
	public void testCreatePie() {

		int[] pieValues = { 30, 20, 15, 13 };
		String[] pieLabels1 = { "Energising Mode", "Organising Mode",
				"Analysing Mode", "Driving Mode" };

		String[] pieLabels2 = { "Leading", "Supporting", "Supplementary",
				"Dormant" };
		String[] pieLabels3 = { "Engaged", "Engaged", "Disengaged", "Disengaed" };

		boolean[] pieExplode = { false, false, true, true };

        MiroConstants constants = MiroConstants.getInstance();

        Color[] pieColors = { constants.miroYellow, constants.miroGreen, constants.miroBlue, constants.miroRed };

		MiroPieChartGenerator pieChart = new MiroPieChartGenerator("Your MiRo Results Chart", "Pivot Point, Extroverted intuition strongly expressed",pieValues,
				pieLabels1, pieLabels2, pieLabels3, pieExplode, pieColors,true,true,true);

		File filePath = new File("web/miro/out");
		
		
		String fileName = "test_piechart.jpg" ;
		
		try {
			pieChart.createPie(filePath.getAbsolutePath(), fileName);
			
		} catch (Exception e) {
			fail("Exception createPie " + e.toString());
		}
		
		File f = new File(filePath+"/"+fileName);
		
		if(!f.exists()) {
			fail("file no found");
		}
		
		
		
	}


    public void testCreateThumbPie() {

        int[] pieValues = { 30, 20, 15, 13 };
        String[] pieLabels1 = { "Energising Mode", "Organising Mode",
                "Analysing Mode", "Driving Mode" };

        String[] pieLabels2 = { "Leading", "Supporting", "Supplementary",
                "Dormant" };
        String[] pieLabels3 = { "Engaged", "Engaged", "Disengaged", "Disengaed" };

        boolean[] pieExplode = { false, false, true, true };

        MiroConstants constants = MiroConstants.getInstance();

        Color[] pieColors = { constants.miroYellow, constants.miroGreen, constants.miroBlue, constants.miroRed };

        MiroPieChartGenerator pieChart = new MiroPieChartGenerator("Your MiRo Results Chart", "Pivot Point, Extroverted intuition strongly expressed",pieValues,
                pieLabels1, pieLabels2, pieLabels3, pieExplode, pieColors,true,true,true);

        File filePath = new File("web/miro/out");

        String fileName = "test_thumb_piechart.jpg" ;

        File outputfile = new File(filePath+"/"+fileName);

        try {
            BufferedImage image = pieChart.getThumbnailPie(150, 150);
            ImageIO.write(image, "png", outputfile);

        } catch (Exception e) {
            fail("Exception createPie " + e.toString());
        }



        if(!outputfile.exists()) {
            fail("file no found");
        }



    }



}
