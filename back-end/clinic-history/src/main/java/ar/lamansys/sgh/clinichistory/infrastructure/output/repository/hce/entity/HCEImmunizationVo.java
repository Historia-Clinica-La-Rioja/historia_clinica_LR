package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import lombok.*;

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