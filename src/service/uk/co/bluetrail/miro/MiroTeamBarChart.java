package uk.co.bluetrail.miro;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.CategoryTextAnnotation;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

/**
 * A bar chart that uses a custom renderer to display different colors within a series.
 * No legend is displayed because there is only one series but the colors are not consistent.
 
 */
public class MiroTeamBarChart {

    /**
     * A custom renderer that returns a different color for each item in a single series.
     */
    class CustomRenderer extends BarRenderer {

        /** The colors. */
        private Paint[] colors;

        /**
         * Creates a new renderer.
         *
         * @param colors  the colors.
         */
        public CustomRenderer(final Paint[] colors) {
            this.colors = colors;
        }

        /**
         * Returns the paint for an item.  Overrides the default behaviour inherited from
         * AbstractSeriesRenderer.
         *
         * @param row  the series.
         * @param column  the category.
         *
         * @return The item color.
         */
        public Paint getItemPaint(final int row, final int column) {
            return this.colors[column % this.colors.length];
        }
    }


	private File filePath;

    
       
    

   
    
    public MiroTeamBarChart(File filePath) {
		this.filePath = filePath;
	}


	/**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a sample chart.
     * @throws IOException 
     */
    public void createBarChart(String title,String fileName, double[] barValues, String[] labels,Color[] barColors, double[] barLevels, String[] levelLabels) {
    	
    	class CustomRenderer extends BarRenderer 
    	{ 
    	 private Paint[] colors;
    	 public CustomRenderer(Color[] colors) 
    	 { 
    	    this.colors = colors;
    	 }
    	 public Paint getItemPaint(final int row, final int column) 
    	 { 
    	    // returns color for each column 
    	    return (this.colors[column % this.colors.length]); 
    	 } 
    	}
    	
    	
    	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    	dataset.addValue(0.00, "", "");
        for(int i = 0;i < barValues.length;i++) {
        	dataset.addValue(barValues[i], labels[i], labels[i]);
        }
    	
    
    	
    	final JFreeChart chart = ChartFactory.createBarChart(
                title,       // chart title
                "",               // domain axis label
                "",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // the plot orientation
                false,                    // include legend
                true,
                false
            );
    	
    	   chart.setBackgroundPaint(Color.white);
            
           
            // get a reference to the plot for further customisation...
            final CategoryPlot plot = chart.getCategoryPlot();
            BarRenderer renderer = new CustomRenderer(barColors);
            plot.setRenderer(renderer);
            
            
            
            //make all the ticks and labels invisisble
            renderer.setItemMargin(-1);
            plot.setRangeGridlinesVisible(false);
            plot.setBackgroundPaint(Color.WHITE);
            ValueAxis ra = plot.getRangeAxis();
            ra.setTickLabelsVisible(false);
            ra.setTickMarksVisible(false);

            for (int i = 0 ;  i < barLevels.length;i++) {
            	CategoryTextAnnotation a = new CategoryTextAnnotation(levelLabels[i], "", barLevels[i]);
                a.setCategoryAnchor(CategoryAnchor.START);
                a.setFont(new Font("SansSerif", Font.PLAIN, 12));
                a.setTextAnchor(TextAnchor.BOTTOM_LEFT);
                plot.addAnnotation(a);
                
                ValueMarker marker = new ValueMarker(barLevels[i],Color.BLACK,new BasicStroke(1.0f));
                plot.addRangeMarker(marker, Layer.BACKGROUND);
                
            }
            
            
            
            plot.setNoDataMessage("NO DATA!");
            
          
            File chartFile = new File( filePath.getAbsoluteFile() + File.separator + "out" + File.separator+ fileName);


            try {
				ChartUtilities.saveChartAsPNG(chartFile, chart, 550, 320);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      

    }



	
    
   
    
}