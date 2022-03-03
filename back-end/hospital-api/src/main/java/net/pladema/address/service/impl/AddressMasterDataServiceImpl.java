package net.pladema.address.service.impl;

import net.pladema.address.repository.CityRepository;
import net.pladema.address.repository.CountryRepository;
import net.pladema.address.repository.DepartmentRepository;
import net.pladema.address.repository.ProvinceRepository;
import net.pladema.address.service.AddressMasterDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.util.Collection;

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
	public <T> Collection<T> findCitiesByDepartment(Short departmentId, Class<T> clazz) {
		return cityRepository.findByDepartment(departmentId, Sort.by(Order.asc(DESCRIPTION)), clazz);
	}

	@Override
	public boolean existProvinceInCountry(Short countryId, Short provinceId) {
		return provinceRepository.existProvinceInCountry(countryId, provinceId);
	}

	@Override
	public boolean existDepartmentInProvince(Short provinceId, Short departmentId) {
		return departmentRepository.existDepartmentInProvince(provinceId, departmentId);
	}

	@Override
	public boolean existCityInDepartment(Short departmentId, Integer cityId) {
		return cityRepository.existCityInDepartment(departmentId, cityId);
	}

}
