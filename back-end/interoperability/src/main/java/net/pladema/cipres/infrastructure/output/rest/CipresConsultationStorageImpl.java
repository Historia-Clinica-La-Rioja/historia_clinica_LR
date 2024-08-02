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
	public Integer sendOutpatientConsultations(List<OutpatientConsultationBo> consultations) {
		AtomicInteger sentQuantity = new AtomicInteger();
		consultations.forEach(c -> {
			Optional<Integer> sentConsultationApiId = sendOutpatientConsultation(c, c.getCipresEncounterId());
			sentConsultationApiId.ifPresent(id -> sentQuantity.addAndGet(1));
		});
		return sentQuantity.get();
	}

	@Override
	public void forwardOutpatientConsultation(OutpatientConsultationBo consultation, Integer cipresEncounterId) {
		log.debug("Forward outpatient consultation with id: " , consultation.getId());
		Optional<Integer> sentConsultationApiId = sendOutpatientConsultation(consultation, cipresEncounterId);
		sentConsultationApiId.ifPresent(consultationApiId -> log.debug("Successfully forwarded, outpatient consultation api id: ", consultationApiId));
	}

	private Optional<Integer> sendOutpatientConsultation(OutpatientConsultationBo consultation, Integer cipresEncounterId) {
		Optional<String> establishment = getEstablishmentId(consultation.getInstitutionSisaCode(), consultation.getId(), cipresEncounterId);
		if (establishment.isPresent()) {
			String establishmentId = establishment.get();
			var apiPatientId = cipresPatientStorage.getPatientId(consultation.getPatient(), establishmentId, consultation.getId(), cipresEncounterId, consultation.getInstitutionId());
			if (apiPatientId.isPresent()) {
				consultation.setApiPatientId(apiPatientId.get());
				return makeAndSendOutpatientConsultation(consultation, buildEstablishmentIRI(establishmentId), cipresEncounterId);
			}
		}
		return Optional.empty();
	}

	private Optional<Integer> makeAndSendOutpatientConsultation(OutpatientConsultationBo consultation,
														   String establishmentIRI,
														   Integer cipresEncounterId) {
		var clinicalSpecialtyIRI = getClinicalSpecialtyIRI(consultation.getClinicalSpecialtyId(), consultation.getClinicalSpecialtySctid(), consultation.getId(), cipresEncounterId);
		if (clinicalSpecialtyIRI.isPresent()) {
			try {
				CipresEncounterBo cipresEncounterBo = cipresEncounterStorage.makeAndSendOutpatientConsultation(consultation, clinicalSpecialtyIRI.get(), establishmentIRI);
				if (cipresEncounterId != null)
					cipresEncounterBo.setId(cipresEncounterId);
				return handleCipresEncounterResponse(cipresEncounterBo);
			} catch (Exception e) {
				return handleCipresEncounterResponse(new CipresEncounterBo(cipresEncounterId, consultation.getId(), null, e.getMessage(), (short) HttpStatus.INTERNAL_SERVER_ERROR.value()));
			}
		}
		return Optional.empty();
	}

	private Optional<Integer> handleCipresEncounterResponse(CipresEncounterBo cipresEncounterBo) {
		return Optional.ofNullable(cipresEncounterRepository.save(mapToEncounterApiSalud(cipresEncounterBo)).getEncounterApiId());
	}

	private CipresEncounter mapToEncounterApiSalud(CipresEncounterBo cipresEncounterBo) {
		var result = new CipresEncounter();
		result.setId(cipresEncounterBo.getId());
		result.setEncounterId(cipresEncounterBo.getEncounterId());
		result.setEncounterApiId(cipresEncounterBo.getEncounterApiId());
		result.setStatus(cipresEncounterBo.getStatus());
		result.setResponseCode(cipresEncounterBo.getResponseCode());
		result.setDate(LocalDate.now());
		return result;
	}

	private Optional<String> getClinicalSpecialtyIRI(Integer clinicalSpecialtyId, String snomedSctid, Integer encounterId, Integer cipresEncounterId) {
		Optional<String> cipresClinicalSpecialtyId = cipresClinicalSpecialtyRepository.findByClinicalSpecialtyId(clinicalSpecialtyId);
		if (cipresClinicalSpecialtyId.isPresent())
			return buildClinicalSpecialtyIRI(cipresClinicalSpecialtyId.get());
		var clinicalSpecialty = cipresEncounterStorage.getClinicalSpecialtyBySnomedCode(snomedSctid, encounterId, cipresEncounterId);
		if (clinicalSpecialty.isPresent()) {
			String id = clinicalSpecialty.get();
			cipresClinicalSpecialtyRepository.save(new CipresClinicalSpecialty(new CipresClinicalSpecialtyPk(clinicalSpecialtyId, Integer.parseInt(id))));
		}
		return clinicalSpecialty.flatMap(this::buildClinicalSpecialtyIRI);
	}

	private Optional<String> getEstablishmentId(String sisaCode, Integer encounterId, Integer cipresEncounterId) {
		Optional<String> establishmentId = cipresEstablishmentRepository.findBySisaCode(sisaCode);
		if (establishmentId.isPresent())
			return establishmentId;
		var establishment = cipresEncounterStorage.getEstablishmentBySisaCode(sisaCode, encounterId, cipresEncounterId);
		if (establishment.isPresent()) {
			String id = establishment.get().getId();
			cipresEstablishmentRepository.save(new CipresEstablishment(new CipresEstablishmentPk(sisaCode, id)));
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
