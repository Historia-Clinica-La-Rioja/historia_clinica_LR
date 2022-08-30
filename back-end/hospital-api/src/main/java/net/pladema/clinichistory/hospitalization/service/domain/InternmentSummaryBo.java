package net.pladema.clinichistory.hospitalization.service.domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.InternmentSummaryVo;
import net.pladema.clinichistory.hospitalization.service.summary.domain.ResponsibleDoctorBo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InternmentSummaryBo {

    private Integer id;
    private DocumentsSummaryBo documents;
    private ResponsibleDoctorBo doctor;
    private Integer bedId;
    private String bedNumber;
    private Integer roomId;
    private String roomNumber;
    private String sectorDescription;
    private String sectorSpecialty;
    private LocalDateTime entryDate;
	private ResponsibleContactBo responsibleContact;
	private LocalDateTime probableDischargeDate;
    private LocalDateTime administrativeDischargeDate;

	private LocalDateTime physicalDischargeDate;
	private LocalDateTime medicalDischargeDate;
	private boolean active;
	private int totalInternmentDays;

    public InternmentSummaryBo(InternmentSummaryVo internmentSummaryVo) {
        this.id = internmentSummaryVo.getId();
        this.documents = new DocumentsSummaryBo(internmentSummaryVo.getDocuments());
        this.bedId = internmentSummaryVo.getBedId();
        this.bedNumber = internmentSummaryVo.getBedNumber();
        this.roomId = internmentSummaryVo.getRoomId();
        this.roomNumber = internmentSummaryVo.getRoomNumber();
        this.sectorDescription = internmentSummaryVo.getSectorDescription();
        this.sectorSpecialty = internmentSummaryVo.getSectorSpecialty();
        this.entryDate = internmentSummaryVo.getEntryDate();
        if (internmentSummaryVo.getDoctor() != null)
            this.doctor = new ResponsibleDoctorBo(internmentSummaryVo.getDoctor());
        if (internmentSummaryVo.getResponsibleContact() != null)
            this.responsibleContact = new ResponsibleContactBo(internmentSummaryVo.getResponsibleContact());
        this.probableDischargeDate = internmentSummaryVo.getProbableDischargeDate();
        this.administrativeDischargeDate = internmentSummaryVo.getAdministrativeDischargeDate();
		this.medicalDischargeDate = internmentSummaryVo.getMedicalDischargeDate();
		this.physicalDischargeDate = internmentSummaryVo.getPhysicalDischargeDate();
		this.active = internmentSummaryVo.getActive();
        this.totalInternmentDays = totalInternmentDays();
    }

    private int totalInternmentDays(){
		var validDate =
				physicalDischargeDate != null ?
						physicalDischargeDate :
						active ?
								LocalDateTime.now() :
								administrativeDischargeDate;
		return (int)ChronoUnit.DAYS.between(getEntryDate(), validDate);
    }

	public boolean freeBed() {
		return (physicalDischargeDate != null) || (administrativeDischargeDate != null);
	}

}
