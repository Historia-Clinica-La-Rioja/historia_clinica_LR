package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.interval;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.NonNull;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.utils.ShapesGenerator;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.springframework.stereotype.Component;

@Component
public class RendererToPlotManyPointsStrategy extends IntervalFormatStrategy {

    public RendererToPlotManyPointsStrategy() {
        dateFormat = createSimpleDateFormat();
    }

    private static SimpleDateFormat createSimpleDateFormat() {
        return new SimpleDateFormat("HH:mm") {

            private static final long serialVersionUID = -1294574239394895774L;

            @Override
            public SimpleDateFormat clone() throws AssertionError {
                throw new AssertionError();
            }

            @Override
            public StringBuffer format(@NonNull Date date, @NonNull StringBuffer toAppendTo, java.text.@NonNull FieldPosition pos) {
                StringBuffer buffer = new StringBuffer();
                int minute = date.getMinutes();

                if (minute != 0 && minute != 15 && minute != 30 && minute != 45)
                    return buffer;
                else
                    buffer.append(super.format(date, toAppendTo, pos));

                return buffer;
            }
        };
    }

    public XYLineAndShapeRenderer getRenderer() {
        return new XYLineAndShapeRenderer()
        {

            private static final long serialVersionUID = -4630680599566487174L;

            @Override
            public void drawDomainLine(Graphics2D g2, XYPlot plot, ValueAxis axis, Rectangle2D dataArea, double value, Paint paint, Stroke stroke) {
                int minute = new Date((long) (value)).getMinutes();

                if (minute == 0 || minute == 15 || minute == 30 || minute == 45)
                    super.drawDomainLine(g2, plot, axis, dataArea, value, Color.BLACK, ShapesGenerator.createDashedBoldedStroke());
                else
                    super.drawDomainLine(g2, plot, axis, dataArea, value, paint, stroke);
            }

        };
    }

    @Override
    public void setDotValuesLabels(XYLineAndShapeRenderer renderer) {
        // do not show labels
    }
}
