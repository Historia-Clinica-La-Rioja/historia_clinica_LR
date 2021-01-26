package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.doctorsoffice.controller.dto.DoctorsOfficeDto;
import net.pladema.sgx.dates.controller.dto.DateTimeDto;
import net.pladema.sgx.masterdata.dto.MasterDataDto;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyCareListDto implements Serializable {

	Integer id;

	DateTimeDto creationDate;

	PatientECEDto patient;

	EmergencyCareEpisodeListTriageDto triage;

	MasterDataDto type;

	MasterDataDto state;

	DoctorsOfficeDto doctorsOffice;
}
