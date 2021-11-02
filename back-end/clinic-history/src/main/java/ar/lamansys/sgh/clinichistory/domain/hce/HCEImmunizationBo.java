package ar.lamansys.sgh.clinichistory.domain.hce;

import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationDoseBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEImmunizationVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HCEImmunizationBo extends  HCEClinicalTermBo{

    private LocalDate administrationDate;

    private String note;

    private Integer institutionId;

    private String institutionInfo;

    private String doctorInfo;

    private ImmunizationDoseBo dose;

    private Short conditionId;

    private Short schemeId;

    private String lotNumber;

    private Integer createdByUserId;

    private boolean billable;

    public HCEImmunizationBo(HCEImmunizationVo source){
        super(source.getId(), source.getSnomed(), source.getStatusId(), source.getStatus(), source.getPatientId());
        this.administrationDate = source.getAdministrationDate();
        this.note = source.getNote();
        this.institutionId = source.getInstitutionId();
        this.institutionInfo = source.getInstitutionInfo();
        this.doctorInfo = source.getDoctorInfo();
        if (source.getDose() != null && source.getDoseOrder() != null)
            this.dose = new ImmunizationDoseBo(source.getDose(), source.getDoseOrder());
        this.conditionId = source.getConditionId();
        this.schemeId = source.getSchemeId();
        this.lotNumber = source.getLotNumber();
        this.createdByUserId = source.getCreatedByUserId();
        this.billable = source.isBillable();
    }
}
