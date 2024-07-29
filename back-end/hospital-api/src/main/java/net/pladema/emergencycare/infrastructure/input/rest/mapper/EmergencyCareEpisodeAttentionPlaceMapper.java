package net.pladema.emergencycare.infrastructure.input.rest.mapper;

import net.pladema.emergencycare.domain.EmergencyCareEpisodeAttentionPlaceBo;
import net.pladema.emergencycare.infrastructure.input.rest.dto.EmergencyCareEpisodeAttentionPlaceDto;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface EmergencyCareEpisodeAttentionPlaceMapper {

	@Named("fromDto")
	EmergencyCareEpisodeAttentionPlaceBo fromDto(EmergencyCareEpisodeAttentionPlaceDto emergencyCareEpisodeAttentionPlaceDto);
}
