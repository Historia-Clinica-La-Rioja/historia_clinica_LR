package net.pladema.clinichistory.documents.repository.hce.domain;

import lombok.*;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.Snomed;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HCEImmunizationVo extends HCEClinicalTermVo{

    private LocalDate administrationDate;

    private LocalDate expirationDate;

    private Integer patientId;

    public HCEImmunizationVo(Integer id, Snomed snomed, String statusId, LocalDate administrationDate,
                             LocalDate expirationDate, Integer patientId) {
        super(id, snomed, statusId);
        this.administrationDate = administrationDate;
        this.expirationDate = expirationDate;
        this.patientId = patientId;
    }
}