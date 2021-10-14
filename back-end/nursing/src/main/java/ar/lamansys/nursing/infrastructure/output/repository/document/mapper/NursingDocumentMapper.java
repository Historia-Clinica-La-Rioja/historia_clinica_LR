package ar.lamansys.nursing.infrastructure.output.repository.document.mapper;

import ar.lamansys.nursing.domain.document.NursingDocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;

@Mapper
public interface NursingDocumentMapper {

    @Named("fromNursingDocumentBo")
    @Mapping(target = "problems", source = "problems")
    @Mapping(target = "notes.evolutionNote", source = "evolutionNote")
    @Mapping(target = "procedures", source = "procedures")
    @Mapping(target = "anthropometricData", source = "anthropometricData")
    @Mapping(target = "vitalSigns", source = "vitalSigns")
    DocumentDto fromNursingDocumentBo(NursingDocumentBo nursingDocumentBo);

    @AfterMapping
    default void mapNullValuesToEmptyLists(@MappingTarget DocumentDto target, NursingDocumentBo source){
        if (target.getProcedures() == null)
            target.setProcedures(new ArrayList<>());
    }
}
