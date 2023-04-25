package net.pladema.establishment.controller;

import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.medicalconsultation.doctorsoffice.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.doctorsoffice.repository.domain.DoctorsOfficeVo;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;

import java.util.List;

public class SectorStore extends BackofficeRepository<Sector, Integer> {

	private SectorRepository repository;

	private DoctorsOfficeRepository doctorsOfficeRepository;

	public SectorStore (SectorRepository repository, DoctorsOfficeRepository doctorsOfficeRepository){
		super(repository, new SingleAttributeBackofficeQueryAdapter<Sector>("description"));
		this.repository = repository;
		this.doctorsOfficeRepository = doctorsOfficeRepository;
	}

	@Override
	public void deleteById(Integer id) {
		assertSectorNoDoctorsOffice(id);
		assertSectorNoChilds(id);
		repository.deleteById(id);
	}

	private void assertSectorNoDoctorsOffice(Integer id) {
		Integer institutionId = repository.getInstitutionId(id);
		List<DoctorsOfficeVo> doctorsOffices = doctorsOfficeRepository.findAllBy(institutionId, id);
		if (!doctorsOffices.isEmpty())
			throw new BackofficeValidationException("Existen consultorios que referencian a éste sector");
	}

	private void assertSectorNoChilds(Integer sectorId){
		List<Sector> childSectors = repository.getChildSectorsBySectorId(sectorId);
		if(!childSectors.isEmpty())
			throw new BackofficeValidationException("Existen sectores hijos de este sector");
	}

}
