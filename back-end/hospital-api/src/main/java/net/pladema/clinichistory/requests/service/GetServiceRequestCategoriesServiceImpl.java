package net.pladema.clinichistory.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.repository.entity.ServiceRequestCategoryRepository;
import net.pladema.clinichistory.requests.service.domain.ServiceRequestCategoryBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetServiceRequestCategoriesServiceImpl implements GetServiceRequestCategoriesService {

	private final ServiceRequestCategoryRepository serviceRequestCategoryRepository;

	public List<ServiceRequestCategoryBo> run() {
		log.debug("Get all Request Categories");
		List<ServiceRequestCategoryBo> result = serviceRequestCategoryRepository.getAll()
				.stream()
				.map(src -> new ServiceRequestCategoryBo(src.getId(), src.getDescription()))
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}
}
