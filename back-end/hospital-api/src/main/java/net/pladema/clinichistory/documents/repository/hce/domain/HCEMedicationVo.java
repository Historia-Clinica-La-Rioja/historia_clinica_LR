package net.pladema.clinichistory.documents.repository.hce.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.Snomed;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEMedicationVo extends HCEClinicalTermVo {

    private Integer dosageId;

    private Boolean chronic = false;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate suspendedStartDate;

    private LocalDate suspendedEndDate;

    public HCEMedicationVo(Integer id, Snomed snomed, String statusId, Integer dosageId,
                           Boolean chronic, LocalDate startDate,
                           LocalDate endDate, LocalDate suspendedStartDate, LocalDate suspendedEndDate) {
        super(id, snomed, statusId);
        this.dosageId = dosageId;
        this.chronic = chronic;
        this.startDate = startDate;
        this.endDate = endDate;
        this.suspendedStartDate = suspendedStartDate;
        this.suspendedEndDate = suspendedEndDate;

    }
}