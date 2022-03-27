package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.List;

public interface SharedSnomedPort {

    List<Integer> addSnomedConcepts(List<SharedSnomedDto> snomedConcepts);

}
