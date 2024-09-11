package net.pladema.emergencycare.infrastructure.input.rest.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalPersonDto;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.emergencycare.controller.dto.EmergencyCareEpisodeListTriageDto;
import net.pladema.emergencycare.controller.dto.EmergencyCarePatientDto;
import net.pladema.person.controller.dto.PersonPhotoDto;

import javax.annotation.Nullable;


@Getter
@Setter
@NoArgsConstructor
public class EmergencyCareAttentionPlaceDetailDto {

	@Nullable
	private EmergencyCarePatientDto patient;

	@Nullable
	private String reason;

	@Nullable
	private MasterDataDto type;

	@Nullable
	private MasterDataDto state;

	@Nullable
	private ProfessionalPersonDto professional;

	@Nullable
	private EmergencyCareEpisodeListTriageDto lastTriage;

	public Boolean hasPersonId(){
		return this.patient != null &&
				this.patient.getPerson() != null &&
				this.patient.getPerson().getId() != null;
	}

	public void setPersonPhoto(PersonPhotoDto personPhotoDto){
		if (hasPersonId())
			this.patient.getPerson().setPhoto(personPhotoDto);
	}
}
