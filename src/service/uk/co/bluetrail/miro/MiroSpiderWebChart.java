package uk.co.bluetrail.miro;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

public class MiroSpiderWebChart {

	File filePath;
	
	
	public MiroSpiderWebChart(File filePath, Color blue) {
	
		this.filePath = filePath;
		
	}

	public void createChart(String fileName, int[] values, String[] labels)  {
	
		String series1 = "First";
        String series2 = "Second";
        

        
        double spokeSize = 5.0;
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        
        
        double val = 0;
        
        for(int i=0;i<values.length;i++) {
        	
        	  String label = + (values[i]) + "% " + labels[i]  ;
         	  dataset.addValue(spokeSize, series1, label);
        	  val = (values[i]/100.00)*spokeSize;
        	  dataset.addValue(val, series2, label);
        	  
        }
        
        

        

        SpiderWebPlot plot = new SpiderWebPlot(dataset);

        plot.setStartAngle(90);

        plot.setInteriorGap(0.30);
        
        plot.setLabelFont(new Font("SansSerif", Font.BOLD, 12));

        plot.setToolTipGenerator(new StandardCategoryToolTipGenerator());

        JFreeChart chart = new JFreeChart("", TextTitle.DEFAULT_FONT, plot, false);

        File chartFile = new File( filePath.getAbsoluteFile() + File.separator + "out" + File.separator+ fileName);

        try {
			ChartUtilities.saveChartAsPNG(chartFile, chart, 600, 400);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
      
		
		
		
		
	}

}
