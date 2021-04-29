package ar.lamansys.odontology.infrastructure.controller;

import ar.lamansys.odontology.application.dosomething.DoSomethingService;
import ar.lamansys.odontology.application.plugin.exception.ToothServiceException;
import ar.lamansys.odontology.domain.TeethBo;
import ar.lamansys.sgh.shared.domain.DoSomethingBo;
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
    private final DoSomethingService doSomethingService;

    private final Logger logger;

    public OdontologyController(ToothService toothService, DoSomethingService doSomethingService) {
        this.toothService = toothService;
        this.doSomethingService = doSomethingService;
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

    @GetMapping("/do-something")
    @ResponseBody
    public String doSomething() {
        DoSomethingBo resultService = doSomethingService.doSomething();
        String result = resultService.getDone();
        logger.debug("Output -> {}", result);
        return result;
    }

}
