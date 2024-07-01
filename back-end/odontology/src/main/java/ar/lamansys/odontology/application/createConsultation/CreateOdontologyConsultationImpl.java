package ar.lamansys.odontology.application.createConsultation;

import static ar.lamansys.odontology.domain.consultation.ConsultationInfoBo.newConsultationInfoBo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


import ar.lamansys.odontology.domain.consultation.ConsultationResponseBo;
import ar.lamansys.odontology.domain.consultation.CreateOdontologyConsultationServiceRequestBo;
import ar.lamansys.odontology.domain.consultation.reference.ReferenceBo;
import ar.lamansys.odontology.domain.consultation.reference.ReferenceProblemBo;
import ar.lamansys.odontology.domain.consultation.reference.ReferenceStudyBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceProblemDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceStudyDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.SharedCreateConsultationServiceRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.odontology.application.createConsultation.exceptions.CreateConsultationException;
import ar.lamansys.odontology.application.createConsultation.exceptions.CreateConsultationExceptionEnum;
import ar.lamansys.odontology.application.odontogram.GetToothService;
import ar.lamansys.odontology.application.odontogram.GetToothSurfacesService;
import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.DiagnosticStorage;
import ar.lamansys.odontology.domain.EOdontologyTopicDto;
import ar.lamansys.odontology.domain.OdontologyDocumentStorage;
import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.ProcedureStorage;
import ar.lamansys.odontology.domain.Publisher;
import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.domain.ToothSurfacesBo;
import ar.lamansys.odontology.domain.consultation.ClinicalTermsValidatorUtils;
import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDiagnosticBo;
import ar.lamansys.odontology.domain.consultation.DoctorInfoBo;
import ar.lamansys.odontology.domain.consultation.OdontologyAppointmentStorage;
import ar.lamansys.odontology.domain.consultation.OdontologyConsultationStorage;
import ar.lamansys.odontology.domain.consultation.OdontologyDoctorStorage;
import ar.lamansys.odontology.domain.consultation.OdontologyDocumentBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.DocumentAppointmentDto;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOdontologyConsultationImpl implements CreateOdontologyConsultation {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOdontologyConsultationImpl.class);

    private static final Integer MIN_PERMANENT_TEETH = 0;
    private static final Integer MAX_PERMANENT_TEETH = 99;
    private static final Integer MIN_TEMPORARY_TEETH = 0;
    private static final Integer MAX_TEMPORARY_TEETH = 99;

    public static final String DEFAULT_PROBLEM_SCTID = "401281000221107";
    public static final String DEFAULT_PROBLEM_PT = "Registro de odontograma";
    public static final String DEFAULT_PROBLEM_SEVERITY = "LA6752-5";

    private final DiagnosticStorage diagnosticStorage;

    private final ProcedureStorage proceduresStorage;

    private final OdontologyConsultationStorage odontologyConsultationStorage;

    private final DateTimeProvider dateTimeProvider;

    private final OdontologyDoctorStorage odontologyDoctorStorage;

    private final OdontologyDocumentStorage odontologyDocumentStorage;

    private final DrawOdontogramService drawOdontogramService;

	private final SharedAppointmentPort sharedAppointmentPort;

	private final SharedReferenceCounterReference sharedReferenceCounterReference;

    private final CpoCeoIndicesCalculator cpoCeoIndicesCalculator;

    private final GetToothSurfacesService getToothSurfacesService;

    private final OdontologyAppointmentStorage odontologyAppointmentStorage;

    private final GetToothService getToothService;

    private final Publisher publisher;

    private final SharedCreateConsultationServiceRequest sharedCreateConsultationServiceRequest;

    private final SharedPatientPort sharedPatientPort;

    public CreateOdontologyConsultationImpl(DiagnosticStorage diagnosticStorage, ProcedureStorage proceduresStorage,
											OdontologyConsultationStorage odontologyConsultationStorage, DateTimeProvider dateTimeProvider,
											OdontologyDoctorStorage odontologyDoctorStorage, OdontologyDocumentStorage odontologyDocumentStorage,
											DrawOdontogramService drawOdontogramService, SharedAppointmentPort sharedAppointmentPort,
											CpoCeoIndicesCalculator cpoCeoIndicesCalculator, GetToothSurfacesService getToothSurfacesService,
											OdontologyAppointmentStorage odontologyAppointmentStorage, GetToothService getToothService,
											Publisher publisher, SharedReferenceCounterReference sharedReferenceCounterReference,
											SharedCreateConsultationServiceRequest sharedCreateOutpatientConsultationServiceRequest,
											SharedPatientPort sharedPatientPort
		) {
        this.diagnosticStorage = diagnosticStorage;
        this.proceduresStorage = proceduresStorage;
        this.odontologyConsultationStorage = odontologyConsultationStorage;
        this.dateTimeProvider = dateTimeProvider;
        this.odontologyDoctorStorage = odontologyDoctorStorage;
        this.odontologyDocumentStorage = odontologyDocumentStorage;
        this.drawOdontogramService = drawOdontogramService;
		this.sharedAppointmentPort = sharedAppointmentPort;
		this.cpoCeoIndicesCalculator = cpoCeoIndicesCalculator;
        this.getToothSurfacesService = getToothSurfacesService;
        this.odontologyAppointmentStorage = odontologyAppointmentStorage;
        this.getToothService = getToothService;
		this.sharedReferenceCounterReference = sharedReferenceCounterReference;
		this.publisher = publisher;
		this.sharedCreateConsultationServiceRequest = sharedCreateOutpatientConsultationServiceRequest;
		this.sharedPatientPort = sharedPatientPort;
	}

    @Override
    @Transactional
    public ConsultationResponseBo run(
    	ConsultationBo consultationBo,
    	List<CreateOdontologyConsultationServiceRequestBo> serviceRequestsToCreate)
	{
		LOG.debug("Input parameter -> consultationBo {}", consultationBo);
		if (consultationBo == null)
			throw new CreateConsultationException(CreateConsultationExceptionEnum.NULL_CONSULTATION, "La información de la consulta es obligatoria");

		DoctorInfoBo doctorInfoBo = odontologyDoctorStorage.getDoctorInfo().orElseThrow(() -> new CreateConsultationException(CreateConsultationExceptionEnum.INVALID_DOCTOR, "El identificador del profesional es inválido"));

		assertContextValid(consultationBo, doctorInfoBo);
		assertTeethQuantityValid(consultationBo);

		processDentalActions(consultationBo);
		Integer patientId = consultationBo.getPatientId();
		drawOdontogramService.run(patientId, consultationBo.getDentalActions());

		setPatientMedicalCoverageIfEmpty(consultationBo, doctorInfoBo);

		LocalDate now = dateTimeProvider.nowDate();
		Integer encounterId = odontologyConsultationStorage.save(newConsultationInfoBo(
				consultationBo,
				doctorInfoBo.getId(),
				now,
				true
		));

		consultationBo.setConsultationId(encounterId);
		cpoCeoIndicesCalculator.run(consultationBo);

		Long documentId = odontologyDocumentStorage.save(new OdontologyDocumentBo(null, consultationBo, encounterId, doctorInfoBo.getId(), now));
		Integer appointmentId = odontologyAppointmentStorage.serveAppointment(patientId, doctorInfoBo.getId(), now);
		if (appointmentId != null)
			this.sharedAppointmentPort.saveDocumentAppointment(new DocumentAppointmentDto(documentId, appointmentId));

		List<Integer> orderIds = new ArrayList<>();
		if (!consultationBo.getReferences().isEmpty()) {
			orderIds = sharedReferenceCounterReference.saveReferences(mapToCompleteReferenceDto(
					consultationBo.getReferences(),
					consultationBo.getInstitutionId(),
					doctorInfoBo.getId(),
					consultationBo.getPatientMedicalCoverageId(), patientId,
					encounterId
			));
		}

		publisher.run(patientId, consultationBo.getInstitutionId(), EOdontologyTopicDto.NUEVA_CONSULTA);

		ConsultationResponseBo result = new ConsultationResponseBo(encounterId, orderIds);

		/**
		 * Create a service request for each procedure
		 */
		BasicPatientDto patientDto = sharedPatientPort.getBasicDataFromPatient(patientId);
		createServiceRequest(
			doctorInfoBo.getId(),
			serviceRequestsToCreate,
			consultationBo.getPatientMedicalCoverageId(),
			patientDto,
			consultationBo.getInstitutionId(),
			encounterId);

		LOG.debug("Output -> result {}", result);
		return result;
    }

	/**
	 * Try to create a service request (and a diagnostic report with its observations) for each procedure
	 */
	private void createServiceRequest(
			Integer doctorId,
			List<CreateOdontologyConsultationServiceRequestBo> procedures,
			Integer medicalCoverageId,
			BasicPatientDto patientDto,
			Integer institutionId,
			Integer newConsultationId)
	{
		for (int i = 0; i < procedures.size(); i++) {
			var procedure = procedures.get(i);
			if (procedure != null) {

				String categoryId = procedure.getCategoryId();
				String healthConditionSctid = procedure.getHealthConditionSctid();
				String healthConditionPt = procedure.getHealthConditionPt();
				SnomedDto snomed = new SnomedDto(procedure.getSnomedSctid(), procedure.getSnomedPt());
				Boolean createWithStatusFinal = procedure.getCreationStatusIsFinal();
				Optional<SharedAddObservationsCommandVo> addObservationsCommand = procedure.getObservations();

				Integer patientId = patientDto.getId();
				Short patientGenderId = patientDto.getPerson().getGender().getId();
				Short patientAge = patientDto.getPerson().getAge();

				sharedCreateConsultationServiceRequest.createOdontologyServiceRequest(doctorId, categoryId, institutionId,
						healthConditionSctid, healthConditionPt, medicalCoverageId, newConsultationId, snomed.getSctid(), snomed.getPt(),
						createWithStatusFinal, addObservationsCommand, patientId, patientGenderId, patientAge);
			}
		}
	}

	private void setPatientMedicalCoverageIfEmpty(ConsultationBo consultationBo, DoctorInfoBo doctorInfoBo) {
		if (consultationBo.getPatientMedicalCoverageId() == null) {
			consultationBo.setPatientMedicalCoverageId(odontologyAppointmentStorage.getPatientMedicalCoverageId(consultationBo.getPatientId(), doctorInfoBo.getId()));
		}
	}

	private void processDentalActions(ConsultationBo consultationBo) {
        LOG.debug("Input parameter -> consultationBo {}", consultationBo);
        setDefaultProblem(consultationBo);
        setCpoCeoIndicesInDentalActions(consultationBo);
        setSurfaceSctids(consultationBo);
        sortDentalActions(consultationBo);
    }

    private void setDefaultProblem(ConsultationBo consultationBo) {
        LOG.debug("Input parameter -> consultationBo {}", consultationBo);
        if (!consultationBo.getDentalActions().isEmpty() && !hasDefaultProblem(consultationBo.getDiagnostics())) {
            ConsultationDiagnosticBo problem = buildDefaultProblem();
            consultationBo.getDiagnostics().add(problem);
        }
    }

    private ConsultationDiagnosticBo buildDefaultProblem() {
        LOG.debug("No input parameters");
        ConsultationDiagnosticBo defaultProblem = new ConsultationDiagnosticBo();
        defaultProblem.setSnomed(new OdontologySnomedBo(DEFAULT_PROBLEM_SCTID, DEFAULT_PROBLEM_PT));
        defaultProblem.setStartDate(dateTimeProvider.nowDate());
        defaultProblem.setSeverity(DEFAULT_PROBLEM_SEVERITY);
        return defaultProblem;
    }

    private boolean hasDefaultProblem(List<ConsultationDiagnosticBo> consultationDiagnosticBoList) {
        return consultationDiagnosticBoList.stream().filter(d -> d.getSnomed().getSctid().equals(DEFAULT_PROBLEM_SCTID) && d.getSnomed().getPt().equals(DEFAULT_PROBLEM_PT)).findAny().isPresent();
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
            ToothBo tooth = getToothService.run(toothId);
            ToothSurfacesBo toothSurfaces = getToothSurfacesService.run(toothId);
            actionsByToothSctid.get(toothId).forEach(actionBo -> {
                actionBo.setAppliedToTemporaryTooth(tooth.isTemporary());
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
        assertThereAreNoRepeatedConcepts(consultationBo);
    }

    private void assertTeethQuantityValid(ConsultationBo consultationBo) {
        LOG.debug("Input parameter -> consultationBo {}", consultationBo);
        Integer permanentTeethPresent = consultationBo.getPermanentTeethPresent();
        if (permanentTeethPresent != null &&
                ((permanentTeethPresent < MIN_PERMANENT_TEETH) || (permanentTeethPresent > MAX_PERMANENT_TEETH))) {
            throw new CreateConsultationException(CreateConsultationExceptionEnum.WRONG_TEETH_QUANTITY,
                    String.format("La cantidad de dientes permanentes presentes debe estar entre %d y %d", MIN_PERMANENT_TEETH, MAX_PERMANENT_TEETH));
        }

        Integer temporaryTeethPresent = consultationBo.getTemporaryTeethPresent();
        if (temporaryTeethPresent != null &&
                ((temporaryTeethPresent < MIN_TEMPORARY_TEETH) || (temporaryTeethPresent > MAX_TEMPORARY_TEETH)))
            throw new CreateConsultationException(CreateConsultationExceptionEnum.WRONG_TEETH_QUANTITY,
                    String.format("La cantidad de dientes temporarios presentes debe estar entre %d y %d", MIN_TEMPORARY_TEETH, MAX_TEMPORARY_TEETH));
    }

    private void setCpoCeoIndicesInDentalActions(ConsultationBo consultationBo) {
        consultationBo.getDentalActions().forEach(dentalAction -> {
            if (dentalAction.isDiagnostic())
                this.setCpoCeoIndicesInDiagnostic(dentalAction);
            else
                this.setCpoCeoIndicesInProcedure(dentalAction);
        });
    }

    private void assertThereAreNoRepeatedConcepts(ConsultationBo consultationBo) {
        CreateConsultationException exception = new CreateConsultationException(CreateConsultationExceptionEnum.REPEATED_CONCEPTS);
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(consultationBo.getReasons()))
            exception.addError("Motivos repetidos");
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(consultationBo.getPersonalHistories().getContent()))
            exception.addError("Antecedentes personales repetidos");
        if (ClinicalTermsValidatorUtils.repeatedClinicalTerms(consultationBo.getAllergies().getContent()))
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

    private void setCpoCeoIndicesInDiagnostic(ConsultationDentalActionBo dentalDiagnostic) {
        Optional<DiagnosticBo> opDiagnostic = diagnosticStorage.getDiagnostic(dentalDiagnostic.getSctid());
        if (opDiagnostic.isEmpty())
            throw new CreateConsultationException(CreateConsultationExceptionEnum.DENTAL_DIAGNOSTIC_NOT_FOUND,
                    "El diagnóstico con ID de Snomed: '" + dentalDiagnostic.getSctid() +
                            "' y término: '" + dentalDiagnostic.getPt() + "' no es un diagnóstico dental aplicable");

        DiagnosticBo diagnostic = opDiagnostic.get();
        if (dentalDiagnostic.isAppliedToTooth() && !diagnostic.isApplicableToTooth())
            throw new CreateConsultationException(CreateConsultationExceptionEnum.DIAGNOSTIC_NOT_APPLICABLE_TO_TOOTH,
                    "El diagnóstico con ID de Snomed: '" + dentalDiagnostic.getSctid() +
                            "' y término: '" + dentalDiagnostic.getPt() + "' no es aplicable a pieza dental");
        if (dentalDiagnostic.isAppliedToSurface() && !diagnostic.isApplicableToSurface())
            throw new CreateConsultationException(CreateConsultationExceptionEnum.DIAGNOSTIC_NOT_APPLICABLE_TO_SURFACE,
                    "El diagnóstico con ID de Snomed: '" + dentalDiagnostic.getSctid() +
                            "' y término: '" + dentalDiagnostic.getPt() + "' no es aplicable a cara dental");

        dentalDiagnostic.setPermanentIndex(diagnostic.getPermanentIndex());
        dentalDiagnostic.setTemporaryIndex(diagnostic.getTemporaryIndex());
    }

    private void setCpoCeoIndicesInProcedure(ConsultationDentalActionBo dentalProcedure) {
        Optional<ProcedureBo> opProcedure = proceduresStorage.getProcedure(dentalProcedure.getSctid());
        if (opProcedure.isEmpty())
            throw new CreateConsultationException(CreateConsultationExceptionEnum.DENTAL_PROCEDURE_NOT_FOUND,
                    "El procedimiento con ID de Snomed: '" + dentalProcedure.getSctid() +
                            "' y término: '" + dentalProcedure.getPt() + "' no es un procedimiento dental aplicable");

        ProcedureBo procedure = opProcedure.get();
        if (dentalProcedure.isAppliedToTooth() && !procedure.isApplicableToTooth())
            throw new CreateConsultationException(CreateConsultationExceptionEnum.PROCEDURE_NOT_APPLICABLE_TO_TOOTH,
                    "El procedimiento con ID de Snomed: '" + dentalProcedure.getSctid() +
                            "' y término: '" + dentalProcedure.getPt() + "' no es aplicable a pieza dental");
        if (dentalProcedure.isAppliedToSurface() && !procedure.isApplicableToSurface())
            throw new CreateConsultationException(CreateConsultationExceptionEnum.PROCEDURE_NOT_APPLICABLE_TO_SURFACE,
                    "El procedimiento con ID de Snomed: '" + dentalProcedure.getSctid() +
                            "' y término: '" + dentalProcedure.getPt() + "' no es aplicable a cara dental");

        dentalProcedure.setPermanentIndex(procedure.getPermanentIndex());
        dentalProcedure.setTemporaryIndex(procedure.getTemporaryIndex());
    }

	private List<CompleteReferenceDto> mapToCompleteReferenceDto(List<ReferenceBo> references, Integer institutionId,
																 Integer doctorId, Integer patientMedicalCoverageId,
																 Integer patientId, Integer encounterId) {
		return references.stream().map(r -> {
			CompleteReferenceDto result = new CompleteReferenceDto();
			result.setNote(r.getNote());
			result.setConsultation(r.getConsultation());
			result.setCareLineId(r.getCareLineId());
			result.setClinicalSpecialtyIds(r.getClinicalSpecialtyIds());
			result.setProblems(mapToReferenceProblemDtoList(r.getProblems()));
			result.setFileIds(r.getFileIds());
			result.setDestinationInstitutionId(r.getDestinationInstitutionId());
			result.setPhonePrefix(r.getPhonePrefix());
			result.setPhoneNumber(r.getPhoneNumber());
			result.setPriority(r.getPriority());
			result.setStudy(r.getStudy() != null ? mapToReferenceStudyDto(r.getStudy()) : null);
			result.setInstitutionId(institutionId);
			result.setDoctorId(doctorId);
			result.setPatientMedicalCoverageId(patientMedicalCoverageId);
			result.setPatientId(patientId);
			result.setEncounterId(encounterId);
			result.setSourceTypeId((int) SourceType.ODONTOLOGY);
			return result;
		}).collect(Collectors.toList());
	}

	private List<ReferenceProblemDto> mapToReferenceProblemDtoList(List<ReferenceProblemBo> problems) {
		return problems.stream()
				.map(rp -> new ReferenceProblemDto(rp.getSnomed().getSctid(), rp.getSnomed().getPt()))
				.collect(Collectors.toList());
	}

	private ReferenceStudyDto mapToReferenceStudyDto(ReferenceStudyBo study) {
		return new ReferenceStudyDto(mapToSharedSnomedDto(study.getProblem()), mapToSharedSnomedDto(study.getPractice()), study.getCategoryId());
	}

	private SharedSnomedDto mapToSharedSnomedDto(OdontologySnomedBo snomedBo) {
		return  new SharedSnomedDto(snomedBo.getSctid(), snomedBo.getPt());
	}
}
