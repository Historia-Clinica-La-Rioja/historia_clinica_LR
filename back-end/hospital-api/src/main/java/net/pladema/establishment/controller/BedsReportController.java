package net.pladema.establishment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.pladema.establishment.controller.dto.BedCategoriesDataDto;
import net.pladema.establishment.controller.service.BedCategoriesDataService;
import net.pladema.establishment.controller.service.domain.BedCategoriesDataFilterBo;
import net.pladema.sgx.service.encryption.EncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bed/reports")
public class BedsReportController {

    private static final Logger LOG = LoggerFactory.getLogger(BedsReportController.class);

    private static final String COVID_DESCRIPTION = "covid";

    private final BedCategoriesDataService bedCategoriesDataService;

    private final EncryptionService encryptionService;

    public BedsReportController(BedCategoriesDataService bedCategoriesDataService,
                                EncryptionService encryptionService){
        this.bedCategoriesDataService = bedCategoriesDataService;
        this.encryptionService = encryptionService;
    }

    @GetMapping(value = "/covid")
    public ResponseEntity<String> getBedCategoriesAndOccupancyForCovid() throws JsonProcessingException {
        LOG.debug("getBedCategoriesAndOccupancyForCovid without parameters");
        List<BedCategoriesDataDto> result = this.bedCategoriesDataService
                .execute(new BedCategoriesDataFilterBo(COVID_DESCRIPTION))
                .stream()
                .map(BedCategoriesDataDto::new)
                .collect(Collectors.toList());
        LOG.debug("getBedCategoriesAndOccupancyForCovid result -> {}", result);
        ObjectMapper objectMapper = new ObjectMapper();
        return encrypt(objectMapper.writeValueAsString(result));
    }

    @GetMapping
    public ResponseEntity<String> getBedCategoriesAndOccupancy() throws JsonProcessingException {
        LOG.debug("getBedCategoriesAndOccupancy without parameters");
        List<BedCategoriesDataDto> result = this.bedCategoriesDataService
                .execute(new BedCategoriesDataFilterBo(null))
                .stream()
                .map(BedCategoriesDataDto::new)
                .collect(Collectors.toList());
        LOG.debug("getBedCategoriesAndOccupancy result -> {}", result);
        ObjectMapper objectMapper = new ObjectMapper();
        return encrypt(objectMapper.writeValueAsString(result));
    }

    private ResponseEntity<String> encrypt(String message){
        try {
            return ResponseEntity.ok().body(encryptionService.encrypt(message));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
