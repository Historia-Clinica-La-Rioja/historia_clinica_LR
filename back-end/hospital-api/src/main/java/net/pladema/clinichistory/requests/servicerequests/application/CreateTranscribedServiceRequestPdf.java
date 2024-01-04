package net.pladema.clinichistory.requests.servicerequests.application;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.files.pdf.PdfService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.reports.controller.dto.FormVDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateTranscribedServiceRequestPdf {

    private final GetTranscribedServiceRequest getTranscribedServiceRequest;
    private final PatientExternalService patientExternalService;
    private final PdfService pdfService;
    private final CreateDeliveryOrderBaseForm createDeliveryOrderBaseForm;
    private final CreateDeliveryOrderFormContext createDeliveryOrderFormContext;
    private final AppointmentService appointmentService;

    public StoredFileBo run(Integer institutionId, Integer patientId, Integer transcribedServiceRequestId, Integer appointmentId) {
        log.debug("Input parameters -> institutionId {}, patientId {}, transcribedServiceRequestId {}", institutionId, patientId, transcribedServiceRequestId);

        StoredFileBo storedFileBo = this.createTranscribedDeliveryOrderForm(patientId, transcribedServiceRequestId, appointmentId);

        log.debug("OUTPUT -> {}", storedFileBo);
        return storedFileBo;
    }

    private StoredFileBo createTranscribedDeliveryOrderForm(Integer patientId, Integer transcribedServiceRequestId, Integer appointmentId) {

        TranscribedServiceRequestBo transcribedServiceRequestBo = getTranscribedServiceRequest.run(transcribedServiceRequestId);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        FormVDto baseFormVDto = createDeliveryOrderBaseForm.run(patientId, transcribedServiceRequestBo, patientDto);
        PatientMedicalCoverageDto patientMedicalCoverageDto = appointmentService.getMedicalCoverageFromAppointment(appointmentId);
        FormVDto formVDto = completeFormV(baseFormVDto, transcribedServiceRequestBo, patientMedicalCoverageDto);
        Map<String, Object> context = createDeliveryOrderFormContext.run(formVDto, transcribedServiceRequestBo);
        String template = "form_report";

        return new StoredFileBo(pdfService.generate(template, context),
                MediaType.APPLICATION_PDF_VALUE,
                this.resolveNameFile(patientDto, transcribedServiceRequestId));
    }

    private FormVDto completeFormV(FormVDto formVDto, TranscribedServiceRequestBo transcribedServiceRequestBo, PatientMedicalCoverageDto medicalCoverage) {

        formVDto.setEstablishment(transcribedServiceRequestBo.getInstitutionName());
        formVDto.setCompleteProfessionalName(transcribedServiceRequestBo.getHealthcareProfessionalName());
        formVDto.setProblems(transcribedServiceRequestBo.getHealthCondition().getPt());

        if (medicalCoverage != null) {
            formVDto.setMedicalCoverage(medicalCoverage.getMedicalCoverageName());
            formVDto.setMedicalCoverageCondition(medicalCoverage.getConditionValue());
            formVDto.setAffiliateNumber(medicalCoverage.getAffiliateNumber());
        }

        return formVDto;
    }


    private String resolveNameFile(BasicPatientDto patientDto, Integer serviceRequestId) {
        return String.format("%s%s.pdf", patientDto.getIdentificationNumber() != null ? patientDto.getIdentificationNumber().concat("_") : "", serviceRequestId);
    }

}
