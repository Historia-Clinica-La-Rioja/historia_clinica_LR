package ar.lamansys.odontology.infrastructure.repository;

import java.util.List;

public interface DiagnosticRepository {

    List<Object[]> getAll();

    List<Object[]> getBySctid(String sctid);

}
