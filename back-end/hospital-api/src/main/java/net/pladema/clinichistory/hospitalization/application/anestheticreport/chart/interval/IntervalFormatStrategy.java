package net.pladema.clinichistory.hospitalization.application.anestheticreport.chart.interval;

import java.text.SimpleDateFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;


@Getter
@NoArgsConstructor
public abstract class IntervalFormatStrategy {

    protected SimpleDateFormat dateFormat;

    public abstract void setDotValuesLabels(XYLineAndShapeRenderer renderer);

    public abstract XYLineAndShapeRenderer getRenderer();
}
