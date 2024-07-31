package net.pladema.emergencycare.infrastructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.application.port.output.HistoricEmergencyEpisodeStorage;
import net.pladema.emergencycare.domain.EmergencyCareEpisodeAttentionPlaceBo;
import net.pladema.emergencycare.repository.HistoricEmergencyEpisodeRepository;
import net.pladema.emergencycare.repository.entity.HistoricEmergencyEpisode;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HistoricEmergencyEpisodeStorageImpl implements HistoricEmergencyEpisodeStorage {

	private final HistoricEmergencyEpisodeRepository historicEmergencyEpisodeRepository;

	@Override
	public HistoricEmergencyEpisodeBo create(HistoricEmergencyEpisodeBo historicEmergencyEpisodeBo) {
		log.debug("Input create parameter -> historicEmergencyEpisodeBo {}", historicEmergencyEpisodeBo);
		HistoricEmergencyEpisodeBo result = mapToBo(
				historicEmergencyEpisodeRepository.save(new HistoricEmergencyEpisode(historicEmergencyEpisodeBo))
		);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public LocalDateTime getLatestChangeStateDateByEpisodeId(Integer episodeId) {
		log.debug("Input getLatestByEmergencyCareEpisodeId parameter -> episodeId {}", episodeId);
		Optional<HistoricEmergencyEpisode> hee = historicEmergencyEpisodeRepository.findLatestByEmergencyCareEpisodeId(episodeId);
		LocalDateTime result = hee.map(HistoricEmergencyEpisode::getChangeStateDate).orElse(null);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Optional<HistoricEmergencyEpisodeBo> getLatestByEpisodeId(Integer episodeId) {
		log.debug("Input getLatestByEpisodeId parameter -> episodeId {}", episodeId);
		Optional<HistoricEmergencyEpisode> hee = historicEmergencyEpisodeRepository.findLatestByEmergencyCareEpisodeId(episodeId);
		Optional<HistoricEmergencyEpisodeBo> result = hee.map(this::mapToBo);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public EmergencyCareEpisodeAttentionPlaceBo getLatestAttentionPlaceByEpisodeId(Integer episodeId) {
		log.debug("Input getLatestAttentionPlaceByEpisodeId parameter -> episodeId {}", episodeId);
		Optional<HistoricEmergencyEpisodeBo> hee = historicEmergencyEpisodeRepository.findLatestByEmergencyCareEpisodeId(episodeId)
				.map(this::mapToBo);
		EmergencyCareEpisodeAttentionPlaceBo result = hee.map(EmergencyCareEpisodeAttentionPlaceBo::new).orElse(null);
		log.debug("Output -> {}", result);
		return result;
	}

	private HistoricEmergencyEpisodeBo mapToBo(HistoricEmergencyEpisode historicEmergencyEpisode) {
		return HistoricEmergencyEpisodeBo.builder()
				.emergencyCareEpisodeId(historicEmergencyEpisode.getPk().getEmergencyCareEpisodeId())
				.changeStateDate(historicEmergencyEpisode.getChangeStateDate())
				.emergencyCareStateId(historicEmergencyEpisode.getEmergencyCareStateId())
				.shockroomId(historicEmergencyEpisode.getShockroomId())
				.doctorsOfficeId(historicEmergencyEpisode.getDoctorsOfficeId())
				.bedId(historicEmergencyEpisode.getBedId())
				.calls(historicEmergencyEpisode.getCalls())
				.build();
	}
}
