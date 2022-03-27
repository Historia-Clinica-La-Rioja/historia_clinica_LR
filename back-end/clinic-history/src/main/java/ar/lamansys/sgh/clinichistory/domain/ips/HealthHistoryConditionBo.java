package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class HealthHistoryConditionBo extends HealthConditionBo {

    private LocalDate startDate;

    private String note;

    public HealthHistoryConditionBo(SnomedBo snomed) {
        super(snomed);
    }

    public HealthHistoryConditionBo() {
        super();
    }
}
