package net.pladema.clinichistory.outpatient.createoutpatient.service;

import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReasonSummaryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ReferenceSummaryVo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.EvolutionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.HealthConditionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.ReferenceSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.ReferenceCounterReferenceFileBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference.CounterReferenceSummaryBo;
import net.pladema.clinichistory.outpatient.repository.NursingConsultationSummaryRepository;
import net.pladema.clinichistory.outpatient.repository.OdontologyConsultationSummaryRepository;
import net.pladema.clinichistory.outpatient.repository.ReferenceCounterReferenceStorage;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationSummaryStorage;
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

    private final OutpatientConsultationSummaryStorage outpatientConsultationSummaryStorage;

    private final OdontologyConsultationSummaryRepository odontologyConsultationSummaryRepository;

    private final NursingConsultationSummaryRepository nursingConsultationSummaryRepository;

    private final ReferenceCounterReferenceStorage referenceCounterReferenceStorage;

    public OutpatientSummaryServiceImpl(OutpatientConsultationSummaryStorage outpatientConsultationSummaryStorage,
                                        OdontologyConsultationSummaryRepository odontologyConsultationSummaryRepository,
                                        NursingConsultationSummaryRepository nursingConsultationSummaryRepository,
                                        ReferenceCounterReferenceStorage referenceCounterReferenceStorage) {
        this.outpatientConsultationSummaryStorage = outpatientConsultationSummaryStorage;
        this.odontologyConsultationSummaryRepository = odontologyConsultationSummaryRepository;
        this.nursingConsultationSummaryRepository = nursingConsultationSummaryRepository;
        this.referenceCounterReferenceStorage = referenceCounterReferenceStorage;
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
        List<OutpatientEvolutionSummaryVo> queryResult = outpatientConsultationSummaryStorage.getAllOutpatientEvolutionSummary(patientId);
        List<Integer> outpatientConsultationIds = queryResult.stream().map(OutpatientEvolutionSummaryVo::getConsultationID).collect(Collectors.toList());
        List<HealthConditionSummaryVo> healthConditions = outpatientConsultationSummaryStorage.getHealthConditionsByPatient(patientId, outpatientConsultationIds);
        List<ReasonSummaryBo> reasons = outpatientConsultationSummaryStorage.getReasonsByPatient(patientId, outpatientConsultationIds);
        List<ProcedureSummaryBo> procedures = outpatientConsultationSummaryStorage.getProceduresByPatient(patientId, outpatientConsultationIds);
        List<EvolutionSummaryBo> result = new ArrayList<>();
        for (OutpatientEvolutionSummaryVo oes : queryResult) {
            EvolutionSummaryBo oesBo = new EvolutionSummaryBo(oes);
            oesBo.setHealthConditions(healthConditions.stream().filter(h -> h.getConsultationID().equals(oes.getConsultationID())).map(HealthConditionSummaryBo::new).collect(Collectors.toList()));
            oesBo.setReasons(reasons.stream().filter(r -> r.getConsultationId().equals(oes.getConsultationID())).map(ReasonBo::new).collect(Collectors.toList()));
            oesBo.setProcedures(procedures.stream().filter(p -> p.getConsultationId().equals(oes.getConsultationID())).map(ProcedureBo::new).collect(Collectors.toList()));
            oesBo.setHealthConditions(healthConditions
                    .stream()
                    .filter(h -> h.getConsultationID().equals(oes.getConsultationID()))
                    .map(hcv -> {
                        List<ReferenceSummaryBo> referenceSummaryBoList = mapToReferenceSummaryBoList(outpatientConsultationSummaryStorage.getReferencesByHealthCondition(hcv.getId(), oes.getConsultationID()));
                        HealthConditionSummaryBo healthConditionSummaryBo = new HealthConditionSummaryBo(hcv);
                        healthConditionSummaryBo.setReferences(getReferencesData(referenceSummaryBoList));
                        return healthConditionSummaryBo;
                    }).collect(Collectors.toList()));
            oesBo.setReasons(reasons.stream().filter(r -> r.getConsultationId().equals(oes.getConsultationID())).map(ReasonBo::new).collect(Collectors.toList()));
            oesBo.setProcedures(procedures.stream().filter(p -> p.getConsultationId().equals(oes.getConsultationID())).map(ProcedureBo::new).collect(Collectors.toList()));
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
        List<ReasonSummaryBo> reasons = odontologyConsultationSummaryRepository.getReasonsByPatient(patientId, odontologyConsultationIds);
        List<ProcedureSummaryBo> procedures = odontologyConsultationSummaryRepository.getProceduresByPatient(patientId, odontologyConsultationIds);
        List<EvolutionSummaryBo> result = new ArrayList<>();
        for (OdontologyEvolutionSummaryVo oes : queryResult) {
            EvolutionSummaryBo oesBo = new EvolutionSummaryBo(oes);
            oesBo.setHealthConditions(healthConditions
                    .stream()
                    .filter(h -> h.getConsultationID().equals(oes.getConsultationId()))
                    .map(hcv -> {
                        List<ReferenceSummaryBo> referenceSummaryBoList = mapToReferenceSummaryBoList(odontologyConsultationSummaryRepository.getReferenceByHealthCondition(hcv.getId(), oes.getConsultationId()));
                        HealthConditionSummaryBo healthConditionSummaryBo = new HealthConditionSummaryBo(hcv);
                        healthConditionSummaryBo.setReferences(getReferencesData(referenceSummaryBoList));
                        return healthConditionSummaryBo;
                    }).collect(Collectors.toList()));
            oesBo.setReasons(reasons.stream().filter(r -> r.getConsultationId().equals(oes.getConsultationId())).map(ReasonBo::new).collect(Collectors.toList()));
            oesBo.setProcedures(procedures.stream().filter(p -> p.getConsultationId().equals(oes.getConsultationId())).map(ProcedureBo::new).collect(Collectors.toList()));
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
        List<ProcedureSummaryBo> procedures = nursingConsultationSummaryRepository.getProceduresByPatient(patientId, nursingConsultationIds);
        List<EvolutionSummaryBo> result = new ArrayList<>();
        for (NursingEvolutionSummaryVo nes : queryResult) {
            EvolutionSummaryBo nesBo = new EvolutionSummaryBo(nes);
            nesBo.setHealthConditions(healthConditions.stream().filter(h -> h.getConsultationID().equals(nes.getConsultationId())).map(HealthConditionSummaryBo::new).collect(Collectors.toList()));
            nesBo.setProcedures(procedures.stream().filter(p -> p.getConsultationId().equals(nes.getConsultationId())).map(ProcedureBo::new).collect(Collectors.toList()));
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
                        CounterReferenceSummaryBo counterReferenceSummaryBo = referenceCounterReferenceStorage.getCounterReference(referenceSummaryBo.getId());
                        referenceSummaryBo.setCounterReference(counterReferenceSummaryBo);
                    });
        }
        return referenceSummaryBoList;
    }

    public List<ReferenceCounterReferenceFileBo> getReferenceFiles(Integer referenceId) {
        LOG.debug("Input parameter -> referenceId {}", referenceId);;
        return referenceCounterReferenceStorage.getReferenceFilesData(referenceId);
    }

}
