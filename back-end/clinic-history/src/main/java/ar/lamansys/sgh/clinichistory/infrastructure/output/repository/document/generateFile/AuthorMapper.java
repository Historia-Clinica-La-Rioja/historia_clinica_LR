package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.clinichistory.domain.document.AuthorBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper
public interface AuthorMapper {

    @Named("toAuthorDto")
    AuthorDto toAuthorDto(AuthorBo authorBo);
}
