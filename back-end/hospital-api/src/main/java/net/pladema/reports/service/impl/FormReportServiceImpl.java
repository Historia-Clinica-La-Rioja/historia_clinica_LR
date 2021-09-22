package net.pladema.reports.service.impl;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.reports.controller.dto.FormVDto;
import net.pladema.reports.repository.FormReportRepository;
import net.pladema.reports.service.FormReportService;
import net.pladema.reports.service.domain.FormVBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class FormReportServiceImpl implements FormReportService {

    private final Logger LOG = LoggerFactory.getLogger(FormReportServiceImpl.class);
    public static final String OUTPUT = "Output -> {}";
    public static final String APPOINTMENT_NOT_FOUND = "appointment.not.found";
    public static final String CONSULTATION_NOT_FOUND = "consultation.not.found";

    private final FormReportRepository formReportRepository;

    public FormReportServiceImpl(FormReportRepository formReportRepository){
        this.formReportRepository = formReportRepository;
    }

    @Override
    public FormVBo getAppointmentData(Integer appointmentId) {
        LOG.debug("Input parameter -> appointmentId {}", appointmentId);
        FormVBo result = formReportRepository.getAppointmentFormVInfo(appointmentId).map(FormVBo::new)
                .orElseThrow(() ->new NotFoundException("bad-appointment-id", APPOINTMENT_NOT_FOUND));
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public FormVBo getConsultationData(Long documentId) {
        LOG.debug("Input parameter -> documentId {}", documentId);
        FormVBo result = formReportRepository.getConsultationFormVInfo(documentId).map(FormVBo::new)
                .orElseThrow(() ->new NotFoundException("bad-outpatient-id", CONSULTATION_NOT_FOUND));
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Map<String, Object> createAppointmentContext(FormVDto reportDataDto){
        LOG.debug("Input parameter -> reportDataDto {}", reportDataDto);
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("establishment", reportDataDto.getEstablishment());
        ctx.put("completePatientName", reportDataDto.getCompletePatientName());
        ctx.put("address", reportDataDto.getAddress());
        ctx.put("reportDate", reportDataDto.getReportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        ctx.put("patientGender", reportDataDto.getPatientGender());
        ctx.put("patientAge", reportDataDto.getPatientAge());
        ctx.put("documentType", reportDataDto.getDocumentType());
        ctx.put("documentNumber", reportDataDto.getDocumentNumber());
        ctx.put("medicalCoverage", reportDataDto.getMedicalCoverage());
        ctx.put("affiliateNumber", reportDataDto.getAffiliateNumber());
        ctx.put("sisaCode", reportDataDto.getSisaCode());
        return ctx;
    }

    @Override
    public Map<String, Object> createConsultationContext(FormVDto reportDataDto){
        LOG.debug("Input parameter -> reportDataDto {}", reportDataDto);
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("establishment", reportDataDto.getEstablishment());
        ctx.put("completePatientName", reportDataDto.getCompletePatientName());
        ctx.put("address", reportDataDto.getAddress());
        ctx.put("reportDate", reportDataDto.getReportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        ctx.put("patientGender", reportDataDto.getPatientGender());
        ctx.put("patientAge", reportDataDto.getPatientAge());
        ctx.put("documentType", reportDataDto.getDocumentType());
        ctx.put("documentNumber", reportDataDto.getDocumentNumber());
        ctx.put("consultationDate", reportDataDto.getConsultationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        ctx.put("problems", reportDataDto.getProblems());
        ctx.put("sisaCode", reportDataDto.getSisaCode());
        ctx.put("cie10Codes", reportDataDto.getCie10Codes());
        return ctx;
    }

    @Override
    public String createConsultationFileName(Long id, ZonedDateTime consultedDate){
        LOG.debug("Input parameters -> id {}, consultedDate {}", id, consultedDate);
        String formattedDate = consultedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String outputFileName = String.format("%s. FormV %s.pdf", id, formattedDate);
        LOG.debug(OUTPUT, outputFileName);
        return outputFileName;
    }
}
