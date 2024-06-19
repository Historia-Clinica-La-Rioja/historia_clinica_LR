package net.pladema.clinichistory.hospitalization.application.createinternmentepisode;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.InternmentEpisodeStorage;
import net.pladema.clinichistory.hospitalization.domain.InternmentEpisodeBo;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisodeStatus;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.ResponsibleContactService;
import net.pladema.clinichistory.hospitalization.domain.exceptions.CreateInternmentEpisodeEnumException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.CreateInternmentEpisodeException;
import net.pladema.establishment.controller.service.BedExternalService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateInternmentEpisode {

    private final FeatureFlagsService featureFlagsService;
    private final InternmentEpisodeStorage internmentEpisodeStorage;
    private final InternmentEpisodeService internmentEpisodeService;
    private final BedExternalService bedExternalService;
    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
    private final ResponsibleContactService responsibleContactService;

    @Transactional
    public InternmentEpisodeBo run(InternmentEpisodeBo episodeBo) {
        log.debug("Input parameters -> InternmentEpisodeBo {}", episodeBo);

        this.assertContextValid(episodeBo);

        episodeBo.setStatusId(InternmentEpisodeStatus.ACTIVE_ID);

        Integer episodeId = internmentEpisodeStorage.save(episodeBo);
        episodeBo.setId(episodeId);

        bedExternalService.updateBedStatusOccupied(episodeBo.getBedId());

        Integer responsibleDoctorId = episodeBo.getResponsibleDoctorId();
        if (responsibleDoctorId != null)
            healthcareProfessionalExternalService.addHealthcareProfessionalGroup(episodeBo.getId(), responsibleDoctorId);

        responsibleContactService.addResponsibleContact(episodeBo.getResponsibleContact(), episodeBo.getId());

        log.debug("Output -> {}", episodeBo);
        return episodeBo;
    }

    private void assertContextValid(InternmentEpisodeBo episodeBo) {
        if (featureFlagsService.isOn(AppFeature.RESPONSIBLE_DOCTOR_REQUIRED))
            Assert.notNull(episodeBo.getResponsibleDoctorId(), "{internment.responsible.doctor.required}");

        this.validateEntryDate(episodeBo);

        boolean hasIntermentEpisodeActiveInInstitution = internmentEpisodeStorage.hasIntermentEpisodeActiveInInstitution(episodeBo.getPatientId(), episodeBo.getInstitutionId());
        if (hasIntermentEpisodeActiveInInstitution)
            throw new CreateInternmentEpisodeException(CreateInternmentEpisodeEnumException.HAS_INTERNMENT_EPISODE_ACTIVE_IN_INSTITUTION, "El paciente ya posee un episodio activo de internación en la institución");

        boolean bedIsOccupied = internmentEpisodeService.existsActiveForBedId(episodeBo.getBedId());
        if (bedIsOccupied)
            throw new CreateInternmentEpisodeException(CreateInternmentEpisodeEnumException.OCCUPIED_BED, "La cama seleccionada para asignar, ya fue ocupada");

    }

    private void validateEntryDate(InternmentEpisodeBo episodeBo) {
        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime entryDate = episodeBo.getEntryDate();

        if (entryDate == null)
            throw new CreateInternmentEpisodeException(CreateInternmentEpisodeEnumException.INVALID_ENTRY_DATE, "La fecha de alta de una internación es obligatorio");
        if (nowDate.minusDays(1).toLocalDate().atStartOfDay().isAfter(entryDate))
            throw new CreateInternmentEpisodeException(CreateInternmentEpisodeEnumException.INVALID_ENTRY_DATE, "La fecha de alta de una internación no debe ser previa al día anterior");
        if (nowDate.isBefore(entryDate))
            throw new CreateInternmentEpisodeException(CreateInternmentEpisodeEnumException.INVALID_ENTRY_DATE, "La fecha de alta de una internación no debe ser superior a la actual");
    }

}
