package ar.lamansys.immunization.infrastructure.output.repository.vaccine;

import ar.lamansys.immunization.domain.vaccine.VaccineSchemeBo;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeStorage;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VaccineSchemeStorageImpl implements VaccineSchemeStorage {

    private final VaccineSchemeRepository vaccineSchemeRepository;

    public VaccineSchemeStorageImpl(VaccineSchemeRepository vaccineSchemeRepository) {
        this.vaccineSchemeRepository = vaccineSchemeRepository;
    }

    @Override
    public boolean isValidScheme(Short id) {
        return vaccineSchemeRepository.existsById(id);
    }

    @Override
    public Optional<VaccineSchemeBo> fetchSchemeById(Short id) {
        return vaccineSchemeRepository.findById(id)
                .map(vaccineScheme ->
                        new VaccineSchemeBo(vaccineScheme.getId(), vaccineScheme.getDescription(),
                                vaccineScheme.getMinimumThresholdDays(), vaccineScheme.getMaximumThresholdDays()));
    }
}
