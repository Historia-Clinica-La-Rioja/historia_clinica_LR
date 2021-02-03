package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.BedCategoriesDataDto;
import net.pladema.establishment.controller.service.BedCategoriesDataService;
import net.pladema.establishment.controller.service.domain.BedCategoriesDataFilterBo;
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

    public BedsReportController(BedCategoriesDataService bedCategoriesDataService){
        this.bedCategoriesDataService = bedCategoriesDataService;
    }

    @GetMapping(value = "/covid")
    public ResponseEntity<List<BedCategoriesDataDto>> getBedCategoriesAndOccupancyForCovid(){
        LOG.debug("getBedCategoriesAndOccupancyForCovid without parameters");
        List<BedCategoriesDataDto> result = this.bedCategoriesDataService.execute(new BedCategoriesDataFilterBo(COVID_DESCRIPTION)).stream()
                .map(BedCategoriesDataDto::new)
                .collect(Collectors.toList());
        LOG.debug("getBedCategoriesAndOccupancyForCovid result -> {}", result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    public ResponseEntity<List<BedCategoriesDataDto>> getBedCategoriesAndOccupancy(){
        LOG.debug("getBedCategoriesAndOccupancy without parameters");
        List<BedCategoriesDataDto> result = this.bedCategoriesDataService.execute(new BedCategoriesDataFilterBo(null)).stream()
                .map(BedCategoriesDataDto::new)
                .collect(Collectors.toList());
        LOG.debug("getBedCategoriesAndOccupancy result -> {}", result);
        return ResponseEntity.ok().body(result);
    }

}
