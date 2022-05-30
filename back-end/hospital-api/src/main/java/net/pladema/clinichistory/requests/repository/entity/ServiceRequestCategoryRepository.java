package net.pladema.clinichistory.requests.repository.entity;

import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRequestCategoryRepository extends JpaRepository<ServiceRequestCategory, String> {

	@Query( "SELECT src " +
			"FROM ServiceRequestCategory src " +
			"ORDER BY src.orden ")
	List<ServiceRequestCategory> getAll();

}
