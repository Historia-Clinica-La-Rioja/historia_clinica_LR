package ar.lamansys.refcounterref.infraestructure.input.service.mapper;

import ar.lamansys.refcounterref.domain.referenceproblem.ReferenceProblemBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceProblemDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ReferenceProblemMapper {

    @Named("fromReferenceProblemBo")
    ReferenceProblemDto fromReferenceProblemBo(ReferenceProblemBo referenceProblemBo);

    @Named("fromReferenceProblemBoList")
    @IterableMapping(qualifiedByName = "fromReferenceProblemBo")
    List<ReferenceProblemDto> fromReferenceProblemBoList(List<ReferenceProblemBo> referenceBoList);

}
