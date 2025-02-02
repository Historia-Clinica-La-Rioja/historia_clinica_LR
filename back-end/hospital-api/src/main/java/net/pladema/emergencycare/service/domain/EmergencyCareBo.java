package net.pladema.emergencycare.service.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.service.domain.BedBo;
import net.pladema.clinichistory.hospitalization.service.domain.RoomBo;
import net.pladema.clinichistory.hospitalization.service.domain.SectorBo;
import net.pladema.emergencycare.repository.domain.EmergencyCareVo;
import net.pladema.emergencycare.repository.entity.EmergencyCareEpisode;
import net.pladema.emergencycare.triage.domain.TriageBo;
import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;
import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;

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

    private String reason;

    private TriageBo triage;

    private String ambulanceCompanyId;

    private PoliceInterventionDetailsBo policeInterventionDetails;

    private Boolean hasPoliceIntervention;

	private ProfessionalPersonBo relatedProfessional;

	private ShockRoomBo shockroom;

	private BedBo bed;

	private LocalDateTime endDate;

	private RoomBo room;

	private String institutionName;

	private Boolean canBeAbsent;

	private LocalDateTime stateUpdatedOn;
	private Short calls;

	private SectorBo sector;

	private EpisodeDischargeSummaryBo dischargeSummary;

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
		this.shockroom = emergencyCareVo.getShockroom() != null ? new ShockRoomBo(emergencyCareVo.getShockroom().getId(), emergencyCareVo.getShockroom().getDescription(), false) : null;
		this.bed = emergencyCareVo.getBed() != null ? new BedBo(emergencyCareVo.getBed().getId(), emergencyCareVo.getBed().getBedNumber(), emergencyCareVo.getRoom() != null ? new RoomBo(emergencyCareVo.getRoom()) : null) : null;
		this.endDate = emergencyCareVo.getEndDate();
		this.institutionName = emergencyCareVo.getInstitutionName();
		this.reason = emergencyCareVo.getReason();
		this.stateUpdatedOn = emergencyCareVo.getStateUpdatedOn();
		this.sector = emergencyCareVo.getSector() != null ? new SectorBo(emergencyCareVo.getSector()) : null;
		this.triage = emergencyCareVo.getTriage() != null ? new TriageBo(emergencyCareVo.getTriage()) : null;
    }

    public EmergencyCareBo(EmergencyCareEpisode emergencyCareEpisode) {
        this.id = emergencyCareEpisode.getId();
        if(emergencyCareEpisode.getPatientId() != null) {
            this.patient = new PatientECEBo(emergencyCareEpisode.getPatientId(),
					emergencyCareEpisode.getPatientMedicalCoverageId(),
					emergencyCareEpisode.getPatientDescription());
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
		this.reason = emergencyCareEpisode.getReason();
		this.createdOn = emergencyCareEpisode.getCreatedOn();
		Integer shockroomId = emergencyCareEpisode.getShockroomId();
		this.shockroom = shockroomId != null ? ShockRoomBo.builder().id(shockroomId).build() : null;
		Integer bedId = emergencyCareEpisode.getBedId();
		this.bed = bedId != null ? BedBo.builder().id(bedId).build() : null;
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

	public String getBedNumber() {
		return bed != null ? bed.getBedNumber(): null;
	}

	public Integer getShockRoomId() {
		return (this.getShockroom() != null) ? this.getShockroom().getId() : null;
	}

	public Integer getBedId() {
		return (this.getBed() != null) ? this.getBed().getId() : null;
	}

}
