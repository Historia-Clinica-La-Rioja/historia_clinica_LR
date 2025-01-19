package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class FamilyHistoryBo extends HealthConditionBo {

    private LocalDate startDate;
    private String note;

    public FamilyHistoryBo(SnomedBo snomed) {
        super(snomed);
    }

    public FamilyHistoryBo() {
        super();
    }
}
