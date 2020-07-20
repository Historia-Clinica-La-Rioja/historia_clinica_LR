package net.pladema.clinichistory.generalstate.repository.domain;

import lombok.*;
import net.pladema.clinichistory.ips.repository.masterdata.entity.Snomed;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HCEInmunizationVo extends HCEClinicalTermVo{

    private LocalDate administrationDate;

    private LocalDate expirationDate;

    private Integer patientId;

    public HCEInmunizationVo(Integer id, Snomed snomed, String statusId, LocalDate administrationDate,
                             LocalDate expirationDate, Integer patientId) {
        super(id, snomed, statusId);
        this.administrationDate = administrationDate;
        this.expirationDate = expirationDate;
        this.patientId = patientId;
    }
}