package net.pladema.address.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.address.repository.projections.AddressProjection;
import net.pladema.address.service.AddressMasterDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/address/masterdata")
@Tag(name = "Address Master Data", description = "Address Master Data")
public class AddressMasterDataController {

	private static final Logger LOG = LoggerFactory.getLogger(AddressMasterDataController.class);

	private final AddressMasterDataService addressMasterDataService;

	public AddressMasterDataController(AddressMasterDataService addressMasterDataService) {
		super();
		this.addressMasterDataService = addressMasterDataService;
	}

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

}