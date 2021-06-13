package ar.lamansys.odontology.infrastructure.controller;

import ar.lamansys.odontology.application.odontogram.GetOdontogramService;
import ar.lamansys.odontology.application.odontogram.GetToothSurfacesService;
import ar.lamansys.odontology.infrastructure.controller.dto.OdontogramQuadrantDto;
import ar.lamansys.odontology.infrastructure.controller.dto.ToothSurfacesDto;
import ar.lamansys.odontology.infrastructure.controller.mapper.OdontogramQuadrantMapper;
import ar.lamansys.odontology.infrastructure.controller.mapper.ToothSurfacesMapper;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontology/odontogram")
@Api(value="Odontologram", tags= { "Odontologram" } )
public class OdontrogramController {
    private final Logger logger;

    private final OdontogramQuadrantMapper odontogramQuadrantMapper;
    private final ToothSurfacesMapper toothSurfacesMapper;
    private final GetOdontogramService getOdontogramService;
    private final GetToothSurfacesService getToothSurfacesService;

    public OdontrogramController(OdontogramQuadrantMapper odontogramQuadrantMapper, ToothSurfacesMapper toothSurfacesMapper, GetOdontogramService getOdontogramService, GetToothSurfacesService getToothSurfacesService) {
        this.odontogramQuadrantMapper = odontogramQuadrantMapper;
        this.toothSurfacesMapper = toothSurfacesMapper;
        this.getOdontogramService = getOdontogramService;
        this.getToothSurfacesService = getToothSurfacesService;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @GetMapping()
    @ResponseBody
    public List<OdontogramQuadrantDto> getOdontogram() {
        List<OdontogramQuadrantDto> result = odontogramQuadrantMapper.parseToOdontongramQuadrantDtoList(getOdontogramService.run());
        logger.debug("Output -> {}", result);
        return result;
    }

    @GetMapping("/tooth/{toothId}/surfaces")
    @ResponseBody
    public ToothSurfacesDto getToothSurfaces(@PathVariable(name = "toothId") String toothId) {
        ToothSurfacesDto result = toothSurfacesMapper.parseToToothSurfacesDto(getToothSurfacesService.run(toothId));
        logger.debug("Output -> {}", result);
        return result;
    }

}