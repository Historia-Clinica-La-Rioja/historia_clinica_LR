package net.pladema.clinichistory.outpatient.createoutpatient.service;

import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationReasonsRepository;
import net.pladema.clinichistory.outpatient.repository.ReasonsRepository;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientConsultationReasons;
import net.pladema.clinichistory.outpatient.repository.domain.Reason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReasonServiceImpl implements ReasonService{

    private static final Logger LOG = LoggerFactory.getLogger(ReasonServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final ReasonsRepository reasonRepository;

    private final OutpatientConsultationReasonsRepository outpatientConsultationReasonsRepository;

    public ReasonServiceImpl(ReasonsRepository reasonRepository,
                             OutpatientConsultationReasonsRepository outpatientConsultationReasonsRepository) {
        this.reasonRepository = reasonRepository;
        this.outpatientConsultationReasonsRepository = outpatientConsultationReasonsRepository;
    }

    @Override
    public List<ReasonBo> addReasons(Integer outpatientId, List<ReasonBo> reasons) {
        LOG.debug("Input parameters outpatientId {}, reasons {}", outpatientId, reasons);
        reasons.stream()
                .map(this::saveReason)
                .forEach(r -> saveOutPatientReason(r, outpatientId));
        LOG.debug(OUTPUT, reasons);
        return reasons;
    }

    @Override
    public List<String> addReasons(List<ReasonBo> reasons) {
        LOG.debug("Input parameters -> reasons {}", reasons);
        List<String> result = reasons.stream()
                .map(this::saveReason)
                .map(ReasonBo::getId)
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    private OutpatientConsultationReasons saveOutPatientReason(ReasonBo reasonBo, Integer outpatientId) {
        LOG.debug("Input parameters reasonBo {}, outpatientId {}", reasonBo, outpatientId);
        Objects.requireNonNull(reasonBo);
        OutpatientConsultationReasons result =
                outpatientConsultationReasonsRepository.save(new OutpatientConsultationReasons(outpatientId, reasonBo.getId()));
        LOG.debug(OUTPUT, result);
        return result;
    }

    private ReasonBo saveReason(ReasonBo reasonBo) {
        LOG.debug("Input parameters reasonBo {}", reasonBo);
        Objects.requireNonNull(reasonBo);
        Reason reason = new Reason(reasonBo.getId(), reasonBo.getPt());
        reasonRepository.save(reason);
        LOG.debug(OUTPUT, reasonBo);
        return reasonBo;
    }
}
