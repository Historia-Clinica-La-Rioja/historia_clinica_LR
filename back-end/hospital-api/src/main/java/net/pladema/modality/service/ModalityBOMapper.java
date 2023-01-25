package net.pladema.modality.service;


import net.pladema.modality.controller.dto.ModalityDto;

import net.pladema.modality.service.domain.ModalityBO;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ModalityBOMapper {

	@Named("toModalityDto")
	ModalityDto toModalityDto(ModalityBO modalityBO);

	@Named("toListModalityDto")
	List<ModalityDto> toListModalityDto(List<ModalityBO> modalitiesBo);
}
