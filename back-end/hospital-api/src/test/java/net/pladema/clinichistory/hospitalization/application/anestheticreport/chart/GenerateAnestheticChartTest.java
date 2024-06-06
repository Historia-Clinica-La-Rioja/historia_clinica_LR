package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.dataset.BloodPressure;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.interval.DefaultRendererStrategy;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.interval.IntervalFormatStrategySelector;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.interval.RendererToPlotManyPointsStrategy;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.dataset.bloodpressure.BloodPressureMax;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.dataset.bloodpressure.BloodPressureMin;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.dataset.EndTidal;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.dataset.O2Saturation;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.dataset.Pulse;
import org.apache.commons.codec.digest.DigestUtils;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;


@ExtendWith({MockitoExtension.class})
@Disabled
class GenerateAnestheticChartTest {

    public static final int WIDTH = 700;
    public static final int HEIGHT = 420;
    final String PATH = "src/test/resources/charts/anestheticreport";
    Function<String, String> pathnameResolver = (String name) -> String.format("%s/%s.%s", PATH, name, "png");

    static final BloodPressureMin BLOOD_PRESSURE_MIN = new BloodPressureMin();
    static final BloodPressureMax BLOOD_PRESSURE_MAX = new BloodPressureMax();
    static final Pulse PULSE = new Pulse();
    static final O2Saturation O_2_SATURATION = new O2Saturation();
    static final EndTidal END_TIDAL = new EndTidal();
    static final BloodPressure BLOOD_PRESSURE = new BloodPressure(BLOOD_PRESSURE_MIN, BLOOD_PRESSURE_MAX);
    ;
    final GenerateAnestheticChart generateAnestheticChart;

    GenerateAnestheticChartTest() {
        DefaultRendererStrategy defaultRendererStrategy = new DefaultRendererStrategy();
        RendererToPlotManyPointsStrategy rendererToPlotManyPointsStrategy = new RendererToPlotManyPointsStrategy();

        this.generateAnestheticChart = new GenerateAnestheticChart(BLOOD_PRESSURE, PULSE, O_2_SATURATION, END_TIDAL,
                new IntervalFormatStrategySelector(defaultRendererStrategy, rendererToPlotManyPointsStrategy));
    }

    static TimeSeries createTimeSeriesBloodPressureMin(boolean overlimit) {

        TimeSeries s = BLOOD_PRESSURE_MIN.createTimeSeries();
        s.add(new Minute(30, new Hour(23, new Day(14, 2, 2024))), 50);
        s.add(new Minute(35, new Hour(23, new Day(14, 2, 2024))), 54);
        s.add(new Minute(40, new Hour(23, new Day(14, 2, 2024))), 59);
        s.add(new Minute(45, new Hour(23, new Day(14, 2, 2024))), 58);
        s.add(new Minute(50, new Hour(23, new Day(14, 2, 2024))), 67);
        s.add(new Minute(55, new Hour(23, new Day(14, 2, 2024))), 79);
        s.add(new Minute(0, new Hour(0, new Day(15, 2, 2024))), 98);
        s.add(new Minute(5, new Hour(0, new Day(15, 2, 2024))), 75);
        s.add(new Minute(10, new Hour(0, new Day(15, 2, 2024))), 79);
        s.add(new Minute(15, new Hour(0, new Day(15, 2, 2024))), 71);
        s.add(new Minute(20, new Hour(0, new Day(15, 2, 2024))), 75);
        s.add(new Minute(25, new Hour(0, new Day(15, 2, 2024))), 85);

        if (overlimit)
            s.add(new Minute(30, new Hour(0, new Day(15, 2, 2024))), 80);

        return s;

    }

    static TimeSeries createTimeSeriesBloodPressureMax(boolean overlimit) {

        TimeSeries s = BLOOD_PRESSURE_MAX.createTimeSeries();
        s.add(new Minute(30, new Hour(23, new Day(14, 2, 2024))), 110);
        s.add(new Minute(35, new Hour(23, new Day(14, 2, 2024))), 112);
        s.add(new Minute(40, new Hour(23, new Day(14, 2, 2024))), 114);
        s.add(new Minute(45, new Hour(23, new Day(14, 2, 2024))), 114);
        s.add(new Minute(50, new Hour(23, new Day(14, 2, 2024))), 117);
        s.add(new Minute(55, new Hour(23, new Day(14, 2, 2024))), 137);
        s.add(new Minute(0, new Hour(0, new Day(15, 2, 2024))), 128);
        s.add(new Minute(5, new Hour(0, new Day(15, 2, 2024))), 131);
        s.add(new Minute(10, new Hour(0, new Day(15, 2, 2024))), 129);
        s.add(new Minute(15, new Hour(0, new Day(15, 2, 2024))), 125);
        s.add(new Minute(20, new Hour(0, new Day(15, 2, 2024))), 111);
        s.add(new Minute(25, new Hour(0, new Day(15, 2, 2024))), 130);

        if (overlimit)
            s.add(new Minute(30, new Hour(0, new Day(15, 2, 2024))), 100);

        return s;

    }

    static TimeSeries createTimeSeriesPulse(boolean overlimit) {

        TimeSeries s = PULSE.createTimeSeries();
        s.add(new Minute(30, new Hour(23, new Day(14, 2, 2024))), 70);
        s.add(new Minute(35, new Hour(23, new Day(14, 2, 2024))), 72);
        s.add(new Minute(40, new Hour(23, new Day(14, 2, 2024))), 74);
        s.add(new Minute(45, new Hour(23, new Day(14, 2, 2024))), 84);
        s.add(new Minute(50, new Hour(23, new Day(14, 2, 2024))), 87);
        s.add(new Minute(55, new Hour(23, new Day(14, 2, 2024))), 97);
        s.add(new Minute(0, new Hour(0, new Day(15, 2, 2024))), 100);
        s.add(new Minute(5, new Hour(0, new Day(15, 2, 2024))), 85);
        s.add(new Minute(10, new Hour(0, new Day(15, 2, 2024))), 82);
        s.add(new Minute(15, new Hour(0, new Day(15, 2, 2024))), 83);
        s.add(new Minute(20, new Hour(0, new Day(15, 2, 2024))), 89);
        s.add(new Minute(25, new Hour(0, new Day(15, 2, 2024))), 85);

        if (overlimit)
            s.add(new Minute(30, new Hour(0, new Day(15, 2, 2024))), 77);

        return s;

    }

    static TimeSeries createTimeSeriesO2Saturation(boolean overlimit) {

        TimeSeries s = O_2_SATURATION.createTimeSeries();
        s.add(new Minute(30, new Hour(23, new Day(14, 2, 2024))), 40);
        s.add(new Minute(35, new Hour(23, new Day(14, 2, 2024))), 53);
        s.add(new Minute(40, new Hour(23, new Day(14, 2, 2024))), 74);
        s.add(new Minute(45, new Hour(23, new Day(14, 2, 2024))), 84);
        s.add(new Minute(50, new Hour(23, new Day(14, 2, 2024))), 87);
        s.add(new Minute(55, new Hour(23, new Day(14, 2, 2024))), 51);
        s.add(new Minute(0, new Hour(0, new Day(15, 2, 2024))), 42);
        s.add(new Minute(5, new Hour(0, new Day(15, 2, 2024))), 68);
        s.add(new Minute(10, new Hour(0, new Day(15, 2, 2024))), 67);
        s.add(new Minute(15, new Hour(0, new Day(15, 2, 2024))), 68);
        s.add(new Minute(20, new Hour(0, new Day(15, 2, 2024))), 69);
        s.add(new Minute(25, new Hour(0, new Day(15, 2, 2024))), 82);

        if (overlimit)
            s.add(new Minute(30, new Hour(0, new Day(15, 2, 2024))), 74);

        return s;
    }

    static TimeSeries createTimeSeriesEndTidal(boolean overlimit) {

        TimeSeries s = END_TIDAL.createTimeSeries();
        s.add(new Minute(30, new Hour(23, new Day(14, 2, 2024))), 160);
        s.add(new Minute(35, new Hour(23, new Day(14, 2, 2024))), 180);
        s.add(new Minute(40, new Hour(23, new Day(14, 2, 2024))), 200);
        s.add(new Minute(45, new Hour(23, new Day(14, 2, 2024))), 150);
        s.add(new Minute(50, new Hour(23, new Day(14, 2, 2024))), 170);
        s.add(new Minute(55, new Hour(23, new Day(14, 2, 2024))), 140);
        s.add(new Minute(0, new Hour(0, new Day(15, 2, 2024))), 210);
        s.add(new Minute(5, new Hour(0, new Day(15, 2, 2024))), 230);
        s.add(new Minute(10, new Hour(0, new Day(15, 2, 2024))), 210);
        s.add(new Minute(15, new Hour(0, new Day(15, 2, 2024))), 230);
        s.add(new Minute(20, new Hour(0, new Day(15, 2, 2024))), 210);
        s.add(new Minute(25, new Hour(0, new Day(15, 2, 2024))), 220);

        if (overlimit)
            s.add(new Minute(30, new Hour(0, new Day(15, 2, 2024))), 200);

        return s;
    }

    static List<XYDataset> createStaticDataset(boolean overlimit) {
        return createDatasets(createTimeSeriesBloodPressureMin(overlimit),
                createTimeSeriesBloodPressureMax(overlimit),
                createTimeSeriesPulse(overlimit),
                createTimeSeriesO2Saturation(overlimit),
                createTimeSeriesEndTidal(overlimit)
        );
    }

    static Stream<Arguments> createEmptyDataset() {
        String expectedMD5 = "3a89cb95867429f3a6ae3da930165d06";
        return Stream.of(Arguments.arguments("static_empty", createDatasets(
                BLOOD_PRESSURE_MIN.createTimeSeries(), BLOOD_PRESSURE_MAX.createTimeSeries(), PULSE.createTimeSeries(),
                O_2_SATURATION.createTimeSeries(), END_TIDAL.createTimeSeries()), expectedMD5));
    }

    static Stream<Arguments> createStaticDatasetWithoutOverlimit() {
        String expectedMD5 = "8a3b09402607fc900061061c4fca2135";
        return Stream.of(Arguments.arguments("static_twelve_points", createStaticDataset(false), expectedMD5));
    }

    static Stream<Arguments> createStaticDatasetWithOverlimit() {
        String expectedMD5 = "ca0e580bf15f04383316797ea4203247";
        return Stream.of(Arguments.arguments("static_thirteen_points", createStaticDataset(true), expectedMD5));
    }

    static List<XYDataset> createDatasets(TimeSeries seriesTAMin, TimeSeries seriesTAMax, TimeSeries seriesPulso, TimeSeries seriesSaturacionO2, TimeSeries seriesEndTidal) {
        TimeSeriesCollection datasetTA = new TimeSeriesCollection();
        TimeSeriesCollection datasetPulso = new TimeSeriesCollection();
        TimeSeriesCollection datasetSaturacionO2 = new TimeSeriesCollection();
        TimeSeriesCollection datasetEndTidal = new TimeSeriesCollection();

        datasetTA.addSeries(seriesTAMin);
        datasetTA.addSeries(seriesTAMax);

        datasetPulso.addSeries(seriesPulso);
        datasetSaturacionO2.addSeries(seriesSaturacionO2);
        datasetEndTidal.addSeries(seriesEndTidal);

        List<XYDataset> datasets = new ArrayList<>();
        datasets.add(datasetTA);
        datasets.add(datasetPulso);
        datasets.add(datasetSaturacionO2);
        datasets.add(datasetEndTidal);

        return datasets;
    }

    static List<XYDataset> createRandomFullDataset(int minute, int hour, int day, int month, int year, int incrementMinutes, int n) {
        TimeSeries s1 = BLOOD_PRESSURE_MIN.createTimeSeries();
        TimeSeries s2 = BLOOD_PRESSURE_MAX.createTimeSeries();
        TimeSeries s3 = PULSE.createTimeSeries();
        TimeSeries s4 = O_2_SATURATION.createTimeSeries();
        TimeSeries s5 = END_TIDAL.createTimeSeries();

        RegularTimePeriod time = new Minute(minute, new Hour(hour, new Day(day, month, year)));
        int i = 0;
        do {

            int value1 = BLOOD_PRESSURE.getPosibleRandomValue();
            int value2 = BLOOD_PRESSURE.getPosibleRandomValue();
            if (value1 > value2) {
                var tmp = value1;
                value1 = value2;
                value2 = tmp;
            }
            int value3 = PULSE.getPosibleRandomValue();
            int value4 = O_2_SATURATION.getPosibleRandomValue();
            int value5 = END_TIDAL.getPosibleRandomValue();

            s1.add(time, value1);
            s2.add(time, value2);
            s3.add(time, value3);
            s4.add(time, value4);
            s5.add(time, value5);


            for (int j = 0; j < incrementMinutes; j++) {
                time = time.next();
            }
            i++;
        } while (i < n);

        return createDatasets(s1, s2, s3, s4, s5);
    }

    static Stream<Arguments> createThreeRandomDatasets() {
        int minute = 25, hour = 22, day = 21, month = 3, year = 2024, incrementMinutes = 5;
        return Stream.of(
                Arguments.arguments("random_eight_points", createRandomFullDataset(minute, hour, day, month, year, incrementMinutes, 8)),
                Arguments.arguments("random_twenty_points", createRandomFullDataset(minute, hour, day, month, year, incrementMinutes, 20)),
                Arguments.arguments("random_forty_points", createRandomFullDataset(minute, hour, day, month, year, incrementMinutes, 40))
        );
    }

    static String getFailSaveCommonMessage(String pathname) {
        return String.format("could not save chart: %s", pathname);
    }

    void saveImageAsSVG(JFreeChart chart, String pathname) {
        int width = 1000;
        int height = 600;
        var g2 = new SVGGraphics2D(width, height);
        chart.draw(g2, new Rectangle(0, 0, width, height));

        try {
            SVGUtils.writeToSVG(new File(pathname), g2.getSVGElement());
        } catch (IOException e) {
            fail(getFailSaveCommonMessage(pathname));
        }
    }

    void saveImageAsPNG(JFreeChart chart, String pathname) {
        try {
            ChartUtils.saveChartAsPNG(new File(pathname), chart, WIDTH, HEIGHT);
        } catch (IOException e) {
            fail(getFailSaveCommonMessage(pathname));
        }
    }

    static String calcMD5(File archivo, String pathname) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(archivo);
            String md5 = DigestUtils.md5Hex(inputStream);
            inputStream.close();
            return md5;
        } catch (IOException e) {
            fail(String.format("Fail to get MD5 from %s", pathname));
        }
        return null;
    }

    @DisplayName("Test integrity chart with static datasets")
    @ParameterizedTest(name = "{index} ==> {0}")
    @MethodSource({"createEmptyDataset", "createStaticDatasetWithoutOverlimit", "createStaticDatasetWithOverlimit"})
    @Timeout(value = 3)
    void run_static_dataset(String name, List<XYDataset> datasets, String expectedMD5) {

        // Arrange
        String pathname = pathnameResolver.apply(name);

        // Act
        JFreeChart jchart = generateAnestheticChart.run(datasets);

        // Assert
        this.saveImageAsPNG(jchart, pathname);
        String md5File = calcMD5(new File(pathname), pathname);

        assertNotNull(md5File);
        assertEquals(expectedMD5, md5File);

    }

    @Disabled("Test to manually review the result")
    @DisplayName("Test integrity chart with random datasets")
    @ParameterizedTest(name = "{index} ==> {0}")
    @MethodSource("createThreeRandomDatasets")
    @Timeout(value = 3)
    void run_random_dataset(String name, List<XYDataset> datasets) {

        // Arrange
        String pathname = pathnameResolver.apply(name);

        // Act
        JFreeChart jchart = generateAnestheticChart.run(datasets);

        // Assert
        this.saveImageAsPNG(jchart, pathname);

    }
}