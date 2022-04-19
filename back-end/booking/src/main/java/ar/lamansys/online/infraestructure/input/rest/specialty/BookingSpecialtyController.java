package ar.lamansys.online.infraestructure.input.rest.specialty;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.online.application.specialty.FetchPracticesByProfessionalAndHealthInsurance;
import ar.lamansys.online.application.specialty.FetchPracticesBySpecialtyAndHealthInsurance;
import ar.lamansys.online.application.specialty.FetchSpecialties;
import ar.lamansys.online.application.specialty.FetchSpecialtiesByProfessional;
import ar.lamansys.online.domain.specialty.BookingSpecialtyBo;
import ar.lamansys.online.infraestructure.input.rest.specialty.dto.BookingSpecialtyDto;
import ar.lamansys.online.infraestructure.input.rest.specialty.dto.PracticeDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/booking")
public class BookingSpecialtyController {

    private final FetchPracticesBySpecialtyAndHealthInsurance fetchPracticesBySpecialtyAndHealthInsurance;
    private final FetchPracticesByProfessionalAndHealthInsurance fetchPracticesByProfessionalAndHealthInsurance;
    private final FetchSpecialties fetchSpecialties;
    private final FetchSpecialtiesByProfessional fetchSpecialtiesByProfessional;

    public BookingSpecialtyController(
            FetchPracticesBySpecialtyAndHealthInsurance fetchPracticesBySpecialtyAndHealthInsurance,
            FetchPracticesByProfessionalAndHealthInsurance fetchPracticesByProfessionalAndHealthInsurance,
            FetchSpecialties fetchSpecialties, 
            FetchSpecialtiesByProfessional fetchSpecialtiesByProfessional
    ) {
        this.fetchPracticesBySpecialtyAndHealthInsurance = fetchPracticesBySpecialtyAndHealthInsurance;
        this.fetchPracticesByProfessionalAndHealthInsurance = fetchPracticesByProfessionalAndHealthInsurance;
        this.fetchSpecialties = fetchSpecialties;
        this.fetchSpecialtiesByProfessional = fetchSpecialtiesByProfessional;
    }

    @GetMapping("/specialty/{clinicalSpecialtyId}/practices/healthinsurance/{healthInsuranceId}")
    public ResponseEntity<List<PracticeDto>> getAllPracticesBySpecialtyAndHealthInsurance(
            @PathVariable(name = "clinicalSpecialtyId") Integer clinicalSpecialtyId,
            @PathVariable(name = "healthInsuranceId") Integer medicalCoverageId,
            @RequestParam(name = "all", required = false, defaultValue = "true") boolean all
    ) {
        var practiceBo= fetchPracticesBySpecialtyAndHealthInsurance.run(clinicalSpecialtyId, medicalCoverageId, all);
        List<PracticeDto> result = practiceBo.stream().map(PracticeDto::new).collect(Collectors.toList());
        log.debug("Get all practices by Clinical Specialty {} and by HealthInsurance {} => {}",
                clinicalSpecialtyId, medicalCoverageId,
                result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/practices/professional/{healthcareProfessionalId}/healthinsurance/{healthInsuranceId}/specialty/{clinicalSpecialtyId}")
    public ResponseEntity<List<PracticeDto>> getAllPracticesByProfessionalAndHealthInsurance(
            @PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId,
            @PathVariable(name = "healthInsuranceId") Integer medicalCoverageId,
            @PathVariable(name = "clinicalSpecialtyId") Integer clinicalSpecialtyId,
            @RequestParam(name = "all", required = false, defaultValue = "true") boolean all) {
        var practiceBo= fetchPracticesByProfessionalAndHealthInsurance.run(healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId, all);
        List<PracticeDto> result = practiceBo.stream().map(PracticeDto::new).collect(Collectors.toList());
        log.debug("Get all practices by healthcareProfessionalId {} and by HealthInsurance {} and by clinical specialty {} => {}", healthcareProfessionalId, medicalCoverageId, clinicalSpecialtyId,
                result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/specialties")
    public ResponseEntity<List<BookingSpecialtyDto>> getAllSpecialties() {
        List<BookingSpecialtyBo> practices = fetchSpecialties.run();
        List<BookingSpecialtyDto> result = practices.stream().map(BookingSpecialtyDto::new).collect(Collectors.toList());
        log.debug("Get all practices => {}", result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/specialties/professional/{healthcareProfessionalId}")
    public ResponseEntity<List<BookingSpecialtyDto>> getSpecialtiesByProfessional(
            @PathVariable(name = "healthcareProfessionalId") Integer healthcareProfessionalId
    ) {
        List<BookingSpecialtyBo> practices = fetchSpecialtiesByProfessional.run(healthcareProfessionalId);
        List<BookingSpecialtyDto> result = practices.stream().map(BookingSpecialtyDto::new).collect(Collectors.toList());
        log.debug("Get all practices for professional {} => {}", healthcareProfessionalId, result);
        return ResponseEntity.ok(result);
    }
}
