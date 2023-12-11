package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class PersonalHistoryBo extends HealthConditionBo {

    private LocalDate startDate;
    private LocalDate inactivationDate;
    private Short typeId;
    private String type;
    private String note;

    public PersonalHistoryBo(SnomedBo snomed) {
        super(snomed);
    }

    public PersonalHistoryBo() {
        super();
    }
}
