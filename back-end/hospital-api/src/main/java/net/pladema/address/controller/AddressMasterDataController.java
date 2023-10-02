package net.pladema.address.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.pladema.address.controller.dto.DepartmentDto;
import net.pladema.address.controller.mapper.DepartmentMapper;
import net.pladema.address.controller.service.domain.DepartmentBo;
import net.pladema.address.repository.projections.AddressProjection;
import net.pladema.address.service.AddressMasterDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/address/masterdata")
@Tag(name = "Address Master Data", description = "Address Master Data")
public class AddressMasterDataController {

	private static final Logger LOG = LoggerFactory.getLogger(AddressMasterDataController.class);

	private final AddressMasterDataService addressMasterDataService;
	private final DepartmentMapper departmentMapper;

	@GetMapping(value = "/province/{provinceId}/cities")
	public ResponseEntity<Collection<AddressProjection>> getCitiesByProvince(@PathVariable("provinceId") Short provinceId) {
		LOG.debug("{}", "All cities");
		return ResponseEntity.ok().body(addressMasterDataService.findCityByProvince(provinceId, AddressProjection.class));
	}

	@GetMapping(value = "/province/{provinceId}/departments")
	public ResponseEntity<Collection<AddressProjection>> getDepartmentsByProvince(@PathVariable("provinceId") Short provinceId) {
		LOG.debug("{}", "All departments");
		return ResponseEntity.ok().body(addressMasterDataService.findDepartmentByProvince(provinceId, AddressProjection.class));
	}

	@GetMapping(value = "/country/{countryId}/provinces")
	public ResponseEntity<Collection<AddressProjection>> getByCountry(@PathVariable("countryId") Short countryId) {
		LOG.debug("{}", "All province");
		return ResponseEntity.ok().body(addressMasterDataService.findByCountry(countryId, AddressProjection.class));
	}

	@GetMapping(value = "/countries")
	public ResponseEntity<Collection<AddressProjection>> getAllCountries() {
		LOG.debug("{}", "All countries");
		return ResponseEntity.ok().body(addressMasterDataService.findAllCountry(AddressProjection.class));
	}

	@GetMapping(value = "/department/{departmentId}/cities")
	public ResponseEntity<Collection<AddressProjection>> getCitiesByDepartment(@PathVariable("departmentId") Short departmentId) {
		LOG.debug("{}", "All cities by department");
		return ResponseEntity.ok().body(addressMasterDataService.findCitiesByDepartment(departmentId, AddressProjection.class));
	}

	@GetMapping(value = "/department/{departmentId}")
	public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable("departmentId") Short departmentId) {
		LOG.debug("{}", "Department by id");
		DepartmentBo result = addressMasterDataService.findDepartmentById(departmentId);
		DepartmentDto resultDto = departmentMapper.fromDepartmentBo(result);
		return ResponseEntity.ok().body(resultDto);
	}

	@GetMapping(value = "/institution/{institutionId}/departments/by-reference-clinical-specialty-filter")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<Collection<AddressProjection>> getDeparmentsByCareLineAndClinicalSpecialty(@PathVariable("institutionId") Integer institutionId,
																									 @RequestParam("clinicalSpecialtyId") Integer clinicalSpecialtyId,
																									 @RequestParam(name = "careLineId", required = false) Integer careLineId) {
		LOG.debug("{}", "All departments by reference for clinical specialty filter");
		return ResponseEntity.ok().body(addressMasterDataService.getDepartmentsByReferenceFilterByClinicalSpecialty(careLineId, clinicalSpecialtyId, AddressProjection.class));
	}

	@GetMapping(value = "/institution/{institutionId}/departments/by-reference-practice-filter")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
	public ResponseEntity<Collection<AddressProjection>> getDepartmentsByCareLineAndPracticesAndClinicalSpecialty(@PathVariable("institutionId") Integer institutionId,
																												  @RequestParam("practiceSnomedId") Integer practiceSnomedId,
																												  @RequestParam(name = "careLineId", required = false) Integer careLineId,
																												  @RequestParam(name = "clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId) {
		LOG.debug("{}", "All departments by reference for practice filter");
		return ResponseEntity.ok().body(addressMasterDataService.getDepartmentsByReferenceFilterByPractice(practiceSnomedId, careLineId, clinicalSpecialtyId, AddressProjection.class));
	}

}