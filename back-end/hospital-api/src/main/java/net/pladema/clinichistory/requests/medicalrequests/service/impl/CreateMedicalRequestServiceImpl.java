package net.pladema.clinichistory.requests.medicalrequests.service.impl;

import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import net.pladema.clinichistory.requests.medicalrequests.repository.MedicalRequestRepository;
import net.pladema.clinichistory.requests.medicalrequests.repository.entity.MedicalRequest;
import net.pladema.clinichistory.requests.medicalrequests.service.CreateMedicalRequestService;
import net.pladema.clinichistory.requests.medicalrequests.service.domain.MedicalRequestBo;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CreateMedicalRequestServiceImpl implements CreateMedicalRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateMedicalRequestServiceImpl.class);

    private final MedicalRequestRepository medicalRequestRepository;

    private final NoteService noteService;

    private final DateTimeProvider dateTimeProvider;

    public CreateMedicalRequestServiceImpl(
            MedicalRequestRepository medicalRequestRepository,
            NoteService noteService,
            DateTimeProvider dateTimeProvider) {
        this.medicalRequestRepository = medicalRequestRepository;
        this.noteService = noteService;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
	@Transactional
    public Integer execute(Integer institutionId, MedicalRequestBo medicalRequestBo) {
        LOG.debug("Input parameters -> institutionId {}, medicalRequestBo {}", institutionId, medicalRequestBo);
        Long noteId = noteService.createNote(medicalRequestBo.getObservations());
        medicalRequestBo.setRequestDate(dateTimeProvider.nowDate());
        Integer result = medicalRequestRepository.save(new MedicalRequest(institutionId, medicalRequestBo, noteId)).getId();
        LOG.debug("Output -> {}", result);
        return result;
    }
}
