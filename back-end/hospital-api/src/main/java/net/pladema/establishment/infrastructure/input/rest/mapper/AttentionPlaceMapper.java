package net.pladema.establishment.infrastructure.input.rest.mapper;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import net.pladema.establishment.domain.FetchAttentionPlaceBlockStatusBo;
import net.pladema.establishment.infrastructure.input.rest.dto.FetchAttentionPlaceStatusDto;

import net.pladema.staff.controller.mapper.HealthcareProfessionalMapper;

import org.mapstruct.Mapper;

@Mapper(uses = {HealthcareProfessionalMapper.class, LocalDateMapper.class})
public interface AttentionPlaceMapper {

	FetchAttentionPlaceStatusDto toDto(FetchAttentionPlaceBlockStatusBo bo);
}
