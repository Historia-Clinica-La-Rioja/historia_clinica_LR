package net.pladema.internation.service.impl;

import net.pladema.internation.repository.core.InternmentEpisodeRepository;
import net.pladema.internation.repository.core.domain.InternmentSummary;
import net.pladema.internation.service.InternmentEpisodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InternmentEpisodeServiceImpl implements InternmentEpisodeService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final InternmentEpisodeRepository internmentEpisodeRepository;

    public InternmentEpisodeServiceImpl(InternmentEpisodeRepository internmentEpisodeRepository) {
        this.internmentEpisodeRepository = internmentEpisodeRepository;
    }


    @Override
    public Optional<InternmentSummary> getIntermentSummary(Integer internmentEpisodeId) {
        LOG.debug("Input parameters -> {}", internmentEpisodeId);
        Optional<InternmentSummary> result = internmentEpisodeRepository.getSummary(internmentEpisodeId);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
