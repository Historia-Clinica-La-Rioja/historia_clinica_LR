package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart;

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
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.interval.IntervalFormatStrategy;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.interval.IntervalFormatStrategySelector;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series.BloodPressureMax;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series.BloodPressureMin;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series.EndTidal;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series.O2Saturation;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series.Pulse;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series.Series;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.utils.ShapesGenerator;
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

    private final List<Series> series = new ArrayList<>();
    private final Font ROBOTO = new Font("Roboto", Font.PLAIN, 12);
    private final Integer INTERVAL_MINUTE = 5;
    private final IntervalFormatStrategySelector strategySelector;

    private IntervalFormatStrategy intervalFormatStrategy;

    public GenerateAnestheticChart(BloodPressureMin bloodPressureMin, BloodPressureMax bloodPressureMax, Pulse pulse, O2Saturation o2Saturation, EndTidal endTidal, IntervalFormatStrategySelector strategySelector) {
        this.strategySelector = strategySelector;
        series.add(bloodPressureMin);
        series.add(bloodPressureMax);
        series.add(pulse);
        series.add(o2Saturation);
        series.add(endTidal);
    }

    public JFreeChart run(@NonNull XYDataset dataset) {
        log.debug("Input parameters -> dataset {}", dataset);
        JFreeChart chart = createChart(dataset);

        if (dataset.getSeriesCount() == 0 || dataset.getItemCount(1) == 0) {
            log.debug("Output -> empty chart");
            return chart;
        }

        setIntervalFormatStrategy(dataset);

        XYPlot plot = (XYPlot) setPlot(chart);
        setAxisX(plot);
        setAxisY(plot);
        setDots(plot);
        setLegend(chart);

        log.debug("Output -> chart {}", chart);
        return chart;
    }

    private void setIntervalFormatStrategy(XYDataset dataset) {
        int numberOfMeasurements = dataset.getItemCount(0);
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

        deleteDuplicatedValuesFromLegend(chart);
    }

    private void deleteDuplicatedValuesFromLegend(JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();
        LegendItemCollection legendItems = new LegendItemCollection();
        int a = 0;
        for (Iterator<LegendItem> it = plot.getLegendItems().iterator(); it.hasNext(); ) {
            LegendItem legendItem = it.next();
            if (a < series.size()) { // just add series from dataset1, ignore dummy dataset
                legendItems.add(legendItem);
                a++;
            } else break;
        }
        plot.setFixedLegendItems(legendItems);
    }

    private void setAxisY(XYPlot plot) {
        series.forEach(series1 -> series1.setRange(plot));
    }

    private void setDots(XYPlot plot) {

        XYLineAndShapeRenderer renderer = intervalFormatStrategy.getRenderer();

        series.forEach(series -> series.setDots(renderer));

        renderer.setDefaultLinesVisible(false);

        intervalFormatStrategy.setDotValuesLabels(renderer);

        plot.setRenderer(0, renderer);

        setSecondDummyDatasetInvisible(plot);

    }

    private void setSecondDummyDatasetInvisible(XYPlot plot) {
        plot.setRenderer(1, new XYLineAndShapeRenderer(false, false));
    }


    private void setAxisX(XYPlot plot) {
        setMainAxisX(plot);
        setSecondaryAxisX(plot);
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

        // second dummy dataset configuration
        plot.setDataset(1, plot.getDataset());
        plot.setDomainAxis(1, dayAxis);
        plot.mapDatasetToDomainAxis(1, 1);

        increaseRangeByFewMinutes(dayAxis);
        formatFirstAndChangeDayDate(plot, dayAxis, dateFormat);
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

        plot.mapDatasetToDomainAxis(0, 0);

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
        var dataset = (TimeSeriesCollection) plot.getDataset();
        var firstDate = dataset.getSeries(0).getTimePeriod(0).getStart();
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

    private void mapSeriesWithAxisY(XYPlot plot) {
        series.forEach(series1 -> series1.mapSeriesWithAxis(plot));
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

        mapSeriesWithAxisY(plot);

        return plot;
    }

    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(null,
                null,
                null,
                dataset,
                true,
                false,
                false
        );
        chart.setBackgroundPaint(Color.WHITE);

        return chart;
    }

}
