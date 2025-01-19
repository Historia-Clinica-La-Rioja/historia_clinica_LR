package ar.lamansys.refcounterref.application.getreceivedreferences;

import ar.lamansys.refcounterref.application.port.ReferenceReportStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.domain.report.ReferenceReportBo;
import ar.lamansys.refcounterref.domain.report.ReferenceReportFilterBo;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GetReceivedReferences {

	private final static List<Short> VALID_REGULATION_STATES = List.of(EReferenceRegulationState.AUDITED.getId(), EReferenceRegulationState.DONT_REQUIRES_AUDIT.getId());

	private final ReferenceReportStorage referenceReportStorage;
	
	public Page<ReferenceReportBo> run(ReferenceReportFilterBo filter, Pageable pageable) {
		if (filter.getRegulationStateId() != null && !VALID_REGULATION_STATES.contains(filter.getRegulationStateId()))
			return new PageImpl<>(Collections.emptyList(), pageable, 0);
		filter.setReceived(true);
		return referenceReportStorage.fetchReferencesReport(filter, pageable);
	}

}
