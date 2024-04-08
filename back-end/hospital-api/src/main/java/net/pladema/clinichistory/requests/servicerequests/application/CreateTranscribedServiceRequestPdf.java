package net.pladema.clinichistory.requests.servicerequests.application;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.files.pdf.PdfService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import net.pladema.reports.service.domain.FormVBo;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class CreateTranscribedServiceRequestPdf {

    private final GetTranscribedServiceRequest getTranscribedServiceRequest;
    private final PatientExternalService patientExternalService;
    private final PdfService pdfService;
    private final CreateDeliveryOrderBaseForm createDeliveryOrderBaseForm;
    private final CreateDeliveryOrderFormContext createDeliveryOrderFormContext;
    private final AppointmentService appointmentService;

    public StoredFileBo run(Integer institutionId, Integer patientId, Integer transcribedServiceRequestId, Integer appointmentId) {
        log.debug("Input parameters -> institutionId {}, patientId {}, transcribedServiceRequestId {}", institutionId, patientId, transcribedServiceRequestId);

        TranscribedServiceRequestBo transcribedServiceRequestBo = getTranscribedServiceRequest.run(transcribedServiceRequestId);
        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientMedicalCoverageBo patientMedicalCoverage = appointmentService.getMedicalCoverageFromAppointment(appointmentId);

        StoredFileBo storedFileBo = AppFeature.HABILITAR_NUEVO_FORMATO_PDF_ORDENES_PRESTACION.isActive()
                ? this.createTranscribedDeliveryOrderForm(patientId, transcribedServiceRequestId, transcribedServiceRequestBo, patientDto, patientMedicalCoverage)
                : this.createRecipeOrderTable(transcribedServiceRequestId, transcribedServiceRequestBo, patientDto, patientMedicalCoverage);

        log.debug("OUTPUT -> {}", storedFileBo);
        return storedFileBo;
    }

    private StoredFileBo createTranscribedDeliveryOrderForm(Integer patientId, Integer transcribedServiceRequestId, TranscribedServiceRequestBo transcribedServiceRequestBo, BasicPatientDto patientDto, PatientMedicalCoverageBo patientMedicalCoverage) {

        FormVBo baseFormV = createDeliveryOrderBaseForm.run(patientId, transcribedServiceRequestBo, patientDto);
        FormVBo formV = completeFormV(baseFormV, transcribedServiceRequestBo, patientMedicalCoverage);
        Map<String, Object> context = createDeliveryOrderFormContext.run(formV, transcribedServiceRequestBo);
        String template = "form_report";

        return new StoredFileBo(pdfService.generate(template, context),
                MediaType.APPLICATION_PDF_VALUE,
                this.resolveNameFile(patientDto, transcribedServiceRequestId));
    }

    private FormVBo completeFormV(FormVBo formV, TranscribedServiceRequestBo transcribedServiceRequest, PatientMedicalCoverageBo medicalCoverage) {

        formV.setEstablishment(transcribedServiceRequest.getInstitutionName());
        formV.setCompleteProfessionalName(transcribedServiceRequest.getHealthcareProfessionalName());
        formV.setProblems(transcribedServiceRequest.getHealthCondition().getPt());

        if (medicalCoverage != null) {
            formV.setMedicalCoverage(medicalCoverage.getMedicalCoverageName());
            formV.setMedicalCoverageCondition(medicalCoverage.getConditionValue());
            formV.setAffiliateNumber(medicalCoverage.getAffiliateNumber());
        }

        return formV;
    }

    private StoredFileBo createRecipeOrderTable(Integer transcribedServiceRequestId, TranscribedServiceRequestBo transcribedServiceRequestBo, BasicPatientDto patientDto, PatientMedicalCoverageBo patientMedicalCoverage) {

        var context = createRecipeOrderTableContext(transcribedServiceRequestBo, patientDto, patientMedicalCoverage);

        String template = "recipe_order_table";

        return new StoredFileBo(pdfService.generate(template, context),
                MediaType.APPLICATION_PDF_VALUE,
                this.resolveNameFile(patientDto, transcribedServiceRequestId));
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

        log.trace("Output -> {}", ctx);
        return ctx;
    }

    private String resolveNameFile(BasicPatientDto patientDto, Integer serviceRequestId) {
        return String.format("%s%s.pdf", patientDto.getIdentificationNumber() != null ? patientDto.getIdentificationNumber().concat("_") : "", serviceRequestId);
    }

}
