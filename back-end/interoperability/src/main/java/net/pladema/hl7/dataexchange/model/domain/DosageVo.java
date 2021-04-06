package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;
import net.pladema.hl7.dataexchange.model.adaptor.FhirString;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
public class DosageVo implements Serializable {

    private static final long serialVersionUID = -7731428159406442579L;

    //route administration
    private String routeCode;
    private String routeTerm;

    private Integer sequence=0;
    private Integer frequency;

    private Integer count;
    private String periodUnit;

    private Double duration;
    private String durationUnit;

    //Dose Quantity
    private String doseQuantityCode;
    private Double doseQuantityValue;
    private String doseQuantityUnit;

    public DosageVo(Integer sequence, Integer timingRepeat, Double duration, String durationUnit,
                        Integer frequency, String periodUnit){
        this.sequence = sequence;
        this.count = timingRepeat;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.frequency = frequency;
        this.periodUnit = periodUnit;
    }

    public FhirCode getRoute(){
        return new FhirCode(routeCode, routeTerm);
    }

    public String getDoseQuantityData(){
        return doseQuantityCode + " " + doseQuantityUnit + " " + doseQuantityValue;
    }

    public boolean hasSequence(){
        return sequence != null;
    }

    public boolean hasRoute(){
        return FhirString.hasText(routeCode) && FhirString.hasText(routeTerm);
    }

    public boolean hasTimingRepeat(){
        return !Stream.of(count, duration, durationUnit, frequency, periodUnit)
                .allMatch(Objects::isNull);
    }

    public boolean hasDuration(){
        return duration != null && FhirString.hasText(durationUnit);
    }

    public boolean hasQuantity(){
        return doseQuantityValue != null && FhirString.hasText(doseQuantityUnit);
    }
}
