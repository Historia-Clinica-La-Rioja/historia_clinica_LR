package net.pladema.medicine.infrastructure.input.rest;

import net.pladema.medicine.infrastructure.input.rest.dto.InstitutionMedicineFinancingStatusDto;
import net.pladema.medicine.infrastructure.output.BackofficeInstitutionMedicineFinancingStatusStore;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
@RequestMapping("backoffice/institutionmedicinesfinancingstatus")
@RestController
public class BackofficeInstitutionMedicineFinancingStatusController extends AbstractBackofficeController<InstitutionMedicineFinancingStatusDto, Integer> {

	public BackofficeInstitutionMedicineFinancingStatusController(BackofficeInstitutionMedicineFinancingStatusStore store){
		super(store);
	}
}
