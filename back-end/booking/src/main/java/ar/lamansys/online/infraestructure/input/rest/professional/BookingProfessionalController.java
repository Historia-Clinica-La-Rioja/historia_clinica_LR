package ar.lamansys.online.infraestructure.input.rest.professional;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.online.application.professional.FetchAvailabilityByPractice;
import ar.lamansys.online.application.professional.FetchAvailabilityByPracticeAndProfessional;
import ar.lamansys.online.application.professional.FetchBookingProfessionals;
import ar.lamansys.online.infraestructure.input.rest.professional.dto.BookingProfessionalDto;
import ar.lamansys.online.infraestructure.input.rest.professional.dto.ProfessionalAvailabilityDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/booking/professional")
public class BookingProfessionalController {

    private final FetchBookingProfessionals fetchBookingProfessionals;

    private final FetchAvailabilityByPracticeAndProfessional fetchAvailabilityByPracticeAndProfessional;

    private final FetchAvailabilityByPractice fetchAvailabilityByPractice;

    public BookingProfessionalController(
            FetchBookingProfessionals fetchBookingProfessionals,
            FetchAvailabilityByPracticeAndProfessional fetchAvailabilityByPracticeAndProfessional,
            FetchAvailabilityByPractice fetchAvailabilityByPractice
    ) {
        this.fetchBookingProfessionals = fetchBookingProfessionals;
        this.fetchAvailabilityByPracticeAndProfessional = fetchAvailabilityByPracticeAndProfessional;
        this.fetchAvailabilityByPractice = fetchAvailabilityByPractice;
    }

    @GetMapping("/institution/{institutionId}/healthinsurance/{healthInsuranceId}")
    public ResponseEntity<List<BookingProfessionalDto>> getAllBookingProfessionals(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "healthInsuranceId") Integer medicalCoverageId,
            @RequestParam(name = "all", required = false, defaultValue = "true") boolean all
    ) {
        var professionalsBo = fetchBookingProfessionals.run(institutionId,medicalCoverageId, all);
        var result = professionalsBo.stream()
                .map(BookingProfessionalDto::new)
                .collect(Collectors.toList());
        log.debug("Get all booking institutions => {}", result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("{professionalId}/institution/{institutionId}/specialty/{clinicalSpecialtyId}/practice/{practiceId}/availability")
    public ResponseEntity<ProfessionalAvailabilityDto> getProfessionalAvailability(
            @PathVariable(name="institutionId") Integer institutionId,
            @PathVariable(name="professionalId") Integer professionalId,
            @PathVariable(name="clinicalSpecialtyId") Integer clinicalSpecialtyId,
            @PathVariable(name="practiceId") Integer practiceId
    ) {
        var professionalAvailabilityBo = fetchAvailabilityByPracticeAndProfessional.run(
                institutionId,
                professionalId,
                clinicalSpecialtyId,
                practiceId
        );
        var result = new ProfessionalAvailabilityDto(professionalAvailabilityBo);
        log.debug("Get availability by professionalId {} and practiceId{} => {}", professionalId, practiceId, result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("institution/{institutionId}/specialty/{clinicalSpecialtyId}/practice/{practiceId}/healthinsurance/{healthInsuranceId}/availability")
    public ResponseEntity<List<ProfessionalAvailabilityDto>> getProfessionalsAvailability(
            @PathVariable(name="healthInsuranceId") Integer medicalCoverageId,
            @PathVariable(name="practiceId") Integer practiceId,
            @PathVariable(name="clinicalSpecialtyId") Integer clinicalSpecialtyId,
            @PathVariable(name="institutionId") Integer institutionId
    ) {
        var professionalAvailabilityBo = fetchAvailabilityByPractice.run(institutionId,
                clinicalSpecialtyId, practiceId, medicalCoverageId);
        var result = professionalAvailabilityBo.stream()
                .map(ProfessionalAvailabilityDto::new)
                .collect(Collectors.toList());
        log.debug("Get availability by practiceId{} => {}", practiceId, result);
        return ResponseEntity.ok(result);
    }

}
