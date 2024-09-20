package net.pladema.reports.application.generators;

import static ar.lamansys.sgx.shared.files.StreamsUtils.streamException;

import net.pladema.reports.domain.InstitutionReportType;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.StreamsUtils;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.reports.application.ReportInstitutionQueryBo;
import net.pladema.reports.application.fetchnominalappointmentdetail.FetchNominalAppointmentDetail;
import net.pladema.reports.domain.ReportSearchFilterBo;

@Slf4j
@AllArgsConstructor
@Service
public class GenerateAppointmentNominalDetailExcelReport implements InstitutionExcelReportGenerator {
	private final FetchNominalAppointmentDetail fetchNominalAppointmentDetail;

	@Override
	public StoredFileBo run(
			ReportInstitutionQueryBo institutionMonthlyReportParams
	) {
		IWorkbook wb = generateReport(institutionMonthlyReportParams, "DNT");
		// armo la respuesta con el workbook obtenido
		String filename = InstitutionReportType.AppointmentNominalDetail.getDescription() + "." + wb.getExtension();
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

		return fetchNominalAppointmentDetail.run(
				title,
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
