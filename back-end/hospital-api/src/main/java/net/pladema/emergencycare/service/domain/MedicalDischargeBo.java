package net.pladema.emergencycare.service.domain;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@ToString
public class MedicalDischargeBo implements IDocumentBo {

    private Long id;

    private Integer patientId;

    private PatientInfoBo patientInfo;

    private Integer institutionId;

    private Integer sourceId;

    private List<ProblemBo> problems;

    private LocalDateTime medicalDischargeOn;

    private Integer medicalDischargeBy;

    private Boolean autopsy;

    private Short dischargeTypeId;

	private String otherDischargeDescription;

	private String observation;

    private Map<String, Object> contextMap;

    @Override
    public short getDocumentType() {
        return DocumentType.EMERGENCY_CARE;
    }

    @Override
    public Integer getEncounterId() {
        return sourceId;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.EMERGENCY_CARE;
    }

    @Override
    public Integer getPatientId() {
        if (patientInfo != null)
            return patientInfo.getId();
        return patientId;
    }

}
