package ar.lamansys.odontology.domain;

import java.util.List;
import java.util.Optional;

public interface DiagnosticStorage {

    List<DiagnosticBo> getDiagnostics();

    Optional<DiagnosticBo> getDiagnostic(String sctid);

}
