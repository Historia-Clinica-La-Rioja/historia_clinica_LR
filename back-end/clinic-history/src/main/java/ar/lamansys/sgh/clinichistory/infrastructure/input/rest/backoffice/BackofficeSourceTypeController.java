package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.backoffice;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceTypeRepository;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/sourcetypes")
public class BackofficeSourceTypeController extends AbstractBackofficeController<SourceType, Short> {

	public BackofficeSourceTypeController(SourceTypeRepository repository) {
		super(new BackofficeRepository<>(
				repository,
				new SingleAttributeBackofficeQueryAdapter<>("description")
		));
	}


}