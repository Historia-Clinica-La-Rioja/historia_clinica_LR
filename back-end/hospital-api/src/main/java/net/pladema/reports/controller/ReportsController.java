package net.pladema.reports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.reports.repository.QueryFactory;
import net.pladema.reports.service.ExcelService;
import net.pladema.reports.service.domain.OutpatientSummaryReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.time.LocalDate;

@RestController
@RequestMapping("reports")
public class ReportsController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportsController.class);

    private final ExcelService excelService;

    private final OutpatientSummaryReport outpatientSummaryReport;

    private final QueryFactory queryFactory;

    private final LocalDateMapper localDateMapper;

    public ReportsController(ExcelService excelService, OutpatientSummaryReport outpatientSummaryReport,
                             QueryFactory queryFactory, LocalDateMapper localDateMapper){
        this.excelService = excelService;
        this.outpatientSummaryReport = outpatientSummaryReport;
        this.queryFactory = queryFactory;
        this.localDateMapper = localDateMapper;
    }

    @GetMapping(value = "/{institutionId}/monthly")
    public @ResponseBody
    void getMonthlyExcelReport(
            @PathVariable Integer institutionId,
            @RequestParam(value="fromDate", required = true) String fromDate,
            @RequestParam(value="toDate", required = true) String toDate,
            @RequestParam(value="clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
            @RequestParam(value="doctorId", required = false) Integer doctorId,
            HttpServletResponse response
    ) throws Exception {
        LOG.debug("Se creará el excel {}", institutionId);
        LOG.debug("Input parameters -> institutionId {}, fromDate {}, toDate {}", institutionId, fromDate, toDate);

        String title = "Informe mensual de consultorio externo - Hoja 2 (IMCE)";
        String[] headers = new String[]{"Provincia", "Municipio", "Cod_Estable", "Establecimiento", "Apellidos paciente", "Nombres paciente", "Tipo documento",
                "Nro documento", "Fecha de nacimiento", "Género autopercibido", "Domicilio", "Teléfono", "Mail", //"Obra social/ Prepaga", "Nro de afiliado",
                "Fecha de atención", "Especialidad", "Profesional", "Motivo de consulta"};

        LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
        LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

        // obtengo el workbook en base a la query pasada como parametro
        IWorkbook wb = this.excelService.buildExcelFromQuery(title, headers, this.queryFactory.query(institutionId, startDate, endDate, clinicalSpecialtyId, doctorId));

        // armo la respuesta con el workbook obtenido
        String filename = title + "." + wb.getExtension();
        response.addHeader("Content-disposition", "attachment;filename=" + filename);
        response.setContentType(wb.getContentType());

        OutputStream out = response.getOutputStream();
        wb.write(out);
        out.close();
        out.flush();
        response.flushBuffer();
    }

    @GetMapping(value = "/{institutionId}/summary")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
    public @ResponseBody void getOutpatientSummaryReport(
            @PathVariable Integer institutionId,
            @RequestParam(value="fromDate") String fromDate,
            @RequestParam(value="toDate") String toDate,
            @RequestParam(value="clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
            @RequestParam(value="doctorId", required = false) Integer doctorId,
            HttpServletResponse response) throws Exception {
        LOG.debug("Outpatient summary Report");
        LOG.debug("Input parameters -> institutionId {}, fromDate {}, toDate {}", institutionId, fromDate, toDate);

        LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
        LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

        IWorkbook workbook = outpatientSummaryReport.build(institutionId, startDate, endDate, doctorId, clinicalSpecialtyId);
        String title = "Resumen Mensual de Consultorios Externos - Hoja 2.1";
        String filename = title + "." + workbook.getExtension();
        response.addHeader("Content-disposition", "attachment;filename=" + filename);
        response.setContentType(workbook.getContentType());

        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.close();
        out.flush();
        response.flushBuffer();

    }
}
