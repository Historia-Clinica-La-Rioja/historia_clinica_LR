package ar.lamansys.odontology.application.diagnostic;

import ar.lamansys.odontology.domain.DiagnosticBo;

import java.util.List;

public interface GetDiagnosticsService {

    List<DiagnosticBo> run();

}
