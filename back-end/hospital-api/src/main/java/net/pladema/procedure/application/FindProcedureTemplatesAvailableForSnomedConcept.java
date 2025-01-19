package net.pladema.procedure.application;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import lombok.RequiredArgsConstructor;
import net.pladema.procedure.application.port.ProcedureTemplateStore;
import net.pladema.procedure.domain.ProcedureTemplateVo;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FindProcedureTemplatesAvailableForSnomedConcept {
	private final ProcedureTemplateStore procedureTemplateStore;
	private final SnomedService snomedService;
	public List<ProcedureTemplateVo> run(String sctid, String pt) {
		return snomedService
		.getSnomedId(new SnomedBo(sctid, pt))
		.map(snomedId -> procedureTemplateStore.findAvailableForSnomedId(snomedId))
		.orElse(Collections.emptyList());
	}
}