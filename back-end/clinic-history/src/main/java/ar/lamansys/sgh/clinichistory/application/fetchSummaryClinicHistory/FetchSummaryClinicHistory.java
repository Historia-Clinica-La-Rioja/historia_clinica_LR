package ar.lamansys.sgh.clinichistory.application.fetchSummaryClinicHistory;

import ar.lamansys.sgh.clinichistory.application.fetchAllDocumentInfo.port.DocumentInvolvedProfessionalStorage;
import ar.lamansys.sgh.clinichistory.application.ports.HCEOutpatientConsultationSummaryStorage;
import ar.lamansys.sgh.clinichistory.application.ports.HCEReferenceCounterReferenceStorage;
import ar.lamansys.sgh.clinichistory.application.ports.NursingConsultationSummaryStorage;
import ar.lamansys.sgh.clinichistory.application.ports.OdontologyConsultationSummaryStorage;
import ar.lamansys.sgh.clinichistory.domain.document.enums.EElectronicSignatureStatus;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.CounterReferenceSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.DocumentDataBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ElectronicJointSignatureProfessionalsBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.EvolutionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.HealthConditionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.NursingEvolutionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.OdontologyEvolutionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.OutpatientEvolutionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReasonSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReferenceCounterReferenceFileBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReferenceSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentRepository;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.security.UserInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FetchSummaryClinicHistory {

    public static final String OUTPUT = "Output -> {}";

    private final HCEOutpatientConsultationSummaryStorage HCEOutpatientConsultationSummaryStorage;

    private final OdontologyConsultationSummaryStorage odontologyConsultationSummaryStorage;

    private final NursingConsultationSummaryStorage nursingConsultationSummaryStorage;

    private final HCEReferenceCounterReferenceStorage hceReferenceCounterReferenceStorage;

	private final DocumentRepository documentRepository;

	private final SharedInstitutionPort sharedInstitutionPort;

	private final SharedLoggedUserPort sharedLoggedUserPort;

	private final DocumentInvolvedProfessionalStorage documentInvolvedProfessionalStorage;

	private final SharedPersonPort sharedPersonPort;

	private final FeatureFlagsService featureFlagsService;

    public List<EvolutionSummaryBo> run(Integer institutionId, Integer patientId) {
        log.debug("FetchSummaryClinicHistory from patientId {}", patientId);
        List<EvolutionSummaryBo> result = new ArrayList<>();
		List<Short> loggedUserRoleIds = sharedLoggedUserPort.getLoggedUserRoleIds(institutionId, UserInfo.getCurrentAuditor());
        result.addAll(getOutpatientEvolutionSummaries(patientId, loggedUserRoleIds));
        result.addAll(getOdontologyEvolutionSummaries(patientId, loggedUserRoleIds));
        result.addAll(getNursingEvolutionSummaries(patientId));
        result.sort(Comparator.comparing(EvolutionSummaryBo::getStartDate).reversed());
        log.debug(OUTPUT, result);
        return result;
    }

	private String getInstitutionFromDocument(DocumentDataBo document) {
		if (document != null) {
			return sharedInstitutionPort.fetchInstitutionById(documentRepository.getInstitutionIdFromDocument(document.getId())).getName();
		} else {
			return null;
		}
	}

    private List<EvolutionSummaryBo> getOutpatientEvolutionSummaries(Integer patientId, List<Short> loggedUserRoleIds) {
        List<OutpatientEvolutionSummaryBo> queryResult = HCEOutpatientConsultationSummaryStorage.getAllOutpatientEvolutionSummary(patientId);
        List<Integer> outpatientConsultationIds = queryResult.stream().map(OutpatientEvolutionSummaryBo::getConsultationId).collect(Collectors.toList());
        List<HealthConditionSummaryBo> healthConditions = HCEOutpatientConsultationSummaryStorage.getHealthConditionsByPatient(patientId, outpatientConsultationIds);
        List<ReasonSummaryBo> reasons = HCEOutpatientConsultationSummaryStorage.getReasonsByPatient(patientId, outpatientConsultationIds);
        List<ProcedureSummaryBo> procedures = HCEOutpatientConsultationSummaryStorage.getProceduresByPatient(patientId, outpatientConsultationIds);
        List<EvolutionSummaryBo> result = new ArrayList<>();
		queryResult.forEach(oes -> processOutpatientEvolutionSummaries(loggedUserRoleIds, oes, healthConditions, reasons, procedures, result));
        log.trace(OUTPUT, result);
        return result;
    }

	private void processOutpatientEvolutionSummaries(List<Short> loggedUserRoleIds, OutpatientEvolutionSummaryBo oes, List<HealthConditionSummaryBo> healthConditions, List<ReasonSummaryBo> reasons, List<ProcedureSummaryBo> procedures, List<EvolutionSummaryBo> result) {
		EvolutionSummaryBo oesBo = new EvolutionSummaryBo(oes);
		oesBo.setHealthConditions(healthConditions.stream().filter(h -> h.getConsultationId().equals(oes.getConsultationId()))
				.peek(hs -> hs.setReferences(getReferencesData(HCEOutpatientConsultationSummaryStorage
						.getReferencesByHealthCondition(hs.getId(), oes.getConsultationId(), loggedUserRoleIds)))).collect(Collectors.toList()));
		oesBo.setReasons(reasons.stream().filter(r -> r.getConsultationId().equals(oes.getConsultationId()))
				.map(ReasonBo::new).collect(Collectors.toList()));
		oesBo.setProcedures(procedures.stream().filter(p -> p.getConsultationId().equals(oes.getConsultationId()))
				.map(ProcedureBo::new).collect(Collectors.toList()));
		oesBo.setInstitutionName(getInstitutionFromDocument(oesBo.getDocument()));
		if (featureFlagsService.isOn(AppFeature.HABILITAR_FIRMA_CONJUNTA)) {
            Optional.ofNullable(oesBo.getDocument())
                    .map(DocumentDataBo::getId)
                    .ifPresentOrElse(documentId -> oesBo.setElectronicJointSignatureProfessionals(this.fetchElectronicJointSignatureProfessionals(documentId)),
                            () -> log.debug("No document present associated with this evolution summary from consultation id {}", oesBo.getConsultationID()));
        }
		result.add(oesBo);
	}

	private ElectronicJointSignatureProfessionalsBo fetchElectronicJointSignatureProfessionals(Long documentId) {
		ElectronicJointSignatureProfessionalsBo result = new ElectronicJointSignatureProfessionalsBo();
		List<Integer> professionalsThatSignedPersonIds = documentInvolvedProfessionalStorage.getDocumentInvolvedProfessionalPersonIdsByDocumentIdAndStatusId(documentId, EElectronicSignatureStatus.SIGNED.getId());
		result.setProfessionalsThatSignedNames(sharedPersonPort.getCompletePersonsNameByIds(professionalsThatSignedPersonIds));
		result.setProfessionalsThatDidNotSignAmount(documentInvolvedProfessionalStorage.getDocumentInvolvedProfessionalAmountThatDidNotSignByDocumentId(documentId, EElectronicSignatureStatus.PENDING.getId()));
		return result;
	}

	private List<EvolutionSummaryBo> getOdontologyEvolutionSummaries(Integer patientId, List<Short> loggedUserRoleIds) {
        List<OdontologyEvolutionSummaryBo> queryResult = odontologyConsultationSummaryStorage.getAllOdontologyEvolutionSummary(patientId);
        List<Integer> odontologyConsultationIds = queryResult.stream().map(OdontologyEvolutionSummaryBo::getConsultationId).collect(Collectors.toList());
        List<HealthConditionSummaryBo> healthConditions = odontologyConsultationSummaryStorage.getHealthConditionsByPatient(patientId, odontologyConsultationIds);
        List<ReasonSummaryBo> reasons = odontologyConsultationSummaryStorage.getReasonsByPatient(patientId, odontologyConsultationIds);
        List<ProcedureSummaryBo> procedures = odontologyConsultationSummaryStorage.getProceduresByPatient(patientId, odontologyConsultationIds);
        List<EvolutionSummaryBo> result = new ArrayList<>();
        for (OdontologyEvolutionSummaryBo oes : queryResult) {
            EvolutionSummaryBo oesBo = new EvolutionSummaryBo(oes);
            oesBo.setHealthConditions(healthConditions.stream().filter(h -> h.getConsultationId().equals(oes.getConsultationId()))
                    .peek(hs -> hs.setReferences(getReferencesData(odontologyConsultationSummaryStorage
                            .getReferencesByHealthCondition(hs.getId(), oes.getConsultationId(), loggedUserRoleIds)))).collect(Collectors.toList()));
            oesBo.setReasons(reasons.stream().filter(r -> r.getConsultationId().equals(oes.getConsultationId()))
                    .map(ReasonBo::new).collect(Collectors.toList()));
            oesBo.setProcedures(procedures.stream().filter(p -> p.getConsultationId().equals(oes.getConsultationId()))
                    .map(ProcedureBo::new).collect(Collectors.toList()));
			oesBo.setInstitutionName(getInstitutionFromDocument(oesBo.getDocument()));
            result.add(oesBo);
        }
        log.trace(OUTPUT, result);
        return result;
    }

    private List<EvolutionSummaryBo> getNursingEvolutionSummaries(Integer patientId) {
        List<NursingEvolutionSummaryBo> queryResult = nursingConsultationSummaryStorage.getAllNursingEvolutionSummary(patientId);
        List<Integer> nursingConsultationIds = queryResult.stream().map(NursingEvolutionSummaryBo::getConsultationId).collect(Collectors.toList());
        List<HealthConditionSummaryBo> healthConditions = nursingConsultationSummaryStorage.getHealthConditionsByPatient(patientId, nursingConsultationIds);
        List<ProcedureSummaryBo> procedures = nursingConsultationSummaryStorage.getProceduresByPatient(patientId, nursingConsultationIds);
        List<EvolutionSummaryBo> result = new ArrayList<>();
        for (NursingEvolutionSummaryBo nes : queryResult) {
            EvolutionSummaryBo nesBo = new EvolutionSummaryBo(nes);
            nesBo.setHealthConditions(healthConditions.stream().filter(h -> h.getConsultationId().equals(nes.getConsultationId())).collect(Collectors.toList()));
            nesBo.setProcedures(procedures.stream().filter(p -> p.getConsultationId().equals(nes.getConsultationId())).map(ProcedureBo::new).collect(Collectors.toList()));
			nesBo.setInstitutionName(getInstitutionFromDocument(nesBo.getDocument()));
			result.add(nesBo);
        }
        log.trace(OUTPUT, result);
        return result;
    }

    private List<ReferenceSummaryBo> getReferencesData(List<ReferenceSummaryBo> referenceSummaryBos) {
        return referenceSummaryBos.stream().peek(referenceSummaryBo -> {
                        referenceSummaryBo.setFiles(getReferenceFiles(referenceSummaryBo.getId()));
                        CounterReferenceSummaryBo counterReferenceSummaryBo = hceReferenceCounterReferenceStorage.getCounterReference(referenceSummaryBo.getId());
                        referenceSummaryBo.setCounterReference(counterReferenceSummaryBo);
                })
                .collect(Collectors.toList());
    }

    private List<ReferenceCounterReferenceFileBo> getReferenceFiles(Integer referenceId) {
        log.debug("Input parameter -> referenceId {}", referenceId);
        return hceReferenceCounterReferenceStorage.getReferenceFilesData(referenceId);
    }
}
