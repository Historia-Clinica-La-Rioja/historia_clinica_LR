package net.pladema.emergencycare.service.domain;

import lombok.*;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareEntrance;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareState;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;
import net.pladema.emergencycare.triage.service.domain.TriageBo;

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

    private String firstname;

    private String lastname;

    private Integer patientId;

    private Integer institutionId;

    private Short triageCategoryId;

    private EEmergencyCareType emergencyCareType;

    private EEmergencyCareState emergencyCareState;

    private EEmergencyCareEntrance emergencyCareEntrance;

    private Integer doctorsOffice;

    private String doctorsOfficeDescription;

    private LocalDateTime createdOn;

    private Integer patientMedicalCoverageId;

    private List<String> reasonIds;

    private TriageBo triage;

    private String ambulanceCompanyId;

    private PoliceInterventionBo policeIntervention;

    public EmergencyCareBo(EmergencyCareVo emergencyCareVo){
        this.id = emergencyCareVo.getId();
        this.firstname = emergencyCareVo.getFirstname();
        this.lastname = emergencyCareVo.getLastname();
        this.patientId = emergencyCareVo.getPatientId();
        this.triageCategoryId = emergencyCareVo.getTriageCategoryId();
        this.emergencyCareType = EEmergencyCareType.getById(emergencyCareVo.getEmergencyCareType());
        this.emergencyCareState = EEmergencyCareState.getById(emergencyCareVo.getEmergencyCareState());
        this.doctorsOffice = emergencyCareVo.getDoctorsOffice();
        this.doctorsOfficeDescription = emergencyCareVo.getDoctorsOfficeDescription();
        this.createdOn = emergencyCareVo.getCreatedOn();
    }

    public EmergencyCareBo(EmergencyCareEpisode emergencyCareEpisode) {
        this.id = emergencyCareEpisode.getId();
        this.patientId = emergencyCareEpisode.getPatientId();
        this.institutionId = emergencyCareEpisode.getInstitutionId();
        this.patientMedicalCoverageId = emergencyCareEpisode.getPatientMedicalCoverageId();
        this.emergencyCareType = (emergencyCareEpisode.getEmergencyCareTypeId() != null) ?
                EEmergencyCareType.getById(emergencyCareEpisode.getEmergencyCareTypeId()) : null;
        this.emergencyCareEntrance = ((emergencyCareEpisode.getEmergencyCareEntranceTypeId()) != null) ?
                EEmergencyCareEntrance.getById(emergencyCareEpisode.getEmergencyCareEntranceTypeId()) : null;
        this.emergencyCareState = (emergencyCareEpisode.getEmergencyCareStateId() != null) ?
                EEmergencyCareState.getById(emergencyCareEpisode.getEmergencyCareStateId()) : null;
        this.ambulanceCompanyId = emergencyCareEpisode.getAmbulanceCompanyId();
    }

    public void setEmergencyCareTypeById(Short id){
        this.emergencyCareType = EEmergencyCareType.getById(id);
    }

    public void setEmergencyEntranceById(Short id){
        this.emergencyCareEntrance = EEmergencyCareEntrance.getById(id);
    }

    public void setEmergencyStateById(Short id){
        this.emergencyCareState = EEmergencyCareState.getById(id);
    }

    public Short getEmergencyCareTypeId() {
        return (this.emergencyCareType != null) ? this.emergencyCareType.getId() : null;
    }

    public Short getEmergencyCareEntranceId() {
        return (this.emergencyCareEntrance != null) ? this.emergencyCareEntrance.getId() : null;
    }

    public Short getEmergencyCareStateId() {
        return (this.emergencyCareState != null) ? this.emergencyCareState.getId() : null;
    }

}
