package net.pladema.establishment.controller.mapper;

import net.pladema.establishment.controller.dto.InstitutionalGroupDto;
import net.pladema.establishment.service.domain.InstitutionalGroupBo;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface InstitutionalGroupMapper {

	@Named("toListInstitutionalGroupDto")
	List<InstitutionalGroupDto> toListInstitutionalGroupDto(List<InstitutionalGroupBo> institutionalGroupBo);

}
