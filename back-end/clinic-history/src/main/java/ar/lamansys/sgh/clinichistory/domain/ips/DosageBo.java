package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Dosage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
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

	public DosageBo(Dosage dosage, Double quantityValue, String quantityUnit) {
		this.id = dosage.getId();
		this.duration = dosage.getDuration();
		this.frequency = dosage.getFrequency();
		this.periodUnit = EUnitsOfTimeBo.map(dosage.getPeriodUnit());
		this.startDate = dosage.getStartDate();
		this.endDate = dosage.getEndDate();
		this.suspendedStartDate = dosage.getSuspendedStartDate();
		this.suspendedEndDate = dosage.getSuspendedEndDate();
		this.chronic = dosage.getChronic();
		this.event = dosage.getEvent();
		this.dosesByUnit = dosage.getDosesByUnit();
		this.dosesByDay = dosage.getDosesByDay();
		if (quantityValue != null)
			this.quantity = new QuantityBo(quantityValue.intValue(), quantityUnit);
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
