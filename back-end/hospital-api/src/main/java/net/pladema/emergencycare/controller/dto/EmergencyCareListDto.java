package net.pladema.emergencycare.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalPersonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.medicalconsultation.doctorsoffice.controller.dto.DoctorsOfficeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import net.pladema.medicalconsultation.shockroom.infrastructure.controller.dto.ShockroomDto;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyCareListDto implements Serializable {

	private static final long serialVersionUID = -6693505419704202447L;

	private Integer id;

	private DateTimeDto creationDate;

	private EmergencyCarePatientDto patient;

	private EmergencyCareEpisodeListTriageDto triage;

	private MasterDataDto type;

	private MasterDataDto state;

	private DoctorsOfficeDto doctorsOffice;

	private ProfessionalPersonDto relatedProfessional;

	private ShockroomDto shockroom;

	private BedDto bed;

	private String reason;

}
