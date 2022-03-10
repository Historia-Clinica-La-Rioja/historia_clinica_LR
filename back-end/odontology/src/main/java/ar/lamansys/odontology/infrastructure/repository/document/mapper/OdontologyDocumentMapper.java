package ar.lamansys.odontology.infrastructure.repository.document.mapper;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.consultation.OdontologyDocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;

@Mapper(uses = {LocalDateMapper.class})
public interface OdontologyDocumentMapper {

    @Named("fromOdontologyDocumentBo")
    @Mapping(target = "problems", source = "diagnostics")
    @Mapping(target = "notes.evolutionNote", source = "evolutionNote")
    DocumentDto fromOdontologyDocumentBo(OdontologyDocumentBo odontologyDocumentBo);

    @AfterMapping
    default void mapNullValuesToEmptyLists(@MappingTarget DocumentDto target, OdontologyDocumentBo source){
        if (target.getDentalActions() == null)
            target.setDentalActions(new ArrayList<>());
        if (target.getReasons() == null)
            target.setReasons(new ArrayList<>());
        if (target.getDiagnosis() == null)
            target.setDiagnosis(new ArrayList<>());
        if (target.getProcedures() == null)
            target.setProcedures(new ArrayList<>());
        if (target.getPersonalHistories() == null)
            target.setPersonalHistories(new ArrayList<>());
        if (target.getAllergies() == null)
            target.setAllergies(new ArrayList<>());
    }

    @Named("fromOdontologySnomedBo")
    SnomedDto fromOdontologySnomedBo(OdontologySnomedBo odontologySnomedBo);

}
