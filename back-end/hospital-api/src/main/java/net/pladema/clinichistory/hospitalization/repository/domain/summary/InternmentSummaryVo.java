package net.pladema.clinichistory.hospitalization.repository.domain.summary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.InternmentEpisodeStatus;
import net.pladema.clinichistory.hospitalization.repository.domain.ResponsibleContact;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

	private Integer clinicalSpecialtyId;

	private String specialty;

	private LocalDate entryDate;

	private LocalDate dischargeDate;

	private ResponsibleContactVo responsibleContact;

	private LocalDateTime probableDischargeDate;

	private Boolean active;

	public InternmentSummaryVo(Integer id, LocalDate entryDate, Long anamnesisDocId, String anamnesisStatusId,
			Long epicrisisDocId, String epicrisisStatusId, Integer bedId, String bedNumber, Integer roomId,
			String roomNumber, String sectorDescription, ClinicalSpecialty sectorSpecialty, ClinicalSpecialty clinicalSpecialty,
			Integer healthcareProfessionalId, String licenseNumber, String firstName, String lastName,
			ResponsibleContact responsibleContact, LocalDateTime probableDischargeDate, LocalDate dischargeDate, Short internmentStatusId) {
		this.id = id;
		this.documents = new DocumentsSummaryVo();
		this.documents.setAnamnesis(new AnamnesisSummaryVo(anamnesisDocId, anamnesisStatusId));
		this.documents.setEpicrisis(new EpicrisisSummaryVo(epicrisisDocId, epicrisisStatusId));
		this.bedId = bedId;
		this.bedNumber = bedNumber;
		this.roomId = roomId;
		this.roomNumber = roomNumber;
		this.sectorDescription = sectorDescription;

		//Fix clinical specialty as speciality (not as service)
		sectorSpecialty.fixSpecialtyType();
		clinicalSpecialty.fixSpecialtyType();
		this.sectorSpecialty = sectorSpecialty.getName();
		this.clinicalSpecialtyId = clinicalSpecialty.getId();
		this.specialty = clinicalSpecialty.getName();

		this.entryDate = entryDate;
		if (healthcareProfessionalId != null)
			this.doctor = new ResponsibleDoctorVo(healthcareProfessionalId, firstName, lastName, licenseNumber);
		if (responsibleContact != null)
			this.responsibleContact = new ResponsibleContactVo(responsibleContact);
		this.probableDischargeDate = probableDischargeDate;
		this.dischargeDate = dischargeDate;
		this.active = this.isActive(internmentStatusId);
	}

	public Boolean isActive(Short internmentStatusId) {
		return internmentStatusId.equals(InternmentEpisodeStatus.ACTIVE_ID);
	}

}
