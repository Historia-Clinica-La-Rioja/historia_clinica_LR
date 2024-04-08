package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart;

import ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series.EndTidal;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series.Pulse;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series.O2Saturation;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series.BloodPressureMax;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series.BloodPressureMin;
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

    public XYDataset run(List<MeasuringPointBo> measuringPointBos) {
        TimeSeries seriesTAMin = bloodPressureMin.createTimeSeries();
        TimeSeries seriesTAMax = bloodPressureMax.createTimeSeries();
        TimeSeries seriesPulso = pulse.createTimeSeries();
        TimeSeries seriesSaturacionO2 = o2Saturation.createTimeSeries();
        TimeSeries seriesEndTidal = endTidal.createTimeSeries();

        measuringPointBos.forEach(measuringPointBo ->
                this.parsePointToTimeSeries(measuringPointBo, seriesTAMin, seriesTAMax, seriesPulso, seriesSaturacionO2, seriesEndTidal));

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(seriesTAMin);
        dataset.addSeries(seriesTAMax);
        dataset.addSeries(seriesPulso);
        dataset.addSeries(seriesSaturacionO2);
        dataset.addSeries(seriesEndTidal);
        return dataset;
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
