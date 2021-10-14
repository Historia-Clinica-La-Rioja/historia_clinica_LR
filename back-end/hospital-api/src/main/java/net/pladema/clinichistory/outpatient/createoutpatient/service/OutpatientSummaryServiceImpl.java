package net.pladema.clinichistory.outpatient.createoutpatient.service;

import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ReasonSummaryVo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.EvolutionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.HealthConditionSummaryBo;
import net.pladema.clinichistory.outpatient.repository.OdontologyConsultationSummaryRepository;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationSummaryRepository;
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

    public OutpatientSummaryServiceImpl(OutpatientConsultationSummaryRepository outpatientConsultationSummaryRepository,
                                        OdontologyConsultationSummaryRepository odontologyConsultationSummaryRepository) {
        this.outpatientConsultationSummaryRepository = outpatientConsultationSummaryRepository;
        this.odontologyConsultationSummaryRepository = odontologyConsultationSummaryRepository;
    }

    @Override
    public List<EvolutionSummaryBo> getSummary(Integer patientId) {
        LOG.debug("Input parameter -> patientId {}", patientId);
        List<EvolutionSummaryBo> result = new ArrayList<>();
        result.addAll(getOutpatientEvolutionSummaries(patientId));
        result.addAll(getOdontologyEvolutionSummaries(patientId));
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
            oesBo.setHealthConditions(healthConditions.stream().filter(h -> h.getConsultationID().equals(oes.getConsultationID())).map(HealthConditionSummaryBo::new).collect(Collectors.toList()));
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

}
