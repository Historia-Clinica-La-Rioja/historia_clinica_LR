package net.pladema.reports.application.generators;

import ar.lamansys.sgx.shared.files.StreamsUtils;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.reports.application.ReportInstitutionQueryBo;
import net.pladema.reports.application.fetchnominalemergencycarepisodedetail.FetchNominalECEpisodeDetail;

import net.pladema.reports.domain.InstitutionReportType;
import net.pladema.reports.domain.ReportSearchFilterBo;

import org.springframework.stereotype.Service;

import static ar.lamansys.sgx.shared.files.StreamsUtils.streamException;

@Slf4j
@AllArgsConstructor
@Service
public class GenerateEmergencyCareNominalDetailExcelReport implements InstitutionExcelReportGenerator {

	private final FetchNominalECEpisodeDetail fetchNominalECEpisodeDetail;

	@Override
	public StoredFileBo run (
			ReportInstitutionQueryBo institutionMonthlyReportParams
	) {
		IWorkbook wb = generateReport(institutionMonthlyReportParams, "DNG");
		// armo la respuesta con el workbook obtenido
		String filename = InstitutionReportType.EmergencyCareNominalDetail.getDescription() + "." + wb.getExtension();
		return new StoredFileBo(
				buildReport(wb),
				wb.getContentType(),
				filename
		);
	}

	protected static FileContentBo buildReport(IWorkbook workbook) {
		return StreamsUtils.writeToContent((out) -> {
			try {
				workbook.write(out);
			} catch (Exception e) {
				throw streamException(e);
			}
		});
	}

	private IWorkbook generateReport(
			ReportInstitutionQueryBo params,
			String title
	) {

		return fetchNominalECEpisodeDetail.run(title,
				new ReportSearchFilterBo(
						params.startDate,
						params.endDate,
						params.institutionId,
						params.clinicalSpecialtyId,
						params.doctorId,
						params.hierarchicalUnitTypeId,
						params.hierarchicalUnitId,
						params.appointmentStateId,
						params.includeHierarchicalUnitDescendants
				)
		);
	}

}
