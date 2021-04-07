package net.pladema.odontology.controller;

import io.swagger.annotations.Api;
import net.pladema.odontology.service.OdontologyService;
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

    private final OdontologyService odontologyService;
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public OdontologyController(OdontologyService odontologyService) {
        this.odontologyService = odontologyService;
    }

    @GetMapping("/info")
    @ResponseBody
    public String getInfo(){
        String result = odontologyService.getInfo();
        LOG.debug("Output -> {}", result);
        return result;
    }

}
