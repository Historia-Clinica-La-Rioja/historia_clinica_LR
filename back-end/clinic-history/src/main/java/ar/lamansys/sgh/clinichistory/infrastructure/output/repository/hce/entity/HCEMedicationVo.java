package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEMedicationVo extends HCEClinicalTermVo {

    private Integer dosageId;

    private Boolean chronic = false;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDate suspendedStartDate;

    private LocalDate suspendedEndDate;

    public HCEMedicationVo(Integer id, Snomed snomed, String statusId, Integer dosageId,
                           Boolean chronic, LocalDateTime startDate,
						   LocalDateTime endDate, LocalDate suspendedStartDate, LocalDate suspendedEndDate) {
        super(id, snomed, statusId);
        this.dosageId = dosageId;
        this.chronic = chronic;
        this.startDate = startDate;
        this.endDate = endDate;
        this.suspendedStartDate = suspendedStartDate;
        this.suspendedEndDate = suspendedEndDate;

    }
}