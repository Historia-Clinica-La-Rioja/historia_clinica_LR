package net.pladema.medicalconsultation.shockroom.controller;
import net.pladema.medicalconsultation.shockroom.repository.ShockroomRepository;
import net.pladema.medicalconsultation.shockroom.repository.entity.Shockroom;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

public class ShockroomStore extends BackofficeRepository<Shockroom, Integer> {

	private ShockroomRepository repository;

	public ShockroomStore(ShockroomRepository repository) {
		super(repository, new SingleAttributeBackofficeQueryAdapter<Shockroom>("description"));
		this.repository = repository;
	}

}
