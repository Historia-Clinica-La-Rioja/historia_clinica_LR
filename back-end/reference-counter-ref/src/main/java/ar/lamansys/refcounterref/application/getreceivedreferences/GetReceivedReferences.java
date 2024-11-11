package ar.lamansys.refcounterref.application.getreceivedreferences;

import ar.lamansys.refcounterref.application.port.ReferenceReportStorage;
import ar.lamansys.refcounterref.domain.report.ReferenceReportBo;
import ar.lamansys.refcounterref.domain.report.ReferenceReportFilterBo;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetReceivedReferences {

	private final ReferenceReportStorage referenceReportStorage;
	
	public Page<ReferenceReportBo> run(ReferenceReportFilterBo filter, Pageable pageable) {
		filter.setReceived(true);
		return referenceReportStorage.fetchReferencesReport(filter, pageable);
	}

}
