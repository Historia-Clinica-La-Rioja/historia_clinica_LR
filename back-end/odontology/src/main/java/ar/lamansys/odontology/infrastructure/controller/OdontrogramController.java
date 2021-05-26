package ar.lamansys.odontology.infrastructure.controller;

import ar.lamansys.odontology.application.odontogram.GetOdontogramService;
import ar.lamansys.odontology.infrastructure.controller.dto.OdontogramQuadrantDto;
import ar.lamansys.odontology.infrastructure.controller.mapper.OdontogramQuadrantMapper;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontology/odontogram")
@Api(value="Odontologram", tags= { "Odontologram" } )
public class OdontrogramController {
    private final Logger LOG;

    private final OdontogramQuadrantMapper odontogramQuadrantMapper;
    private final GetOdontogramService getOdontogramService;

    public OdontrogramController(OdontogramQuadrantMapper odontogramQuadrantMapper, GetOdontogramService getOdontogramService) {
        this.odontogramQuadrantMapper = odontogramQuadrantMapper;
        this.getOdontogramService = getOdontogramService;
        this.LOG = LoggerFactory.getLogger(getClass());
    }

    @GetMapping()
    @ResponseBody
    public List<OdontogramQuadrantDto> getOdontogram() {
        List<OdontogramQuadrantDto> result = odontogramQuadrantMapper.parseToOdontongramQuadrantDtoList(getOdontogramService.run());
        LOG.debug("Output -> {}", result);
        return result;
    }
}