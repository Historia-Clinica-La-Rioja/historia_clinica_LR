package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.BloodPressure;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.ChartDataset;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.EndTidal;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.O2Saturation;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.Pulse;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.DatasetConfigurator;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.interval.IntervalFormatStrategy;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.interval.IntervalFormatStrategySelector;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.utils.ShapesGenerator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.AxisLabelLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GenerateAnestheticChart {

    private final List<ChartDataset> chartDatasets = new ArrayList<>();
    private final Font ROBOTO = new Font("Roboto", Font.PLAIN, 12);
    private final Integer INTERVAL_MINUTE = 5;
    private final Integer DATASET_TO_DUMMY;
    private final IntervalFormatStrategySelector strategySelector;

    private IntervalFormatStrategy intervalFormatStrategy;

    public GenerateAnestheticChart(BloodPressure bloodPressure, Pulse pulse, O2Saturation o2Saturation, EndTidal endTidal, IntervalFormatStrategySelector strategySelector) {
        this.strategySelector = strategySelector;
        chartDatasets.add(bloodPressure);
        chartDatasets.add(pulse);
        chartDatasets.add(o2Saturation);
        chartDatasets.add(endTidal);
        DATASET_TO_DUMMY = endTidal.getIndexFromDatasetList();
    }

    public JFreeChart run(@NonNull List<XYDataset> datasets) {
        log.debug("Input parameters -> dataset {}", datasets);
        JFreeChart chart = createChart();

        if (datasets.isEmpty() || datasets.get(0).getSeriesCount() == 0 || datasets.get(0).getItemCount(0) == 0) {
            log.debug("Output -> empty chart");
            return chart;
        }

        XYPlot plot = (XYPlot) setPlot(chart);
        mapDatasetWithIndexConfiguration(datasets, plot);

        setIntervalFormatStrategy(datasets);

        setAxisX(plot);
        setAxisY(plot);
        setRendererDots(plot);
        setLegend(chart);

        log.debug("Output -> chart {}", chart);
        return chart;
    }

    private Integer getTotalSeries() {
        return chartDatasets.stream()
                .map(DatasetConfigurator::getTotalSeries)
                .reduce(0, Integer::sum);
    }

    private void mapDatasetWithIndexConfiguration(@NonNull List<XYDataset> datasets, XYPlot plot) {
        chartDatasets.forEach(chartDataset -> {
            int indexFromDataset = chartDataset.getIndexFromDatasetList();
            plot.setDataset(indexFromDataset, datasets.get(indexFromDataset));
        });
    }

    private void setIntervalFormatStrategy(List<XYDataset> datasets) {
        int numberOfMeasurements = datasets.get(0).getItemCount(0);
        this.intervalFormatStrategy = strategySelector.apply(numberOfMeasurements);
    }

    private void setLegend(JFreeChart chart) {

        var legend = chart.getLegend();
        legend.setPosition(RectangleEdge.TOP);
        legend.setHorizontalAlignment(HorizontalAlignment.CENTER);
        legend.setItemLabelPadding(new RectangleInsets(0, 0, 0, 6));
        legend.setBorder(1, 1, 1, 1);
        legend.setPadding(2, 2, 2, 2);
        legend.setMargin(2, 0, 4, 0);
        legend.setItemFont(ROBOTO);

        deleteDuplicatedValuesFromLegend(chart.getXYPlot());
    }

    private void deleteDuplicatedValuesFromLegend(XYPlot plot) {
        LegendItemCollection legendItems = new LegendItemCollection();
        int count = 0;
        Iterator<LegendItem> it = plot.getLegendItems().iterator();
        while (count < getTotalSeries() && it.hasNext()) { // ignore last dummy dataset
            LegendItem legendItem = it.next();
            legendItems.add(legendItem);
            count++;
        }
        plot.setFixedLegendItems(legendItems);
    }

    private void setAxisY(XYPlot plot) {
        chartDatasets.forEach(chartDataset -> chartDataset.setRange(plot));
        mapDatasetWithAxisY(plot);
    }

    private void setRendererDots(XYPlot plot) {

        chartDatasets.forEach(chartDataset -> {
            XYLineAndShapeRenderer renderer = intervalFormatStrategy.getRenderer();

            intervalFormatStrategy.setDotValuesLabels(renderer);

            mapDatasetWithRenderer(plot, chartDataset.getIndexFromDatasetList(), renderer);

            chartDataset.setDots(renderer);
        });

        makeDummyDatasetInvisible(plot);
    }

    private void mapDatasetWithRenderer(XYPlot plot, int chartDatasetPosition, XYLineAndShapeRenderer renderer) {
        plot.setRenderer(chartDatasetPosition, renderer);
    }

    private void makeDummyDatasetInvisible(XYPlot plot) {
        XYLineAndShapeRenderer noRenderer = new XYLineAndShapeRenderer(false, false);
        mapDatasetWithRenderer(plot, getDummyDatasetPosition(), noRenderer);
    }

    private int getDummyDatasetPosition() {
        return DATASET_TO_DUMMY + 1;
    }

    private void setAxisX(XYPlot plot) {
        setMainAxisX(plot);
        mapDatasetWithAxisX(plot);
        setSecondaryAxisX(plot);
    }

    private void mapDatasetWithAxisX(XYPlot plot) {
        chartDatasets.forEach(dataset -> dataset.mapDatasetWithAxisX(plot));
    }

    private void setSecondaryAxisX(XYPlot plot) {
        String dateFormat = "dd-MM-yyyy";
        DateAxis dayAxis = new DateAxis(null);
        dayAxis.setLabelLocation(AxisLabelLocation.HIGH_END);
        dayAxis.setVerticalTickLabels(false);
        dayAxis.setAutoTickUnitSelection(false);
        dayAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, INTERVAL_MINUTE));
        dayAxis.setTickMarksVisible(false);
        dayAxis.setLabelFont(ROBOTO);
        dayAxis.setTickLabelFont(ROBOTO);

        mapDummyDatasetWithSecondaryAxisX(plot, dayAxis);

        increaseRangeByFewMinutes(dayAxis);
        formatFirstAndChangeDayDate(plot, dayAxis, dateFormat);
    }

    private void mapDummyDatasetWithSecondaryAxisX(XYPlot plot, DateAxis dayAxis) {
        XYDataset dummyDataset = getDummyDataset(plot);
        plot.setDataset(getDummyDatasetPosition(), dummyDataset);
        plot.setDomainAxis(1, dayAxis);
        plot.mapDatasetToDomainAxis(getDummyDatasetPosition(), 1);
    }

    private XYDataset getDummyDataset(XYPlot plot) {
        return plot.getDataset(DATASET_TO_DUMMY);
    }

    private void setMainAxisX(XYPlot plot) {
        DateAxis rangeX = (DateAxis) plot.getDomainAxis();
        rangeX.setLabelLocation(AxisLabelLocation.LOW_END);
        rangeX.setVerticalTickLabels(false);
        rangeX.setAutoTickUnitSelection(false);
        rangeX.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, INTERVAL_MINUTE));
        rangeX.setTickMarksVisible(false);
        rangeX.setLabelFont(ROBOTO);
        setTickLabelFont(rangeX);

        plot.setDomainAxis(0, rangeX);

        increaseRangeByFewMinutes(rangeX);
        rangeX.setDateFormatOverride(intervalFormatStrategy.getDateFormat());
    }

    private void setTickLabelFont(DateAxis rangeX) {
        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.TRACKING, -0.075);
        Font tickLabelFont = ROBOTO.deriveFont(attributes);
        rangeX.setTickLabelFont(tickLabelFont);
    }

    private void formatFirstAndChangeDayDate(XYPlot plot, DateAxis domainAxis, String hourFormat) {
        var firstDataset = (TimeSeriesCollection) plot.getDataset(0);
        var firstDate = firstDataset.getSeries(0).getTimePeriod(0).getStart();
        domainAxis.setDateFormatOverride(new SimpleDateFormat(hourFormat) {

            private static final long serialVersionUID = -1294574239394895774L;

            @Override
            public SimpleDateFormat clone() throws AssertionError {
                throw new AssertionError();
            }

            @Override
            public StringBuffer format(@NonNull Date date, @NonNull StringBuffer toAppendTo, java.text.@NonNull FieldPosition pos) {
                StringBuffer buffer = new StringBuffer();

                if (date.equals(firstDate) || (date.getHours() == 0 && date.getMinutes() == 0))
                    buffer.append(super.format(date, toAppendTo, pos));

                return buffer;
            }
        });
    }

    private void increaseRangeByFewMinutes(DateAxis rangeX) {
        var date = rangeX.calculateHighestVisibleTickValue(new DateTickUnit(DateTickUnitType.MINUTE, INTERVAL_MINUTE));
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime newLastDateTime = localDateTime.plusMinutes(INTERVAL_MINUTE);
        rangeX.setMaximumDate(Date.from(newLastDateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    private void mapDatasetWithAxisY(XYPlot plot) {
        chartDatasets.forEach(dataset -> dataset.mapDatasetWithAxisY(plot));
    }

    private Plot setPlot(JFreeChart chart) {

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(true);
        plot.setOutlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlineStroke(ShapesGenerator.createDashedStroke());
        plot.setDomainCrosshairVisible(false);

        plot.setRangeGridlinesVisible(false);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setRangeCrosshairVisible(false);

        plot.setAxisOffset(new RectangleInsets(10., 6., 0., 10.));

        return plot;
    }

    private JFreeChart createChart() {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(null,
                null,
                null,
                null,
                true,
                false,
                false
        );
        chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }

}
