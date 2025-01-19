package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.image;

import org.jfree.chart.JFreeChart;

public interface GetChartImage {
    String run(JFreeChart chart, int width, int height);
}
