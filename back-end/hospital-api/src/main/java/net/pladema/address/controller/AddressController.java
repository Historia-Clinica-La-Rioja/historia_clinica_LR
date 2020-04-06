package net.pladema.address.controller;


import io.swagger.annotations.Api;
import net.pladema.address.repository.projections.AddressProjection;
import net.pladema.address.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/address")
@Api(value = "Address", tags = { "Address" })
public class AddressController {

	private static final Logger LOG = LoggerFactory.getLogger(AddressController.class);

	private final AddressService addressService;

	public AddressController(AddressService addressService) {
		super();
		this.addressService = addressService;
	}

	@GetMapping(value = "/province/{provinceId}/cities")
	public ResponseEntity<Collection<AddressProjection>> getCitiesByProvince(@PathVariable("provinceId") Short provinceId) {
		LOG.debug("{}", "All cities");
		return ResponseEntity.ok().body(addressService.findCityByProvince(provinceId, AddressProjection.class));
	}

	@GetMapping(value = "/province/{provinceId}/departments")
	public ResponseEntity<Collection<AddressProjection>> getDepartmentsByProvince(@PathVariable("provinceId") Short provinceId) {
		LOG.debug("{}", "All departments");
		return ResponseEntity.ok().body(addressService.findDepartmentByProvince(provinceId, AddressProjection.class));
	}

	@GetMapping(value = "/country/{countryId}/provinces")
	public ResponseEntity<Collection<AddressProjection>> getByCountry(@PathVariable("countryId") Short countryId) {
		LOG.debug("{}", "All province");
		return ResponseEntity.ok().body(addressService.findByCountry(countryId, AddressProjection.class));
	}

	@GetMapping(value = "/countries")
	public ResponseEntity<Collection<AddressProjection>> getAllCountries() {
		LOG.debug("{}", "All countries");
		return ResponseEntity.ok().body(addressService.findAllCountry(AddressProjection.class));
	}

	@GetMapping(value = "/department/{department_id}/cities")
	public ResponseEntity<Collection<AddressProjection>> getCitiesByDepartment(@PathVariable("department_id") Short department_id) {
		LOG.debug("{}", "All cities by department");
		return ResponseEntity.ok().body(addressService.findCitiesByDepartment(department_id, AddressProjection.class));
	}

}