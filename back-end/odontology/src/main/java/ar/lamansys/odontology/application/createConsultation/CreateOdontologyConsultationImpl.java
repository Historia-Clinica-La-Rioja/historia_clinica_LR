package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.application.createConsultation.exceptions.CreateConsultationException;
import ar.lamansys.odontology.application.createConsultation.exceptions.CreateConsultationExceptionEnum;
import ar.lamansys.odontology.application.odontogram.GetToothSurfacesService;
import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.DiagnosticStorage;
import ar.lamansys.odontology.domain.OdontologyDocumentStorage;
import ar.lamansys.odontology.domain.ProcedureStorage;
import ar.lamansys.odontology.domain.ToothSurfacesBo;
import ar.lamansys.odontology.domain.consultation.ClinicalTermsValidatorUtils;
import ar.lamansys.odontology.domain.consultation.DoctorInfoBo;
import ar.lamansys.odontology.domain.consultation.OdontologyDoctorStorage;
import ar.lamansys.odontology.domain.consultation.OdontologyConsultationStorage;
import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.ConsultationInfoBo;
import ar.lamansys.odontology.domain.consultation.OdontologyDocumentBo;
import ar.lamansys.odontology.domain.consultation.AppointmentStorage;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CreateOdontologyConsultationImpl implements CreateOdontologyConsultation {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOdontologyConsultationImpl.class);

    private final DiagnosticStorage diagnosticStorage;

    private final ProcedureStorage proceduresStorage;

    private final OdontologyConsultationStorage odontologyConsultationStorage;

    private final DateTimeProvider dateTimeProvider;

    private final OdontologyDoctorStorage odontologyDoctorStorage;

    private final OdontologyDocumentStorage odontologyDocumentStorage;

    private final DrawOdontogramService drawOdontogramService;

    private final GetToothSurfacesService getToothSurfacesService;

    private final AppointmentStorage appointmentStorage;

    public CreateOdontologyConsultationImpl(DiagnosticStorage diagnosticStorage,
                                         ProcedureStorage proceduresStorage,
                                         OdontologyConsultationStorage odontologyConsultationStorage,
                                         DateTimeProvider dateTimeProvider,
                                         OdontologyDoctorStorage odontologyDoctorStorage,
                                         OdontologyDocumentStorage odontologyDocumentStorage,
                                         DrawOdontogramService drawOdontogramService,
                                         GetToothSurfacesService getToothSurfacesService,
                                         AppointmentStorage appointmentStorage) {
        this.diagnosticStorage = diagnosticStorage;
        this.proceduresStorage = proceduresStorage;
        this.odontologyConsultationStorage = odontologyConsultationStorage;
        this.dateTimeProvider = dateTimeProvider;
        this.odontologyDoctorStorage = odontologyDoctorStorage;
        this.odontologyDocumentStorage = odontologyDocumentStorage;
        this.drawOdontogramService = drawOdontogramService;
        this.getToothSurfacesService = getToothSurfacesService;
        this.appointmentStorage = appointmentStorage;
    }

    @Override
    @Transactional
    public void run(ConsultationBo consultationBo) {
        LOG.debug("Input parameter -> odontologyConsultationBo {}", consultationBo);
        if (consultationBo == null)
            throw new CreateConsultationException(CreateConsultationExceptionEnum.NULL_CONSULTATION,
                    "La información de la consulta es obligatoria");

        DoctorInfoBo doctorInfoBo = odontologyDoctorStorage.getDoctorInfo().orElseThrow(() ->
                new CreateConsultationException(CreateConsultationExceptionEnum.INVALID_DOCTOR, "El identificador del profesional es inválido"));

        assertContextValid(consultationBo, doctorInfoBo);

        setSurfaceSctids(consultationBo);

        sortDentalActions(consultationBo);
        drawOdontogramService.run(consultationBo.getPatientId(), consultationBo.getDentalActions());

        Integer medicalCoverageId = appointmentStorage.getPatientMedicalCoverageId(consultationBo.getPatientId(), doctorInfoBo.getId());
        LocalDate now = dateTimeProvider.nowDate();
        Integer encounterId = odontologyConsultationStorage.save(
                new ConsultationInfoBo(null,
                        consultationBo,
                        medicalCoverageId,
                        doctorInfoBo.getId(),
                        now,
                        true));

        odontologyDocumentStorage.save(new OdontologyDocumentBo(null, consultationBo, encounterId, doctorInfoBo.getId()));
        appointmentStorage.serveAppointment(consultationBo.getPatientId(), doctorInfoBo.getId(), now);

        LOG.debug("No output");
    }

    private void sortDentalActions(ConsultationBo consultationBo) {
        LOG.debug("Input parameter -> consultationBo {}", consultationBo);
        // leaves diagnostics at the beginning and procedures at the end
        consultationBo.getDentalActions().sort(Comparator.comparing(ConsultationDentalActionBo::isProcedure));
    }

    private void setSurfaceSctids(ConsultationBo consultationBo) {
        LOG.debug("Input parameter -> consultationBo {}", consultationBo);
        if (consultationBo.getDentalActions() == null) return;

        Map<String, List<ConsultationDentalActionBo>> actionsByToothSctid = consultationBo.getDentalActions()
                .stream()
                .collect(Collectors.groupingBy(ConsultationDentalActionBo::getToothSctid));

        actionsByToothSctid.keySet().forEach(toothId -> {
            ToothSurfacesBo toothSurfaces = getToothSurfacesService.run(toothId);
            actionsByToothSctid.get(toothId).forEach(actionBo -> {
                if (actionBo.isAppliedToSurface())
                    actionBo.setSurface(toothSurfaces.getSurface(actionBo.getSurfacePosition()));
            });
        });
    }

    private void assertContextValid(ConsultationBo consultationBo, DoctorInfoBo doctorInfoBo) {
        if (consultationBo.getInstitutionId() == null)
            throw new CreateConsultationException(CreateConsultationExceptionEnum.NULL_INSTITUTION_ID, "El id de la institución es obligatorio");
        if (consultationBo.getPatientId() == null)
            throw new CreateConsultationException(CreateConsultationExceptionEnum.NULL_PATIENT_ID, "El id del paciente es obligatorio");
        if (consultationBo.getClinicalSpecialtyId() == null)
            throw new CreateConsultationException(CreateConsultationExceptionEnum.NULL_CLINICAL_SPECIALTY_ID, "El id de especialidad es obligatorio");
        if (!doctorInfoBo.hasSpecialty(consultationBo.getClinicalSpecialtyId()))
            throw new CreateConsultationException(CreateConsultationExceptionEnum.INVALID_CLINICAL_SPECIALTY_ID, "El doctor no posee la especialidad indicada");
        if (consultationBo.getDentalActions() != null)
            consultationBo.getDentalActions().forEach(dentalAction -> {
                if (dentalAction.isDiagnostic())
                    this.validateDentalDiagnostic(dentalAction);
                else
                    this.validateDentalProcedure(dentalAction);
            });
        assertThereAreNoRepeatedConcepts(consultationBo);
    }

    private void assertThereAreNoRepeatedConcepts(ConsultationBo consultationBo) {
        CreateConsultationException exception = new CreateConsultationException(CreateConsultationExceptionEnum.REPEATED_CONCEPTS);
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(consultationBo.getReasons()))
            exception.addError("Motivos repetidos");
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(consultationBo.getPersonalHistories()))
            exception.addError("Antecedentes personales repetidos");
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(consultationBo.getAllergies()))
            exception.addError("Alergias repetidas");
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(consultationBo.getDiagnostics()))
            exception.addError("Diagnósticos repetidos");
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(consultationBo.getProcedures()))
            exception.addError("Procedimientos repetidos");
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(consultationBo.getMedications()))
            exception.addError("Medicaciones repetidas");

        if (exception.hasErrors())
            throw exception;
    }

    private void validateDentalDiagnostic(ConsultationDentalActionBo dentalDiagnostic) {
        Optional<DiagnosticBo> opDiagnostic = diagnosticStorage.getDiagnostic(dentalDiagnostic.getSctid());
        if (opDiagnostic.isEmpty())
            throw new CreateConsultationException(CreateConsultationExceptionEnum.DENTAL_DIAGNOSTIC_NOT_FOUND,
                    "El diagnóstico con sctid: " + dentalDiagnostic.getSctid() +
                            " y prefered term: '" + dentalDiagnostic.getPt() + "' no es un diagnóstico dental aplicable");

        DiagnosticBo diagnostic = opDiagnostic.get();
        if (dentalDiagnostic.isAppliedToTooth() && !diagnostic.isApplicableToTooth())
            throw new CreateConsultationException(CreateConsultationExceptionEnum.DIAGNOSTIC_NOT_APPLICABLE_TO_TOOTH,
                    "El diagnóstico con sctid: " + dentalDiagnostic.getSctid() +
                            " y prefered term: '" + dentalDiagnostic.getPt() + "' no es aplicable a pieza dental");
        if (dentalDiagnostic.isAppliedToSurface() && !diagnostic.isApplicableToSurface())
            throw new CreateConsultationException(CreateConsultationExceptionEnum.DIAGNOSTIC_NOT_APPLICABLE_TO_SURFACE,
                    "El diagnóstico con sctid: " + dentalDiagnostic.getSctid() +
                            " y prefered term: '" + dentalDiagnostic.getPt() + "' no es aplicable a cara dental");
    }

    private void validateDentalProcedure(ConsultationDentalActionBo dentalProcedure) {
        Optional<ProcedureBo> opProcedure = proceduresStorage.getProcedure(dentalProcedure.getSctid());
        if (opProcedure.isEmpty())
            throw new CreateConsultationException(CreateConsultationExceptionEnum.DENTAL_PROCEDURE_NOT_FOUND,
                    "El procedimiento con sctid: " + dentalProcedure.getSctid() +
                            " y prefered term: '" + dentalProcedure.getPt() + "' no es un procedimiento dental aplicable");

        ProcedureBo procedure = opProcedure.get();
        if (dentalProcedure.isAppliedToTooth() && !procedure.isApplicableToTooth())
            throw new CreateConsultationException(CreateConsultationExceptionEnum.PROCEDURE_NOT_APPLICABLE_TO_TOOTH,
                    "El procedimiento con sctid: " + dentalProcedure.getSctid() +
                            " y prefered term: '" + dentalProcedure.getPt() + "' no es aplicable a pieza dental");
        if (dentalProcedure.isAppliedToSurface() && !procedure.isApplicableToSurface())
            throw new CreateConsultationException(CreateConsultationExceptionEnum.PROCEDURE_NOT_APPLICABLE_TO_SURFACE,
                    "El procedimiento con sctid: " + dentalProcedure.getSctid() +
                            " y prefered term: '" + dentalProcedure.getPt() + "' no es aplicable a cara dental");
    }

}
