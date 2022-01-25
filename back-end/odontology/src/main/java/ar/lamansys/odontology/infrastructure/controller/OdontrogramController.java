package ar.lamansys.odontology.infrastructure.controller;

import ar.lamansys.odontology.application.odontogram.FetchOdontogramDrawingsService;
import ar.lamansys.odontology.application.odontogram.GetOdontogramService;
import ar.lamansys.odontology.application.odontogram.GetToothSurfacesService;
import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;
import ar.lamansys.odontology.infrastructure.controller.dto.DrawingsDto;
import ar.lamansys.odontology.infrastructure.controller.dto.OdontogramQuadrantDto;
import ar.lamansys.odontology.infrastructure.controller.dto.ToothDrawingsDto;
import ar.lamansys.odontology.infrastructure.controller.dto.ToothSurfacesDto;
import ar.lamansys.odontology.infrastructure.controller.mapper.OdontogramQuadrantMapper;
import ar.lamansys.odontology.infrastructure.controller.mapper.ToothSurfacesMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/odontology/odontogram")
@Tag(name = "Odontogram", description = "Odontogram")
public class OdontrogramController {
    private final Logger logger;

    private final OdontogramQuadrantMapper odontogramQuadrantMapper;
    private final ToothSurfacesMapper toothSurfacesMapper;
    private final GetOdontogramService getOdontogramService;
    private final GetToothSurfacesService getToothSurfacesService;
    private final FetchOdontogramDrawingsService fetchOdontogramDrawingsService;

    public OdontrogramController(OdontogramQuadrantMapper odontogramQuadrantMapper,
                                 ToothSurfacesMapper toothSurfacesMapper,
                                 GetOdontogramService getOdontogramService,
                                 GetToothSurfacesService getToothSurfacesService,
                                 FetchOdontogramDrawingsService fetchOdontogramDrawingsService) {
        this.odontogramQuadrantMapper = odontogramQuadrantMapper;
        this.toothSurfacesMapper = toothSurfacesMapper;
        this.getOdontogramService = getOdontogramService;
        this.getToothSurfacesService = getToothSurfacesService;
        this.fetchOdontogramDrawingsService = fetchOdontogramDrawingsService;
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

    @GetMapping("/drawings/{patientId}")
    @ResponseBody
    public List<ToothDrawingsDto> getOdontogramDrawings(@PathVariable(name = "patientId") Integer patientId){
        logger.debug("Input parameter -> patientId {}", patientId);
        List<ToothDrawingsDto> result = this.mapTo(fetchOdontogramDrawingsService.run(patientId));
        logger.debug("Output -> {}", result);
        return result;
    }

    private List<ToothDrawingsDto> mapTo(List<ToothDrawingsBo> toothDrawingsBoList) {
        return toothDrawingsBoList.stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

    private ToothDrawingsDto mapTo(ToothDrawingsBo toothDrawingsBo) {
        DrawingsDto drawingsDto = DrawingsDto.builder()
                .whole(toothDrawingsBo.getWholeToothSctid())
                .internal(toothDrawingsBo.getInternalSctid())
                .external(toothDrawingsBo.getExternalSctid())
                .central(toothDrawingsBo.getCentralSctid())
                .left(toothDrawingsBo.getLeftSctid())
                .right(toothDrawingsBo.getRightSctid())
                .build();
        return new ToothDrawingsDto(toothDrawingsBo.getToothId(), drawingsDto);
    }

}