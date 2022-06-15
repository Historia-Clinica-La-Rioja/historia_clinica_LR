package net.pladema.clinichistory.hospitalization.repository.domain.summary;

import java.time.LocalDateTime;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ResponsibleDoctorVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import net.pladema.clinichistory.hospitalization.repository.domain.ResponsibleContact;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InternmentSummaryVo {

	private Integer id;
	private DocumentsSummaryVo documents;
	private ResponsibleDoctorVo doctor;
	private Integer bedId;
	private String bedNumber;
	private Integer roomId;
	private String roomNumber;
	private String sectorDescription;
	private String sectorSpecialty;
	private LocalDateTime entryDate;
	private ResponsibleContactVo responsibleContact;
	private Boolean active;
	private LocalDateTime administrativeDischargeDate;
	private LocalDateTime probableDischargeDate;
	private LocalDateTime physicalDischargeDate;
	private LocalDateTime medicalDischargeDate;

	public InternmentSummaryVo(Integer id, LocalDateTime entryDate, Long anamnesisDocId, String anamnesisStatusId,
							   Long epicrisisDocId, String epicrisisStatusId,
							   Integer bedId, String bedNumber, Integer roomId, String roomNumber,
							   String sectorDescription,
							   ResponsibleDoctorVo responsibleDoctorVo,
							   ResponsibleContact responsibleContact, Short internmentStatusId,
							   LocalDateTime probableDischargeDate, LocalDateTime administrativeDischargeDate,
							   LocalDateTime physicalDischargeDate,  LocalDateTime medicalDischargeDate) {
		this.id = id;
		this.documents = new DocumentsSummaryVo();
		this.documents.setAnamnesis(new AnamnesisSummaryVo(anamnesisDocId, anamnesisStatusId));
		this.documents.setEpicrisis(new EpicrisisSummaryVo(epicrisisDocId, epicrisisStatusId));
		this.bedId = bedId;
		this.bedNumber = bedNumber;
		this.roomId = roomId;
		this.roomNumber = roomNumber;
		this.sectorDescription = sectorDescription;

		this.sectorSpecialty = null;
		this.entryDate = entryDate;
		this.doctor = responsibleDoctorVo;
		if (responsibleContact != null)
			this.responsibleContact = new ResponsibleContactVo(responsibleContact);
		this.active = this.isActive(internmentStatusId);
		this.probableDischargeDate = probableDischargeDate;
		this.administrativeDischargeDate = administrativeDischargeDate;
		this.medicalDischargeDate = medicalDischargeDate;
		this.physicalDischargeDate = physicalDischargeDate;
	}

	public Boolean isActive(Short internmentStatusId) {
		return internmentStatusId.equals(InternmentEpisodeStatus.ACTIVE_ID);
	}

}
