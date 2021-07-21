package ar.lamansys.sgh.clinichistory.domain.hce;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEImmunizationVo;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEImmunizationBo extends  HCEClinicalTermBo{

    private LocalDate administrationDate;

    private String note;

    private Integer institutionId;

    private Short doseId;

    private Short conditionId;

    private Short schemeId;

    private String lotNumber;

    private Integer createdByUserId;

    public HCEImmunizationBo(HCEImmunizationVo source){
        super(source.getId(), source.getSnomed(), source.getStatusId(), source.getStatus(), source.getPatientId());
        this.administrationDate = source.getAdministrationDate();
        this.note = source.getNote();
        this.institutionId = source.getInstitutionId();
        this.doseId = source.getDoseId();
        this.conditionId = source.getConditionId();
        this.schemeId = source.getSchemeId();
        this.lotNumber = source.getLotNumber();
        this.createdByUserId = source.getCreatedByUserId();
    }
}
