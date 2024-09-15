package net.pladema.medicalconsultation.diary.service.impl;

import static ar.lamansys.sgx.shared.security.UserInfo.getCurrentAuditor;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

import net.pladema.medicalconsultation.diary.service.domain.ActiveDiaryAliasBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.controller.service.InstitutionExternalService;
import net.pladema.medicalconsultation.appointment.domain.enums.EAppointmentModality;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentSearchBo;
import net.pladema.medicalconsultation.appointment.service.domain.EmptyAppointmentBo;
import net.pladema.medicalconsultation.diary.repository.DiaryLabelRepository;
import net.pladema.medicalconsultation.diary.repository.DiaryRepository;
import net.pladema.medicalconsultation.diary.repository.domain.CompleteDiaryListVo;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryListVo;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.diary.repository.entity.DiaryLabel;
import net.pladema.medicalconsultation.diary.service.DiaryAssociatedProfessionalService;
import net.pladema.medicalconsultation.diary.service.DiaryCareLineService;
import net.pladema.medicalconsultation.diary.service.DiaryOpeningHoursService;
import net.pladema.medicalconsultation.diary.service.DiaryPracticeService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.ActiveDiaryClinicalSpecialtyBo;
import net.pladema.medicalconsultation.diary.service.domain.CompleteDiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.OpeningHoursBo;
import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundException;
import net.pladema.medicalconsultation.repository.entity.MedicalAttentionType;
import net.pladema.permissions.controller.external.LoggedUserExternalService;
import net.pladema.permissions.repository.enums.ERole;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiaryServiceImpl implements DiaryService {

    private static final String INPUT_DIARY_ID = "Input parameters -> diaryId {}";
    private static final String OUTPUT = "Output -> {}";

    private final DiaryOpeningHoursService diaryOpeningHoursService;

    private final AppointmentService appointmentService;

    private final DiaryAssociatedProfessionalService diaryAssociatedProfessionalService;

    private final DiaryCareLineService diaryCareLineService;

    private final DiaryRepository diaryRepository;

    private final LoggedUserExternalService loggedUserExternalService;

    private final InstitutionExternalService institutionExternalService;

    private final DateTimeProvider dateTimeProvider;

    private final DiaryPracticeService diaryPracticeService;

    private final DiaryLabelRepository diaryLabelRepository;

    private final SnomedService snomedService;

    private final FeatureFlagsService featureFlagsService;

	@Override
	@Transactional
	public Integer addDiary(DiaryBo diaryToSave) {
        log.debug("Input parameters -> diaryToSave {}", diaryToSave);

        validateDiary(diaryToSave);

        Diary diary = createDiaryInstance(diaryToSave);
        Integer diaryId = persistDiary(diaryToSave, diary);
        diaryToSave.setId(diaryId);
        setDiaryLabels(diaryToSave);
        log.debug("Diary saved -> {}", diaryToSave);
        return diaryId;
    }

    private Integer persistDiary(DiaryBo diaryToSave, Diary diary) {
        diary = diaryRepository.save(diary);
        Integer diaryId = diary.getId();
        diaryOpeningHoursService.update(diaryId, diaryToSave.getDiaryOpeningHours());
        diaryCareLineService.updateCareLinesAssociatedToDiary(diaryId, diaryToSave.getCareLines());
        diaryAssociatedProfessionalService.updateDiaryAssociatedProfessionals(diaryToSave.getDiaryAssociatedProfessionalsId(), diaryId);
        diaryPracticeService.updateDiaryPractices(diaryToSave.getPracticesId(), diaryId);
        return diaryId;
    }

    private Diary createDiaryInstance(DiaryBo diaryBo) {
        Diary diary = new Diary();
        return mapDiaryBo(diaryBo, diary);
    }

    private Diary mapDiaryBo(DiaryBo diaryBo, Diary diary) {
        diary.setId(diaryBo.getId() != null ? diaryBo.getId() : null);
        diary.setHealthcareProfessionalId(diaryBo.getHealthcareProfessionalId());
        diary.setDoctorsOfficeId(diaryBo.getDoctorsOfficeId());
        diary.setStartDate(diaryBo.getStartDate());
        diary.setEndDate(diaryBo.getEndDate());
        diary.setAppointmentDuration(diaryBo.getAppointmentDuration());
        diary.setAutomaticRenewal(diaryBo.isAutomaticRenewal());
        diary.setProfessionalAsignShift(diaryBo.isProfessionalAssignShift());
        diary.setIncludeHoliday(diaryBo.isIncludeHoliday());
        diary.setActive(true);
        diary.setClinicalSpecialtyId(diaryBo.getClinicalSpecialtyId());
        diary.setAlias(diaryBo.getAlias());
        diary.setPredecessorProfessionalId(diaryBo.getPredecessorProfessionalId());
        diary.setHierarchicalUnitId(diaryBo.getHierarchicalUnitId());
        // db cries with not-null values if not setted
        diary.setCreatedBy(getCurrentAuditor());
        diary.setCreatedOn(LocalDateTime.now());
        diary.setDeleted(false);
        return diary;
    }

    @Override
    @Transactional
    public Integer persistDiary(DiaryBo diaryToSave) {
        Diary diary = createDiaryInstance(diaryToSave);
        diary = diaryRepository.save(diary);
        Integer diaryId = diary.getId();
        diaryCareLineService.updateCareLinesAssociatedToDiary(diaryId, diaryToSave.getCareLines());
        diaryAssociatedProfessionalService.updateDiaryAssociatedProfessionals(diaryToSave.getDiaryAssociatedProfessionalsId(), diaryId);
        diaryPracticeService.updateDiaryPractices(diaryToSave.getPracticesId(), diaryId);
        diaryOpeningHoursService.update(diaryId, diaryToSave.getDiaryOpeningHours()); // must be here at the end of method
        return diaryId;
    }

    @Override
    @Transactional
    public Boolean deleteDiary(Integer diaryId) {
        log.debug(INPUT_DIARY_ID, diaryId);
        Diary diaryToDelete = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new EntityNotFoundException("diary.invalid.id"));
        diaryToDelete.setDeleted(Boolean.TRUE);
        diaryToDelete.setDeletedBy(getCurrentAuditor());
        diaryToDelete.setDeletedOn(LocalDateTime.now());
        diaryToDelete.setActive(false);
        diaryRepository.save(diaryToDelete);
        log.debug("Diary with diaryId {} DELETED", diaryId);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public void setDiaryLabels(DiaryBo diaryToUpdate) {
        diaryToUpdate.getDiaryLabelBo().forEach(diaryLabelBo -> {
            if (diaryLabelBo.getId() == null) {
                diaryLabelBo.setDiaryId(diaryToUpdate.getId());
                Integer id = diaryLabelRepository.save(new DiaryLabel(diaryLabelBo)).getId();
                diaryLabelBo.setId(id);
            } else {
                diaryLabelRepository.findById(diaryLabelBo.getId()).ifPresent(diaryLabel -> {
                    diaryLabel.setDescription(diaryLabelBo.getDescription());
                    diaryLabel.setColorId(diaryLabelBo.getColorId());
                    diaryLabelRepository.save(diaryLabel);
                });
            }
        });
    }

    @Override
    public List<DiaryBo> getAllOverlappingDiary(@NotNull Integer healthcareProfessionalId, @NotNull Integer doctorsOfficeId,
                                                @NotNull Integer institutionId, @NotNull LocalDate newDiaryStart, @NotNull LocalDate newDiaryEnd,
                                                Optional<Integer> excludeDiaryId) {
        log.debug(
                "Input parameters -> doctorsOfficeId {}, newDiaryStart {}, newDiaryEnd {}",
                doctorsOfficeId, newDiaryStart, newDiaryEnd);
        List<Diary> diaries = excludeDiaryId.isPresent()
                ? diaryRepository.findAllOverlappingDiaryExcludingDiary(healthcareProfessionalId, doctorsOfficeId,
                institutionId, newDiaryStart, newDiaryEnd, excludeDiaryId.get())
                : diaryRepository.findAllOverlappingDiary(healthcareProfessionalId, doctorsOfficeId, institutionId, newDiaryStart,
                newDiaryEnd);
        List<DiaryBo> result = diaries.stream().map(this::createDiaryBoInstance).collect(Collectors.toList());
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Collection<DiaryBo> getActiveDiariesBy(Integer associatedHealthcareProfessionalId, Integer healthcareProfessionalId, Integer specialtyId, Integer institutionId) {
        log.debug("Input parameters -> healthcareProfessionalId {}, specialtyId {}, institutionId {}", healthcareProfessionalId, specialtyId, institutionId);
        List<DiaryListVo> diaries;

        if (specialtyId == null)
            if (healthcareProfessionalId.equals(associatedHealthcareProfessionalId) || associatedHealthcareProfessionalId == null || loggedUserExternalService.hasAnyRoleInstitution(institutionId, ERole.ADMINISTRADOR_AGENDA, ERole.ADMINISTRATIVO))
                diaries = diaryRepository.getActiveDiariesFromProfessional(healthcareProfessionalId, institutionId);
            else
                diaries = diaryRepository.getActiveAssociatedDiariesFromProfessional(associatedHealthcareProfessionalId, healthcareProfessionalId, institutionId);
        else if (healthcareProfessionalId.equals(associatedHealthcareProfessionalId) || associatedHealthcareProfessionalId == null)
            diaries = diaryRepository.getActiveDiariesFromProfessionalAndSpecialty(healthcareProfessionalId, specialtyId, institutionId);
        else
            diaries = diaryRepository.getActiveAssociatedDiariesFromProfessionalAndSpecialty(associatedHealthcareProfessionalId, healthcareProfessionalId, specialtyId, institutionId);

        List<DiaryBo> result = diaries.stream().map(this::createDiaryBoInstance).collect(toList());
        log.debug(OUTPUT, result);
        return result;
    }

    private DiaryBo createDiaryBoInstance(DiaryListVo diaryListVo) {
        log.debug("Input parameters -> diaryListVo {}", diaryListVo);
        DiaryBo result = new DiaryBo();
        List<SnomedBo> practices = diaryPracticeService.getAllByDiaryId(diaryListVo.getId());
        List<String> stringPractices = practices.stream().map(SnomedBo::getPt).collect(Collectors.toList());
        result.setId(diaryListVo.getId());
        result.setDoctorsOfficeId(diaryListVo.getDoctorsOfficeId());
        result.setDoctorsOfficeDescription(diaryListVo.getDoctorsOfficeDescription());
        result.setStartDate(diaryListVo.getStartDate());
        result.setEndDate(diaryListVo.getEndDate());
        result.setAppointmentDuration(diaryListVo.getAppointmentDuration());
        result.setAutomaticRenewal(diaryListVo.isAutomaticRenewal());
        result.setProfessionalAssignShift(diaryListVo.isProfessionalAssignShift());
        result.setIncludeHoliday(diaryListVo.isIncludeHoliday());
        result.setAlias(diaryListVo.getAlias());
        result.setClinicalSpecialtyName(diaryListVo.getClinicalSpecialtyName());
        result.setPredecessorProfessionalId(diaryListVo.getPredecessorProfessionalId());
        result.setHierarchicalUnitId(diaryListVo.getHierarchicalUnitId());
        result.setPractices(stringPractices);
        log.debug(OUTPUT, result);
        return result;
    }

    private CompleteDiaryBo createCompleteDiaryBoInstance(CompleteDiaryListVo completeDiaryListVo) {
        log.debug("Input parameters -> diaryListVo {}", completeDiaryListVo);
        CompleteDiaryBo result = new CompleteDiaryBo(createDiaryBoInstance(completeDiaryListVo));
        result.setSectorId(completeDiaryListVo.getSectorId());
        result.setSectorDescription(completeDiaryListVo.getSectorDescription());
        result.setClinicalSpecialtyId(completeDiaryListVo.getClinicalSpecialtyId());
        result.setHealthcareProfessionalId(completeDiaryListVo.getHealthcareProfessionalId());
        result.setDoctorsOfficeDescription(completeDiaryListVo.getDoctorsOfficeDescription());
        result.setDoctorFirstName(completeDiaryListVo.getDoctorFirstName());
        result.setDoctorLastName(completeDiaryListVo.getDoctorLastName());
        result.setDoctorMiddleNames(completeDiaryListVo.getDoctorMiddleNames());
        result.setDoctorOtherLastNames(completeDiaryListVo.getDoctorOtherLastNames());
        result.setDoctorNameSelfDetermination(completeDiaryListVo.getDoctorNameSelfDetermination());
        result.setHierarchicalUnitId(completeDiaryListVo.getHierarchicalUnitId());
        result.setHierarchicalUnitAlias(completeDiaryListVo.getHierarchicalUnitAlias());
        result.setPracticesInfo(new ArrayList<>());
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Optional<CompleteDiaryBo> getDiary(Integer diaryId) {
        log.debug(INPUT_DIARY_ID, diaryId);
        Optional<CompleteDiaryBo> result = diaryRepository.getDiary(diaryId).map(this::createCompleteDiaryBoInstance)
                .map(completeOpeningHours());
        result.ifPresent(completeDiaryBo -> {
            completeDiaryBo.setAssociatedProfessionalsInfo(diaryAssociatedProfessionalService.getAllDiaryAssociatedProfessionalsInfo(diaryId));
            completeDiaryBo.setCareLinesInfo(diaryCareLineService.getAllCareLinesByDiaryId(diaryId, completeDiaryBo.getHealthcareProfessionalId()));
            completeDiaryBo.setPracticesInfo(diaryPracticeService.getAllByDiaryId(diaryId));
        });
        log.debug(OUTPUT, result);
        return result;
    }

    private Function<CompleteDiaryBo, CompleteDiaryBo> completeOpeningHours() {
        return completeDiary -> {
            Collection<DiaryOpeningHoursBo> diaryOpeningHours = diaryOpeningHoursService
                    .getDiariesOpeningHours(Stream.of(completeDiary.getId()).collect(toList()));
            completeDiary.setDiaryOpeningHours(new ArrayList<>(diaryOpeningHours));
            return completeDiary;
        };
    }

    @Override
    public DiaryBo getDiaryById(Integer diaryId) {
        log.debug(INPUT_DIARY_ID, diaryId);
        Diary resultQuery = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NotFoundException("diaryId", "diaryId -> " + diaryId + " does not exist"));
        DiaryBo result = createDiaryBoInstance(resultQuery);
        result.setDiaryAssociatedProfessionalsId(diaryAssociatedProfessionalService.getAllDiaryAssociatedProfessionalsInfo(diaryId)
                .stream().map(ProfessionalPersonBo::getId)
                .collect(toList()));
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Integer getInstitution(Integer diaryId) {
        return diaryRepository.getInstitutionIdByDiary(diaryId)
                .orElseThrow(() -> new DiaryNotFoundException(DiaryNotFoundEnumException.DIARY_ID_NOT_FOUND, "La agenda solicitada no existe"));
    }

    private DiaryBo createDiaryBoInstance(Diary diary) {
        log.debug("Input parameters -> diary {}", diary);
        DiaryBo result = new DiaryBo();
        result.setId(diary.getId());
        result.setDoctorsOfficeId(diary.getDoctorsOfficeId());
        result.setStartDate(diary.getStartDate());
        result.setEndDate(diary.getEndDate());
        result.setAppointmentDuration(diary.getAppointmentDuration());
        result.setAutomaticRenewal(diary.isAutomaticRenewal());
        result.setProfessionalAssignShift(diary.isProfessionalAsignShift());
        result.setIncludeHoliday(diary.isIncludeHoliday());
        result.setHealthcareProfessionalId(diary.getHealthcareProfessionalId());
        result.setDeleted(diary.isDeleted());
        result.setAlias(diary.getAlias());
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Optional<DiaryBo> getDiaryByAppointment(Integer appointmentId) {
        log.debug("Input parameters -> appointmentId {}", appointmentId);
        Optional<DiaryBo> result = diaryRepository.getDiaryByAppointment(appointmentId).map(this::createDiaryBoInstance);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Integer getDiaryIdByAppointment(Integer appointmentId) {
        log.debug("Input parameters -> appointmentId {}", appointmentId);
        Integer result = diaryRepository.getDiaryByAppointment(appointmentId).map(Diary::getId).orElse(null);
        log.debug(OUTPUT, result);
        return result;
    }


    @Override
    public Optional<CompleteDiaryBo> getCompleteDiaryByAppointment(Integer appointmentId) {
        log.debug("Input parameters -> appointmentId {}", appointmentId);
        Optional<CompleteDiaryBo> result = diaryRepository.getCompleteDiaryByAppointment(appointmentId).map(this::createCompleteDiaryBoInstance);
        log.debug(OUTPUT, result);
        return result;

    }

    @Override
    public Boolean hasPractices(Integer diaryId) {
        log.debug("Input parameters -> diaryId {},", diaryId);
        return diaryPracticeService.hasPractice(diaryId);
    }

    @Override
    public Boolean hasActiveDiariesInInstitution(Integer healthcareProfessionalId, Integer institutionId) {
        log.debug("Input parameters -> healthcareProfessionalId {}, institutionId {}", healthcareProfessionalId, institutionId);
        Boolean result = diaryRepository.hasActiveDiariesInInstitution(healthcareProfessionalId, institutionId);
        log.debug(OUTPUT, result);
        return result;
    }

    @Override
    public List<String> getActiveDiariesAliases(Integer institutionId) {
        log.debug("Input parameters -> institutionId {}", institutionId);
        List<String> result = diaryRepository.getActiveDiariesAliases(institutionId);
        log.debug(OUTPUT, result);
        return result;
    }

	/**
	 * The aliases of the diaries (without repetitions) linked to the given institution and clinical specialty.
	 * Not all diares have an alias.
	 *
	 * @param institutionId
	 * @param clinicalSpecialtyId
	 * @param withPractices If true, only return aliases of diaries that have practices attached
	 * @return
	 */
	@Override
	public Set<ActiveDiaryAliasBo> getActiveDiariesAliasesByClinicalSpecialty(Integer institutionId, Integer clinicalSpecialtyId, Boolean withPractices) {
		log.debug("Input parameters -> institutionId {} clinicalSpecialtyId {}", institutionId, clinicalSpecialtyId);
		Boolean mustHavePractices = withPractices != null && withPractices;

		List<DiaryListVo> diaries;
		if (mustHavePractices)
			diaries = diaryRepository.getActiveDiariesWithPracticesByInstitutionAndSpecialty(institutionId, clinicalSpecialtyId);
		else
			diaries = diaryRepository.getActiveDiariesWithoutPracticesByInstitutionAndSpecialty(institutionId, clinicalSpecialtyId);

		var result = diaries
			.stream()
			.filter(diary -> Objects.nonNull(diary.getAlias()) && !diary.getAlias().isBlank())
			.map(diary -> new ActiveDiaryAliasBo(diary.getId(), diary.getAlias()))
			.collect(Collectors.toSet());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<ActiveDiaryClinicalSpecialtyBo> getActiveDiariesClinicalSpecialties(Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		var result = diaryRepository
			.getActiveDiariesClinicalSpecialties(institutionId)
			.stream()
			.map(cs -> new ActiveDiaryClinicalSpecialtyBo(cs.getId(), cs.getName()))
			.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

    @Override
    public List<EmptyAppointmentBo> getEmptyAppointmentsBySearchCriteria(Integer institutionId, AppointmentSearchBo searchCriteria, Boolean mustFilterByModality) {
        log.debug("Input parameters -> institutionId {}, searchCriteria {}, mustFilterByModality {}", institutionId, searchCriteria, mustFilterByModality);
        List<EmptyAppointmentBo> emptyAppointments = new ArrayList<>();
        validateSearchCriteria(searchCriteria);

        List<CompleteDiaryBo> diaries = getActiveDiariesBySearchCriteria(
        	institutionId,
        	searchCriteria.getAliasOrSpecialtyName(),
        	searchCriteria.getPracticeId(),
        	searchCriteria.getClinicalSpecialtyId(),
        	searchCriteria.getDiaryId()
		);

		if (mustFilterByModality && featureFlagsService.isOn(AppFeature.HABILITAR_TELEMEDICINA)) {
            this.filterAllDiariesByModality(searchCriteria, diaries);
		}

		LocalDateTime currentDateTime = dateTimeProvider.nowDateTimeWithZone(institutionExternalService.getTimezone(institutionId));
		for (CompleteDiaryBo diary: diaries)
			emptyAppointments = getEmptyAppointmentBos(searchCriteria, emptyAppointments, diary, currentDateTime);
		emptyAppointments.sort(Comparator.comparing(EmptyAppointmentBo::getDate).thenComparing(EmptyAppointmentBo::getHour));
		log.debug(OUTPUT, emptyAppointments);
        return emptyAppointments;
    }

    private void filterAllDiariesByModality(AppointmentSearchBo searchCriteria, List<CompleteDiaryBo> diaries) {
        diaries.forEach(diaryBo -> this.setOpeningHoursFilteringByModality(searchCriteria, diaryBo));
	}

	private void setOpeningHoursFilteringByModality(AppointmentSearchBo searchCriteria, CompleteDiaryBo diaryBo) {
		var openingHoursFiltered = diaryBo.getDiaryOpeningHours()
				.stream()
				.filter(openingHours -> this.isOnSiteOrPatientVirtualAttention(searchCriteria.getModality(), openingHours))
				.collect(toList());
		diaryBo.setDiaryOpeningHours(openingHoursFiltered);
	}

	private boolean isOnSiteOrPatientVirtualAttention(EAppointmentModality modality, DiaryOpeningHoursBo openingHours) {
		return (EAppointmentModality.ON_SITE_ATTENTION.equals(modality) && openingHours.getOnSiteAttentionAllowed())
                || (EAppointmentModality.PATIENT_VIRTUAL_ATTENTION.equals(modality) && openingHours.getPatientVirtualAttentionAllowed());
    }

    private List<EmptyAppointmentBo> getEmptyAppointmentBos(AppointmentSearchBo searchCriteria,
                                                            List<EmptyAppointmentBo> emptyAppointments,
                                                            CompleteDiaryBo diary,
                                                            LocalDateTime currentDateTime) {
        Collection<AppointmentBo> assignedAppointments = appointmentService.getAppointmentsByDiaries(List.of(diary.getId()), searchCriteria.getInitialSearchDate(), searchCriteria.getEndingSearchDate());
        List<EmptyAppointmentBo> availableAppointments = getDiaryAvailableAppointments(diary, searchCriteria, assignedAppointments, currentDateTime);
        emptyAppointments.addAll(availableAppointments);
        return emptyAppointments;
    }

	/**
	 * Fetches the active diaries that match the given condition.
	 * There are many ways of filtering the diaries. This method has a condition for each supported combination
	 * of parameters.
	 * @return
	 */
    private List<CompleteDiaryBo> getActiveDiariesBySearchCriteria(
    	Integer institutionId,
    	String aliasOrClinicalSpecialtyName,
    	Integer practiceId,
    	Integer clinicalSpecialtyId,
    	Integer diaryId
	) {
        log.debug("Input parameters -> institutionId {}, aliasOrClinicalSpecialtyName {}, practice {}", institutionId, aliasOrClinicalSpecialtyName, practiceId);

		//Specialty, diaryId and practice
		if (clinicalSpecialtyId != null && diaryId != null && practiceId != null) {
			return diaryRepository
				.getActiveDiariesByClinicalSpecialtyIdAndDiaryIdAndPracticeId(
					institutionId,
					clinicalSpecialtyId,
					diaryId,
					practiceId)
				.stream()
				.map(this::createCompleteDiaryBoInstanceWithPractice)
				.map(cd -> completeOpeningHoursByMedicalAttentionType(cd, MedicalAttentionType.PROGRAMMED))
				.collect(toList());
		}

		//Specialty and practice
		if (clinicalSpecialtyId != null && diaryId == null && practiceId != null) {
			return diaryRepository
					.getActiveDiariesByClinicalSpecialtyIdAndPracticeId(
							institutionId,
							clinicalSpecialtyId,
							practiceId)
					.stream()
					.map(this::createCompleteDiaryBoInstanceWithPractice)
					.map(cd -> completeOpeningHoursByMedicalAttentionType(cd, MedicalAttentionType.PROGRAMMED))
					.collect(toList());
		}

		//Specialty and diaryId
		if (clinicalSpecialtyId != null && diaryId != null && practiceId == null) {
			return diaryRepository
				.getActiveDiariesByClinicalSpecialtyIdAndDiaryId(
					institutionId,
					clinicalSpecialtyId,
					diaryId)
				.stream()
				.map(this::createCompleteDiaryBoInstanceWithPractice)
				.map(cd -> completeOpeningHoursByMedicalAttentionType(cd, MedicalAttentionType.PROGRAMMED))
				.collect(toList());
		}

		//Specialty
		if (clinicalSpecialtyId != null && diaryId == null && practiceId == null) {
			return diaryRepository
				.getActiveDiariesByClinicalSpecialtyId(
						institutionId,
						clinicalSpecialtyId)
				.stream()
				.map(this::createCompleteDiaryBoInstanceWithPractice)
				.map(cd -> completeOpeningHoursByMedicalAttentionType(cd, MedicalAttentionType.PROGRAMMED))
				.collect(toList());
		}

        if (aliasOrClinicalSpecialtyName != null && practiceId != null) {
            return diaryRepository.getActiveDiariesByAliasOrClinicalSpecialtyNameAndPracticeId(institutionId, aliasOrClinicalSpecialtyName, practiceId).stream()
                    .map(this::createCompleteDiaryBoInstanceWithPractice)
                    .map(cd -> completeOpeningHoursByMedicalAttentionType(cd, MedicalAttentionType.PROGRAMMED))
                    .collect(toList());
		}

        if (aliasOrClinicalSpecialtyName != null && practiceId == null) {
            return diaryRepository.getActiveDiariesByAliasOrClinicalSpecialtyName(institutionId, aliasOrClinicalSpecialtyName).stream()
                    .map(this::createCompleteDiaryBoInstance)
                    .map(cd -> completeOpeningHoursByMedicalAttentionType(cd, MedicalAttentionType.PROGRAMMED))
                    .collect(toList());
		}
		
		if (aliasOrClinicalSpecialtyName == null && practiceId != null) {
        	return diaryRepository.getActiveDiariesByPracticeId(institutionId, practiceId).stream()
                .map(this::createCompleteDiaryBoInstanceWithPractice)
                .map(cd -> completeOpeningHoursByMedicalAttentionType(cd, MedicalAttentionType.PROGRAMMED))
                .collect(toList());
		}
		return Collections.emptyList();
    }

    private List<EmptyAppointmentBo> getDiaryAvailableAppointments(CompleteDiaryBo diary,
                                                                   AppointmentSearchBo searchCriteria,
                                                                   Collection<AppointmentBo> assignedAppointments,
                                                                   LocalDateTime currentDateTime) {
        List<EmptyAppointmentBo> result = new ArrayList<>();
        Map<Short, Map<Integer, List<LocalTime>>> potentialAppointmentTimesByDay = new HashMap<>();
        diary.getDiaryOpeningHours().forEach(openingHours -> {
            if (searchCriteria.getDaysOfWeek().contains(openingHours.getOpeningHours().getDayWeekId())) {
                potentialAppointmentTimesByDay.computeIfAbsent(openingHours.getOpeningHours().getDayWeekId(), k -> new HashMap<>());
                potentialAppointmentTimesByDay.get(openingHours.getOpeningHours().getDayWeekId())
                        .put(openingHours.getOpeningHours().getId(), generateEmptyAppointmentsHoursFromOpeningHours(openingHours.getOpeningHours(), diary, searchCriteria));
            }
        });

        if (searchCriteria.getPracticeId() != null)
            setPractice(searchCriteria.getPracticeId(), diary);

        LocalDate searchInitialDate = searchCriteria.getInitialSearchDate();
        LocalDate searchEndingDate = searchCriteria.getEndingSearchDate();
        List<LocalDate> daysBetweenLimits = searchInitialDate.datesUntil(searchEndingDate).collect(Collectors.toList());
        daysBetweenLimits.add(searchEndingDate);
        daysBetweenLimits.forEach(day -> {
            if (day.compareTo(diary.getStartDate()) >= 0 && day.compareTo(diary.getEndDate()) <= 0 && result.size() < 20) {
                generateDayEmptyAppointments(diary, result, potentialAppointmentTimesByDay, day, assignedAppointments, currentDateTime);
            }
        });
        return result;
    }

    private void setPractice(Integer practiceId, CompleteDiaryBo diary) {
        List<SnomedBo> practiceToSet = new ArrayList<SnomedBo>();
        practiceToSet.add(snomedService.getSnomed(practiceId));
        diary.setPracticesInfo(practiceToSet);
    }

    private void generateDayEmptyAppointments(CompleteDiaryBo diary,
                                              List<EmptyAppointmentBo> result,
                                              Map<Short, Map<Integer, List<LocalTime>>> potentialAppointmentTimesByDay,
                                              LocalDate day,
                                              Collection<AppointmentBo> assignedAppointments,
                                              LocalDateTime currentDateTime) {
        int currentDayOfWeek = day.getDayOfWeek().getValue() == 7 ? 0 : day.getDayOfWeek().getValue();
        Map<Integer, List<LocalTime>> emptyAppointmentTimesOfCurrentDayOpeningHours = potentialAppointmentTimesByDay.get((short) currentDayOfWeek);
        if (emptyAppointmentTimesOfCurrentDayOpeningHours != null) {
            emptyAppointmentTimesOfCurrentDayOpeningHours.forEach((openingHoursId, openingHoursTimeList) ->
                    result.addAll(openingHoursTimeList.stream()
                            .filter(time -> {
                                if (day.compareTo(currentDateTime.toLocalDate()) > 0)
                                    return true;
                                else
                                    return time.compareTo(currentDateTime.toLocalTime()) > 0;
                            })
                            .map(time -> createEmptyAppointmentBoFromRawData(time, day, diary, openingHoursId))
                            .filter(emptyAppointment -> {
                                var time = emptyAppointment.getHour();
                                var date = emptyAppointment.getDate();
                                return assignedAppointments.stream().filter(appointment -> appointment.getDate().equals(date) && appointment.getHour().equals(time)).findAny().isEmpty();
                            })
                            .collect(Collectors.toList())));
        }
    }

    private List<LocalTime> generateEmptyAppointmentsHoursFromOpeningHours(OpeningHoursBo openingHours, CompleteDiaryBo diary, AppointmentSearchBo searchCriteria) {
        LocalTime searchCriteriaInitialTime = searchCriteria.getInitialSearchTime();
        long iterationAmount = ChronoUnit.MINUTES.between(searchCriteriaInitialTime, searchCriteria.getEndSearchTime()) / diary.getAppointmentDuration();
        List<LocalTime> generatedHours = new ArrayList<>();
        for (int currentEmptyAppointment = 0; currentEmptyAppointment < iterationAmount; currentEmptyAppointment++) {
            if (searchCriteriaInitialTime.compareTo(openingHours.getFrom()) >= 0 && searchCriteriaInitialTime.compareTo(openingHours.getTo()) < 0) {
                generatedHours.add(searchCriteriaInitialTime);
            }
            searchCriteriaInitialTime = searchCriteriaInitialTime.plusMinutes(diary.getAppointmentDuration());
        }
        return generatedHours;
    }

    private EmptyAppointmentBo createEmptyAppointmentBoFromRawData(LocalTime emptyAppointmentTime, LocalDate emptyAppointmentDate, CompleteDiaryBo diary, Integer openingHoursId) {
        EmptyAppointmentBo result = new EmptyAppointmentBo(diary.getDoctorLastName(), diary.getDoctorOtherLastNames(), diary.getDoctorFirstName(), diary.getDoctorMiddleNames(), diary.getDoctorNameSelfDetermination());
        result.setDiaryId(diary.getId());
        result.setDate(emptyAppointmentDate);
        result.setHour(emptyAppointmentTime);
        result.setOverturnMode(false);
        result.setPatientId(null);
        result.setOpeningHoursId(openingHoursId);
        result.setDoctorsOfficeDescription(diary.getDoctorsOfficeDescription());
        result.setClinicalSpecialtyName(diary.getClinicalSpecialtyName());
        result.setAlias(diary.getAlias());
        if (diary.hasPractices())
            result.setPractice(diary.getPracticesInfo().get(0));
        return result;
    }

    private void validateDiary(DiaryBo diaryBo) {
        if (diaryBo.getPredecessorProfessionalId() != null && diaryBo.getHierarchicalUnitId() == null)
            throw new DiaryException(DiaryEnumException.PREDECESSOR_PROFESSIONAL_WITHOUT_HIERARCHICAL_UNIT,
                    "No se puede ingresar un profesional a reemplazar sin seleccionar la unidad jerárquica a la que pertenece");
        diaryBo.getDiaryOpeningHours().forEach(openingHour -> {
            if (openingHour.getOnSiteAttentionAllowed() == null && openingHour.getPatientVirtualAttentionAllowed() == null && openingHour.getSecondOpinionVirtualAttentionAllowed() == null)
                throw new DiaryException(DiaryEnumException.MODALITY_NOT_FOUND, "Una de las franjas horarias no cuenta con una modalidad definida");
        });
    }

    private CompleteDiaryBo createCompleteDiaryBoInstanceWithPractice(CompleteDiaryListVo vo) {
        var bo = this.createCompleteDiaryBoInstance(vo);
        bo.setPracticesInfo(diaryPracticeService.getAllByDiaryId(vo.getId()));
        return bo;
    }

    private void validateSearchCriteria(AppointmentSearchBo searchCriteria) {
        if (searchCriteria.getClinicalSpecialtyId() == null && searchCriteria.getAliasOrSpecialtyName() == null && searchCriteria.getPracticeId() == null) {
            throw new DiaryException(DiaryEnumException.SEARCH_CRITERIA_NOT_FOUND,
                    "No se puede realizar la búsqueda sin seleccionar el tipo de atención ");
        }
    }

    private CompleteDiaryBo completeOpeningHoursByMedicalAttentionType(CompleteDiaryBo completeDiary, short medicalAttentionTypeId) {
        Collection<DiaryOpeningHoursBo> diaryOpeningHours = diaryOpeningHoursService
                .getDiariesOpeningHoursByMedicalAttentionType(Stream.of(completeDiary.getId()).collect(toList()), medicalAttentionTypeId);
        completeDiary.setDiaryOpeningHours(new ArrayList<>(diaryOpeningHours));
        return completeDiary;
    }

}
