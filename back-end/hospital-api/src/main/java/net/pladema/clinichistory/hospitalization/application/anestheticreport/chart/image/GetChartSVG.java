package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.image;

import java.awt.Rectangle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.JFreeChart;
import org.jfree.svg.SVGGraphics2D;

@Slf4j
@RequiredArgsConstructor
public class GetChartSVG implements GetChartImage {

    public String run(JFreeChart chart, int width, int height) {
        log.debug("Input parameters -> width {}, height {}", width, height);

        SVGGraphics2D g2 = new SVGGraphics2D(width, height);
        g2.setRenderingHint(JFreeChart.KEY_SUPPRESS_SHADOW_GENERATION, true);
        Rectangle r = new Rectangle(0, 0, width, height);
        chart.draw(g2, r);
        String element = g2.getSVGElement();

        log.debug("Output -> chart svg generated");
        return element;
    }

    public String getFileExtension() {
        return ".svg";
    }

}
