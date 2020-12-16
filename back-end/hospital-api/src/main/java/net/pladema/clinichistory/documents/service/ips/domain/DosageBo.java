package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EUnitsOfTimeBo;

import java.time.LocalDate;

@Getter
@Setter
public class DosageBo {

    private Integer id;

    private boolean chronic = false;

    private Double duration;

    private Integer frequency;

    private EUnitsOfTimeBo periodUnit;

    private LocalDate startDate;

    private LocalDate suspendedStartDate;

    private LocalDate suspendedEndDate;

    public String getPeriodUnit(){
        return periodUnit != null ? periodUnit.getValue() : null;
    }

    public LocalDate getEndDate() {
        return startDate != null && duration != null && !chronic ?
                startDate.plusDays(duration.longValue()) : null;
    }

}
