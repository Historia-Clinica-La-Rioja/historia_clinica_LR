package ar.lamansys.refcounterref.infraestructure.output.repository.document.mapper;

import ar.lamansys.refcounterref.domain.document.CounterReferenceDocumentBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto.DocumentDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;

@Mapper(uses = {LocalDateMapper.class})
public interface CounterReferenceDocumentMapper {

    @Named("fromCounterReferenceDocumentBo")
    @Mapping(target = "notes.evolutionNote", source = "counterReferenceNote")
    @Mapping(target = "procedures", source = "procedures")
    @Mapping(target = "medications", source = "medications")
    @Mapping(target = "allergies", source = "allergies")
    DocumentDto fromCounterReferenceDocumentBo(CounterReferenceDocumentBo counterReferenceDocumentBo);

    @AfterMapping
    default void mapNullValuesToEmptyLists(@MappingTarget DocumentDto target, CounterReferenceDocumentBo source){
        if (target.getProcedures() == null)
            target.setProcedures(new ArrayList<>());
        if (target.getMedications() == null)
            target.setMedications(new ArrayList<>());
        if (target.getAllergies() == null)
            target.setAllergies(new ArrayList<>());
    }

}