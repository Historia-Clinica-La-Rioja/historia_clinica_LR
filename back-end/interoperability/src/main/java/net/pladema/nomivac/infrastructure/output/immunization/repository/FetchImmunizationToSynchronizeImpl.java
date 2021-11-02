package net.pladema.nomivac.infrastructure.output.immunization.repository;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgx.shared.scheduling.infrastructure.output.service.SynchData;
import net.pladema.nomivac.domain.immunization.FetchImmunizationToSynchronize;
import net.pladema.nomivac.domain.immunization.ImmunizationBo;
import net.pladema.nomivac.domain.immunization.NomivacConditionInfoBo;
import net.pladema.nomivac.domain.immunization.NomivacDoseInfoBo;
import net.pladema.nomivac.domain.immunization.NomivacInstitutionBo;
import net.pladema.nomivac.domain.immunization.NomivacPatientBo;
import net.pladema.nomivac.domain.immunization.NomivacSchemeInfoBo;
import net.pladema.nomivac.domain.immunization.NomivacVaccineBo;
import net.pladema.nomivac.infrastructure.configuration.NomivacCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Conditional(NomivacCondition.class)
public class FetchImmunizationToSynchronizeImpl implements FetchImmunizationToSynchronize {

    private final ImmunizationPersistence immunizationPersistence;

    private final SharedPatientPort sharedPatientPort;

    private final SharedInstitutionPort sharedInstitutionPort;

    public FetchImmunizationToSynchronizeImpl(ImmunizationPersistence immunizationPersistence,
                                              SharedPatientPort sharedPatientPort,
                                              SharedInstitutionPort sharedInstitutionPort) {
        this.immunizationPersistence = immunizationPersistence;
        this.sharedPatientPort = sharedPatientPort;
        this.sharedInstitutionPort = sharedInstitutionPort;
    }

    @Override
    public Optional<ImmunizationBo> run() {
        return immunizationPersistence.firstEntityToSend().map(this::mapToImmunizationBo);
    }

    private ImmunizationBo mapToImmunizationBo(SynchData<VNomivacImmunizationData, NomivacImmunizationSync, Integer> synchData) {
        return ImmunizationBo
                .builder()
                .id(synchData.data.getId())
                .patient(getPatientData(synchData.data.getPatientId()))
                .institution(getInstitutionInfo(synchData.data.getInstitutionId()))
                .vaccine(new NomivacVaccineBo(synchData.data.getVaccineSctid(), synchData.data.getVaccinePt()))
                .statusId(synchData.data.getStatusId())
                .administrationDate(synchData.data.getAdministrationDate())
                .expirationDate(synchData.data.getExpirationDate())
                .condition(new NomivacConditionInfoBo(synchData.data.getConditionId(), synchData.data.getConditionDescription()))
                .scheme(new NomivacSchemeInfoBo(synchData.data.getSchemeId(), synchData.data.getSchemeDescription()))
                .dose(new NomivacDoseInfoBo(synchData.data.getDose(), synchData.data.getDoseOrder()))
                .lotNumber(synchData.data.getLotNumber())
                .note(synchData.data.getNote())
                .deleteable(synchData.shouldSendDelete())
                .build();
    }

    private NomivacInstitutionBo getInstitutionInfo(Integer institutionId) {
        return Optional.ofNullable(sharedInstitutionPort.fetchInstitutionById(institutionId))
                .map(institution -> new NomivacInstitutionBo(institution.getName(), institution.getSisaCode()))
                .orElse(null);
    }

    private NomivacPatientBo getPatientData(Integer patientId) {
        return Optional.ofNullable(sharedPatientPort.getBasicDataFromPatient(patientId))
                .map( patient ->
                        new NomivacPatientBo(
                                patient.getId(),
                                patient.getFirstName(),
                                patient.getMiddleName(),
                                patient.getLastName(),
                                patient.getIdentificationNumber()))
                .orElse(null);
    }
}
