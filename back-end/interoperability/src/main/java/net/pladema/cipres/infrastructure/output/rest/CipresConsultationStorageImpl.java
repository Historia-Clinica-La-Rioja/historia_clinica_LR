package net.pladema.cipres.infrastructure.output.rest;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.cipres.application.port.CipresConsultationStorage;

import net.pladema.cipres.application.port.CipresEncounterStorage;
import net.pladema.cipres.application.port.CipresPatientStorage;
import net.pladema.cipres.domain.CipresEncounterBo;
import net.pladema.cipres.domain.OutpatientConsultationBo;
import net.pladema.cipres.infrastructure.output.repository.CipresClinicalSpecialty;
import net.pladema.cipres.infrastructure.output.repository.CipresClinicalSpecialtyPk;
import net.pladema.cipres.infrastructure.output.repository.CipresClinicalSpecialtyRepository;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounter;
import net.pladema.cipres.infrastructure.output.repository.CipresEncounterRepository;
import net.pladema.cipres.infrastructure.output.repository.CipresEstablishment;
import net.pladema.cipres.infrastructure.output.repository.CipresEstablishmentPk;
import net.pladema.cipres.infrastructure.output.repository.CipresEstablishmentRepository;
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

	private final CipresEncounterStorage cipresEncounterStorage;

	private final CipresPatientStorage cipresPatientStorage;

	private final CipresEncounterRepository cipresEncounterRepository;

	private final CipresEstablishmentRepository cipresEstablishmentRepository;

	private final CipresClinicalSpecialtyRepository cipresClinicalSpecialtyRepository;

	@Override
	public Integer createOutpatientConsultations(List<OutpatientConsultationBo> consultations) {
		AtomicInteger sentQuantity = new AtomicInteger();
		consultations.forEach(c -> {
			Optional<String> establishment = getEstablishmentId(c.getInstitutionSisaCode(), c.getId());
			if (establishment.isPresent()) {
				String establishmentId = establishment.get();
				var apiPatientId = cipresPatientStorage.getPatientId(c.getPatient(), establishmentId, c.getId());
				if (apiPatientId.isPresent()) {
					c.setApiPatientId(apiPatientId.get());
					Optional<Integer> consultationIdSaved = createOutpatientConsultation(c, buildEstablishmentIRI(establishmentId));
					if (consultationIdSaved.isPresent())
						sentQuantity.addAndGet(1);
				}
			}});
		return sentQuantity.get();
	}

	private Optional<Integer> createOutpatientConsultation(OutpatientConsultationBo consultation, String establishmentIRI) {
		var clinicalSpecialtyIRI = getClinicalSpecialtyIRI(consultation.getClinicalSpecialtyId(), consultation.getClinicalSpecialtySctid(), consultation.getId());
		if (clinicalSpecialtyIRI.isPresent()) {
			try {
				CipresEncounterBo cipresEncounterBo = cipresEncounterStorage.createOutpatientConsultation(consultation, clinicalSpecialtyIRI.get(), establishmentIRI);
				return handleCipresEncounterResponse(cipresEncounterBo);
			} catch (Exception e) {
				String message = e.getCause() != null && e.getCause().getMessage() != null ? e.getCause().getMessage() : "Error interno en el servicio externo - API SALUD";
				CipresEncounterBo cipresEncounterBo = new CipresEncounterBo(consultation.getId(), message, (short) HttpStatus.INTERNAL_SERVER_ERROR.value());
				return handleCipresEncounterResponse(cipresEncounterBo);
			}
		}
		return Optional.empty();
	}

	private Optional<Integer> handleCipresEncounterResponse(CipresEncounterBo cipresEncounterBo) {
		return Optional.of(cipresEncounterRepository.save(mapToEncounterApiSalud(cipresEncounterBo)).getId());
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

	private Optional<String> getClinicalSpecialtyIRI(Integer clinicalSpecialtyId, String snomedSctid, Integer encounterId) {
		Optional<String> cipresClinicalSpecialtyId = cipresClinicalSpecialtyRepository.findByClinicalSpecialtyId(clinicalSpecialtyId);
		if (cipresClinicalSpecialtyId.isPresent())
			return buildClinicalSpecialtyIRI(cipresClinicalSpecialtyId.get());
		var clinicalSpecialty = cipresEncounterStorage.getClinicalSpecialtyBySnomedCode(snomedSctid, encounterId);
		if (clinicalSpecialty.isPresent()) {
			String id = clinicalSpecialty.get();
			cipresClinicalSpecialtyRepository.save(new CipresClinicalSpecialty(new CipresClinicalSpecialtyPk(clinicalSpecialtyId, Integer.parseInt(id))));
		}
		return clinicalSpecialty.flatMap(this::buildClinicalSpecialtyIRI);
	}

	private Optional<String> getEstablishmentId(String sisaCode, Integer encounterId) {
		Optional<String> establishmentId = cipresEstablishmentRepository.findBySisaCode(sisaCode);
		if (establishmentId.isPresent())
			return establishmentId;
		var establishment = cipresEncounterStorage.getEstablishmentBySisaCode(sisaCode, encounterId);
		if (establishment.isPresent()) {
			String id = establishment.get().getId();
			cipresEstablishmentRepository.save(new CipresEstablishment(new CipresEstablishmentPk(sisaCode, Integer.parseInt(id))));
			return Optional.of(id);
		}
		return Optional.empty();
    }

	private String buildEstablishmentIRI(String establishmentId) {
		return CipresMasterData.ESTABLISHMENT_IRI + establishmentId;
	}

	private Optional<String> buildClinicalSpecialtyIRI(String clinicalSpecialtyId) {
		return Optional.of(CipresMasterData.CLINICAL_SPECIALTY_IRI + clinicalSpecialtyId);
	}

}
