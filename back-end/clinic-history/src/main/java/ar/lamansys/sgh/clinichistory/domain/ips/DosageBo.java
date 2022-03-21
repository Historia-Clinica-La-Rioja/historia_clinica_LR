package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;

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

    private LocalDate endDate;

    private LocalDate suspendedStartDate;

    private LocalDate suspendedEndDate;

    private boolean chronic = false;

	private String event;

    public String getPeriodUnit(){
        return periodUnit != null ? periodUnit.getValue() : null;
    }

    public LocalDate getEndDate() {
        if  (endDate != null)
            return endDate;
        return startDate != null && duration != null ?
                startDate.plusDays(duration.longValue()) : null;
    }

    public boolean isExpired(){
        return getEndDate() != null && LocalDate.now().isBefore(getEndDate());
    }
}
