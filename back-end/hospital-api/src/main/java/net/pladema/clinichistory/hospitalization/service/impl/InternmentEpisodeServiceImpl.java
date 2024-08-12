package net.pladema.clinichistory.hospitalization.service.impl;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgx.shared.auditable.entity.Updateable;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.files.pdf.EPDFTemplate;
import ar.lamansys.sgx.shared.files.pdf.GeneratedPdfResponseService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.GeneratedBlobBo;
import ar.lamansys.sgx.shared.security.UserInfo;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.fetchEpisodeDocumentTypeById.FetchEpisodeDocumentTypeById;
import net.pladema.clinichistory.hospitalization.application.getanestheticreportdraft.GetLastAnestheticReportDraftFromInternmentEpisode;
import net.pladema.clinichistory.hospitalization.application.port.InternmentEpisodeStorage;
import net.pladema.clinichistory.hospitalization.application.validateadministrativedischarge.ValidateAdministrativeDischarge;
import net.pladema.clinichistory.hospitalization.repository.EvolutionNoteDocumentRepository;
import net.pladema.clinichistory.hospitalization.repository.InternmentEpisodeRepository;
import net.pladema.clinichistory.hospitalization.repository.PatientDischargeRepository;
import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;
import net.pladema.clinichistory.hospitalization.repository.domain.EvolutionNoteDocument;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import net.pladema.clinichistory.hospitalization.repository.domain.PatientDischarge;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.DocumentsSummaryVo;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.EvaluationNoteSummaryVo;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.InternmentSummaryVo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentTypeBo;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentSummaryBo;
import net.pladema.clinichistory.hospitalization.service.domain.PatientDischargeBo;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.GeneratePdfException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentEpisodeNotFoundException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.MoreThanOneConsentDocumentException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.PatientNotFoundException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.PersonNotFoundException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.SaveMedicalDischargeException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.SaveMedicalDischargeExceptionEnum;
import net.pladema.clinichistory.hospitalization.service.summary.domain.ResponsibleDoctorBo;
import net.pladema.establishment.repository.MedicalCoveragePlanRepository;
import net.pladema.establishment.service.InstitutionService;
import net.pladema.establishment.service.domain.InstitutionBo;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import net.pladema.person.repository.entity.Person;
import net.pladema.person.service.PersonService;
import net.pladema.staff.application.getlicensenumberbyprofessional.GetLicenseNumberByProfessional;
import net.pladema.staff.domain.ProfessionalLicenseNumberBo;
import net.pladema.staff.service.HealthcareProfessionalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.util.XRRuntimeException;

import static net.pladema.staff.repository.entity.EpisodeDocumentType.SURGICAL_CONSENT;

@Slf4j
@RequiredArgsConstructor
@Service
public class InternmentEpisodeServiceImpl implements InternmentEpisodeService {

    private static final String INPUT_PARAMETERS_INTERNMENT_EPISODE = "Input parameters -> internmentEpisodeId {}";
    private static final String INPUT_PARAMETERS = "Input parameters -> {}";
    private static final String INTERNMENT_NOT_FOUND = "internmentepisode.not.found";
    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String WRONG_ID_EPISODE = "wrong-id-episode";
	private static final String SURGICAL = "Quirúrgico";
	private static final String ADMISSION = "Ingreso a internación";

    private final InternmentEpisodeRepository internmentEpisodeRepository;

    private final EvolutionNoteDocumentRepository evolutionNoteDocumentRepository;

    private final PatientDischargeRepository patientDischargeRepository;

	private final MedicalCoveragePlanRepository medicalCoveragePlanRepository;

    private final DocumentService documentService;

	private final InternmentEpisodeStorage internmentEpisodeStorage;

	private final FeatureFlagsService featureFlagsService;

	private final GeneratedPdfResponseService generatedPdfResponseService;

	private final PatientService patientService;

	private final PersonService personService;

	private final InstitutionService institutionService;

	private final FetchEpisodeDocumentTypeById fetchEpisodeDocumentTypeById;

	private final HealthcareProfessionalService healthcareProfessionalService;

	private final GetLicenseNumberByProfessional getLicenseNumberByProfessional;

	private final ValidateAdministrativeDischarge validateAdministrativeDischarge;

	private final GetLastAnestheticReportDraftFromInternmentEpisode getLastAnestheticReportDraftFromInternmentEpisode;

	@Override
    public void updateAnamnesisDocumentId(Integer internmentEpisodeId, Long anamnesisDocumentId) {
        log.debug("Input parameters -> internmentEpisodeId {}, anamnesisDocumentId {}", internmentEpisodeId, anamnesisDocumentId);
        internmentEpisodeRepository.updateAnamnesisDocumentId(internmentEpisodeId, anamnesisDocumentId, LocalDateTime.now());
	}

	@Override
	public void updateEpicrisisDocumentId(Integer internmentEpisodeId, Long epicrisisId) {
		log.debug("Input parameters -> internmentEpisodeId {}, epicrisisId {}", internmentEpisodeId, epicrisisId);
		internmentEpisodeRepository.updateEpicrisisDocumentId(internmentEpisodeId, epicrisisId, LocalDateTime.now());
	}

	@Override
	public EvolutionNoteDocument addEvolutionNote(Integer internmentEpisodeId, Long evolutionNoteId) {
		log.debug("Input parameters -> internmentEpisodeId {}, evolutionNoteId {}", internmentEpisodeId,
				evolutionNoteId);
		EvolutionNoteDocument result = new EvolutionNoteDocument(evolutionNoteId, internmentEpisodeId);
		result = evolutionNoteDocumentRepository.save(result);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean haveAnamnesis(Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		boolean result = internmentEpisodeRepository.haveAnamnesis(internmentEpisodeId);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean havePhysicalDischarge(Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		boolean result = patientDischargeRepository.findById(internmentEpisodeId).map(pd -> {
			if (pd.getPhysicalDischargeDate() != null)
				return true;
			return false;
		}).orElse(false);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean haveMedicalDischarge(Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		boolean result =  patientDischargeRepository.findById(internmentEpisodeId).map(pd -> {
			if (pd.getMedicalDischargeDate() != null)
				return true;
			return false;
		}).orElse(false);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean haveEvolutionNoteAfterAnamnesis(Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		InternmentEpisode intermentEpisode = internmentEpisodeRepository.findById(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("internment-episode-not-exists",
						String.format("No existe el episodio de internación con id %s", internmentEpisodeId)));
		Long anamnesisDocId = intermentEpisode.getAnamnesisDocId();
		if (anamnesisDocId == null)
			return false;
		boolean result = internmentEpisodeRepository.haveEvolutionNoteAfterAnamnesis(internmentEpisodeId, anamnesisDocId);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean haveUpdatesAfterEpicrisis(Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		InternmentEpisode intermentEpisode = internmentEpisodeRepository.findById(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("internment-episode-not-exists",
						String.format("No existe el episodio de internación con id %s", internmentEpisodeId)));
		Long epicrisisDocId = intermentEpisode.getEpicrisisDocId();
		if (epicrisisDocId == null)
			return false;
		boolean result = internmentEpisodeRepository.haveUpdatesAfterEpicrisis(internmentEpisodeId, epicrisisDocId);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean haveEpicrisis(Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		boolean result = internmentEpisodeRepository.hasFinalEpicrisis(internmentEpisodeId);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public LocalDateTime getEntryDate(Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		LocalDateTime result = internmentEpisodeRepository.getEntryDate(internmentEpisodeId);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean canCreateEpicrisis(Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		boolean result = internmentEpisodeRepository.canCreateEpicrisis(internmentEpisodeId);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public Optional<InternmentSummaryBo> getIntermentSummary(Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS, internmentEpisodeId);
		Optional<InternmentSummaryVo> resultQuery = internmentEpisodeStorage.getSummary(internmentEpisodeId);
		AtomicReference<Optional<InternmentSummaryBo>> result = new AtomicReference<>(Optional.empty());
		resultQuery.ifPresent(r -> {
			DocumentsSummaryVo summaries = r.getDocuments();
			summaries.setLastEvaluationNote(getLastEvaluationNoteSummary(internmentEpisodeId).orElse(new EvaluationNoteSummaryVo()));

			InternmentSummaryBo internmentSummaryBo = new InternmentSummaryBo(r);
			getLastAnestheticReportDraftFromInternmentEpisode.run(internmentEpisodeId)
					.ifPresent(internmentSummaryBo::setLastAnestheticReport);
			result.set(Optional.of(internmentSummaryBo));

			var nameSelfDetermination = resultQuery.get()
					.getDoctor()
					.getNameSelfDetermination();
			if (featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && nameSelfDetermination != null && !nameSelfDetermination.isEmpty())
				result.get()
						.get()
						.getDoctor()
						.setFirstName(nameSelfDetermination);
		});
		log.debug(LOGGING_OUTPUT, result);
		return result.get();
	}

    private Optional<EvaluationNoteSummaryVo> getLastEvaluationNoteSummary(Integer internmentEpisodeId){
		log.debug(INPUT_PARAMETERS, internmentEpisodeId);
        Page<EvaluationNoteSummaryVo> resultQuery = evolutionNoteDocumentRepository.getLastEvaluationNoteSummary(internmentEpisodeId, PageRequest.of(0, 1));
		Optional<EvaluationNoteSummaryVo> result = resultQuery.getContent().stream().findFirst();
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public Optional<Integer> getPatient(Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS, internmentEpisodeId);
		Optional<Integer> result = internmentEpisodeRepository.getPatient(internmentEpisodeId);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public PatientDischargeBo saveMedicalDischarge(PatientDischargeBo patientDischargeBo){
		log.debug(INPUT_PARAMETERS, patientDischargeBo);
		return patientDischargeRepository.findById(patientDischargeBo.getInternmentEpisodeId()).map(pd -> {
			if (pd.getMedicalDischargeDate() != null)
				throw new SaveMedicalDischargeException(SaveMedicalDischargeExceptionEnum.MEDICAL_DISCHARGE_ALREADY_EXISTS, String.format("Ya existe un alta medica correspondiente a la internacion %s", patientDischargeBo.getInternmentEpisodeId()));
			pd.setMedicalDischargeDate(patientDischargeBo.getMedicalDischargeDate());
			pd.setDischargeTypeId(patientDischargeBo.getDischargeTypeId());
			PatientDischargeBo result = new PatientDischargeBo(patientDischargeRepository.save(pd));
			log.debug(LOGGING_OUTPUT, result);
			return result;
		}).orElseGet(()->
		{
			PatientDischarge entityResult = patientDischargeRepository.save(new PatientDischarge(patientDischargeBo));
			PatientDischargeBo result = new PatientDischargeBo(entityResult);
			log.debug(LOGGING_OUTPUT, result);
			return result;
		});
	}

	@Override
	public PatientDischargeBo saveAdministrativeDischarge(PatientDischargeBo patientDischargeBo) {
		log.debug(INPUT_PARAMETERS, patientDischargeBo);

		validateAdministrativeDischarge.run(patientDischargeBo.getInternmentEpisodeId());

		return patientDischargeRepository.findById(patientDischargeBo.getInternmentEpisodeId()).map(pd -> {
			PatientDischargeBo result = new PatientDischargeBo(updatePatientDischarge(pd, patientDischargeBo));
			log.debug(LOGGING_OUTPUT, result);
			return result;
		}).orElseThrow(() -> new NotFoundException("medical-discharge-not-exists",
				String.format("No existe alta medica para el episodio %s", patientDischargeBo.getInternmentEpisodeId()))
		);
	}

	private PatientDischarge updatePatientDischarge(PatientDischarge patientDischarge, PatientDischargeBo patientDischargeBo) {
		log.debug("Input parameters -> patientDischargeBo {} , patientDischarge {}", patientDischargeBo, patientDischarge);
		patientDischarge.setInternmentEpisodeId(patientDischargeBo.getInternmentEpisodeId());
		patientDischarge.setDischargeTypeId(patientDischargeBo.getDischargeTypeId());
		patientDischarge.setAdministrativeDischargeDate(patientDischargeBo.getAdministrativeDischargeDate());
		patientDischarge.setMedicalDischargeDate(patientDischargeBo.getMedicalDischargeDate());
		patientDischarge.setPhysicalDischargeDate(patientDischargeBo.getPhysicalDischargeDate());
		patientDischarge = patientDischargeRepository.save(patientDischarge);
		log.debug(LOGGING_OUTPUT, patientDischarge);
		return patientDischarge;
	}

	@Override
	public void updateInternmentEpisodeStatus(Integer internmentEpisodeId, Short statusId) {
		log.debug("Input parameters -> {}, {}", internmentEpisodeId, statusId);
		InternmentEpisode internmentEpisode = internmentEpisodeRepository.findById(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException(WRONG_ID_EPISODE, INTERNMENT_NOT_FOUND));
		internmentEpisode.setStatusId(statusId);
		internmentEpisodeRepository.save(internmentEpisode);
	}

	@Override
	public List<InternmentEpisode> findByBedId(Integer bedId) {
		log.debug("Input parameters -> bedId {} ", bedId);
		List<InternmentEpisode> result = internmentEpisodeRepository.findByBedId(bedId);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}


	@Override
	public InternmentEpisode getInternmentEpisode(Integer internmentEpisodeId, Integer institutionId) {
		log.debug("Input parameters -> internmentEpisodeId {}, institutionId {}", internmentEpisodeId, institutionId);
		InternmentEpisode internmentEpisode = internmentEpisodeRepository.getInternmentEpisode(internmentEpisodeId,institutionId)
				.orElseThrow(() -> new NotFoundException(WRONG_ID_EPISODE, INTERNMENT_NOT_FOUND));
		log.debug(LOGGING_OUTPUT, internmentEpisode);
		return internmentEpisode;
	}

	@Override
	public boolean existsActiveForBedId(Integer bedId) {
		log.debug("Input parameters -> bedId {} ", bedId);
		List<InternmentEpisode> episodes = this.findByBedId(bedId);
		boolean result = episodes != null && !episodes.isEmpty() && anyActive(episodes);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public LocalDateTime getLastUpdateDateOfInternmentEpisode(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {} ", internmentEpisodeId);
		LocalDateTime entryDate = this.getEntryDate(internmentEpisodeId);
		if (entryDate == null)
			throw new NotFoundException(WRONG_ID_EPISODE, INTERNMENT_NOT_FOUND);
		List<Updateable> intermentDocuments = documentService.getUpdatableDocuments(internmentEpisodeId);
		List<LocalDateTime> dates = intermentDocuments.stream()
				.map( doc -> doc.getUpdatedOn())
				.collect(Collectors.toList());
		dates.add(entryDate);
		LocalDateTime result = dates.stream().max(LocalDateTime::compareTo).orElse(entryDate);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public LocalDateTime updateInternmentEpisodeProbableDischargeDate(Integer internmentEpisodeId, LocalDateTime probableDischargeDate) {
		log.debug("Input parameters -> internmentEpisodeId {}, probableDischargeDate {}", internmentEpisodeId, probableDischargeDate);
		Integer currentUser = UserInfo.getCurrentAuditor();
		internmentEpisodeRepository.updateInternmentEpisodeProbableDischargeDate(internmentEpisodeId, probableDischargeDate, currentUser, LocalDateTime.now());
		log.debug(LOGGING_OUTPUT, probableDischargeDate);
		return probableDischargeDate;
    }

    private boolean anyActive(List<InternmentEpisode> episodes) {
        log.debug("Input parameters -> episodes {}", episodes);
        boolean result = episodes.stream().anyMatch(InternmentEpisode::isActive);
        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public Integer updateInternmentEpisodeBed(Integer internmentEpisodeId, Integer newBedId) {
        log.debug("Input parameters -> internmentEpisodeId {}, newBedId {}", internmentEpisodeId, newBedId);
        Integer currentUser = UserInfo.getCurrentAuditor();
        return internmentEpisodeRepository.findById(internmentEpisodeId).map(internmentEpisode -> {
			internmentEpisodeRepository.updateInternmentEpisodeBed(internmentEpisodeId, newBedId, currentUser, LocalDateTime.now());
			log.debug(LOGGING_OUTPUT, newBedId);
			return internmentEpisode.getBedId();
		}).get();
    }

	@Override
	public Optional<PatientMedicalCoverageBo> getMedicalCoverage(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		Optional<PatientMedicalCoverageBo> result = internmentEpisodeRepository.getInternmentEpisodeMedicalCoverage(internmentEpisodeId).map(PatientMedicalCoverageBo::new).map(bo -> {
			if (bo.getPlanId() != null)
				bo.setPlanName(medicalCoveragePlanRepository.findById(bo.getPlanId()).get().getPlan());
			return bo;
		});
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public void deleteAnamnesisDocumentId(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		Integer currentUser = UserInfo.getCurrentAuditor();
		internmentEpisodeRepository.deleteAnamnesisDocumentId(internmentEpisodeId, currentUser, LocalDateTime.now());
	}

	@Override
	public void deleteEpicrisisDocumentId(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		Integer currentUser = UserInfo.getCurrentAuditor();
		internmentEpisodeRepository.deleteEpicrisisDocumentId(internmentEpisodeId, currentUser, LocalDateTime.now());
	}

	@Override
	public PatientDischargeBo savePatientPhysicalDischarge(Integer internmentEpisodeId) {
		log.debug(INPUT_PARAMETERS, internmentEpisodeId);
		return patientDischargeRepository.findById(internmentEpisodeId).map(patientDischarge -> {
				patientDischarge.setPhysicalDischargeDate(LocalDateTime.now());
				PatientDischarge entityResult = patientDischargeRepository.save(patientDischarge);
				PatientDischargeBo result = new PatientDischargeBo(entityResult);
				log.debug(LOGGING_OUTPUT, result);
				return result;
			}).orElseGet(() -> {
				PatientDischargeBo patientDischargeBo = new PatientDischargeBo(internmentEpisodeId, null, null, DischargeType.OTRO, LocalDateTime.now() );
				PatientDischarge entityResult = patientDischargeRepository.save(new PatientDischarge(patientDischargeBo));
				PatientDischargeBo result = new PatientDischargeBo(entityResult);
				log.debug(LOGGING_OUTPUT, result);
				return result;
			});
	}

	@Override
	public boolean haveMoreThanOneIntermentEpisodesFromPatients(List<Integer> patients) {
		log.debug("Input parameters -> patients {}", patients);
		boolean result = internmentEpisodeRepository.haveMoreThanOneFromPatients(patients, InternmentEpisodeStatus.ACTIVE_ID);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public GeneratedBlobBo generateEpisodeDocumentType(Integer institutionId, Integer consentId, Integer internmentEpisodeId, List<String> procedures, String observations, String professionalId) throws GeneratePdfException, PatientNotFoundException, PersonNotFoundException, InternmentEpisodeNotFoundException {
		log.debug("Input parameters -> institutionId {}, consentId {}, internmentEpisodeId {}, procedures {}, observations {}, professionalId {}", institutionId, consentId, internmentEpisodeId, procedures, observations, professionalId);
		InternmentEpisode internmentEpisode = getInternmentEpisode(internmentEpisodeId, institutionId);
		Optional<Patient> patient = patientService.getPatient(internmentEpisode.getPatientId());
		if (patient.isEmpty())
			throw new PatientNotFoundException();
		var pa = patient.get();
		Optional<Person> person = personService.findByPatientId(pa.getId());
		if (person.isEmpty())
			throw new PersonNotFoundException();
		var pe = person.get();
		Optional<InternmentSummaryBo> internmentSummaryBo = getIntermentSummary(internmentEpisodeId);
		if (internmentSummaryBo.isEmpty())
			throw new InternmentEpisodeNotFoundException();
		var isbo = internmentSummaryBo.get();

		if (consentId.equals((int) SURGICAL_CONSENT)) {
			var professional = healthcareProfessionalService.findActiveProfessionalById(Integer.parseInt((professionalId)));
			var licenses = getLicenseNumberByProfessional.run(professional.getId());
			isbo.setDoctor(new ResponsibleDoctorBo(professional.getId(), professional.getFirstName(), professional.getLastName(), licenses
					.stream()
					.map(ProfessionalLicenseNumberBo::getCompleteTypeLicenseNumber)
					.collect(Collectors.toList())));
		}

		InstitutionBo institutionBo = institutionService.get(institutionId);
		EpisodeDocumentTypeBo episodeDocumentTypeBo = fetchEpisodeDocumentTypeById.run(consentId);
		Map<String, Object> context = createContext(
				mapToBasicDataPersonDto(pe),
				isbo.getDoctor(),
				institutionBo.getName(),
				LocalDateTime.from(internmentEpisode.getEntryDate().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3"))),
				internmentEpisodeId,
				episodeDocumentTypeBo.getRichTextBody(),
				consentId,
				procedures,
				observations);


		return getGenerate(context, EPDFTemplate.CONSENT_DOCUMENT, "Documento de consentimiento");
	}

	@Override
	public void existsConsentDocumentInInternmentEpisode(Integer internmentEpisodeId, Integer consentId) throws MoreThanOneConsentDocumentException {
		log.debug("Input parameters -> internmentEpisodeId {}, consentId {}", internmentEpisodeId, consentId);
		if (internmentEpisodeRepository.existsConsentDocumentInInternmentEpisode(internmentEpisodeId, consentId))
			throw new MoreThanOneConsentDocumentException();
	}

	@Override
	public Integer getInternmentEpisodeSectorId(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		Integer result = internmentEpisodeRepository.getInternmentEpisodeSectorId(internmentEpisodeId);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public Integer getInternmentEpisodeRoomId(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		Integer result = internmentEpisodeRepository.getInternmentEpisodeRoomId(internmentEpisodeId);
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

	private Map<String, Object> createContext(BasicDataPersonDto personDto, ResponsibleDoctorBo doctor, String institutionName, LocalDateTime entryDate, Integer internmentEpisodeId, String richBody, Integer consentId, List<String> procedures, String observations){
		Map<String, Object> ctx = new HashMap<>();
		ctx.put("personDto", personDto);
		ctx.put("doctorDto", doctor);
		ctx.put("institutionName", institutionName);
		ctx.put("entryDate", entryDate);
		ctx.put("internmentEpisodeId", internmentEpisodeId);
		ctx.put("richBody", richBody);
		ctx.put("subtitle", consentId.equals((int) SURGICAL_CONSENT) ? SURGICAL : ADMISSION);
		ctx.put("procedures", procedures);
		ctx.put("observations", observations);
		ctx.put("selfPerceivedFF", featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));
		return ctx;
	}

	private BasicDataPersonDto mapToBasicDataPersonDto(Person person) {
		BasicDataPersonDto dto = new BasicDataPersonDto();
		dto.setFirstName(person.getFirstName());
		dto.setLastName(person.getLastName());
		dto.setMiddleNames(person.getMiddleNames());
		dto.setIdentificationNumber(person.getIdentificationNumber());
		return dto;
	}

	private GeneratedBlobBo getGenerate(Map<String, Object> context, EPDFTemplate template, String filename) throws GeneratePdfException {
		try {
			return generatedPdfResponseService.generatePdf(template, context, filename);
		} catch(XRRuntimeException exc) {
			log.error(exc.getMessage());
			throw new GeneratePdfException(exc.getMessage());
		} catch(Exception exc) {
			log.error(exc.getMessage(), exc);
			throw new GeneratePdfException(exc.getMessage());
		}
	}

}
