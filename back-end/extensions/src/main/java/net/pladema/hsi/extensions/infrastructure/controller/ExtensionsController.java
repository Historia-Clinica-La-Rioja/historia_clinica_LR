package net.pladema.hsi.extensions.infrastructure.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/extensions")
@Tag(name = "Extensions", description = "Extensions")
public class ExtensionsController {

    private final Logger logger;
    private final ExtensionService extensionService;

    public ExtensionsController(ExtensionService extensionService) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.extensionService = extensionService;
    }

    @GetMapping("/menu")
    @ResponseBody
    public UIMenuItemDto[] getSystemMenu(){
        return extensionService.getSystemMenu();
    }

    @GetMapping("/page/{menuId}")
    @ResponseBody
    public UIPageDto getSystemPage(
            @PathVariable("menuId") String menuId
    ){
        return extensionService.getSystemPage(menuId);
    }


    @GetMapping("/institution/{institutionId}/menu")
    @ResponseBody
    public UIMenuItemDto[] getInstitutionMenu(
            @PathVariable("institutionId") Integer institutionId
    ){
        return extensionService.getInstitutionMenu(institutionId);
    }

    @GetMapping("/institution/{institutionId}/page/{menuId}")
    @ResponseBody
    public UIPageDto getInstitutionPage(
            @PathVariable("institutionId") Integer institutionId,
            @PathVariable("menuId") String menuId
    ){
        return extensionService.getInstitutionPage(institutionId, menuId);
    }

}
