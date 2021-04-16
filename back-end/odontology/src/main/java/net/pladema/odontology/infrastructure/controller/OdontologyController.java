package net.pladema.odontology.infrastructure.controller;

import io.swagger.annotations.Api;
import net.pladema.odontology.application.plugin.ToothService;
import net.pladema.odontology.application.plugin.exception.ToothServiceException;
import net.pladema.odontology.domain.TeethBo;
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
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public OdontologyController(ToothService toothService) {
        this.toothService = toothService;
    }

    @GetMapping("/info")
    @ResponseBody
    public String getInfo() throws ToothServiceException {
        TeethBo resultService = toothService.getTeeth();
        String result = resultService.getInfo();
        LOG.debug("Output -> {}", result);
        return result;
    }

}
