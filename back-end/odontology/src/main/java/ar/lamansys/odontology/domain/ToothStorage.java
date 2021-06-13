package ar.lamansys.odontology.domain;

import java.util.List;
import java.util.Optional;

public interface ToothStorage {

    List<ToothBo> getAll();

    Optional<ToothBo> get(String toothId);
}
