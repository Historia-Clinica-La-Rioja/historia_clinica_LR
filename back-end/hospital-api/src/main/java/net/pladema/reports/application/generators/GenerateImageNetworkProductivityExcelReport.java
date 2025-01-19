package net.pladema.reports.application.generators;

import ar.lamansys.sgx.shared.files.StreamsUtils;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.reports.imageNetworkProductivity.domain.ImageNetworkProductivityFilterBo;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.reports.application.ReportInstitutionQueryBo;
import net.pladema.reports.imageNetworkProductivity.application.GenerateImageNetworkProductivitySheet;

import static ar.lamansys.sgx.shared.files.StreamsUtils.streamException;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenerateImageNetworkProductivityExcelReport implements InstitutionExcelReportGenerator {

	private final GenerateImageNetworkProductivitySheet generateImageNetworkProductivitySheet;

	@Override
	public StoredFileBo run(ReportInstitutionQueryBo institutionMonthlyReportParams) {
		ImageNetworkProductivityFilterBo filter = toFilter(institutionMonthlyReportParams);
		IWorkbook result = generateImageNetworkProductivitySheet.run(filter);

		return new StoredFileBo(
				buildReport(result),
				result.getContentType(),
				generateFileName(result.getExtension())
		);
	}

	private ImageNetworkProductivityFilterBo toFilter(
		ReportInstitutionQueryBo reportParams
	) {
		ImageNetworkProductivityFilterBo filter =
			new ImageNetworkProductivityFilterBo(
				reportParams.institutionId,
				reportParams.startDate,
				reportParams.endDate,
				asShort(reportParams.clinicalSpecialtyId),
				reportParams.doctorId
			);
		return filter;
	}

	private Short asShort(Integer clinicalSpecialtyId) {
		return clinicalSpecialtyId != null ? clinicalSpecialtyId.shortValue() : null;
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

	private String generateFileName(String extension) {
		return "Detalle nominal de prestaciones imágenes diagnósticas." + extension;
	}
}
