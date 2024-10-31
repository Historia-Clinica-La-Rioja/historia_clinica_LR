package net.pladema.application.port.output;

import java.util.List;

public interface ServiceRequestTemplatePort  {

	void saveAll(Integer serviceRequestId, List<Integer> templateIds);
}
