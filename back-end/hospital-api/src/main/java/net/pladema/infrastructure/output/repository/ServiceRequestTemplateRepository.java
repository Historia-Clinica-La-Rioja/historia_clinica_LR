package net.pladema.infrastructure.output.repository;

import net.pladema.infrastructure.output.repository.entity.ServiceRequestTemplate;
import net.pladema.infrastructure.output.repository.entity.ServiceRequestTemplatePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRequestTemplateRepository extends JpaRepository<ServiceRequestTemplate, ServiceRequestTemplatePK> {

}