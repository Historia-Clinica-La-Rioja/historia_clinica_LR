package net.pladema.establishment.controller.constraints.validator.entities;

import net.pladema.emergencycare.service.EmergencyCareEpisodeService;

import net.pladema.establishment.application.attentionplaces.FetchAttentionPlaceBlockStatus;

import net.pladema.establishment.repository.BedRepository;

import org.springframework.stereotype.Component;

import net.pladema.establishment.repository.entity.Bed;
import net.pladema.clinichistory.hospitalization.controller.externalservice.InternmentEpisodeExternalService;
import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;

import java.util.Objects;

@Component
public class BackofficeBedEntityValidator extends BackofficeEntityValidatorAdapter<Bed, Integer> {
	
	InternmentEpisodeExternalService internmentEpisodeExternalService;

	EmergencyCareEpisodeService emergencyCareEpisodeService;

	FetchAttentionPlaceBlockStatus fetchAttentionPlaceBlockStatus;
	BedRepository bedRepository;

	public BackofficeBedEntityValidator(
		InternmentEpisodeExternalService internmentEpisodeExternalService,
		EmergencyCareEpisodeService emergencyCareEpisodeService,
		FetchAttentionPlaceBlockStatus fetchAttentionPlaceBlockStatus,
		BedRepository bedRepository
	)
	{
		this.internmentEpisodeExternalService = internmentEpisodeExternalService;
		this.emergencyCareEpisodeService = emergencyCareEpisodeService;
		this.fetchAttentionPlaceBlockStatus = fetchAttentionPlaceBlockStatus;
		this.bedRepository = bedRepository;
	}

	@Override
	public void assertUpdate(Integer id, Bed entity) {
		assertUpdateStatus(id, entity);
		if (Boolean.TRUE.equals(this.internmentEpisodeExternalService.existsActiveForBedId(id))) {
			throw new BackofficeValidationException("beds.existsHospitalization");
		}
	}

	private void assertUpdateStatus(Integer id, Bed update) {
		Integer institutionId = bedRepository.getInstitutionId(id);
		bedRepository.findById(id).ifPresent(bed -> {
			Boolean statusChanged = bed.statusChanged(update);
			if (statusChanged && fetchAttentionPlaceBlockStatus.isBedBlocked(institutionId, id)) {
				throw new BackofficeValidationException("beds.blocked");
			}
		});

	}

	@Override
	public void assertDelete(Integer id) {
		this.assertIntermentAndEmergencyEpisode(id);
	}

	private void assertIntermentAndEmergencyEpisode(Integer id) {
		if (Boolean.TRUE.equals(this.internmentEpisodeExternalService.existsActiveForBedId(id)))
			throw new BackofficeValidationException("beds.existsHospitalization");

		if (Boolean.TRUE.equals(this.emergencyCareEpisodeService.isBedOccupiedByEmergencyEpisode(id)))
			throw new BackofficeValidationException("beds.existsEmergencyEpisode");
	}

}
