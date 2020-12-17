package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EUnitsOfTimeBo;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class DosageBo {

    private Integer id;

    private Double duration;

    private Integer frequency;

    private EUnitsOfTimeBo periodUnit;

    private LocalDate startDate;

    private LocalDate suspendedStartDate;

    private LocalDate suspendedEndDate;

    private boolean chronic = false;

    public String getPeriodUnit(){
        return periodUnit != null ? periodUnit.getValue() : null;
    }

    public LocalDate getEndDate() {
        return startDate != null && duration != null ?
                startDate.plusDays(duration.longValue()) : null;
    }

    public boolean isExpired(){
        return getEndDate() != null && LocalDate.now().isBefore(getEndDate());
    }
}
