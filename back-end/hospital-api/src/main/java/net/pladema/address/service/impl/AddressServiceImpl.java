package net.pladema.address.service.impl;

import java.awt.*;
import java.util.Collection;
import java.util.Optional;

import net.pladema.address.repository.*;
import net.pladema.address.repository.entity.Address;
import net.pladema.person.repository.entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import net.pladema.address.repository.entity.City;
import net.pladema.address.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	private static final Logger LOG = LoggerFactory.getLogger(AddressServiceImpl.class);

	private static final String DESCRIPTION = "description";

	private final CityRepository cityRepository;

	private final ProvinceRepository provinceRepository;

	private final DepartmentRepository departmentRepository;

	private final CountryRepository countryRepository;

	private final AddressRepository addressRepository;

	public AddressServiceImpl(CityRepository cityRepository, ProvinceRepository provinceRepository,
			DepartmentRepository departmentRepository, CountryRepository countryRepository, AddressRepository addressRepository) {
		super();
		this.cityRepository = cityRepository;
		this.provinceRepository = provinceRepository;
		this.countryRepository = countryRepository;
		this.departmentRepository = departmentRepository;
		this.addressRepository = addressRepository;
		LOG.debug("{}", "created service");
	}

	@Override
	public <T> Collection<T> findCityByProvince(Short provinceId, Class<T> clazz) {
		return cityRepository.findByProvince(provinceId, Sort.by(Order.asc(DESCRIPTION)), clazz);
	}

	@Override
	public <T> Collection<T> findDepartmentByProvince(Short provinceId, Class<T> clazz) {
		return departmentRepository.findByProvince(provinceId, Sort.by(Order.asc(DESCRIPTION)), clazz);
	}

	@Override
	public <T> Collection<T> findByCountry(Short countryId, Class<T> clazz) {
		return provinceRepository.findByCountry(countryId, Sort.by(Order.asc(DESCRIPTION)), clazz);
	}

	@Override
	public <T> Collection<T> findAllCountry(Class<T> clazz) {
		return countryRepository.findAllProjectedBy(Sort.by(Order.asc(DESCRIPTION)), clazz);
	}

	@Override
	public <T> Collection<T> findCitiesByDepartment(Short department_id, Class<T> clazz) {
		return cityRepository.findByDepartment(department_id, Sort.by(Order.asc(DESCRIPTION)), clazz);
	}

	@Override
	public Optional<City> findCityById(Integer idCity) {
		return cityRepository.findById(idCity);
	}

	@Override
	public Address addAddress(Address address) {
		LOG.debug("Going to save -> {}", address);
		Address addressSaved = addressRepository.save(address);
		LOG.debug("Address saved -> {}", addressSaved);
		return addressSaved;
	}
}
