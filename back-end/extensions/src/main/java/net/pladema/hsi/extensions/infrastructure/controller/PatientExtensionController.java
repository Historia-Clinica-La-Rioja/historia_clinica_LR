package net.pladema.hsi.extensions.infrastructure.controller;

import net.pladema.hsi.extensions.domain.ExtensionService;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIMenuItemDto;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIPageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/extensions/patient/{patientId}")
//@Tag(name="Extensions", description= "Extensions" )
public class PatientExtensionController {

    private final Logger logger;
    private final ExtensionService extensionService;

    public PatientExtensionController(ExtensionService extensionService) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.extensionService = extensionService;
    }

    @GetMapping("/menu")
    @ResponseBody
    public UIMenuItemDto[] getPatientMenu(
            @PathVariable("patientId") Integer patientId
    ){
        return extensionService.getPatientMenu(patientId);
    }

    @GetMapping("/page/{menuId}")
    @ResponseBody
    public UIPageDto getPatientPage(
            @PathVariable("patientId") Integer patientId,
            @PathVariable("menuId") String menuId
    ){
        return extensionService.getPatientPage(patientId, menuId);
    }

}
