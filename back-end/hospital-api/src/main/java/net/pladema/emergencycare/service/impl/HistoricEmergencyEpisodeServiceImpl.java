package net.pladema.emergencycare.service.impl;

import net.pladema.emergencycare.repository.HistoricEmergencyEpisodeRepository;
import net.pladema.emergencycare.repository.entity.HistoricEmergencyEpisode;
import net.pladema.emergencycare.service.HistoricEmergencyEpisodeService;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HistoricEmergencyEpisodeServiceImpl implements HistoricEmergencyEpisodeService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoricEmergencyEpisodeServiceImpl.class);

    private final HistoricEmergencyEpisodeRepository historicEmergencyEpisodeRepository;

    public HistoricEmergencyEpisodeServiceImpl(HistoricEmergencyEpisodeRepository historicEmergencyEpisodeRepository) {
        this.historicEmergencyEpisodeRepository = historicEmergencyEpisodeRepository;
    }

    @Override
    public HistoricEmergencyEpisodeBo saveChange(HistoricEmergencyEpisodeBo historicEmergencyEpisodeBo) {
        LOG.debug("Input parameter -> historicEmergencyEpisodeBo {}", historicEmergencyEpisodeBo);
        HistoricEmergencyEpisode toSave = new HistoricEmergencyEpisode(historicEmergencyEpisodeBo);
        toSave = historicEmergencyEpisodeRepository.save(toSave);
        historicEmergencyEpisodeBo.setChangeStateDate(toSave.getChangeStateDate());
        LOG.debug("Output -> {}", historicEmergencyEpisodeBo);
        return historicEmergencyEpisodeBo;
    }
}
