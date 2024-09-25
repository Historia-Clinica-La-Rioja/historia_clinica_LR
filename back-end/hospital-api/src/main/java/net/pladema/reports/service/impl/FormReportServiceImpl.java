package net.pladema.reports.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.reports.controller.dto.FormVDto;
import net.pladema.reports.domain.FormVParametersBo;
import net.pladema.reports.service.domain.FormVBo;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.medicalconsultation.appointment.service.DocumentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.DocumentAppointmentBo;
import net.pladema.reports.repository.FormReportRepository;
import net.pladema.reports.repository.entity.FormVReportDataVo;
import net.pladema.reports.service.FormReportService;

@Slf4j
@RequiredArgsConstructor
@Service
public class FormReportServiceImpl implements FormReportService {

    public static final String OUTPUT = "Output -> {}";
    public static final String APPOINTMENT_NOT_FOUND = "appointment.not.found";
    public static final String CONSULTATION_NOT_FOUND = "consultation.not.found";

    private final FormReportRepository formReportRepository;
	private final DocumentAppointmentService documentAppointmentService;
	private final DocumentService documentService;

    @Override
    public FormVBo getAppointmentData(FormVParametersBo parametersBo) {
		log.debug("Input parameter -> FormVParametersBo {}", parametersBo);
		Integer appointmentId = parametersBo.getAppointmentId();

		FormVBo result = Optional.ofNullable(appointmentId)
				.flatMap(formReportRepository::getAppointmentFormVInfo)
				.map(FormVBo::new)
				.orElseThrow(() ->new NotFoundException("bad-appointment-id", APPOINTMENT_NOT_FOUND));

		Optional<DocumentAppointmentBo> documentAppointmentOpt = Optional.ofNullable(parametersBo.getDocumentId())
				.map(documentId -> new DocumentAppointmentBo(documentId, appointmentId))
				.or(() -> documentAppointmentService.getDocumentAppointmentForAppointment(appointmentId));

		if (documentAppointmentOpt.isPresent()) {

			DocumentAppointmentBo documentAppointment = documentAppointmentOpt.get();
			Long documentId = documentAppointment.getDocumentId();

			Short sourceType = this.documentService.getSourceType(documentId);
			Short documentSourceType = sourceType == SourceType.IMMUNIZATION ? SourceType.OUTPATIENT : sourceType;

			switch (documentSourceType){

				case SourceType.OUTPATIENT: {
					var outpatientResultOpt = formReportRepository.getConsultationFormVInfo(documentId);
					if(outpatientResultOpt.isPresent()) {
						var outpatientconsultationData = outpatientResultOpt.get();
						result.setProblems(outpatientconsultationData.getProblems());
						result.setCie10Codes(outpatientconsultationData.getCie10Codes());
						log.debug("Output -> {}", result);
						return result;
					}
				}
				break;

				case SourceType.ODONTOLOGY: {
					List<FormVReportDataVo> odontologyListData = formReportRepository.getOdontologyConsultationFormVDataInfo(documentId);
					odontologyListData.addAll(formReportRepository.getOdontologyConsultationFormVOtherDataInfo(documentId));
					if(!odontologyListData.isEmpty()) {
						var completeResult = completeFormVBo(result, odontologyListData);
						log.debug("Output -> {}", completeResult);
						return completeResult;
					}
				}
				break;

				case SourceType.NURSING: {
					Optional<FormVReportDataVo> nursingListData = formReportRepository.getNursingConsultationFormVDataInfo(documentId);
					if(nursingListData.isPresent()){
						var completeResult = completeFormVBo(result, nursingListData);
						log.debug("Output -> {}", completeResult);
						return completeResult;
					}
				}
				break;
			}
		}

		return result;
	}

	@Override
	public FormVBo getConsultationData(FormVParametersBo parametersBo) {
		log.debug("Input parameter -> FormVParametersBo {}", parametersBo);
		Long documentId = parametersBo.getDocumentId();

		Optional<DocumentAppointmentBo> documentAppointmentOpt = Optional.ofNullable(documentId)
				.flatMap(documentAppointmentService::getDocumentAppointmentForDocument);

		if (documentAppointmentOpt.isPresent()) {
			parametersBo.setAppointmentId(documentAppointmentOpt.get().getAppointmentId());
			return this.getAppointmentData(parametersBo);
		}

		log.debug("Input parameter -> documentId {}", documentId);
		FormVBo result;

		Short sourceType = this.documentService.getSourceType(documentId);
		Short documentSourceType = sourceType == SourceType.IMMUNIZATION ? SourceType.OUTPATIENT : sourceType;

		switch (documentSourceType){

			case SourceType.OUTPATIENT: {
				var outpatientResultOpt = formReportRepository.getConsultationFormVInfo(documentId);
				if(outpatientResultOpt.isPresent()) {
					result = new FormVBo(outpatientResultOpt.get());
					log.debug("Output -> {}", result);
					return result;
				}
			}
			break;

			case SourceType.ODONTOLOGY: {
				var odontologyResultOpt = formReportRepository.getOdontologyConsultationFormVGeneralInfo(documentId);
				if(odontologyResultOpt.isPresent()) {
					result = new FormVBo(odontologyResultOpt.get());
					List<FormVReportDataVo> odontologyListData = formReportRepository.getOdontologyConsultationFormVDataInfo(documentId);
					odontologyListData.addAll(formReportRepository.getOdontologyConsultationFormVOtherDataInfo(documentId));
					var completeResult = completeFormVBo(result, odontologyListData);
					log.debug("Output -> {}", completeResult);
					return completeResult;
				}
			}
			break;

			case SourceType.NURSING: {
				var nursingResultOpt = formReportRepository.getNursingConsultationFormVGeneralInfo(documentId);
				if(nursingResultOpt.isPresent()) {
					result = new FormVBo(nursingResultOpt.get());
					Optional<FormVReportDataVo> nursingListData = formReportRepository.getNursingConsultationFormVDataInfo(documentId);
					var completeResult = completeFormVBo(result, nursingListData);
					log.debug("Output -> {}", completeResult);
					return completeResult;
				}
			}
			break;
		}

		throw new NotFoundException("bad-consultation-id", CONSULTATION_NOT_FOUND);
	}

	private FormVBo completeFormVBo(FormVBo result, List<FormVReportDataVo> listData) {
		if(!listData.isEmpty()){
			for(FormVReportDataVo reportData : listData){
				result = this.setProblems(result, reportData.getDiagnostics());
				result = this.setCie10Codes(result, reportData.getCie10Codes());
			}
		}
		return result;
	}

	private FormVBo completeFormVBo(FormVBo result, Optional<FormVReportDataVo> dataOpt) {
		if(dataOpt.isPresent()){
			var data = dataOpt.get();
			result = this.setProblems(result, data.getDiagnostics());
			result = this.setCie10Codes(result, data.getCie10Codes());
		}
		return result;
	}

	private FormVBo setProblems(FormVBo report, String problems){
		if(report.getProblems() == null)
			report.setProblems(problems);
		else
			report.setProblems(report.getProblems() + "| " + problems);
		return report;
	}

	private FormVBo setCie10Codes(FormVBo report, String cie10Codes){
		if(cie10Codes == null)
			cie10Codes = "-";
		if(report.getCie10Codes() == null)
			report.setCie10Codes(cie10Codes);
		else
			report.setCie10Codes(report.getCie10Codes() + "| " + cie10Codes);
		return report;
	}

	@Override
    public Map<String, Object> createAppointmentContext(FormVDto reportDataDto){
        log.debug("Input parameter -> reportDataDto {}", reportDataDto);
        Map<String, Object> ctx = loadBasicContext(reportDataDto);
        ctx.put("medicalCoverage", reportDataDto.getMedicalCoverage());
        ctx.put("affiliateNumber", reportDataDto.getAffiliateNumber());
		ctx.put("problems", reportDataDto.getProblems());
		ctx.put("cie10Codes", reportDataDto.getCie10Codes());
		ctx.put("consultationDate", reportDataDto.getConsultationDate() != null ? formatConsultationDate(reportDataDto.getConsultationDate()) : null);
        return ctx;
    }

	private String formatConsultationDate(LocalDateTime consultationDate) {
		return consultationDate.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

    @Override
    public Map<String, Object> createConsultationContext(FormVDto reportDataDto){
		return createAppointmentContext(reportDataDto);
    }

    private Map<String, Object> loadBasicContext(FormVDto reportDataDto){
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("establishment", reportDataDto.getEstablishment());
        ctx.put("completePatientName", reportDataDto.getCompletePatientName());
		ctx.put("formalPatientName", reportDataDto.getFormalPatientName());
        ctx.put("address", reportDataDto.getAddress());
        ctx.put("reportDate", reportDataDto.getReportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        ctx.put("patientGender", reportDataDto.getPatientGender());
        ctx.put("patientAge", reportDataDto.getPatientAge());
        ctx.put("documentType", reportDataDto.getDocumentType());
        ctx.put("documentNumber", reportDataDto.getDocumentNumber());
        ctx.put("code", reportDataDto.getSisaCode());
        return ctx;
    }

    @Override
    public String createConsultationFileName(Long id, ZonedDateTime consultedDate){
        log.debug("Input parameters -> id {}, consultedDate {}", id, consultedDate);
        String formattedDate = consultedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String outputFileName = String.format("%s-FormV %s.pdf", id, formattedDate);
        log.debug(OUTPUT, outputFileName);
        return outputFileName;
    }
}
