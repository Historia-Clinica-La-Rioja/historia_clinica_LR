package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;

public interface SharedSnomedPort {

    List<Integer> addSnomedConcepts(List<SharedSnomedDto> snomedConcepts);

	List<Integer> addSnomedSynonyms(List<SharedSnomedDto> snomedSynonyms);

	SharedSnomedDto getSnomed(Integer id);

}
