package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.series;

import java.text.DecimalFormat;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;

public abstract class BloodPressure extends Series {

    protected static final int POINT_PLACE_OFFSET = 4;

    protected BloodPressure() {
        super.minRange = 20;
        super.maxRange = 140;
    }

    @Override
    protected String getLabel() {
        return "TA";
    }

    @Override
    protected int getIndexAxisY() {
        return 3;
    }

    @Override
    protected NumberTickUnit getTickUnit() {
        return new NumberTickUnit(20., new DecimalFormat("0"));
    }

    @Override
    public void mapSeriesWithAxis(XYPlot plot) {
        plot.mapDatasetToRangeAxis(this.getSerieNumber(), 0);
    }

}
