package net.pladema.reports.service.impl;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.reports.controller.dto.AnnexIIDto;
import net.pladema.reports.repository.AnnexReportRepository;
import net.pladema.reports.service.AnnexReportService;
import net.pladema.reports.service.domain.AnnexIIBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnnexReportServiceImpl implements AnnexReportService {

    private final Logger LOG = LoggerFactory.getLogger(AnnexReportServiceImpl.class);
    public static final String OUTPUT = "Output -> {}";
    public static final String APPOINTMENT_NOT_FOUND = "appointment.not.found";

    private final AnnexReportRepository annexReportRepository;

    public AnnexReportServiceImpl(AnnexReportRepository annexReportRepository){
        this.annexReportRepository = annexReportRepository;
    }

    @Override
    public AnnexIIBo execute(Integer appointmentId) {
        LOG.debug("Input parameters -> appointmentId {}", appointmentId);
        AnnexIIBo result = annexReportRepository.getAnexoInfo(appointmentId).map(AnnexIIBo::new)
                .orElseThrow(() ->new NotFoundException("bad-appointment-id", APPOINTMENT_NOT_FOUND));
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Map<String, Object> createContext(AnnexIIDto reportDataDto){
        LOG.debug("Input parameters -> reportDataDto {}", reportDataDto);
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("fechaReporte", reportDataDto.getFechaReporte().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        ctx.put("hospital", reportDataDto.getHospital());
        ctx.put("nombreCompletoPaciente", reportDataDto.getNombreCompletoPaciente());
        ctx.put("tipoDocumento", reportDataDto.getTipoDocumento());
        ctx.put("numeroDocumento", reportDataDto.getNumeroDocumento());
        ctx.put("sexoPaciente", reportDataDto.getSexoPaciente());
        ctx.put("edadPaciente", reportDataDto.getEdadPaciente());
        ctx.put("fechaAtencion", reportDataDto.getFechaAtencion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        LOG.debug(OUTPUT, ctx);
        return ctx;
    }

    @Override
    public String createOutputFileName(Integer appointmentId, ZonedDateTime consultedDate){
        LOG.debug("Input parameters -> appointmentId {}, consultedDate {}", appointmentId, consultedDate);
        String formattedDate = consultedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String outputFileName = String.format("%s. AnexoII %s.pdf", appointmentId, formattedDate);
        LOG.debug(OUTPUT, outputFileName);
        return outputFileName;
    }
}
