package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.PostAnesthesiaStatusBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.PostAnesthesiaStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface PostAnesthesiaStatusMapper {

    @Named("toPostAnesthesiaStatusDto")
    PostAnesthesiaStatusDto toPostAnesthesiaStatusDto(PostAnesthesiaStatusBo PostAnesthesiaStatusBo);

    @Named("toPostAnesthesiaStatusBo")
    PostAnesthesiaStatusBo toPostAnesthesiaStatusBo(PostAnesthesiaStatusDto PostAnesthesiaStatusDto);
}
