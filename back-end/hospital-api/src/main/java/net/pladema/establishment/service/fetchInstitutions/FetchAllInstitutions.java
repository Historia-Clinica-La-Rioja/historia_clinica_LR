package net.pladema.establishment.service.fetchInstitutions;

import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;
import net.pladema.establishment.service.domain.InstitutionBo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FetchAllInstitutions {

    private final InstitutionRepository institutionRepository;

    public FetchAllInstitutions(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public List<InstitutionBo> run() {
        return institutionRepository.findAll()
                .stream()
                .map(this::mapTo)
                .collect(Collectors.toList());
    }

	public List<InstitutionBasicInfoBo> findByDepartmentId(Short departmentId) {
		return institutionRepository.findByDeparmentId(departmentId);
	}

    private InstitutionBo mapTo(Institution institution){
        InstitutionBo result = new InstitutionBo();
        result.setId(institution.getId());
        result.setName(institution.getName());
        result.setCuit(institution.getCuit());
        result.setEmail(institution.getEmail());
        result.setPhone(institution.getPhone());
        result.setSisaCode(institution.getSisaCode());
        result.setTimezone(institution.getTimezone());
        result.setWebsite(institution.getWebsite());
        result.setAddressId(institution.getAddressId());
        return result;
    }
}
