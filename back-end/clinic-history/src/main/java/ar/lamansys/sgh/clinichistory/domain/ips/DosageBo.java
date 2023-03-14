package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class DosageBo {

    private Integer id;

    private Double duration;

    private Integer frequency;

    private EUnitsOfTimeBo periodUnit;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDate suspendedStartDate;

    private LocalDate suspendedEndDate;

    private boolean chronic = false;

	private String event;

	private QuantityBo quantity;

	private Double dosesByUnit;

	private Double dosesByDay;

	public DosageBo(Double duration, Double dosesByUnit, Double dosesByDay) {
		this.duration = duration;
		this.dosesByUnit = dosesByUnit;
		this.dosesByDay = dosesByDay;
	}

	public String getPeriodUnit(){
        return periodUnit != null ? periodUnit.getValue() : null;
    }

    public LocalDateTime getEndDate() {
        if  (endDate != null)
            return endDate;
        return startDate != null && duration != null ?
                startDate.plusDays(duration.longValue()) : null;
    }

    public boolean isExpired(){
        return getEndDate() != null && LocalDate.now().isBefore(getEndDate().toLocalDate());
    }
}
