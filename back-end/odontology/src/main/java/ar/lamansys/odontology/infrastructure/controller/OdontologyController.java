package ar.lamansys.odontology.infrastructure.controller;

import ar.lamansys.odontology.application.plugin.exception.ToothServiceException;
import ar.lamansys.odontology.domain.TeethBo;
import io.swagger.annotations.Api;
import ar.lamansys.odontology.application.plugin.ToothService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/odontology")
@Api(value="Odontology", tags= { "Odontology" } )
public class OdontologyController {

    private final ToothService toothService;
    private final Logger logger;

    public OdontologyController(ToothService toothService) {
        this.toothService = toothService;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @GetMapping("/info")
    @ResponseBody
    public String getInfo() throws ToothServiceException {
        TeethBo resultService = toothService.getTeeth();
        String result = resultService.getInfo();
        logger.debug("Output -> {}", result);
        return result;
    }

}
