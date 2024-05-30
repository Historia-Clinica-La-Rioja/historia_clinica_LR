package net.pladema.clinichistory.requests.transcribed.application.generatepdf;

import static ar.lamansys.sgx.shared.files.pdf.EPDFTemplate.FORM_REPORT;
import static ar.lamansys.sgx.shared.files.pdf.EPDFTemplate.RECIPE_ORDER_TABLE;

import java.util.HashMap;
import java.util.Map;

import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.clinichistory.requests.servicerequests.application.CreateDeliveryOrderBaseForm;
import net.pladema.clinichistory.requests.servicerequests.application.CreateDeliveryOrderFormContext;
import net.pladema.clinichistory.requests.transcribed.application.getbyid.GetTranscribedServiceRequest;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.files.pdf.GeneratedPdfResponseService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.GeneratedBlobBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.transcribed.domain.TranscribedServiceRequestBo;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import net.pladema.reports.service.domain.FormVBo;


@Slf4j
@RequiredArgsConstructor
@Service
public class CreateTranscribedServiceRequestPdf {

    private final GetTranscribedServiceRequest getTranscribedServiceRequest;
    private final PatientExternalService patientExternalService;
    private final GeneratedPdfResponseService generatedPdfResponseService;
    private final CreateDeliveryOrderBaseForm createDeliveryOrderBaseForm;
    private final CreateDeliveryOrderFormContext createDeliveryOrderFormContext;
    private final AppointmentService appointmentService;
	private final FeatureFlagsService featureFlagsService;

    public GeneratedBlobBo run(Integer institutionId, Integer patientId, Integer transcribedServiceRequestId, Integer appointmentId) {
        log.debug("Input parameters -> institutionId {}, patientId {}, transcribedServiceRequestId {}", institutionId, patientId, transcribedServiceRequestId);

        TranscribedServiceRequestBo transcribedServiceRequestBo = getTranscribedServiceRequest.run(transcribedServiceRequestId);
        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientMedicalCoverageBo patientMedicalCoverage = appointmentService.getMedicalCoverageFromAppointment(appointmentId);

        var storedFileBo = AppFeature.HABILITAR_NUEVO_FORMATO_PDF_ORDENES_PRESTACION.isActive()
                ? this.createTranscribedDeliveryOrderForm(patientId, transcribedServiceRequestId, transcribedServiceRequestBo, patientDto, patientMedicalCoverage)
                : this.createRecipeOrderTable(transcribedServiceRequestId, transcribedServiceRequestBo, patientDto, patientMedicalCoverage);

        log.debug("OUTPUT -> {}", storedFileBo);
        return storedFileBo;
    }

    private GeneratedBlobBo createTranscribedDeliveryOrderForm(Integer patientId, Integer transcribedServiceRequestId, TranscribedServiceRequestBo transcribedServiceRequestBo, BasicPatientDto patientDto, PatientMedicalCoverageBo patientMedicalCoverage) {

        FormVBo baseFormV = createDeliveryOrderBaseForm.run(patientId, transcribedServiceRequestBo, patientDto);
        FormVBo formV = completeFormV(baseFormV, transcribedServiceRequestBo, patientMedicalCoverage);
        Map<String, Object> context = createDeliveryOrderFormContext.run(formV, transcribedServiceRequestBo);

		return generatedPdfResponseService.generatePdf(FORM_REPORT,
			context,
			this.resolveNameFile(patientDto, transcribedServiceRequestId)
		);
    }

    private FormVBo completeFormV(FormVBo formV, TranscribedServiceRequestBo transcribedServiceRequest, PatientMedicalCoverageBo medicalCoverage) {

        formV.setEstablishment(transcribedServiceRequest.getInstitutionName());
        formV.setCompleteProfessionalName(transcribedServiceRequest.getHealthcareProfessionalName());

        if (medicalCoverage != null) {
            formV.setMedicalCoverage(medicalCoverage.getMedicalCoverageName());
            formV.setMedicalCoverageCondition(medicalCoverage.getConditionValue());
            formV.setAffiliateNumber(medicalCoverage.getAffiliateNumber());
        }

        return formV;
    }

    private GeneratedBlobBo createRecipeOrderTable(Integer transcribedServiceRequestId, TranscribedServiceRequestBo transcribedServiceRequestBo, BasicPatientDto patientDto, PatientMedicalCoverageBo patientMedicalCoverage) {

        var context = createRecipeOrderTableContext(transcribedServiceRequestBo, patientDto, patientMedicalCoverage);

		return generatedPdfResponseService.generatePdf(RECIPE_ORDER_TABLE,
				context,
				this.resolveNameFile(patientDto, transcribedServiceRequestId)
		);
    }

    private Map<String, Object> createRecipeOrderTableContext(TranscribedServiceRequestBo transcribedServiceRequest,
                                                              BasicPatientDto patientDto,
                                                              PatientMedicalCoverageBo patientMedicalCoverage) {
        log.trace("Input parameters -> transcribedServiceRequest {}, patientDto {}, patientMedicalCoverage {}",
                transcribedServiceRequest, patientDto, patientMedicalCoverage);

        Map<String, Object> ctx = new HashMap<>();
        ctx.put("recipe", false);
        ctx.put("order", true);
        ctx.put("request", transcribedServiceRequest);
        ctx.put("patient", patientDto);
        ctx.put("professionalName", transcribedServiceRequest.getHealthcareProfessionalName());
        ctx.put("patientCoverage", patientMedicalCoverage);
        ctx.put("institutionName", transcribedServiceRequest.getInstitutionName());
        ctx.put("requestDate", transcribedServiceRequest.getReportDate());
		ctx.put("selfPerceivedFF", featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));

        log.trace("Output -> {}", ctx);
        return ctx;
    }

    private String resolveNameFile(BasicPatientDto patientDto, Integer serviceRequestId) {
        return String.format("%s%s", patientDto.getIdentificationNumber() != null ? patientDto.getIdentificationNumber().concat("_") : "", serviceRequestId);
    }

}
