package ar.lamansys.odontology.infrastructure.controller;

import ar.lamansys.odontology.application.diagnostic.GetDiagnosticsService;
import ar.lamansys.odontology.application.procedure.GetProceduresService;
import ar.lamansys.odontology.infrastructure.controller.dto.OdontologyConceptDto;
import ar.lamansys.odontology.infrastructure.controller.mapper.OdontologyConceptMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/odontology/concepts")
@Tag(name = "Odontology concepts", description = "Odontology concepts")
public class OdontologyConceptsController {

    private static final Logger LOG = LoggerFactory.getLogger(OdontologyConceptsController.class);

    private final GetDiagnosticsService getDiagnosticsService;

    private final GetProceduresService getProceduresService;

    private final OdontologyConceptMapper odontologyConceptMapper;

    public OdontologyConceptsController(GetDiagnosticsService getDiagnosticsService,
                                        GetProceduresService getProceduresService,
                                        OdontologyConceptMapper odontologyConceptMapper) {
        this.getDiagnosticsService = getDiagnosticsService;
        this.getProceduresService = getProceduresService;
        this.odontologyConceptMapper = odontologyConceptMapper;
    }

    @GetMapping("/diagnostics")
    public List<OdontologyConceptDto> getDiagnostics() {
        LOG.debug("No input parameters");
        List<OdontologyConceptDto> result = odontologyConceptMapper.fromDiagnosticBoList(getDiagnosticsService.run());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

    @GetMapping("/procedures")
    public List<OdontologyConceptDto> getProcedures() {
        LOG.debug("No input parameters");
        List<OdontologyConceptDto> result = odontologyConceptMapper.fromProcedureBoList(getProceduresService.run());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

}
