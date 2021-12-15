package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.backoffice;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.DocumentTypeRepository;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/documenttypes")
public class BackofficeDocumentTypeController extends AbstractBackofficeController<DocumentType, Short> {

	public BackofficeDocumentTypeController(DocumentTypeRepository repository) {
		super(new BackofficeRepository<>(
				repository,
				new SingleAttributeBackofficeQueryAdapter<>("description")
		));
	}


}