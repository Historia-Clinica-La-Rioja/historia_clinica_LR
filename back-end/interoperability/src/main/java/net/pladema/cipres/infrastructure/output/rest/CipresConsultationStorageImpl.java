package net.pladema.cipres.infrastructure.output.rest;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.cipres.application.port.CipresConsultationStorage;

import net.pladema.cipres.application.port.CipresEncounterStorage;
import net.pladema.cipres.domain.CipresEncounterBo;
import net.pladema.cipres.domain.OutpatientConsultationBo;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounter;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounterRepository;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresMasterData;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CipresConsultationStorageImpl implements CipresConsultationStorage {

	private final CipresEncounterStorage cipresEncounterStorage;

	private final CipresEncounterRepository cipresEncounterRepository;


	@Override
	public void createOutpatientConsultations(List<OutpatientConsultationBo> consultations) {
		consultations.stream().forEach(c -> {
			String clinicalSpecialtyIRI = getClicnialSpecialtyIRI(c.getClinicalSpecialtySctid());
			String establishmentIRI = getEstablishmentIRI(c.getInstitutionSisaCode());
			if (clinicalSpecialtyIRI != null && establishmentIRI != null) {
				CipresEncounterBo cipresEncounterBo = cipresEncounterStorage.createOutpatientConsultation(c, clinicalSpecialtyIRI, establishmentIRI);
				if (cipresEncounterBo.getResponseCode() != HttpStatus.INTERNAL_SERVER_ERROR.value())
					cipresEncounterRepository.save(mapToEncounterApiSalud(cipresEncounterBo));
			}
		});
	}

	private CipresEncounter mapToEncounterApiSalud(CipresEncounterBo cipresEncounterBo) {
		var result = new CipresEncounter();
		result.setEncounterId(cipresEncounterBo.getEncounterId());
		if (cipresEncounterBo.getEncounterApiId() != null)
			result.setEncounterApiId(cipresEncounterBo.getEncounterApiId());
		result.setStatus(cipresEncounterBo.getStatus());
		result.setResponseCode(cipresEncounterBo.getResponseCode());
		return result;
	}

	private String getClicnialSpecialtyIRI(String snomedSctid) {
		String clinicalSpecialtyId = cipresEncounterStorage.getClinicalSpecialtiyBySnomedCode(snomedSctid);
		if (clinicalSpecialtyId != null)
			return CipresMasterData.CLINICAL_SPECIALTY_IRI + clinicalSpecialtyId;
		else
			log.warn("No se ha encontrado especialidad en la api que contenga el código snomed de especialidad asociado a la consulta.");
		return null;
	}

	private String getEstablishmentIRI(String sisaCode) {
		String establishmentId = cipresEncounterStorage.getEstablishmentByRefesCode(sisaCode);
		if (establishmentId != null)
			return CipresMasterData.ESTABLISHMENT_IRI + establishmentId;
		else
			log.warn("No se ha encontrado establecimiento en la api que contenga el código sisa de la institucion asociada a la consulta.");
		return null;
	}

}
