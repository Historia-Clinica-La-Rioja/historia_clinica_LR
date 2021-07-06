package ar.lamansys.odontology.infrastructure.controller;

import ar.lamansys.odontology.application.diagnostic.GetDiagnosticsService;
import ar.lamansys.odontology.infrastructure.controller.dto.OdontologyConceptDto;
import ar.lamansys.odontology.infrastructure.controller.mapper.OdontologyConceptMapper;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/odontology/concepts")
@Api(value="Odontology concepts", tags= { "Odontology concepts" } )
public class OdontologyConceptsController {

    private static final Logger LOG = LoggerFactory.getLogger(OdontologyConceptsController.class);

    private final GetDiagnosticsService getDiagnosticsService;

    private final OdontologyConceptMapper odontologyConceptMapper;

    public OdontologyConceptsController(GetDiagnosticsService getDiagnosticsService,
                                        OdontologyConceptMapper odontologyConceptMapper) {
        this.getDiagnosticsService = getDiagnosticsService;
        this.odontologyConceptMapper = odontologyConceptMapper;
    }

    @GetMapping("/diagnostics")
    List<OdontologyConceptDto> getDiagnostics() {
        LOG.debug("No input parameters");
        List<OdontologyConceptDto> result = odontologyConceptMapper.fromDiagnosticBoList(getDiagnosticsService.run());
        LOG.debug("Output size -> {}", result.size());
        LOG.trace("Output -> {}", result);
        return result;
    }

}
