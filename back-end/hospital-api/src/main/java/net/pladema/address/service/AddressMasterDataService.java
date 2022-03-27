package net.pladema.address.service;

import java.util.Collection;

public interface AddressMasterDataService {

	<T> Collection<T> findCityByProvince(Short provinceId, Class<T> clazz);

	<T> Collection<T> findDepartmentByProvince(Short provinceId, Class<T> clazz);

	<T> Collection<T> findByCountry(Short countryId, Class<T> clazz);

	<T> Collection<T> findAllCountry(Class<T> clazz);

	<T> Collection<T> findCitiesByDepartment(Short departmentId, Class<T> clazz);

	boolean existProvinceInCountry(Short countryId, Short provinceId);

	boolean existDepartmentInProvince(Short provinceId, Short departmentId);

	boolean existCityInDepartment(Short departmentId, Integer cityId);

}
