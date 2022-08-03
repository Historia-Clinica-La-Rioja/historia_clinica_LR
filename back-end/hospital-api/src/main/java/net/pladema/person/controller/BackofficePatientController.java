package net.pladema.person.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/patient")
public class BackofficePatientController extends AbstractBackofficeController<Patient, Integer> {

	public BackofficePatientController(PatientRepository repository) {
		super(repository);
	}

}