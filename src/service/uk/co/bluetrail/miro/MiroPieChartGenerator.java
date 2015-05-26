package uk.co.bluetrail.miro;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.util.DefaultShadowGenerator;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.Rotation;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * PieChart generation using the input provided in main method Uses JFreeChat
 * 1.0.6
 * 
 * @author dhandapani
 */
public class MiroPieChartGenerator
{

	// instance declaration

	private int[] pieValues;

	private String[] pieLabels1;

	private String[] pieLabels2;

	private String[] pieLabels3;

	private boolean[] pieExplode;

	private Color[] pieColors;

	private String pieTitle;
    private String pieSubTitle;
	private Font titleFont = null;
	
	private boolean hideLegend = true;
	private boolean hideTitle = true ;
    private boolean hideSubTitle = true;
	
	
	public void setTitleFont(Font f){
		titleFont = f;
	}
	
	
	
	
	public MiroPieChartGenerator(String pieTitle, String pieSubTitle,int[] pieValues, String[] pieLabels1,
			String[] pieLabels2, String[] pieLabels3, boolean[] pieExplode,
			Color[] pieColors, boolean hideLegend, boolean hideTitle, boolean hideSubTitle)
	{

		this.pieTitle = pieTitle;
        this.pieSubTitle = pieSubTitle;

		this.pieValues = pieValues;
		this.pieLabels1 = pieLabels1;

		this.pieLabels2 = pieLabels2;
		this.pieLabels3 = pieLabels3;

		this.pieExplode = pieExplode;
		this.pieColors = pieColors;
		
		this.hideLegend = hideLegend;
		this.hideTitle = hideTitle;
        this.hideSubTitle = hideSubTitle;

	}

	/**
	 * @param filePath
	 *            the filePath and directory including last slash where the jpg
	 *            will be created.
	 */
	public void createPie(String filePath, String fileName)
	{

		/*
		 * this is the function that needs to be created. See the notes below.
		 * The resulting chart should be as near possible to 1. This function
		 * should take the information passed in the constructor and write a
		 * graphic file (can be jpeg/png or gif you decide) to filePath/fileName
		 * 2. The charting software should be http://www.jfree.org/jfreechart/
		 * 3. The first pie slice pieValues[0] should be positioned at the
		 * bottom of the pie and centered around 90 degrees - see attached
		 * diagram. 4. each pieSlice should be in the colors passed in
		 * pieColors. The colors have been passed as strings, happy to change
		 * this to other values if it is easy for java free chart 5. If a the
		 * pieExplode[SliceNumber] is true then that slice should be exploded
		 * out - see diagram. 6. the values in the the pieValues array should be
		 * added to the pie in a clockwise direction - so in the diagram
		 * yellowslice=0 greenslice=1 etc. 7. there will be a 4 slices in the
		 * array i.e pieValues.length = 4 8. Each slice will have a legend entry
		 * which should be as near as you can get it to the legend in the
		 * diagram. The first line in the legend is from the pieLabels1 array,
		 * the 2nd line is from the pieLabels2 array and the third line is from
		 * pieLables3
		 */

		DefaultPieDataset piedataset = new DefaultPieDataset();

		for (int i = 0; i < pieValues.length; i++)
		{
			// Sets name and value to each slice in pie data set
			piedataset.setValue(pieLabels1[i] + "\n" + pieLabels2[i] + "\n"
					+ pieLabels3[i], pieValues[i]);
		}

		// Creates JFreeChart
		if(hideTitle) {
			this.pieTitle = "";
		}
		
		JFreeChart jfreechart = ChartFactory.createPieChart(pieTitle,piedataset, true, true, false);  
		PiePlot pieplot = (PiePlot) jfreechart.getPlot();


        DefaultShadowGenerator dsg = new DefaultShadowGenerator(1,Color.white,1,1,1);

        pieplot.setShadowGenerator(dsg);

		// Sets bachgroung color of chart to white
		jfreechart.setBackgroundPaint(Color.white);
        jfreechart.setPadding(new RectangleInsets(-50.0, -50.0, -50.0, -50.0));


		pieplot.setBackgroundPaint(Color.WHITE);
		// To make legend display in Left side of Chart
		LegendTitle legend = jfreechart.getLegend();
		legend.setPosition(RectangleEdge.LEFT);
		legend.setVisible(!this.hideLegend);

//        if(!hideSubTitle && !this.pieSubTitle.equals("")) {
//            jfreechart.addSubtitle(new TextTitle(this.pieSubTitle,
//                    new Font("Dialog", Font.ITALIC, 14), Color.black,
//                    RectangleEdge.BOTTOM, HorizontalAlignment.CENTER,
//                    VerticalAlignment.TOP, new RectangleInsets(0.0, 0.0, 10.0, 0.0)));
//        }



		// Message to be displayed when there is no data
		pieplot.setNoDataMessage("No data available");
		// To remove border of chart.
		pieplot.setOutlineVisible(false);

		for (int i = 0; i < pieValues.length; i++)
		{
			// Applies color to each slice
			pieplot.setSectionPaint(pieLabels1[i] + "\n" + pieLabels2[i] + "\n"
					+ pieLabels3[i], pieColors[i]);
			if (pieExplode[i])
			{
				// Sets explode if it is configured
				pieplot.setExplodePercent(pieLabels1[i] + "\n" + pieLabels2[i]
						+ "\n" + pieLabels3[i], 0.1D);
			}
		}
		// Sets the angle for the first slice
		pieplot.setStartAngle(270 + getAngleForFirstSlice());
		pieplot.setDirection(Rotation.CLOCKWISE);

		// To avoid label display in chart
		pieplot.setLabelGenerator(null);


		try
		{

			// path seperator('/' or '\'). java will choose automatically based
			// on OS(Win/Lynx)
			char seperator = File.separatorChar;
			System.out.println("File saved under - " + filePath + fileName);

			// Saves the generated chart as JPG file
			ChartUtilities.saveChartAsJPEG(new File( filePath + seperator + fileName), jfreechart,
					650, 400);
			

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Calculates the angle for the first slice
	 * 
	 * @return
	 */
	private double getAngleForFirstSlice()
	{

		double angle = 0;
		try
		{
			double totalArea = 0;

			for (int i = 0; i < pieValues.length; i++)
			{
				totalArea = totalArea + pieValues[i];
			}

			angle = ((((pieValues[0] * 100) / totalArea) / 100) * 360) / 2;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return angle;
	}

	/**
	 * This is a set of example values as a test. This should run in the final
	 * implementation unchanged!!
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{

		// User configured inputs
		
		

		int[] pieValues = { 30, 20, 15, 13 };
		String[] pieLabels1 = { "Energising Mode", "Organising Mode",
				"Analysing Mode", "Driving Mode" };

		String[] pieLabels2 = { "Leading", "Supporting", "Supplementary",
				"Dormant" };
		String[] pieLabels3 = { "Engaged", "Engaged", "Disengaged", "Disengaed" };

		boolean[] pieExplode = { false, false, true, true };
		// String[] pieColors = { "yellow", "green", "blue", "red" };
		Color[] pieColors = { Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED };

		MiroPieChartGenerator pieChart = new MiroPieChartGenerator("Your MiRo Results Chart", "A subtitile",pieValues,
				pieLabels1, pieLabels2, pieLabels3, pieExplode, pieColors,false,false,false);

		pieChart.createPie("/users/richard/documents/", "demoPie.jpg");
	}
	
	public BufferedImage getThumbnailPie(int width, int height) throws Exception
	{
	
		
	
		DefaultPieDataset piedataset = new DefaultPieDataset();
	
		for (int i = 0; i < pieValues.length; i++)
		{
			// Sets name and value to each slice in pie data set
			piedataset.setValue(pieLabels1[i] + "\n" + pieLabels2[i] + "\n"
					+ pieLabels3[i], pieValues[i]);
		}
	
		// Creates JFreeChart
		JFreeChart jfreechart = ChartFactory.createPieChart(pieTitle,
				piedataset,false, true, false);  
		PiePlot pieplot = (PiePlot) jfreechart.getPlot();

        DefaultShadowGenerator dsg = new DefaultShadowGenerator(1,Color.white,1,1,1);

        pieplot.setShadowGenerator(dsg);

		// Sets bachgroung color of chart to white
		jfreechart.setBackgroundPaint(Color.white);
		
		if(titleFont != null) {
			TextTitle textTitle = new TextTitle(pieTitle,titleFont);
			jfreechart.setTitle(textTitle);
		}
		
	
		// Message to be displayed when there is no data
		pieplot.setNoDataMessage("No data available");
		// To remove border of chart.
		pieplot.setOutlineVisible(false);
		
	
		for (int i = 0; i < pieValues.length; i++)
		{
			// Applies color to each slice
			pieplot.setSectionPaint(pieLabels1[i] + "\n" + pieLabels2[i] + "\n"
					+ pieLabels3[i], pieColors[i]);
			if (pieExplode[i])
			{
				// Sets explode if it is configured
				pieplot.setExplodePercent(pieLabels1[i] + "\n" + pieLabels2[i]
						+ "\n" + pieLabels3[i], 0.1D);
			}
		}
		// Sets the angle for the first slice
		pieplot.setStartAngle(270 + getAngleForFirstSlice());
		pieplot.setDirection(Rotation.CLOCKWISE);
	
		// To avoid label display in chart
		pieplot.setLabelGenerator(null);
			
		return jfreechart.createBufferedImage(width, height) ;
		
		
	}
	
	
}