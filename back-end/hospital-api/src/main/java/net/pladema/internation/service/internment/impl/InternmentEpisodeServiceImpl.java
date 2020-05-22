package net.pladema.internation.service.internment.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.pladema.internation.repository.core.EvolutionNoteDocumentRepository;
import net.pladema.internation.repository.core.InternmentEpisodeRepository;
import net.pladema.internation.repository.core.PatientDischargeRepository;
import net.pladema.internation.repository.core.domain.EvaluationNoteSummaryVo;
import net.pladema.internation.repository.core.domain.InternmentSummaryVo;
import net.pladema.internation.repository.core.entity.EvolutionNoteDocument;
import net.pladema.internation.repository.core.entity.InternmentEpisode;
import net.pladema.internation.repository.core.entity.PatientDischarge;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import net.pladema.sgx.exceptions.NotFoundException;

@Service
public class InternmentEpisodeServiceImpl implements InternmentEpisodeService {

	private static final String INPUT_PARAMETERS_INTERNMENT_EPISODE = "Input parameters -> internmentEpisodeId {}";
	public static final String INPUT_PARAMETERS = "Input parameters -> {}";
	private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeServiceImpl.class);

	private static final String LOGGING_OUTPUT = "Output -> {}";

	private final InternmentEpisodeRepository internmentEpisodeRepository;

	private final EvolutionNoteDocumentRepository evolutionNoteDocumentRepository;

	private final PatientDischargeRepository patientDischargeRepository;

	private static final short ACTIVO = 1;

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
	public Optional<InternmentSummaryVo> getIntermentSummary(Integer internmentEpisodeId) {
		LOG.debug(INPUT_PARAMETERS, internmentEpisodeId);
		Optional<InternmentSummaryVo> result = internmentEpisodeRepository.getSummary(internmentEpisodeId);
        result.ifPresent(r ->
            r.getDocuments().setLastEvaluationNote(getLastEvaluationNoteSummary(internmentEpisodeId).orElse(null))
        );
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
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
	public PatientDischarge addPatientDischarge(PatientDischarge patientDischarge) {
		LOG.debug(INPUT_PARAMETERS, patientDischarge);
		PatientDischarge result = patientDischargeRepository.save(patientDischarge);
		LOG.debug(LOGGING_OUTPUT, result);
		return result;
	}

	@Override
	public void updateInternmentEpisodeSatus(Integer internmentEpisodeId, Short statusId) {
		LOG.debug("Input parameters -> {}, {}", internmentEpisodeId, statusId);
		InternmentEpisode internmentEpisode = internmentEpisodeRepository.findById(internmentEpisodeId)
				.orElseThrow(() -> new NotFoundException("wrong-id-episode", "Internment episode not found"));
		internmentEpisode.setStatusId(statusId);
		internmentEpisodeRepository.save(internmentEpisode);
	}

	@Override
	public InternmentEpisode getInternmentEpisode(Integer internmentEpisodeId, Integer institutionId) {
		InternmentEpisode internmentEpisode = internmentEpisodeRepository.getInternmentEpisode(internmentEpisodeId,institutionId)
				.orElseThrow(() -> new NotFoundException("wrong-id-episode", "Internment episode not found"));
		return internmentEpisode;
	}

}
