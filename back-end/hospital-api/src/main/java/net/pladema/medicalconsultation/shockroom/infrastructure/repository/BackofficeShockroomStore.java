package net.pladema.medicalconsultation.shockroom.infrastructure.repository;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.entity.Shockroom;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

public class BackofficeShockroomStore extends BackofficeRepository<Shockroom, Integer> {

	private ShockroomRepository repository;

	public BackofficeShockroomStore(ShockroomRepository repository) {
		super(repository, new SingleAttributeBackofficeQueryAdapter<Shockroom>("description"));
		this.repository = repository;
	}

}
