package ar.lamansys.odontology.infrastructure.repository.document.mapper;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.consultation.OdontologyDocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface OdontologyDocumentMapper {

    @Named("fromOdontologyDocumentBo")
    @Mapping(target = "diagnosis", source = "diagnostics")
    @Mapping(target = "notes.evolutionNote", source = "evolutionNote")
    DocumentDto fromOdontologyDocumentBo(OdontologyDocumentBo odontologyDocumentBo);

    @Named("fromOdontologySnomedBo")
    SnomedDto fromOdontologySnomedBo(OdontologySnomedBo odontologySnomedBo);

}
