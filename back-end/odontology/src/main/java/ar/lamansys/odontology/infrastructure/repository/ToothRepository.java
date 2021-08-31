package ar.lamansys.odontology.infrastructure.repository;

import java.util.List;

public interface ToothRepository {
    List<Object[]> getAll();

    Object[] get(String toothId);
}
