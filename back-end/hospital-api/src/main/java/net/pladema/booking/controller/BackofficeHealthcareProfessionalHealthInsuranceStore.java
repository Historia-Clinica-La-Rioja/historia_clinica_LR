package net.pladema.booking.controller;

import net.pladema.booking.repository.BackofficeProfessionalMedicalCoverageRepository;
import net.pladema.booking.repository.entity.VProfessionalMedicalCoverage;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.booking.repository.HealthcareProfessionalHealthInsuranceRepository;
import net.pladema.booking.repository.entity.HealthcareProfessionalHealthInsurance;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeHealthcareProfessionalHealthInsuranceStore implements
        BackofficeStore<VProfessionalMedicalCoverage, Integer> {

	private final HealthcareProfessionalHealthInsuranceRepository healthcareProfessionalHealthInsuranceRepository;
	private final BackofficeProfessionalMedicalCoverageRepository backofficeProfessionalMedicalCoverageRepository;


	public BackofficeHealthcareProfessionalHealthInsuranceStore(
			HealthcareProfessionalHealthInsuranceRepository healthcareProfessionalHealthInsuranceRepository,
			BackofficeProfessionalMedicalCoverageRepository backofficeProfessionalMedicalCoverageRepository) {
		this.healthcareProfessionalHealthInsuranceRepository = healthcareProfessionalHealthInsuranceRepository;
		this.backofficeProfessionalMedicalCoverageRepository = backofficeProfessionalMedicalCoverageRepository;
    }

    @Override
    public Page<VProfessionalMedicalCoverage> findAll(VProfessionalMedicalCoverage example, Pageable pageable) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("medicalCoverageId", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		List<VProfessionalMedicalCoverage> result = backofficeProfessionalMedicalCoverageRepository.findAll(Example.of(example, customExampleMatcher))
				.stream()
				.collect(Collectors.toList());

		return new PageImpl<>(result, pageable, result.size());
    }

    private VProfessionalMedicalCoverage map(HealthcareProfessionalHealthInsurance healthcareProfessionalHealthInsurance) {
        return new VProfessionalMedicalCoverage(
                healthcareProfessionalHealthInsurance.getId(),
				healthcareProfessionalHealthInsurance.getHealthcareProfessionalId(),
                healthcareProfessionalHealthInsurance.getMedicalCoverageId()
        );
    }

    @Override
    public List<VProfessionalMedicalCoverage> findAll() {
        return healthcareProfessionalHealthInsuranceRepository.findAll().stream().map(this::map).collect(Collectors.toList());
    }


    @Override
    public List<VProfessionalMedicalCoverage> findAllById(List<Integer> ids) {
        return healthcareProfessionalHealthInsuranceRepository.findAllById(ids).stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public Optional<VProfessionalMedicalCoverage> findById(Integer id) {
        return healthcareProfessionalHealthInsuranceRepository.findById(id).map(this::map);
    }

    @Override
    public VProfessionalMedicalCoverage save(VProfessionalMedicalCoverage  entity) {
        HealthcareProfessionalHealthInsurance healthcareProfessionalHealthInsurance = new HealthcareProfessionalHealthInsurance(
                entity.getId(),
                entity.getHealthcareProfessionalId(),
                entity.getMedicalCoverageId()
        );
        return map(healthcareProfessionalHealthInsuranceRepository.save(healthcareProfessionalHealthInsurance));
    }

    @Override
    public void deleteById(Integer id) {
		healthcareProfessionalHealthInsuranceRepository.deleteById(id);
    }

    @Override
    public Example<VProfessionalMedicalCoverage> buildExample(VProfessionalMedicalCoverage entity) {
        return null;
    }


}
