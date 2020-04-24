package net.pladema.renaper.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import net.pladema.renaper.controller.dto.PersonBasicDataResponseDto;
import net.pladema.renaper.controller.mapper.RenaperMapper;
import net.pladema.renaper.services.RenaperService;
import net.pladema.renaper.services.domain.PersonDataResponse;

@RestController
@RequestMapping("/renaper")
@Api(value = "Renaper", tags = { "Renaper" })
public class RenaperController {

	private static final Logger LOG = LoggerFactory.getLogger(RenaperController.class);
	
    private final RenaperService renaperService;

    private final RenaperMapper renaperMapper;

    public RenaperController(RenaperService renaperService, RenaperMapper renaperMapper) {
        super();
        this.renaperService = renaperService;
        this.renaperMapper = renaperMapper;
    }

    @GetMapping(value = "/searchPerson")
    public ResponseEntity<PersonBasicDataResponseDto> getBasicPerson(
            @RequestParam(value = "identificationNumber", required = true) String identificationNumber,
            @RequestParam(value = "genderId", required = true) Short genderId){
    	LOG.debug("Input data -> identificationNumber: {} , genderId: {} ", identificationNumber, genderId);
    	Optional<PersonDataResponse> personData = renaperService.getPersonData(identificationNumber, genderId);
        LOG.debug("Resultado -> {}", personData);
        if (!personData.isPresent()) {
        	return ResponseEntity.noContent().build();
        }
        PersonBasicDataResponseDto result = renaperMapper.fromPersonDataResponse(personData.get());
        return  ResponseEntity.ok().body(result);
    }
}
