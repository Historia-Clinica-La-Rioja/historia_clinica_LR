package net.pladema.internation.service.internment.impl;

import net.pladema.internation.repository.documents.EvolutionNoteDocumentRepository;
import net.pladema.internation.repository.documents.PatientDischargeRepository;
import net.pladema.internation.repository.documents.entity.EvolutionNoteDocument;
import net.pladema.internation.repository.documents.entity.InternmentEpisode;
import net.pladema.internation.repository.documents.entity.PatientDischarge;
import net.pladema.internation.repository.internment.InternmentEpisodeRepository;
import net.pladema.internation.repository.internment.domain.summary.EvaluationNoteSummaryVo;
import net.pladema.internation.repository.internment.domain.summary.InternmentSummaryVo;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.internation.service.internment.summary.domain.InternmentSummaryBo;
import net.pladema.internation.service.internment.summary.domain.PatientDischargeBo;
import net.pladema.sgx.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class InternmentEpisodeServiceImpl implements InternmentEpisodeService {


	private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeServiceImpl.class);

	private static final String INPUT_PARAMETERS_INTERNMENT_EPISODE = "Input parameters -> internmentEpisodeId {}";
	private static final String INPUT_PARAMETERS = "Input parameters -> {}";
	private static final String INTERNMENT_NOT_FOUND = "internmentepisode.not.found";
	private static final String LOGGING_OUTPUT = "Output -> {}";
	private static final short ACTIVO = 1;

	private final InternmentEpisodeRepository internmentEpisodeRepository;

	private final EvolutionNoteDocumentRepository evolutionNoteDocumentRepository;

	private final PatientDischargeRepository patientDischargeRepository;

	public InternmentEpisodeServiceImpl(InternmentEpisodeRepository internmentEpisodeRepository,
			EvolutionNoteDocumentRepository evolutionNoteDocumentRepository,
			PatientDischargeRepository patientDischargeRepository) {
		this.internmentEpisodeRepository = internmentEpisodeRepository;
		this.evolutionNoteDocumentRepository = evolutionNoteDocumentRepository;
		this.patientDischargeRepository = patientDischargeRepository;
	}

	@Override
	public void updateAnamnesisDocumentId(Integer internmentEpisodeId, Long anamnesisDocumentId) {
        LOG.debug("Input parameters -> internmentEpisodeId {}, anamnesisDocumentId {}", internmentEpisodeId, anamnesisDocumentId);
        internmentEpisodeRepository.updateAnamnesisDocumentId(internmentEpisodeId, anamnesisDocumentId, LocalDateTime.now());
	}

	@Override
	public void updateEpicrisisDocumentId(Integer internmentEpisodeId, Long epicrisisId) {
		LOG.debug("Input parameters -> internmentEpisodeId {}, epicrisisId {}", internmentEpisodeId, epicrisisId);
		internmentEpisodeRepository.updateEpicrisisDocumentId(internmentEpisodeId, epicrisisId, LocalDateTime.now());
	}

	@Override
	public EvolutionNoteDocument addEvolutionNote(Integer internmentEpisodeId, Long evolutionNoteId) {
		LOG.debug("Input parameters -> internmentEpisodeId {}, evolutionNoteId {}", internmentEpisodeId,
				evolutionNoteId);
		EvolutionNoteDocument result = new EvolutionNoteDocument(evolutionNoteId, internmentEpisodeId);
		result = evolutionNoteDocumentRepository.save(result);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public InternmentEpisode addInternmentEpisode(InternmentEpisode internmentEpisode, Integer institutionId) {
		LOG.debug("Input parameters -> internmentEpisode {}, institutionId {}", internmentEpisode, institutionId);
		internmentEpisode.setInstitutionId(institutionId);
		internmentEpisode.setStatusId(ACTIVO);
		internmentEpisode.setEntryDate(LocalDate.now());
		InternmentEpisode result = internmentEpisodeRepository.save(internmentEpisode);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean haveAnamnesis(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		boolean result = internmentEpisodeRepository.haveAnamnesis(internmentEpisodeId);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean haveEpicrisis(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		boolean result = internmentEpisodeRepository.haveEpicrisis(internmentEpisodeId);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public LocalDate getEntryDate(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		LocalDate result = internmentEpisodeRepository.getEntryDate(internmentEpisodeId);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public boolean haveAnamnesisAndEvolutionNote(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS_INTERNMENT_EPISODE, internmentEpisodeId);
		boolean result = internmentEpisodeRepository.haveAnamnesisAndEvolutionNote(internmentEpisodeId);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public Optional<InternmentSummaryBo> getIntermentSummary(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS, internmentEpisodeId);
		Optional<InternmentSummaryVo> resultQuery = internmentEpisodeRepository.getSummary(internmentEpisodeId);
		AtomicReference<Optional<InternmentSummaryBo>> result = new AtomicReference<>(Optional.empty());
		resultQuery.ifPresent(r -> {
			r.getDocuments().setLastEvaluationNote(getLastEvaluationNoteSummary(internmentEpisodeId).orElse(new EvaluationNoteSummaryVo()));
			result.set(Optional.of(new InternmentSummaryBo(r)));

		});
		LOG.debug(LOGGING_OUTPUT, result);
		return result.get();
	}

    private Optional<EvaluationNoteSummaryVo> getLastEvaluationNoteSummary(Integer internmentEpisodeId){
		LOG.debug(INPUT_PARAMETERS, internmentEpisodeId);
        Page<EvaluationNoteSummaryVo> resultQuery = evolutionNoteDocumentRepository.getLastEvaluationNoteSummary(internmentEpisodeId, PageRequest.of(0, 1));
		Optional<EvaluationNoteSummaryVo> result = resultQuery.getContent().stream().findFirst();
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public Optional<Integer> getPatient(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS, internmentEpisodeId);
		Optional<Integer> result = internmentEpisodeRepository.getPatient(internmentEpisodeId);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public PatientDischargeBo savePatientDischarge(PatientDischargeBo patientDischargeBo) {
		LOG.debug(INPUT_PARAMETERS, patientDischargeBo);
		return patientDischargeRepository.findById(patientDischargeBo.getInternmentEpisodeId()).map(pd -> {
			PatientDischargeBo result = new PatientDischargeBo(updatePatientDischarge(pd, patientDischargeBo));
			LOG.debug(LOGGING_OUTPUT, result);
			return result;
		}).orElseGet(() -> {
			PatientDischarge entityResult = patientDischargeRepository.save(new PatientDischarge(patientDischargeBo));
			PatientDischargeBo result = new PatientDischargeBo(entityResult);
			LOG.debug(LOGGING_OUTPUT, result);
			return result;
		});
	}

	private PatientDischarge updatePatientDischarge(PatientDischarge patientDischarge, PatientDischargeBo patientDischargeBo) {
		LOG.debug(INPUT_PARAMETERS, patientDischargeBo, patientDischarge);
		patientDischarge.setInternmentEpisodeId(patientDischargeBo.getInternmentEpisodeId());
		patientDischarge.setDischargeTypeId(patientDischargeBo.getDischargeTypeId());
		patientDischarge.setAdministrativeDischargeDate(patientDischargeBo.getAdministrativeDischargeDate());
		patientDischarge = patientDischargeRepository.save(patientDischarge);
		LOG.debug(LOGGING_OUTPUT, patientDischarge);
		return patientDischarge;
	}

	@Override
	public void updateInternmentEpisodeSatus(Integer internmentEpisodeId, Short statusId) {
		LOG.debug("Input parameters -> {}, {}", internmentEpisodeId, statusId);
		InternmentEpisode internmentEpisode = internmentEpisodeRepository.findById(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("wrong-id-episode", INTERNMENT_NOT_FOUND));
		internmentEpisode.setStatusId(statusId);
		internmentEpisodeRepository.save(internmentEpisode);
	}
	
	@Override
	public List<InternmentEpisode> findByBedId(Integer bedId) {
		return internmentEpisodeRepository.findByBedId(bedId);
	}


	@Override
	public InternmentEpisode getInternmentEpisode(Integer internmentEpisodeId, Integer institutionId) {
		InternmentEpisode internmentEpisode = internmentEpisodeRepository.getInternmentEpisode(internmentEpisodeId,institutionId)
				.orElseThrow(() -> new NotFoundException("wrong-id-episode", INTERNMENT_NOT_FOUND));
		return internmentEpisode;
	}
	
	@Override
	public Boolean existsActiveForBedId(Integer bedId) {
		List<InternmentEpisode> episodes = this.findByBedId(bedId);
		return episodes != null && !episodes.isEmpty() && anyActive(episodes);
	}

	private boolean anyActive(List<InternmentEpisode> episodes) {
		return episodes.stream().anyMatch(InternmentEpisode::isActive);
	}

}
