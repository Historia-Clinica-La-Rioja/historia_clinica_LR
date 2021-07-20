package ar.lamansys.odontology.infrastructure.repository;

import java.util.List;

public interface ProcedureRepository {

    List<Object[]> getAll();

    List<Object[]> getBySctid(String sctid);
}
