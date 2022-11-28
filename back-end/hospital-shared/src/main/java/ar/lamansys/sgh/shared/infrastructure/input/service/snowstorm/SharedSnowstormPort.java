package ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm;


import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;

import java.util.List;

public interface SharedSnowstormPort {

    String mapSctidToNomivacId(String sctid) throws SnowstormPortException;

	SharedSnowstormSearchDto getConcepts(String term, String ecl) throws SnowstormPortException;

	SharedSnowstormSearchDto getConceptsByEcl(String ecl) throws SnowstormPortException;

	SharedSnowstormSearchDto getConcepts(List<Integer> ids) throws SnowstormPortException;

	SharedSnowstormSearchItemDto getConceptById(String conceptId) throws SnowstormPortException;

}
