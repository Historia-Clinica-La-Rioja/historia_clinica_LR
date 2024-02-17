package net.pladema.cipres.infrastructure.output.rest;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.cipres.application.port.CipresConsultationStorage;

import net.pladema.cipres.application.port.CipresEncounterStorage;
import net.pladema.cipres.application.port.CipresPatientStorage;
import net.pladema.cipres.domain.CipresEncounterBo;
import net.pladema.cipres.domain.OutpatientConsultationBo;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounter;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounterRepository;
import net.pladema.cipres.infrastructure.output.rest.domain.CipresMasterData;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class CipresConsultationStorageImpl implements CipresConsultationStorage {

	private static final String ADDRESS_ERROR = "Debe completar el domicilio del paciente";

	private final CipresEncounterStorage cipresEncounterStorage;

	private final CipresPatientStorage cipresPatientStorage;

	private final CipresEncounterRepository cipresEncounterRepository;

	@Override
	public Integer createOutpatientConsultations(List<OutpatientConsultationBo> consultations) {
		AtomicInteger sentQuantity = new AtomicInteger();
		consultations.forEach(c -> {
			var establishment = cipresEncounterStorage.getEstablishmentBySisaCode(c.getInstitutionSisaCode());
			if (establishment.isPresent()) {
				var establishmentId = establishment.get().getId();
				var apiPatientId = cipresPatientStorage.getPatientId(c.getPatient(), establishmentId);
				if (apiPatientId.isPresent()) {
					c.setApiPatientId(apiPatientId.get());
					var consultationIdSaved = createOutpatientConsultation(c, establishmentId);
					if (consultationIdSaved.isPresent())
						sentQuantity.addAndGet(1);
				}
			}
		});
		return sentQuantity.get();
	}

	private Optional<Integer> createOutpatientConsultation(OutpatientConsultationBo consultation, String establishmentId) {
		var clinicalSpecialtyIRI = getClinicalSpecialtyIRI(consultation.getClinicalSpecialtySctid());
		if (clinicalSpecialtyIRI.isPresent()) {
			CipresEncounterBo cipresEncounterBo = cipresEncounterStorage.createOutpatientConsultation(consultation, clinicalSpecialtyIRI.get(), getEstablishmentIRI2(establishmentId));
			return handleCipresEncounterResponse(cipresEncounterBo);
		}
		return Optional.empty();
	}

	private Optional<Integer> handleCipresEncounterResponse(CipresEncounterBo cipresEncounterBo) {
		int responseCode = cipresEncounterBo.getResponseCode();
		if (isValidToSave(responseCode, cipresEncounterBo.getStatus()))
			return Optional.of(cipresEncounterRepository.save(mapToEncounterApiSalud(cipresEncounterBo)).getId());
		return Optional.empty();
	}

	private boolean isValidToSave(int responseCode, String status) {
		return responseCode != HttpStatus.INTERNAL_SERVER_ERROR.value() &&
				responseCode != HttpStatus.SERVICE_UNAVAILABLE.value() &&
				!(responseCode == HttpStatus.UNPROCESSABLE_ENTITY.value() && status.contains(ADDRESS_ERROR));
	}

	private CipresEncounter mapToEncounterApiSalud(CipresEncounterBo cipresEncounterBo) {
		var result = new CipresEncounter();
		result.setEncounterId(cipresEncounterBo.getEncounterId());
		result.setEncounterApiId(cipresEncounterBo.getEncounterApiId());
		result.setStatus(cipresEncounterBo.getStatus());
		result.setResponseCode(cipresEncounterBo.getResponseCode());
		result.setDate(LocalDate.now());
		return result;
	}

	private Optional<String> getClinicalSpecialtyIRI(String snomedSctid) {
		Optional<String> clinicalSpecialtyId = cipresEncounterStorage.getClinicalSpecialtiyBySnomedCode(snomedSctid);
        return clinicalSpecialtyId.map(s -> CipresMasterData.CLINICAL_SPECIALTY_IRI + s);
    }

	private String getEstablishmentIRI2(String esthablishmentId) {
		return CipresMasterData.ESTABLISHMENT_IRI + esthablishmentId;
	}

}
