package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.image;

import org.jfree.chart.JFreeChart;

public interface GetChartImage {
    String run(JFreeChart chart, int width, int height);
}
