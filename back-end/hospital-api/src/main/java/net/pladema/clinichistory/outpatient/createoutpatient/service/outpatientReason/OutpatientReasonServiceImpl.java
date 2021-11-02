package net.pladema.clinichistory.outpatient.createoutpatient.service.outpatientReason;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationReasonsRepository;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientConsultationReasons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OutpatientReasonServiceImpl implements OutpatientReasonService {

    private static final Logger LOG = LoggerFactory.getLogger(OutpatientReasonServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final ReasonPort reasonPort;

    private final OutpatientConsultationReasonsRepository outpatientConsultationReasonsRepository;

    public OutpatientReasonServiceImpl(ReasonPort reasonPort,
                                       OutpatientConsultationReasonsRepository outpatientConsultationReasonsRepository) {
        this.reasonPort = reasonPort;
        this.outpatientConsultationReasonsRepository = outpatientConsultationReasonsRepository;
    }

    @Override
    public List<ReasonBo> addReasons(Integer outpatientId, List<ReasonBo> reasons) {
        LOG.debug("Input parameters outpatientId {}, reasons {}", outpatientId, reasons);
        reasons.stream()
                .map(reasonPort::saveReason)
                .forEach(r -> saveOutPatientReason(r, outpatientId));
        LOG.debug(OUTPUT, reasons);
        return reasons;
    }

    private OutpatientConsultationReasons saveOutPatientReason(ReasonBo reasonBo, Integer outpatientId) {
        LOG.debug("Input parameters reasonBo {}, outpatientId {}", reasonBo, outpatientId);
        Objects.requireNonNull(reasonBo);
        OutpatientConsultationReasons result =
                outpatientConsultationReasonsRepository.save(new OutpatientConsultationReasons(outpatientId, reasonBo.getSctid()));
        LOG.debug(OUTPUT, result);
        return result;
    }


}
