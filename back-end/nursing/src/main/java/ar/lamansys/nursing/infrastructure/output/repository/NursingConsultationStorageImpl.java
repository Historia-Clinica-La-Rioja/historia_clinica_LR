package ar.lamansys.nursing.infrastructure.output.repository;

import ar.lamansys.nursing.domain.NursingConsultationInfoBo;
import ar.lamansys.nursing.application.port.NursingConsultationStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NursingConsultationStorageImpl implements NursingConsultationStorage {

    private static final Logger LOG = LoggerFactory.getLogger(NursingConsultationStorageImpl.class);

    private final NursingConsultationRepository nursingConsultationRepository;

    public NursingConsultationStorageImpl(NursingConsultationRepository nursingConsultationRepository) {
        this.nursingConsultationRepository = nursingConsultationRepository;
    }

    @Override
    public Integer save(NursingConsultationInfoBo nursingConsultationInfoBo) {
        LOG.debug("Input parameters -> nursingConsultationInfoBo {}", nursingConsultationInfoBo);

        NursingConsultation nursingConsultation = new NursingConsultation(nursingConsultationInfoBo);
        Integer nursingConsultationId = nursingConsultationRepository.save(nursingConsultation).getId();

        LOG.debug("Output -> {}", nursingConsultationId);

        return nursingConsultationId;
    }

	@Override
	public List<Integer> getNursingConsultationIdsFromPatients(List<Integer> patients) {
		LOG.debug("Input parameters -> patients{}", patients);

		List<Integer> result = nursingConsultationRepository.getNursingConsultationIdsFromPatients(patients);

		LOG.debug("Output -> {}", result);

		return result;
	}

	@Override
	public List<NursingConsultation> findAllByIds(List<Integer> ids) {
		LOG.debug("Input parameters -> NursingConsultationIds{}", ids);

		List<NursingConsultation> result = nursingConsultationRepository.findAllById(ids);

		LOG.debug("Output -> {}", result);

		return result;
	}

	@Override
	public Optional<Integer> getPatientMedicalCoverageId(Integer id){
		LOG.debug("Input parameters -> id{}", id);

		Optional<Integer> result = nursingConsultationRepository.getPatientMedicalCoverageId(id);

		LOG.debug("Output -> {}", result);

		return result;
	}

}
