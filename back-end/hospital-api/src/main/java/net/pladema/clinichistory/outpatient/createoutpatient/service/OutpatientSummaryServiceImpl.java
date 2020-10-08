package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.HealthConditionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientEvolutionSummaryBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationSummaryRepository;
import net.pladema.clinichistory.outpatient.repository.domain.HealthConditionSummaryVo;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientEvolutionSummaryVo;
import net.pladema.clinichistory.outpatient.repository.domain.ProcedureSummaryVo;
import net.pladema.clinichistory.outpatient.repository.domain.ReasonSummaryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OutpatientSummaryServiceImpl implements OutpatientSummaryService{

    private static final Logger LOG = LoggerFactory.getLogger(OutpatientSummaryServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final OutpatientConsultationSummaryRepository outpatientConsultationSummaryRepository;

    public OutpatientSummaryServiceImpl(OutpatientConsultationSummaryRepository outpatientConsultationSummaryRepository) {
        this.outpatientConsultationSummaryRepository = outpatientConsultationSummaryRepository;
    }

    @Override
    public List<OutpatientEvolutionSummaryBo> getSummary(Integer patientId) {
        List<OutpatientEvolutionSummaryVo> queryResult = outpatientConsultationSummaryRepository.getAllOutpatientEvolutionSummary(patientId);
        List<Integer> OutpatientConsultationIds = queryResult.stream().map(OutpatientEvolutionSummaryVo::getConsultationID).collect(Collectors.toList());
        List<HealthConditionSummaryVo> healthcConditions = outpatientConsultationSummaryRepository.getHealthConditionsByPatient(patientId, OutpatientConsultationIds);
        List<ReasonSummaryVo> reasons = outpatientConsultationSummaryRepository.getReasonsByPatient(patientId, OutpatientConsultationIds);
        List<ProcedureSummaryVo> procedures = outpatientConsultationSummaryRepository.getProceduresByPatient(patientId, OutpatientConsultationIds);
        List<OutpatientEvolutionSummaryBo> result = new ArrayList<>();
        for(OutpatientEvolutionSummaryVo oes: queryResult){
            OutpatientEvolutionSummaryBo oesBo = new OutpatientEvolutionSummaryBo(oes);
            oesBo.setHealthConditions(healthcConditions.stream().filter(h -> h.getConsultationID().equals(oes.getConsultationID())).map(HealthConditionSummaryBo::new).collect(Collectors.toList()));
            oesBo.setReasons(reasons.stream().filter(r -> r.getConsultationID().equals(oes.getConsultationID())).map(ReasonBo::new).collect(Collectors.toList()));
            oesBo.setProcedures(procedures.stream().filter(p -> p.getConsultationID().equals(oes.getConsultationID())).map(ProcedureBo::new).collect(Collectors.toList()));
            result.add(oesBo);
        }
        LOG.debug(OUTPUT, result);
        return result;
    }
}
