package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private String note;

    private Integer institutionId;

    private String institutionInfo;

    private String doctorInfo;

    private String dose;

    private Short doseOrder;

    private Short conditionId;

    private Short schemeId;

    private String lotNumber;

    private Integer createdByUserId;

    private boolean billable;

    public HCEImmunizationVo(Integer id, Snomed snomed, String statusId, LocalDate administrationDate,
                             LocalDate expirationDate, Integer patientId, Integer institutionId,
                             Short conditionId, Short schemeId, String dose, Short doseOrder, String lotNumber,
                             String note, Integer createdByUserId, String institutionInfo, String doctorInfo, Boolean billable) {
        super(id, snomed, statusId);
        this.administrationDate = administrationDate;
        this.expirationDate = expirationDate;
        this.patientId = patientId;
        this.note = note;
        this.institutionId = institutionId;
        this.institutionInfo = institutionInfo;
        this.doctorInfo = doctorInfo;
        this.dose = dose;
        this.doseOrder = doseOrder;
        this.conditionId = conditionId;
        this.schemeId = schemeId;
        this.lotNumber = lotNumber;
        this.createdByUserId = createdByUserId;
        this.billable = billable != null && billable;
    }
}