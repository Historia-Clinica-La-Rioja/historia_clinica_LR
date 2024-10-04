package ar.lamansys.refcounterref.infraestructure.output.repository.reference;

import ar.lamansys.refcounterref.application.getreference.exceptions.ReferenceException;
import ar.lamansys.refcounterref.application.getreference.exceptions.ReferenceExceptionEnum;
import ar.lamansys.refcounterref.application.port.HistoricReferenceRegulationStorage;
import ar.lamansys.refcounterref.application.port.ReferenceCounterReferenceFileStorage;
import ar.lamansys.refcounterref.application.port.ReferenceHealthConditionStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStudyStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceCounterReferenceType;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.domain.enums.EReferenceStatus;
import ar.lamansys.refcounterref.domain.reference.CompleteReferenceBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceDataBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceRequestBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceStudyBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceSummaryBo;
import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;
import ar.lamansys.refcounterref.domain.snomed.SnomedBo;
import ar.lamansys.refcounterref.infraestructure.output.repository.referenceclinicalspecialty.ReferenceClinicalSpecialty;
import ar.lamansys.refcounterref.infraestructure.output.repository.referenceclinicalspecialty.ReferenceClinicalSpecialtyRepository;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencehealthcondition.ReferenceHealthConditionRepository;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencenote.ReferenceNote;
import ar.lamansys.refcounterref.infraestructure.output.repository.referencenote.ReferenceNoteRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReferenceStorageImpl implements ReferenceStorage {

	private static final Integer OUTPATIENT_SOURCE_TYPE_ID = 1;

	private static final Short APPROVED_REGULATION_STATE = 1;

    private final ReferenceRepository referenceRepository;

    private final ReferenceNoteRepository referenceNoteRepository;

    private final ReferenceHealthConditionRepository referenceHealthConditionRepository;

    private final ReferenceHealthConditionStorage referenceHealthConditionStorage;

    private final ReferenceCounterReferenceFileStorage referenceCounterReferenceFileStorage;

	private final ReferenceStudyStorage referenceStudyStorage;

	private final FeatureFlagsService featureFlagsService;

	private final SharedPersonPort sharedPersonPort;

	private final HistoricReferenceRegulationStorage historicReferenceRegulationStorage;
	
	private final ReferenceClinicalSpecialtyRepository referenceClinicalSpecialtyRepository;

    @Override
	@Transactional
    public List<Integer> save(List<CompleteReferenceBo> referenceBoList) {
		log.debug("Input parameters -> referenceBoList {}", referenceBoList);
		List<Integer> orderIds = new ArrayList<>();
        referenceBoList.forEach(referenceBo -> processReference(referenceBo, orderIds));
		return orderIds;
    }

	private void processReference(CompleteReferenceBo referenceBo, List<Integer> orderIds) {
		Reference ref = new Reference(referenceBo);
		if (referenceBo.getNote() != null)
			saveReferenceNote(referenceBo, ref);
		Reference reference = referenceRepository.save(ref);
		Integer referenceId = reference.getId();
		List<Integer> referenceHealthConditionIds = referenceHealthConditionStorage.saveProblems(referenceId, referenceBo);
		log.debug("referenceHealthConditionIds, referenceId -> {} {}", referenceHealthConditionIds, referenceId);
		saveReferenceClinicalSpecialties(referenceId, referenceBo.getClinicalSpecialtyIds());
		if (referenceBo.getStudy() != null)
			saveReferenceOrder(referenceBo, orderIds, reference, referenceId);
		saveReferenceRegulationState(reference, referenceBo);
		if (referenceBo.getFileIds() != null)
			referenceCounterReferenceFileStorage.updateReferenceCounterReferenceId(referenceId, referenceBo.getFileIds());
	}

	private void saveReferenceRegulationState(Reference reference, CompleteReferenceBo referenceBo) {
		var regulationStateId = historicReferenceRegulationStorage.saveReferenceRegulation(reference.getId(), referenceBo);
		reference.setRegulationStateId(regulationStateId);
		referenceRepository.save(reference);
	}

	private void saveReferenceOrder(CompleteReferenceBo referenceBo, List<Integer> orderIds, Reference reference, Integer referenceId) {
		Integer orderId = referenceStudyStorage.save(referenceBo);
		reference.setServiceRequestId(orderId);
		referenceRepository.save(reference);
		orderIds.add(orderId);
		log.debug("orderId, referenceId -> {} {}", orderId, referenceId);
	}

	private void saveReferenceNote(CompleteReferenceBo referenceBo, Reference ref) {
		Integer referenceNoteId = referenceNoteRepository.save(new ReferenceNote(referenceBo.getNote())).getId();
		ref.setReferenceNoteId(referenceNoteId);
	}

	private void saveReferenceClinicalSpecialties(Integer referenceId, List<Integer> clinicalSpecialtyIds) {
		if (clinicalSpecialtyIds != null)
			clinicalSpecialtyIds.forEach(clinicalSpecialty -> saveReferenceClinicalSpecialty(referenceId, clinicalSpecialty));
	}

	private void saveReferenceClinicalSpecialty(Integer referenceId, Integer clinicalSpecialty) {
		ReferenceClinicalSpecialty referenceClinicalSpecialty = new ReferenceClinicalSpecialty(referenceId, clinicalSpecialty);
		referenceClinicalSpecialtyRepository.save(referenceClinicalSpecialty);
	}

	@Override
    public List<ReferenceDataBo> getReferences(Integer patientId, List<Integer> clinicalSpecialtyIds, List<Short> loggedUserRoleIds) {
		log.debug("Input parameters -> patientId {}, clinicalSpecialtyIds {}, loggedUserRoleIds {}", patientId, clinicalSpecialtyIds, loggedUserRoleIds);
		List<ReferenceDataBo> queryResult = referenceRepository.getReferencesFromOutpatientConsultation(patientId, clinicalSpecialtyIds, loggedUserRoleIds, APPROVED_REGULATION_STATE);
       	queryResult.addAll(referenceRepository.getReferencesFromOdontologyConsultation(patientId, clinicalSpecialtyIds, loggedUserRoleIds, APPROVED_REGULATION_STATE));
		return setReferenceDetails(queryResult);
    }

    @Override
    public List<ReferenceProblemBo> getReferencesProblems(Integer patientId, List<Short> loggedUserRoleIds) {
        log.debug("Input parameters -> patientId {}, loggedUserRoleIds {}", patientId, loggedUserRoleIds);
        return referenceHealthConditionRepository.getReferencesProblemsByPatientId(patientId, loggedUserRoleIds, EReferenceRegulationState.REJECTED.getId());
    }

	@Override
	public List<ReferenceSummaryBo> getReferencesSummary(Integer patientId, Integer clinicalSpecialtyId, Integer careLineId, Integer practiceId, List<Short> userRoleIds) {
    	log.debug("Input parameters -> patientId {}, clinicalSpecialtyid {}, careLineId {}, practiceId {} ", patientId, clinicalSpecialtyId, careLineId, practiceId);
		List<ReferenceSummaryBo> queryResult = getReferencesSummaryBySearchCriteria(patientId, clinicalSpecialtyId, careLineId, practiceId, userRoleIds);
		boolean featureFlagNameSelfDetermination = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS);
		queryResult.forEach(r -> r.setIncludeNameSelfDetermination(featureFlagNameSelfDetermination));
		log.debug("Output -> references {} ", queryResult);
		return queryResult;
	}

	private List<ReferenceSummaryBo> getReferencesSummaryBySearchCriteria(Integer patientId, Integer clinicalSpecialtyId, Integer careLineId, Integer practiceId, List<Short> userRoleIds) {

		if (clinicalSpecialtyId != null && practiceId == null)
			return getReferencesSummaryByClinicalSpecialty(patientId, clinicalSpecialtyId, careLineId, userRoleIds);

		if (clinicalSpecialtyId == null && practiceId != null)
			return  getReferencesSummaryByPractice(patientId, practiceId, careLineId, userRoleIds);

		return getReferencesSummaryByClinicalSpecialtyAndPractice(patientId, clinicalSpecialtyId, practiceId, careLineId, userRoleIds);
	}

	private List<ReferenceSummaryBo> getReferencesSummaryByClinicalSpecialty(Integer patientId, Integer clinicalSpecialtyId, Integer careLineId, List<Short> userRoleIds) {
		List<ReferenceSummaryBo> queryResult;
		if (careLineId != null){
			queryResult = getReferencesSummaryByClinicalSpecialtyIdAndCareLineId(patientId, clinicalSpecialtyId, careLineId, userRoleIds);
		} else {
			queryResult = getReferencesSummaryByClinicalSpecialtyId(patientId, clinicalSpecialtyId, userRoleIds);
		}
		return queryResult;
	}

	private List<ReferenceSummaryBo> getReferencesSummaryByPractice(Integer patientId, Integer practiceId, Integer careLineId, List<Short> userRoleIds) {
		List<ReferenceSummaryBo> queryResult;
		if(careLineId != null) {
			queryResult = getReferencesSummaryByPracticeIdAndCareLineId(patientId, practiceId, careLineId, userRoleIds);
		} else {
			queryResult = getReferencesSummaryByPracticeId(patientId, practiceId, userRoleIds);
		}
		return queryResult;
	}

	private List<ReferenceSummaryBo> getReferencesSummaryByClinicalSpecialtyAndPractice(Integer patientId, Integer clinicalSpecialtyId, Integer practiceId, Integer careLineId, List<Short> userRoleIds) {
		List<ReferenceSummaryBo> queryResult;
		if (careLineId != null) {
			queryResult = getReferencesSummaryByClinicalSpecialtyIdAndPracticeIdAndCareLineId(patientId, clinicalSpecialtyId, practiceId, careLineId, userRoleIds);
		} else {
			queryResult = getReferencesSummaryByClinicalSpecialtyIdAndPracticeId(patientId, clinicalSpecialtyId, practiceId, userRoleIds);
		}
		return queryResult;
	}
	
	@Override
	public Optional<ReferenceDataBo> getReferenceData(Integer referenceId) {
		Integer sourceTypeId = referenceRepository.getReferenceEncounterTypeId(referenceId);
		if (sourceTypeId == null)
			return Optional.empty();
		Optional<ReferenceDataBo> result = sourceTypeId.equals(OUTPATIENT_SOURCE_TYPE_ID) ? referenceRepository.getReferenceDataFromOutpatientConsultation(referenceId) :
				referenceRepository.getReferenceDataFromOdontologyConsultation(referenceId);
		result.ifPresent(referenceDataBo -> setReferenceDetails(Collections.singletonList(referenceDataBo)));
		return result;
	}

	@Override
	public void delete(Integer referenceId) {
		referenceRepository.deleteAndUpdateStatus(referenceId, EReferenceStatus.ERROR.getId());
	}

	private List<ReferenceDataBo> setReferenceDetails(List<ReferenceDataBo> references) {
		List<Integer> referenceIds = references.stream().map(ReferenceDataBo::getId).collect(Collectors.toList());
		var referencesProblems = referenceHealthConditionRepository.getReferencesProblems(referenceIds);
		var files = referenceCounterReferenceFileStorage.getFilesByReferenceCounterReferenceIdsAndType(referenceIds, EReferenceCounterReferenceType.REFERENCIA);
		var referencesStudiesIds = references.stream().filter(r -> r.getServiceRequestId() != null).collect(Collectors.toMap(ReferenceDataBo::getServiceRequestId, ReferenceDataBo::getId));
		Map<Integer, Pair<SnomedBo, String>> referencesProcedures = referenceStudyStorage.getReferencesProcedures(referencesStudiesIds);
		return references.stream()
				.peek(ref -> {
					ref.setProblems(referencesProblems.stream()
							.filter(rp -> rp.getReferenceId().equals(ref.getId()))
							.map(rp -> rp.getSnomed().getPt()).collect(Collectors.toList()));
					ref.setFiles(files.get(ref.getId()));
					ref.setProcedure(referencesProcedures.get(ref.getId()) != null ? referencesProcedures.get(ref.getId()).getLeft() : null);
					ref.setProcedureCategory(referencesProcedures.get(ref.getId()) != null ? referencesProcedures.get(ref.getId()).getRight() : null);
					ref.setProfessionalFullName(sharedPersonPort.getCompletePersonNameById(ref.getProfessionalPersonId()));
					ref.setDestinationClinicalSpecialties(referenceClinicalSpecialtyRepository.getClinicalSpecialtiesByReferenceId(ref.getId()));
				}).collect(Collectors.toList());
	}

	@Override
	public Optional<ReferenceRequestBo> getReferenceByServiceRequestId(Integer serviceRequestId){
		log.debug("Input parameters -> serviceRequestId {} ", serviceRequestId);
		Optional<ReferenceRequestBo> ref =  referenceRepository.getReferenceByServiceRequestId(serviceRequestId);
		ref.ifPresent(r ->
				r.setClinicalSpecialties(referenceClinicalSpecialtyRepository.getClinicalSpecialtiesByReferenceId(r.getId())
		));
		return ref;
	}

	@Override
	public Optional<Reference> findById(Integer referenceId) {
		log.debug("Input parameters -> referenceId {} ", referenceId);
		return referenceRepository.findById(referenceId);
	}

	@Override
	public Short getReferenceRegulationStateId(Integer referenceId) {
		log.debug("Input parameters -> referenceId {} ", referenceId);
		return referenceRepository.getReferenceRegulationStateId(referenceId).get(0);
	}

	private List<ReferenceSummaryBo> getReferencesSummaryByClinicalSpecialtyIdAndCareLineId(Integer patientId, Integer clinicalSpecialtyId, Integer careLineId, List<Short> userRoleIds){
		List<ReferenceSummaryBo> result;
		result = referenceRepository.getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyIdAndCareLineId(patientId, clinicalSpecialtyId, careLineId, userRoleIds);
		result.addAll(referenceRepository.getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyIdAndCareLineId(patientId, clinicalSpecialtyId, careLineId, userRoleIds));
		return result;
	}

	private List<ReferenceSummaryBo> getReferencesSummaryByClinicalSpecialtyId(Integer patientId, Integer clinicalSpecialtyId, List<Short> userRoleIds){
		List<ReferenceSummaryBo> result;
		result = referenceRepository.getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyId(patientId, clinicalSpecialtyId, userRoleIds);
		result.addAll(referenceRepository.getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyId(patientId, clinicalSpecialtyId, userRoleIds));
		return result;
	}

	private List<ReferenceSummaryBo> getReferencesSummaryByPracticeIdAndCareLineId(Integer patientId, Integer practiceId, Integer careLineId, List<Short> userRoleIds){
		List<ReferenceSummaryBo> result;
		result = referenceRepository.getReferencesSummaryFromOutpatientConsultationByPracticeIdAndCareLineId(patientId, practiceId, careLineId, userRoleIds);
		result.addAll(referenceRepository.getReferencesSummaryFromOdontologyConsultationByPracticeIdAndCareLineId(patientId, practiceId, careLineId, userRoleIds));
		return result;
	}

	private List<ReferenceSummaryBo> getReferencesSummaryByPracticeId(Integer patientId, Integer practiceId, List<Short> userRoleIds){
		List<ReferenceSummaryBo> result;
		result = referenceRepository.getReferencesSummaryFromOutpatientConsultationByPracticeId(patientId, practiceId, userRoleIds);
		result.addAll(referenceRepository.getReferencesSummaryFromOdontologyConsultationByPracticeId(patientId, practiceId, userRoleIds));
		return result;
	}

	private List<ReferenceSummaryBo> getReferencesSummaryByClinicalSpecialtyIdAndPracticeIdAndCareLineId(Integer patientId, Integer clinicalSpecialtyId, Integer practiceId, Integer careLineId, List<Short> userRoleIds){
		List<ReferenceSummaryBo> result;
		result = referenceRepository.getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyIdAndPracticeIdAndCareLineId(patientId, clinicalSpecialtyId, careLineId, practiceId, userRoleIds);
		result.addAll(referenceRepository.getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyIdAndPracticeIdAndCareLineId(patientId, clinicalSpecialtyId, careLineId, practiceId, userRoleIds));
		return result;
	}

	private List<ReferenceSummaryBo> getReferencesSummaryByClinicalSpecialtyIdAndPracticeId(Integer patientId, Integer clinicalSpecialtyId, Integer practiceId, List<Short> userRoleIds){
		List<ReferenceSummaryBo> result;
		result = referenceRepository.getReferencesSummaryFromOutpatientConsultationByClinicalSpecialtyIdAndPracticeId(patientId, clinicalSpecialtyId, practiceId, userRoleIds);
		result.addAll(referenceRepository.getReferencesSummaryFromOdontologyConsultationByClinicalSpecialtyIdAndPracticeId(patientId, clinicalSpecialtyId, practiceId, userRoleIds));
		return result;
	}

	@Override
	public void deleteAndUpdateStatus(Integer referenceId, Short statusId){
		log.debug("Input parameters -> referenceId {}, statusId {}", referenceId, statusId);
		referenceRepository.deleteAndUpdateStatus(referenceId, statusId);
	}

	@Override
	public Optional<Integer> getReferenceEncounterTypeId(Integer referenceId){
		log.debug("Input parameters -> referenceId {}", referenceId);
		Optional<Integer> result = Optional.ofNullable(referenceRepository.getReferenceEncounterTypeId(referenceId));
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Optional<ReferenceStudyBo> getReferenceStudy (Integer referenceId){
		log.debug("Input parameteres -> referenceId {}", referenceId);
		Optional<ReferenceStudyBo> result = referenceRepository.getReferenceStudy(referenceId);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public void updateDestinationInstitution(Integer referenceId, Integer institutionId) {
		log.debug("Input parameteres -> referenceId {}, institutionId {}", referenceId, institutionId);
		var reference = this.referenceRepository.findById(referenceId).orElseThrow(() ->
				new ReferenceException(ReferenceExceptionEnum.INVALID_REFERENCE_ID,  String.format("La referencia con id %s no existe", referenceId)));
		if (!Objects.equals(reference.getDestinationInstitutionId(), institutionId)) {
			reference.setDestinationInstitutionId(institutionId);
			this.referenceRepository.save(reference);
		}
	}

	@Override
	public Integer getDestinationInstitutionId(Integer referenceId) {
		log.debug("Input parameteres -> referenceId {}", referenceId);
		return this.referenceRepository.getDestinationInstitutionId(referenceId);
	}

}
