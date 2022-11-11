package net.pladema.reports.service.impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.medicalconsultation.appointment.service.DocumentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.DocumentAppointmentBo;
import net.pladema.reports.controller.dto.AnnexIIDto;
import net.pladema.reports.repository.AnnexReportRepository;
import net.pladema.reports.repository.entity.AnnexIIOdontologyDataVo;
import net.pladema.reports.repository.entity.AnnexIIOdontologyVo;
import net.pladema.reports.repository.entity.AnnexIIReportDataVo;
import net.pladema.reports.service.AnnexReportService;
import net.pladema.reports.service.domain.AnnexIIBo;

@Service
public class AnnexReportServiceImpl implements AnnexReportService {

    private final Logger LOG = LoggerFactory.getLogger(AnnexReportServiceImpl.class);
    public static final String OUTPUT = "Output -> {}";
    public static final String APPOINTMENT_NOT_FOUND = "appointment.not.found";
    public static final String CONSULTATION_NOT_FOUND = "consultation.not.found";

    private final AnnexReportRepository annexReportRepository;
	private final DocumentAppointmentService documentAppointmentService;
	private final DocumentService documentService;

    public AnnexReportServiceImpl(AnnexReportRepository annexReportRepository, DocumentAppointmentService documentAppointmentService, DocumentService documentService){
        this.annexReportRepository = annexReportRepository;
		this.documentAppointmentService = documentAppointmentService;
		this.documentService = documentService;
	}

    @Override
    public AnnexIIBo getAppointmentData(Integer appointmentId) {
        LOG.debug("Input parameter -> appointmentId {}", appointmentId);
        AnnexIIBo result = annexReportRepository.getAppointmentAnnexInfo(appointmentId).map(AnnexIIBo::new)
                .orElseThrow(() ->new NotFoundException("bad-appointment-id", APPOINTMENT_NOT_FOUND));

		Optional<DocumentAppointmentBo> documentAppointmentOpt = this.documentAppointmentService.getDocumentAppointmentForAppointment(appointmentId);
		if(documentAppointmentOpt.isPresent()){

			DocumentAppointmentBo documentAppointment = documentAppointmentOpt.get();
			Long documentId = documentAppointment.getDocumentId();

			Short sourceType = this.documentService.getSourceType(documentId);
			Short documentSourceType = sourceType == SourceType.IMMUNIZATION ? SourceType.OUTPATIENT : sourceType;

			switch (documentSourceType){

				case SourceType.OUTPATIENT: {
					var outpatientConsultationData = annexReportRepository.getConsultationAnnexInfo(documentId);
					if (outpatientConsultationData.isPresent()) {
						var outpatientconsultationData = outpatientConsultationData.get();
						result.setSpecialty(outpatientconsultationData.getSpecialty());
						result.setProblems(outpatientconsultationData.getProblems());
						result.setHasProcedures(outpatientconsultationData.getHasProcedures());
						result.setExistsConsultation(outpatientconsultationData.getExistsConsultation());
						LOG.debug("Output -> {}", result);
						return result;
					}
				}
				break;

				case SourceType.ODONTOLOGY: {
					var odontologyConsultationGeneralData = annexReportRepository
							.getOdontologyConsultationAnnexSpecialityAndHasProcedures(documentId);
					if(odontologyConsultationGeneralData.isPresent()){
						var completeResult = completeAnnexIIOdontologyBo(result, odontologyConsultationGeneralData);
						completeResult = completeAnnexIIOdontologyBo(completeResult, annexReportRepository.getOdontologyConsultationAnnexDataInfo(documentId));
						completeResult = completeAnnexIIOdontologyBo(completeResult, annexReportRepository.getOdontologyConsultationAnnexOtherDataInfo(documentId));
						LOG.debug("Output -> {}", completeResult);
						return completeResult;
					}
				}
				break;

				case SourceType.NURSING: {
					var nursingConsultationGeneralData = annexReportRepository.getNursingConsultationAnnexDataInfo(documentId);
					if(nursingConsultationGeneralData.isPresent()){
						var completeResult = completeAnnexIINursingBo(result, nursingConsultationGeneralData);
						LOG.debug("Output -> {}", completeResult);
						return completeResult;
					}
				}
				break;
			}
		}

		return result;
	}

    @Override
    public AnnexIIBo getConsultationData(Long documentId) {

		Optional<DocumentAppointmentBo> documentAppointmentOpt = this.documentAppointmentService.getDocumentAppointmentForDocument(documentId);
		if(documentAppointmentOpt.isPresent()){
			return this.getAppointmentData(documentAppointmentOpt.get().getAppointmentId());
		}

        LOG.debug("Input parameter -> documentId {}", documentId);
		AnnexIIBo result;

		Short sourceType = this.documentService.getSourceType(documentId);
		Short documentSourceType = sourceType == SourceType.IMMUNIZATION ? SourceType.OUTPATIENT : sourceType;

		switch (documentSourceType){

			case SourceType.OUTPATIENT: {
				var outpatientResultOpt = annexReportRepository.getConsultationAnnexInfo(documentId);
				if(outpatientResultOpt.isPresent()) {
					result = new AnnexIIBo(outpatientResultOpt.get());
					LOG.debug("Output -> {}", result);
					return result;
				}
			}
			break;

			case SourceType.ODONTOLOGY: {
				var odontologyResultOpt = annexReportRepository.getOdontologyConsultationAnnexGeneralInfo(documentId);
				if(odontologyResultOpt.isPresent()) {
					result = new AnnexIIBo(odontologyResultOpt.get());
					Optional<AnnexIIOdontologyVo> consultationSpecialityandHasProcedures = annexReportRepository
							.getOdontologyConsultationAnnexSpecialityAndHasProcedures(documentId);
					var completeResult = completeAnnexIIOdontologyBo(result, consultationSpecialityandHasProcedures);
					completeResult = completeAnnexIIOdontologyBo(completeResult, annexReportRepository.getOdontologyConsultationAnnexDataInfo(documentId));
					completeResult = completeAnnexIIOdontologyBo(completeResult, annexReportRepository.getOdontologyConsultationAnnexOtherDataInfo(documentId));
					LOG.debug("Output -> {}", completeResult);
					return completeResult;
				}
			}
			break;

			case SourceType.NURSING: {
				var nursingResultOpt = annexReportRepository.getNursingConsultationAnnexGeneralInfo(documentId);
				if(nursingResultOpt.isPresent()) {
					result = new AnnexIIBo(nursingResultOpt.get());
					Optional<AnnexIIReportDataVo> nursingData = annexReportRepository.getNursingConsultationAnnexDataInfo(documentId);
					var completeResult = completeAnnexIINursingBo(result, nursingData);
					LOG.debug("Output -> {}", completeResult);
					return completeResult;
				}
			}
			break;
		}

		throw new NotFoundException("bad-consultation-id", CONSULTATION_NOT_FOUND);
	}

	private AnnexIIBo completeAnnexIINursingBo(AnnexIIBo result, Optional<AnnexIIReportDataVo> nursingDataOpt) {
		if(nursingDataOpt.isPresent()){
			result.setExistsConsultation(true);
			var nursingData = nursingDataOpt.get();
			result.setSpecialty(nursingData.getSpeciality());
			result.setProblems(nursingData.getDiagnostics());
			result.setHasProcedures(nursingData.getHasProcedures());
		}
		LOG.debug("Output -> {}", result);
		return result;
	}

	private AnnexIIBo completeAnnexIIOdontologyBo(AnnexIIBo result, Optional<AnnexIIOdontologyVo> odontologyDataOpt) {
		if(odontologyDataOpt.isPresent()){
			result.setExistsConsultation(true);
			var odontologyData = odontologyDataOpt.get();
			result.setSpecialty(odontologyData.getSpeciality());
			result.setHasProcedures(odontologyData.getHasProcedures());
		}
		LOG.debug("Output -> {}", result);
		return result;
	}

	private AnnexIIBo completeAnnexIIOdontologyBo(AnnexIIBo result, List<AnnexIIOdontologyDataVo> listData){
		if(!listData.isEmpty()){
			if(result.getProblems() == null)
				result.setProblems("");
			for(AnnexIIOdontologyDataVo i : listData){
				String annexDiagnostic = i.getDiagnostic();
				if(i.getCie10Code() != null)
					annexDiagnostic += "(" + i.getCie10Code() + ")| ";
				else
					annexDiagnostic += "| ";
				result.setProblems(result.getProblems() + annexDiagnostic);
			}
		}
		return result;
	}

	@Override
    public Map<String, Object> createAppointmentContext(AnnexIIDto reportDataDto){
        LOG.debug("Input parameter -> reportDataDto {}", reportDataDto);
        Map<String, Object> ctx = loadBasicContext(reportDataDto);
        ctx.put("appointmentState", reportDataDto.getAppointmentState());
        ctx.put("attentionDate",
				reportDataDto.getAttentionDate() != null ? reportDataDto.getAttentionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
					: null);
        ctx.put("medicalCoverage", reportDataDto.getMedicalCoverage());
        ctx.put("affiliateNumber", reportDataDto.getAffiliateNumber());
		ctx.put("existsConsultation", reportDataDto.getExistsConsultation());
		ctx.put("hasProcedures", reportDataDto.getHasProcedures());
		ctx.put("specialty", reportDataDto.getSpecialty());
		ctx.put("problems", reportDataDto.getProblems());
		ctx.put("rnos", reportDataDto.getRnos());
        return ctx;
    }

    @Override
    public Map<String, Object> createConsultationContext(AnnexIIDto reportDataDto){
        Map<String, Object> ctx = this.createAppointmentContext(reportDataDto);
        ctx.put("consultationDate",
				reportDataDto.getConsultationDate() != null ? reportDataDto.getConsultationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
						: null);
        return ctx;
    }

    private Map<String, Object> loadBasicContext(AnnexIIDto reportDataDto) {
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("reportDate", reportDataDto.getReportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        ctx.put("establishment", reportDataDto.getEstablishment());
        ctx.put("completePatientName", reportDataDto.getCompletePatientName());
        ctx.put("documentType", reportDataDto.getDocumentType());
        ctx.put("documentNumber", reportDataDto.getDocumentNumber());
        ctx.put("patientGender", reportDataDto.getPatientGender());
        ctx.put("patientAge", reportDataDto.getPatientAge());
        ctx.put("sisaCode", reportDataDto.getSisaCode());
        return ctx;
    }

    @Override
    public String createConsultationFileName(Long documentId, ZonedDateTime consultedDate){
        LOG.debug("Input parameters -> documentId {}, consultedDate {}", documentId, consultedDate);
        String formattedDate = consultedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String outputFileName = String.format("%s. AnexoII %s.pdf", documentId, formattedDate);
        LOG.debug(OUTPUT, outputFileName);
        return outputFileName;
    }
}
