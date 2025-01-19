package net.pladema.infrastructure.port.output;

import lombok.RequiredArgsConstructor;
import net.pladema.application.port.output.ServiceRequestTemplatePort;
import net.pladema.infrastructure.output.repository.ServiceRequestTemplateRepository;
import net.pladema.infrastructure.output.repository.entity.ServiceRequestTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ServiceRequestTemplatePortImpl implements ServiceRequestTemplatePort {

	private final ServiceRequestTemplateRepository serviceRequestTemplateRepository;

	@Override
	public void saveAll(Integer serviceRequestId, List<Integer> templateIds) {
		serviceRequestTemplateRepository.saveAll(mapToEntityList(serviceRequestId, templateIds));
	}

	private List<ServiceRequestTemplate> mapToEntityList(Integer serviceRequestId, List<Integer> templateIds) {
		return templateIds.stream().map(templateId -> mapToEntity(serviceRequestId, templateId)).collect(Collectors.toList());
	}

	private ServiceRequestTemplate mapToEntity(Integer serviceRequestId, Integer templateId) {
		return new ServiceRequestTemplate(serviceRequestId, templateId);
	}
}
