package net.pladema.establishment.controller;

import ar.lamansys.sgh.shared.infrastructure.input.service.CareLineDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.controller.mapper.CareLineMapper;
import net.pladema.establishment.service.CareLineService;
import net.pladema.establishment.service.domain.CareLineBo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(value = "CareLine", tags = {"Care Line"})
@RequestMapping("/institution/{institutionId}/carelines")
public class CareLineController {

    private final CareLineService careLineService;
    private final CareLineMapper careLineMapper;

    @GetMapping()
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<List<CareLineDto>> getAll(@PathVariable(name = "institutionId") Integer institutionId) {
        List<CareLineBo> careLinesBo = careLineService.getCareLines();
        log.debug("Get all care lines  => {}", careLinesBo);
        return ResponseEntity.ok(careLineMapper.toListCareLineDto(careLinesBo));
    }

}