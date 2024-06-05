package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart;

import ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.EndTidal;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.Pulse;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.O2Saturation;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.bloodpressure.BloodPressureMax;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset.bloodpressure.BloodPressureMin;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParsePointsToTimeSeries {

    private final BloodPressureMin bloodPressureMin;
    private final BloodPressureMax bloodPressureMax;
    private final Pulse pulse;
    private final O2Saturation o2Saturation;
    private final EndTidal endTidal;

    public List<XYDataset> run(List<MeasuringPointBo> measuringPointBos) {
        TimeSeries seriesTAMin = bloodPressureMin.createTimeSeries();
        TimeSeries seriesTAMax = bloodPressureMax.createTimeSeries();
        TimeSeries seriesPulso = pulse.createTimeSeries();
        TimeSeries seriesSaturacionO2 = o2Saturation.createTimeSeries();
        TimeSeries seriesEndTidal = endTidal.createTimeSeries();

        measuringPointBos.forEach(measuringPointBo ->
                this.parsePointToTimeSeries(measuringPointBo, seriesTAMin, seriesTAMax, seriesPulso, seriesSaturacionO2, seriesEndTidal));

        return createDatasets(seriesTAMin, seriesTAMax, seriesPulso, seriesSaturacionO2, seriesEndTidal);
    }

    @NonNull
    private static List<XYDataset> createDatasets(TimeSeries seriesTAMin, TimeSeries seriesTAMax, TimeSeries seriesPulso, TimeSeries seriesSaturacionO2, TimeSeries seriesEndTidal) {
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

    private void parsePointToTimeSeries(MeasuringPointBo measuringPointBo, TimeSeries seriesTAMin,
                                        TimeSeries seriesTAMax, TimeSeries seriesPulso, TimeSeries seriesSaturacionO2,
                                        TimeSeries seriesEndTidal) {
        RegularTimePeriod timePeriod = this.getRegularTimePeriod(measuringPointBo);
        seriesTAMin.add(timePeriod, measuringPointBo.getBloodPressureMin());
        seriesTAMax.add(timePeriod, measuringPointBo.getBloodPressureMax());
        seriesPulso.add(timePeriod, measuringPointBo.getBloodPulse());
        seriesSaturacionO2.add(timePeriod, measuringPointBo.getO2Saturation());
        seriesEndTidal.add(timePeriod, measuringPointBo.getCo2EndTidal());
    }

    public RegularTimePeriod getRegularTimePeriod(MeasuringPointBo measuringPointBo) {
        int minute = measuringPointBo.getTime().getMinute();
        int hour = measuringPointBo.getTime().getHour();
        int day = measuringPointBo.getDate().getDayOfMonth();
        int month = measuringPointBo.getDate().getMonthValue();
        int year = measuringPointBo.getDate().getYear();
        return new Minute(minute, new Hour(hour, new Day(day, month, year)));
    }
}
