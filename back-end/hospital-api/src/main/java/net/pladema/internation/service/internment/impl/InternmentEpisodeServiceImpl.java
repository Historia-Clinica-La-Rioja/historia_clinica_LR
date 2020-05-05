package net.pladema.internation.service.internment.impl;

import net.pladema.internation.repository.core.InternmentEpisodeRepository;
import net.pladema.internation.repository.core.domain.InternmentSummary;
import net.pladema.internation.service.internment.InternmentEpisodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InternmentEpisodeServiceImpl implements InternmentEpisodeService {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentEpisodeServiceImpl.class);

    private final InternmentEpisodeRepository internmentEpisodeRepository;

    public InternmentEpisodeServiceImpl(InternmentEpisodeRepository internmentEpisodeRepository) {
        this.internmentEpisodeRepository = internmentEpisodeRepository;
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
    public void addEvaluationNote(Integer intermentEpisodeId, Long id) {
        //TODO Add a la tabla EvaluationNoteDocument una entrada nueva.
    }

    @Override
    public Optional<InternmentSummary> getIntermentSummary(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> {}", internmentEpisodeId);
        Optional<InternmentSummary> result = internmentEpisodeRepository.getSummary(internmentEpisodeId);
        LOG.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<Integer> getPatient(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> {}", internmentEpisodeId);
        Optional<Integer> result = internmentEpisodeRepository.getPatient(internmentEpisodeId);
        LOG.debug("Output -> {}", result);
        return result;
    }


}
