package org.bh.gui.chart;

import java.util.Iterator;
import java.util.List;

import javax.swing.UIManager;

import org.bh.platform.IPlatformListener;
import org.bh.platform.PlatformEvent;
import org.bh.platform.Services;
import org.bh.platform.PlatformEvent.Type;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

/**
 *
 * BHwaterfallChart to create the waterfallChart
 *
 * <p>
 * waterfallChart is created and modified
 *
 * @author Lars
 * @version 0.1, 16.12.2009
 *
 */
@SuppressWarnings("serial")
public class BHwaterfallChart extends BHChart implements IBHAddGroupValue, IPlatformListener {

    private DefaultCategoryDataset dataset;
    private GroupedStackedBarRenderer groupRenderer;
    private KeyToGroupMap groupMap;
    private String[] categories;
    private CategoryPlot plot;
    private SubCategoryAxis domainAxis;

    protected BHwaterfallChart(
            final Dataset dataset, final String key, boolean legend, boolean tooltips) {
        super(key);
        this.dataset = (DefaultCategoryDataset) dataset;

        chart = ChartFactory.createWaterfallChart(translator.translate(key), translator.translate(key.concat(BHChart.DIMX)), translator.translate(key.concat(BHChart.DIMY)), this.dataset, PlotOrientation.VERTICAL, legend, tooltips, false);

        if ("Nimbus".equals(UIManager.getLookAndFeel().getName())) {
            chart.setBackgroundPaint(UIManager.getColor("Chart.background"));
        }
        CategoryItemRenderer renderer = chart.getCategoryPlot().getRenderer();
        renderer.setBaseItemLabelGenerator(new BHChartLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        renderer.setToolTipGenerator(new CategoryToolTipGenerator() {
			
			@Override
			public String generateToolTip(CategoryDataset dataset, int row, int column) {
				return Services.numberToString(dataset.getValue(row, column).doubleValue());
			}
		});

        final CategoryAxis domainAxis = chart.getCategoryPlot().getDomainAxis();
        domainAxis.setMaximumCategoryLabelLines(5);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        reloadText();
        Services.addPlatformListener(this);
    }

    @Override
    public final void addValue(Number value, Comparable row, Comparable<String> columnKey) {

        this.dataset.addValue(value, row, columnKey);
        chart.fireChartChanged();
    }

    @Override
    public void addValues(List<?> list) {
        Iterator<?> it = list.iterator();

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j <= i; j++) {
                while (it.hasNext()) {
                    this.dataset.addValue((Number) list.get(i), i,
                            (String) list.get(j));
                    chart.fireChartChanged();
                }
            }
        }
    }

    public void addValue(Number value, Comparable row, Comparable<String> columnKey, int catIdx) {
        this.addValue(value, row, columnKey);
        groupMap.mapKeyToGroup(row, categories[catIdx]);
        this.groupRenderer.setSeriesToGroupMap(groupMap);
    }

    public void setDefaultGroupSettings(String[] categories) {
        this.categories = categories;
        this.groupMap = new KeyToGroupMap(categories[0]);
//                    String cats = "";
//                    for(String s : categories){
//                        cats = cats + " / " + translator.translate(s);
//                    }
        this.domainAxis = new SubCategoryAxis("");
        this.domainAxis.setCategoryMargin(0.05);
        for (String s : categories) {
            this.domainAxis.addSubCategory(translator.translate(s));
        }
        //this.domainAxis.
        this.plot.setDomainAxis(domainAxis);
        this.groupRenderer.setItemMargin(0.0);
        this.plot.setRenderer(groupRenderer);
    }

    @Override
    public final void addValue(Number value, Comparable<String> columnKey) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "This method has not been implemented");
    }

    @Override
    public final void addSeries(Comparable<String> key, double[] values, int bins,
            final double minimum, final double maximum) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "This method has not been implemented");
    }

    @Override
    public void addSeries(Comparable<String> seriesKey, double[][] data, Integer amountOfValues, Integer average) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "This method has not been implemented");
    }

    /**
     * Handle PlatformEvents
     */
    @Override
    public void platformEvent(PlatformEvent e) {
        if (e.getEventType() == Type.LOCALE_CHANGED) {
            this.reloadText();
        }
    }
}
