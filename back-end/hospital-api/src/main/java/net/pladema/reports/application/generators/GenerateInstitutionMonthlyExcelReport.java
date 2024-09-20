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
import net.pladema.reports.application.fetchnominalconsultationdetail.FetchNominalConsultationDetail;
import net.pladema.reports.repository.QueryFactory;

@Slf4j
@AllArgsConstructor
@Service
public class GenerateInstitutionMonthlyExcelReport implements InstitutionExcelReportGenerator {

	private final QueryFactory queryFactory;

	private final FetchNominalConsultationDetail fetchNominalConsultationDetail;

	@Override
	public StoredFileBo run(
			ReportInstitutionQueryBo institutionMonthlyReportParams
	) {
		IWorkbook wb = generateReport(institutionMonthlyReportParams, "DNCE-Hoja 2");
		// armo la respuesta con el workbook obtenido
		String filename = InstitutionReportType.Monthly.getDescription() + "." + wb.getExtension();

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
		String[] headers = new String[]{"Provincia", "Municipio", "Cod_Estable", "Establecimiento", "Tipo de unidad jerárquica", "Unidad jerárquica", "Apellidos paciente", "Nombres paciente", "Nombre autopercibido", "Tipo documento",
				"Nro documento", "Fecha de nacimiento", "Género autopercibido", "Domicilio", "Teléfono", "Mail", "Obra social/Prepaga", "Nro de afiliado",
				"Fecha de atención", "Especialidad", "Profesional", "Motivo de consulta", "Problemas de Salud / Diagnóstico", "Procedimientos", "Peso", "Talla", "Tensión sistólica",
				"Tensión diastólica", "Riesgo cardiovascular", "Hemoglobina glicosilada", "Glucemia", "Perímetro cefálico", "C-P-O", "c-e-o"};

		return fetchNominalConsultationDetail.run(title, headers, this.queryFactory.query(
				params.institutionId,
				params.startDate,
				params.endDate,
				params.clinicalSpecialtyId,
				params.doctorId,
				params.hierarchicalUnitTypeId,
				params.hierarchicalUnitId,
				params.includeHierarchicalUnitDescendants
		), params.institutionId);
	}

}
