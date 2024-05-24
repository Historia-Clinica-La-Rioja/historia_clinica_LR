package net.pladema.clinichistory.outpatient.createoutpatient.controller.service;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.SnomedMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.ReasonExternalService;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.ReasonDto;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.outpatient.createoutpatient.service.outpatientReason.ReasonPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReasonPortImpl implements ReasonPort {

    private final ReasonExternalService reasonExternalService;
	private final SnomedMapper snomedMapper;

    @Override
    public ReasonBo saveReason(ReasonBo reasonBo) {
        ReasonDto reasonDto = new ReasonDto(new SnomedDto(reasonBo.getSnomed().getSctid(), reasonBo.getPt()));
        reasonExternalService.addReasons(List.of(reasonDto));
        return reasonBo;

    }

	@Override
	public Optional<ReasonBo> getByReasonId(String reasonId) {
		return reasonExternalService.getByReasonId(reasonId).map(dto -> snomedMapper.toReasonBo(dto.getSnomed()));
	}

	@Override
	public List<ReasonBo> saveReasons(List<ReasonBo> reasonsBo) {
		List<ReasonDto> reasonsDto = reasonsBo.stream()
				.map(r -> new ReasonDto(new SnomedDto(r.getSnomed().getSctid(), r.getPt())))
				.collect(Collectors.toList());
		reasonExternalService.addReasons(reasonsDto);
		return reasonsBo;
	}
}
