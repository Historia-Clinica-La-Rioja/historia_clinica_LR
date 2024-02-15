package net.pladema.audit.controller;

import net.pladema.audit.repository.entity.ViewClinicHistoryAudit;
import net.pladema.audit.controller.constraints.validator.permissions.BackofficeViewClinicHistoryAuditValidator;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/vclinichistoryaudit")
public class BackOfficeViewClinicHistoryAuditController extends AbstractBackofficeController<ViewClinicHistoryAudit, Integer> {


	public BackOfficeViewClinicHistoryAuditController(BackofficeViewClinicHistoryAuditStore store, BackofficeViewClinicHistoryAuditValidator validator) {
		super(store,validator);
	}


}

