package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import ar.lamansys.immunization.domain.vaccine.VaccineConditionApplicationBo;
import ar.lamansys.immunization.domain.vaccine.VaccineConditionApplicationStorage;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VaccineConditionApplicationStorageImpl implements VaccineConditionApplicationStorage {

    private final VaccineConditionApplicationRepository vaccineConditionApplicationRepository;

    public VaccineConditionApplicationStorageImpl(VaccineConditionApplicationRepository vaccineConditionApplicationRepository) {
        this.vaccineConditionApplicationRepository = vaccineConditionApplicationRepository;
    }


    @Override
    public boolean isValidConditionApplication(Short id) {
        return vaccineConditionApplicationRepository.existsById(id);
    }

    @Override
    public Optional<VaccineConditionApplicationBo> fetchConditionApplicationById(Short id) {
        return vaccineConditionApplicationRepository.findById(id)
                .map(vaccineConditionApplication ->
                        new VaccineConditionApplicationBo(vaccineConditionApplication.getId(), vaccineConditionApplication.getDescription()));
    }
}
