package net.pladema.clinichistory.outpatient.createoutpatient.service;

import ar.lamansys.refcounterref.infraestructure.input.service.ReferenceCounterReferenceService;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ReasonSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ReferenceSummaryVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.CounterReferenceSummaryDto;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.EvolutionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.HealthConditionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.ReferenceSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.ProfessionalInfoBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.ReferenceCounterReferenceFileBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.CounterReferenceProcedureBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.CounterReferenceSummaryBo;
import net.pladema.clinichistory.outpatient.repository.NursingConsultationSummaryRepository;
import net.pladema.clinichistory.outpatient.repository.OdontologyConsultationSummaryRepository;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationSummaryRepository;
import net.pladema.clinichistory.outpatient.repository.domain.NursingEvolutionSummaryVo;
import net.pladema.clinichistory.outpatient.repository.domain.OdontologyEvolutionSummaryVo;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientEvolutionSummaryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OutpatientSummaryServiceImpl implements OutpatientSummaryService {

    private static final Logger LOG = LoggerFactory.getLogger(OutpatientSummaryServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final OutpatientConsultationSummaryRepository outpatientConsultationSummaryRepository;

    private final OdontologyConsultationSummaryRepository odontologyConsultationSummaryRepository;

    private final NursingConsultationSummaryRepository nursingConsultationSummaryRepository;

    private final ReferenceCounterReferenceService referenceCounterReferenceService;

    public OutpatientSummaryServiceImpl(OutpatientConsultationSummaryRepository outpatientConsultationSummaryRepository,
                                        OdontologyConsultationSummaryRepository odontologyConsultationSummaryRepository,
                                        NursingConsultationSummaryRepository nursingConsultationSummaryRepository,
                                        ReferenceCounterReferenceService referenceCounterReferenceService) {
        this.outpatientConsultationSummaryRepository = outpatientConsultationSummaryRepository;
        this.odontologyConsultationSummaryRepository = odontologyConsultationSummaryRepository;
        this.nursingConsultationSummaryRepository = nursingConsultationSummaryRepository;
        this.referenceCounterReferenceService = referenceCounterReferenceService;
    }

    @Override
    public List<EvolutionSummaryBo> getSummary(Integer patientId) {
        LOG.debug("Input parameter -> patientId {}", patientId);
        List<EvolutionSummaryBo> result = new ArrayList<>();
        result.addAll(getOutpatientEvolutionSummaries(patientId));
        result.addAll(getOdontologyEvolutionSummaries(patientId));
        result.addAll(getNursingEvolutionSummaries(patientId));
        result.sort(Comparator.comparing(EvolutionSummaryBo::getStartDate).reversed());
        LOG.debug(OUTPUT, result);
        return result;
    }

    private List<EvolutionSummaryBo> getOutpatientEvolutionSummaries(Integer patientId) {
        LOG.debug("Input parameter -> patientId {}", patientId);
        List<OutpatientEvolutionSummaryVo> queryResult = outpatientConsultationSummaryRepository.getAllOutpatientEvolutionSummary(patientId);
        List<Integer> outpatientConsultationIds = queryResult.stream().map(OutpatientEvolutionSummaryVo::getConsultationID).collect(Collectors.toList());
        List<HealthConditionSummaryVo> healthConditions = outpatientConsultationSummaryRepository.getHealthConditionsByPatient(patientId, outpatientConsultationIds);
        List<ReasonSummaryVo> reasons = outpatientConsultationSummaryRepository.getReasonsByPatient(patientId, outpatientConsultationIds);
        List<ProcedureSummaryVo> procedures = outpatientConsultationSummaryRepository.getProceduresByPatient(patientId, outpatientConsultationIds);
        List<EvolutionSummaryBo> result = new ArrayList<>();
        for (OutpatientEvolutionSummaryVo oes : queryResult) {
            EvolutionSummaryBo oesBo = new EvolutionSummaryBo(oes);
            oesBo.setHealthConditions(healthConditions
                    .stream()
                    .filter(h -> h.getConsultationID().equals(oes.getConsultationID()))
                    .map(hcv -> {
                        List<ReferenceSummaryBo> referenceSummaryBoList = mapToReferenceSummaryBoList(outpatientConsultationSummaryRepository.getReferencesByHealthCondition(hcv.getId(), oes.getConsultationID()));
                        HealthConditionSummaryBo healthConditionSummaryBo = new HealthConditionSummaryBo(hcv);
                        healthConditionSummaryBo.setReferences(getReferencesData(referenceSummaryBoList));
                        return healthConditionSummaryBo;
                    }).collect(Collectors.toList()));
            oesBo.setReasons(reasons.stream().filter(r -> r.getConsultationID().equals(oes.getConsultationID())).map(ReasonBo::new).collect(Collectors.toList()));
            oesBo.setProcedures(procedures.stream().filter(p -> p.getConsultationID().equals(oes.getConsultationID())).map(ProcedureBo::new).collect(Collectors.toList()));
            result.add(oesBo);
        }
        LOG.debug("Output size -> {}", result.size());
        LOG.trace(OUTPUT, result);
        return result;
    }

    private List<EvolutionSummaryBo> getOdontologyEvolutionSummaries(Integer patientId) {
        LOG.debug("Input parameter -> patientId {}", patientId);
        List<OdontologyEvolutionSummaryVo> queryResult = odontologyConsultationSummaryRepository.getAllOdontologyEvolutionSummary(patientId);
        List<Integer> odontologyConsultationIds = queryResult.stream().map(OdontologyEvolutionSummaryVo::getConsultationId).collect(Collectors.toList());
        List<HealthConditionSummaryVo> healthConditions = odontologyConsultationSummaryRepository.getHealthConditionsByPatient(patientId, odontologyConsultationIds);
        List<ReasonSummaryVo> reasons = odontologyConsultationSummaryRepository.getReasonsByPatient(patientId, odontologyConsultationIds);
        List<ProcedureSummaryVo> procedures = odontologyConsultationSummaryRepository.getProceduresByPatient(patientId, odontologyConsultationIds);
        List<EvolutionSummaryBo> result = new ArrayList<>();
        for (OdontologyEvolutionSummaryVo oes : queryResult) {
            EvolutionSummaryBo oesBo = new EvolutionSummaryBo(oes);
            oesBo.setHealthConditions(healthConditions.stream().filter(h -> h.getConsultationID().equals(oes.getConsultationId())).map(HealthConditionSummaryBo::new).collect(Collectors.toList()));
            oesBo.setReasons(reasons.stream().filter(r -> r.getConsultationID().equals(oes.getConsultationId())).map(ReasonBo::new).collect(Collectors.toList()));
            oesBo.setProcedures(procedures.stream().filter(p -> p.getConsultationID().equals(oes.getConsultationId())).map(ProcedureBo::new).collect(Collectors.toList()));
            result.add(oesBo);
        }
        LOG.debug("Output size -> {}", result.size());
        LOG.trace(OUTPUT, result);
        return result;
    }

    private List<EvolutionSummaryBo> getNursingEvolutionSummaries(Integer patientId) {
        LOG.debug("Input parameter -> patientId {}", patientId);
        List<NursingEvolutionSummaryVo> queryResult = nursingConsultationSummaryRepository.getAllNursingEvolutionSummary(patientId);
        List<Integer> nursingConsultationIds = queryResult.stream().map(NursingEvolutionSummaryVo::getConsultationId).collect(Collectors.toList());
        List<HealthConditionSummaryVo> healthConditions = nursingConsultationSummaryRepository.getHealthConditionsByPatient(patientId, nursingConsultationIds);
        List<ProcedureSummaryVo> procedures = nursingConsultationSummaryRepository.getProceduresByPatient(patientId, nursingConsultationIds);
        List<EvolutionSummaryBo> result = new ArrayList<>();
        for (NursingEvolutionSummaryVo nes : queryResult) {
            EvolutionSummaryBo nesBo = new EvolutionSummaryBo(nes);
            nesBo.setHealthConditions(healthConditions.stream().filter(h -> h.getConsultationID().equals(nes.getConsultationId())).map(HealthConditionSummaryBo::new).collect(Collectors.toList()));
            nesBo.setProcedures(procedures.stream().filter(p -> p.getConsultationID().equals(nes.getConsultationId())).map(ProcedureBo::new).collect(Collectors.toList()));
            result.add(nesBo);
        }
        LOG.debug("Output size -> {}", result.size());
        LOG.trace(OUTPUT, result);
        return result;
    }

    public List<ReferenceSummaryBo> mapToReferenceSummaryBoList(List<ReferenceSummaryVo> referenceSummaryVoList) {
        List<ReferenceSummaryBo> referenceSummaryBoList = new ArrayList<>();
        referenceSummaryVoList.stream().forEach(referenceSummaryVo ->
                referenceSummaryBoList.add(new ReferenceSummaryBo(referenceSummaryVo.getId(), referenceSummaryVo.getCareLine(), referenceSummaryVo.getClinicalSpecialty(),
                        referenceSummaryVo.getNote()))
        );
        return referenceSummaryBoList;
    }

    private List<ReferenceSummaryBo> getReferencesData(List<ReferenceSummaryBo> referenceSummaryBoList) {
        LOG.debug("Input parameter -> referenceSummaryBoList {}", referenceSummaryBoList);
        if (!referenceSummaryBoList.isEmpty()) {
            referenceSummaryBoList
                    .stream()
                    .forEach(referenceSummaryBo -> {
                        referenceSummaryBo.setFiles(getReferenceFiles(referenceSummaryBo.getId()));
                        CounterReferenceSummaryBo counterReferenceSummaryBo = mapToCounterReferenceSummaryBo(referenceCounterReferenceService.getCounterReference(referenceSummaryBo.getId()));
                        referenceSummaryBo.setCounterReference(counterReferenceSummaryBo);
                    });
        }
        return referenceSummaryBoList;
    }

    public CounterReferenceSummaryBo mapToCounterReferenceSummaryBo(CounterReferenceSummaryDto counterReferenceSummaryDto) {
        return new CounterReferenceSummaryBo(counterReferenceSummaryDto.getId(),
                counterReferenceSummaryDto.getClinicalSpecialty(),
                counterReferenceSummaryDto.getNote(),
                counterReferenceSummaryDto.getPerformedDate(),
                counterReferenceSummaryDto.getProfessional() != null ? new ProfessionalInfoBo(counterReferenceSummaryDto.getProfessional().getId(),
                        counterReferenceSummaryDto.getProfessional().getFirstName(),
                        counterReferenceSummaryDto.getProfessional().getLastName()) : null,
                counterReferenceSummaryDto.getFiles() != null ? counterReferenceSummaryDto.getFiles()
                        .stream()
                        .map(crf -> new ReferenceCounterReferenceFileBo(crf.getFileId(), crf.getFileName()))
                        .collect(Collectors.toList()) : null,
                counterReferenceSummaryDto.getProcedures() != null ? counterReferenceSummaryDto.getProcedures()
                        .stream()
                        .map(crp -> new CounterReferenceProcedureBo(crp.getSnomed().getSctid(), crp.getSnomed().getPt()))
                        .collect(Collectors.toList()) : null);
    }

    public List<ReferenceCounterReferenceFileBo> getReferenceFiles(Integer referenceId) {
        LOG.debug("Input parameter -> referenceId {}", referenceId);
        return referenceCounterReferenceService.getReferenceFiles(referenceId)
                .stream()
                .map(rf -> new ReferenceCounterReferenceFileBo(rf.getFileId(), rf.getFileName()))
                .collect(Collectors.toList());
    }

}
