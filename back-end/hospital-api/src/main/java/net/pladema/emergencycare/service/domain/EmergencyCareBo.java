package net.pladema.emergencycare.service.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyCareBo {

    private Integer id;

    private PatientECEBo patient;

    private Integer institutionId;

    private Short triageCategoryId;

    private String triageName;

    private String triageColorCode;

    private Short emergencyCareTypeId;

    private Short emergencyCareStateId;

    private Short emergencyCareEntranceId;

    private DoctorsOfficeBo doctorsOffice;

    private LocalDateTime createdOn;

    private List<ReasonBo> reasons;

    private TriageBo triage;

    private String ambulanceCompanyId;

    private PoliceInterventionDetailsBo policeInterventionDetails;

    private Boolean hasPoliceIntervention;

    public EmergencyCareBo(EmergencyCareVo emergencyCareVo){
        this.id = emergencyCareVo.getId();
        this.patient = emergencyCareVo.getPatient() != null ? new PatientECEBo(emergencyCareVo.getPatient()) : null;
        this.triageCategoryId = emergencyCareVo.getTriageCategoryId();
        this.triageName = emergencyCareVo.getTriageName();
        this.triageColorCode = emergencyCareVo.getTriageColorCode();
        this.institutionId = emergencyCareVo.getInstitutionId();
        this.emergencyCareTypeId = emergencyCareVo.getEmergencyCareTypeId();
        this.emergencyCareStateId = emergencyCareVo.getEmergencyCareStateId();
        this.emergencyCareEntranceId = emergencyCareVo.getEmergencyCareEntranceTypeId();
        this.doctorsOffice = emergencyCareVo.getDoctorsOffice() != null ? new DoctorsOfficeBo(emergencyCareVo.getDoctorsOffice()) : null;
        this.ambulanceCompanyId = emergencyCareVo.getAmbulanceCompanyId();
        if (emergencyCareVo.getPoliceInterventionDetails()!= null)
            this.policeInterventionDetails = new PoliceInterventionDetailsBo(emergencyCareVo.getPoliceInterventionDetails());
        this.createdOn = emergencyCareVo.getCreatedOn();
        this.hasPoliceIntervention = emergencyCareVo.getHasPoliceIntervention();
    }

    public EmergencyCareBo(EmergencyCareEpisode emergencyCareEpisode) {
        this.id = emergencyCareEpisode.getId();
        if(emergencyCareEpisode.getPatientId() != null) {
            this.patient = new PatientECEBo(emergencyCareEpisode.getPatientId(), emergencyCareEpisode.getPatientMedicalCoverageId());
        }
        this.institutionId = emergencyCareEpisode.getInstitutionId();
        this.emergencyCareEntranceId = emergencyCareEpisode.getEmergencyCareEntranceTypeId();
        this.emergencyCareTypeId = emergencyCareEpisode.getEmergencyCareTypeId();
        this.emergencyCareStateId = emergencyCareEpisode.getEmergencyCareStateId();
        this.ambulanceCompanyId = emergencyCareEpisode.getAmbulanceCompanyId();
        if (emergencyCareEpisode.getDoctorsOfficeId() != null){
            this.doctorsOffice = new DoctorsOfficeBo();
            this.doctorsOffice.setId(emergencyCareEpisode.getDoctorsOfficeId());
        }
        this.hasPoliceIntervention = emergencyCareEpisode.getHasPoliceIntervention();
    }

    public void setTriageRiskFactorIds(List<Integer> riskFactorIds) {
        this.getTriage().setRiskFactorIds(riskFactorIds);
    }

    public Integer getPersonId() {
        return (this.getPatient() != null && this.getPatient().getPerson() != null) ?
            this.getPatient().getPerson().getId() : null;
    }

    public Integer getDoctorsOfficeId() {
        return (this.getDoctorsOffice() != null) ? this.getDoctorsOffice().getId() : null;
    }

}
