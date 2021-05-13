package ar.lamansys.odontology.infrastructure.controller;

import ar.lamansys.odontology.application.fetchodontogram.GetOdontogramInfoService;
import ar.lamansys.odontology.infrastructure.controller.dto.TeethGroupDto;
import ar.lamansys.odontology.infrastructure.controller.mapper.OdontogramMapper;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/odontology/odontogram")
@Api(value="Odontology", tags= { "Odontology" } )
public class OdontrogramController {
    private final Logger LOG;

    private final OdontogramMapper odontogramMapper;
    private final GetOdontogramInfoService getOdontogramInfoService;

    public OdontrogramController(OdontogramMapper odontogramMapper, GetOdontogramInfoService getOdontogramInfoService) {
        this.odontogramMapper = odontogramMapper;
        this.getOdontogramInfoService = getOdontogramInfoService;
        this.LOG = LoggerFactory.getLogger(getClass());
    }

    @GetMapping()
    @ResponseBody
    public List<TeethGroupDto> getOdontogramInfo() {
        List<TeethGroupDto> result = odontogramMapper.parseToTeethGroupList(getOdontogramInfoService.run());
        LOG.debug("Response validate -> {}", result);
        return result;
    }
}