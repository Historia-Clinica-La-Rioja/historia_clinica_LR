package net.pladema.address.service.impl;

import net.pladema.address.repository.*;
import net.pladema.address.repository.entity.City;
import net.pladema.address.repository.entity.Province;
import net.pladema.address.service.AddressMasterDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class AddressMasterDataServiceImpl implements AddressMasterDataService {

	private static final Logger LOG = LoggerFactory.getLogger(AddressMasterDataServiceImpl.class);

	private static final String DESCRIPTION = "description";

	private final CityRepository cityRepository;

	private final ProvinceRepository provinceRepository;

	private final DepartmentRepository departmentRepository;

	private final CountryRepository countryRepository;

	public AddressMasterDataServiceImpl(CityRepository cityRepository, ProvinceRepository provinceRepository,
										DepartmentRepository departmentRepository, CountryRepository countryRepository) {
		super();
		this.cityRepository = cityRepository;
		this.provinceRepository = provinceRepository;
		this.countryRepository = countryRepository;
		this.departmentRepository = departmentRepository;
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
	public Optional<Province> findProvinceById(Short provinceId) {
		return provinceRepository.findById(provinceId);
	}


}
