package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.image;

import java.awt.Rectangle;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.JFreeChart;
import org.jfree.svg.SVGGraphics2D;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GetChartSVG implements GetChartImage {

    private static final int POINT_AXIS_X = 0;
    private static final int POINT_AXIS_Y = 0;

    public String run(JFreeChart chart, int width, int height) {
        log.debug("Input parameters -> width {}, height {}", width, height);

        SVGGraphics2D g2 = getSvgGraphics2D(width, height);
        Rectangle r = new Rectangle(POINT_AXIS_X, POINT_AXIS_Y, width, height);
        chart.draw(g2, r);
        String element = g2.getSVGElement();

        log.debug("Output -> chart svg generated");
        return element;
    }

    @NonNull
    private SVGGraphics2D getSvgGraphics2D(int width, int height) {
        SVGGraphics2D g2 = new SVGGraphics2D(width, height);
        g2.setRenderingHint(JFreeChart.KEY_SUPPRESS_SHADOW_GENERATION, true);
        return g2;
    }

}
