package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.dataset;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class ChartDataset implements DatasetConfigurator {

    protected Integer minRange;
    protected Integer maxRange;

}
