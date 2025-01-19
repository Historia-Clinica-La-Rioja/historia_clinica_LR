package net.pladema.address.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.address.controller.mapper.DepartmentMapper;
import net.pladema.address.controller.service.domain.DepartmentBo;
import net.pladema.address.repository.CityRepository;
import net.pladema.address.repository.CountryRepository;
import net.pladema.address.repository.DepartmentRepository;
import net.pladema.address.repository.ProvinceRepository;
import net.pladema.address.repository.entity.Department;
import net.pladema.address.service.AddressMasterDataService;

import net.pladema.snowstorm.repository.entity.SnomedGroupType;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddressMasterDataServiceImpl implements AddressMasterDataService {

	private static final String DESCRIPTION = "description";

	private final CityRepository cityRepository;

	private final ProvinceRepository provinceRepository;

	private final DepartmentRepository departmentRepository;

	private final CountryRepository countryRepository;

	private final DepartmentMapper departmentMapper;

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
	public <T> Collection<T> findAllCitiesByDepartment(Short departmentId, Class<T> clazz) {
		return cityRepository.findAllByDepartment(departmentId, Sort.by(Order.asc(DESCRIPTION)), clazz);
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

	@Override
	public DepartmentBo findDepartmentById(Short departmentId) {
		Department result = departmentRepository.findDepartmentById(departmentId);
		return departmentMapper.fromDepartmentToDepartmentBo(result);
	}

	@Override
	public <T> Collection<T> getDepartmentsByReferenceFilterByClinicalSpecialty(Integer careLineId, List<Integer> clinicalSpecialtyIds, Class<T> clazz) {
		log.debug("Input parameters -> careLineId {}, clinicalSpecialtyIds {}, clazz {}", careLineId, clinicalSpecialtyIds, clazz);
		if (careLineId != null)
			return departmentRepository.findAllByCareLineIdAndClinicalSpecialtyId(careLineId, clinicalSpecialtyIds, clazz);
		return departmentRepository.findAllByProfessionalsWithClinicalSpecialtyId(clinicalSpecialtyIds, clazz);
	}

	@Override
	public <T> Collection<T> getDepartmentsByReferenceFilterByPractice(Integer practiceSnomedId, Integer careLineId, List<Integer> clinicalSpecialtyIds, Class<T> clazz) {
		log.debug("Input parameters -> practiceSnomedId {}, careLineId {}, clinicalSpecialtyIds {}, clazz {}", practiceSnomedId, careLineId, clinicalSpecialtyIds, clazz);
		if (careLineId != null && (clinicalSpecialtyIds == null || clinicalSpecialtyIds.isEmpty()))
			return departmentRepository.findAllByCareLineIdAndPracticeSnomedId(careLineId, practiceSnomedId, clazz);
		if (careLineId != null)
			return departmentRepository.findAllByCareLineIdClinicalSpecialtyIdAndPracticeSnomedId(careLineId, clinicalSpecialtyIds, practiceSnomedId, clazz);
		if (clinicalSpecialtyIds != null && !clinicalSpecialtyIds.isEmpty())
			return departmentRepository.findAllByClinicalSpecialtyIdAndPracticeSnomedId(clinicalSpecialtyIds, practiceSnomedId, SnomedECL.PROCEDURE.toString(), SnomedGroupType.SEARCH_GROUP, clazz);
		return departmentRepository.findAllByPractice(practiceSnomedId, SnomedECL.PROCEDURE.toString(), SnomedGroupType.SEARCH_GROUP, clazz);
	}


	@Override
	public <T> Collection<T> getDepartmentsByInstitutions() {
		log.debug("Fetch departments by institutions domain");
		Collection<T> result = departmentRepository.findAllByInstitutions();
		log.debug("Output result -> {} ", result);
		return result;
	}
}
