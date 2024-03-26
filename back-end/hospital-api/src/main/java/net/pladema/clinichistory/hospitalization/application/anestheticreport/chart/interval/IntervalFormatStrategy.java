package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.interval;

import java.text.SimpleDateFormat;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public interface IntervalFormatStrategy {

    XYLineAndShapeRenderer getRenderer();
    SimpleDateFormat getDateFormat();
    void setDotValuesLabels(XYLineAndShapeRenderer renderer);
}
