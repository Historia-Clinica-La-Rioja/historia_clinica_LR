package ar.lamansys.sgh.clinichistory.application.reason;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ReasonsRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Reason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ReasonServiceImpl implements ReasonService {

    private static final Logger LOG = LoggerFactory.getLogger(ReasonServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final ReasonsRepository reasonRepository;

    public ReasonServiceImpl(ReasonsRepository reasonRepository) {
        this.reasonRepository = reasonRepository;
    }

    @Override
    public List<String> addReasons(List<ReasonBo> reasons) {
        LOG.debug("Input parameters -> reasons {}", reasons);
        List<String> result = reasons.stream()
                .map(this::saveReason)
                .map(ReasonBo::getSctid)
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }


    private ReasonBo saveReason(ReasonBo reasonBo) {
        LOG.debug("Input parameters reasonBo {}", reasonBo);
        Objects.requireNonNull(reasonBo);
        Reason reason = new Reason(reasonBo.getSctid(), reasonBo.getPt());
        reasonRepository.save(reason);
        LOG.debug(OUTPUT, reasonBo);
        return reasonBo;
    }
}
