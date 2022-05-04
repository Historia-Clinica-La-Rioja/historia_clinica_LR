package net.pladema.clinichistory.requests.service;

import net.pladema.clinichistory.requests.service.domain.ServiceRequestCategoryBo;

import java.util.List;

public interface GetServiceRequestCategoriesService {

	List<ServiceRequestCategoryBo> run();

}
