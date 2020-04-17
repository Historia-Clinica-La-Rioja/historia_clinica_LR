package net.pladema.address.service;

import net.pladema.address.repository.entity.Address;
import net.pladema.address.repository.entity.City;

import java.util.Collection;
import java.util.Optional;

public interface AddressMasterDataService {

	public <T> Collection<T> findCityByProvince(Short provinceId, Class<T> clazz);

	public <T> Collection<T> findDepartmentByProvince(Short provinceId, Class<T> clazz);

	public <T> Collection<T> findByCountry(Short countryId, Class<T> clazz);

	public <T> Collection<T> findAllCountry(Class<T> clazz);

	public <T> Collection<T> findCitiesByDepartment(Short department_id, Class<T> clazz);

	public Optional<City> findCityById(Integer idCity);
}
