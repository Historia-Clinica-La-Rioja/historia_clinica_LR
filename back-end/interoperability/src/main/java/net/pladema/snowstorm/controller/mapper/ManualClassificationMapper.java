package net.pladema.snowstorm.controller.mapper;

import jdk.jfr.Name;
import net.pladema.snowstorm.controller.dto.ManualClassificationDto;
import net.pladema.snowstorm.services.domain.ManualClassificationBo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ManualClassificationMapper {

	@Name("fromManualClassificationBo")
	ManualClassificationDto fromManualClassificationBo(ManualClassificationBo manualClassificationBo);

	@Named("fromManualClassificationBoList")
	@IterableMapping(qualifiedByName = "fromManualClassificationBo")
	List<ManualClassificationDto> fromManualClassificationBoList(List<ManualClassificationBo> manualClassificationBos);
}
