package comp475.teammuon.projecthulk;

import java.awt.Dimension;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Toolkit;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

public class HistChartForm extends javax.swing.JInternalFrame
{
    public HistChartForm(String appTitle, String chartTitle, final XYDataset data)
    {
        super(appTitle,true,true,true,true);
        
        JFreeChart chart = createChart(data, chartTitle);
        ChartPanel chartPanel = new ChartPanel(chart);
        
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() 
                < Toolkit.getDefaultToolkit().getScreenSize().getWidth())
            chartPanel.setPreferredSize(new Dimension(
             4*(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*.2), 
             3*(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*.2)
        ));
        
        else
            chartPanel.setPreferredSize(new Dimension(
             4*(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*.2),
             3*(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*.2)
        ));
        
        setContentPane(chartPanel);
    }
    
    private JFreeChart createChart(final XYDataset dataset, String title) 
    {
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
        
        JFreeChart chart = ChartFactory.createXYAreaChart(title, 
                "Time (ns)", "Fall", dataset, PlotOrientation.VERTICAL, 
                rootPaneCheckingEnabled, rootPaneCheckingEnabled, 
                rootPaneCheckingEnabled);
        
        XYPlot plot = (XYPlot) chart.getPlot();
        TickUnitSource ticks = NumberAxis.createIntegerTickUnits();
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setStandardTickUnits(ticks);
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setStandardTickUnits(ticks);
        plot.setRenderer(renderer);
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        
        Stroke stroke = new BasicStroke(3f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_BEVEL);
        
        renderer.setBaseOutlineStroke(stroke);
        
        return chart;
    }
    
    public static void showChart(AppWorkspace topLevelParent, CR_Session crSess)
    {
        showChart(topLevelParent, crSess, false, 0, 0, false, 0, 0);
    }
    
    public static void showChart(AppWorkspace topLevelParent, 
            CR_Session crSess, boolean timeCheck, double timeMin, double timeMax,
            boolean enCheck, double enMin, double enMax)
    {
        double smallestTrigger = Double.MAX_VALUE;
        
        XYSeries[] XYPlotSeries = new XYSeries[4];
        XYPlotSeries[0] = new XYSeries("Port 1", true);
        XYPlotSeries[1] = new XYSeries("Port 2", true);
        XYPlotSeries[2] = new XYSeries("Port 3", true);
        XYPlotSeries[3] = new XYSeries("Port 4", true);
        
        if(crSess != null)
        {
            for(CR_Event x : crSess.getEvents())
                for(CR_EventData y : x.getEvents())
                    if(y.getTriggerCount1().getLong() != 0)
                        smallestTrigger 
                                = Math.min(smallestTrigger, calcEnergy(y) - 1);
            
            for(CR_Event x : crSess.getEvents())
                for(CR_EventData y : x.getEvents())
                    if(y.getTriggerCount1().getLong() != 0)
                    {
                        boolean xCheck = (!timeCheck 
                                || calcTime(y, smallestTrigger) >= timeMin
                                && calcTime(y, smallestTrigger) <= timeMax
                                );
                        boolean yCheck = (!enCheck
                                || calcEnergy(y) >= enMin
                                && calcEnergy(y) <= enMax
                                );
                        
                        //Gotta figure this out, yo./
                        if(xCheck && yCheck&&y.getPort()>=1&&y.getPort()<=4)
                            XYPlotSeries[y.getPort()-1].add(
                                    calcTime(y, smallestTrigger), calcEnergy(y));
                    }
            
            topLevelParent.showChartWindow("Fall vs. Time", crSess.getFilename(),
                    XYPlotSeries);
        }
    }
    
    private static double calcTime(CR_EventData y, double smallestTrigger)
    {
        return 10 * (y.getTriggerCount1().getLong() - smallestTrigger) + 1.25 
                * y.getRiseValue().getLong();
    }
    
    private static double calcEnergy(CR_EventData y)
    {
        return y.getFallValue().getLong();
    }
}