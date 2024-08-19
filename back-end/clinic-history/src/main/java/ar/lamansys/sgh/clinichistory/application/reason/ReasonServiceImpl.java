package ar.lamansys.sgh.clinichistory.application.reason;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ReasonsRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.VReasonsRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.Reason;

@Service
public class ReasonServiceImpl implements ReasonService {

    private static final Logger LOG = LoggerFactory.getLogger(ReasonServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final ReasonsRepository reasonRepository;

	private final VReasonsRepository vReasonsRepository;

    public ReasonServiceImpl(ReasonsRepository reasonRepository, VReasonsRepository vReasonsRepository) {
        this.reasonRepository = reasonRepository;
		this.vReasonsRepository = vReasonsRepository;
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

	@Override
	public List<ReasonBo> fetchFromDocumentId(Long documentId) {
		return vReasonsRepository.fetchFromDocumentId(documentId)
				.stream()
				.map(vReason -> {
					ReasonBo result = new ReasonBo();
					result.setSnomed(new SnomedBo(vReason.getId(), vReason.getDescription()));
					return result;
				})
				.collect(Collectors.toList());
	}

	@Override
	public Optional<ReasonBo> getByReasonId(String reasonId) {
		Optional<Reason> reasonOpt = reasonRepository.findById(reasonId);
		if (reasonOpt.isPresent()){
			Reason r = reasonOpt.get();
			ReasonBo reasonBo = new ReasonBo();
			reasonBo.setSnomed(new SnomedBo(r.getId(), r.getDescription()));
			return Optional.of(reasonBo);
		}
		return Optional.empty();
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
