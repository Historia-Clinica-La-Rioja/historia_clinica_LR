package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.doctorsoffice.controller.dto.DoctorsOfficeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyCareListDto implements Serializable {

	Integer id;

	DateTimeDto creationDate;

	EmergencyCarePatientDto patient;

	EmergencyCareEpisodeListTriageDto triage;

	MasterDataDto type;

	MasterDataDto state;

	DoctorsOfficeDto doctorsOffice;
}
