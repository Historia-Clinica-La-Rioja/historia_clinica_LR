package ar.lamansys.immunization.infrastructure.input.service;

import ar.lamansys.immunization.application.fetchVaccineConditionApplicationInfo.FetchVaccineConditionApplicationInfo;
import ar.lamansys.immunization.application.fetchVaccineSchemeInfo.FetchVaccineSchemeInfo;
import ar.lamansys.immunization.domain.vaccine.VaccineSchemeBo;
import ar.lamansys.immunization.domain.vaccine.VaccineConditionApplicationBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.SharedImmunizationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineConditionDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineSchemeInfoDto;
import org.springframework.stereotype.Service;

@Service
public class SharedImmunizationPortImpl implements SharedImmunizationPort {

    private final FetchVaccineSchemeInfo fetchVaccineSchemeInfo;

    private final FetchVaccineConditionApplicationInfo fetchVaccineConditionApplicationInfo;

    public SharedImmunizationPortImpl(FetchVaccineSchemeInfo fetchVaccineSchemeInfo,
                                      FetchVaccineConditionApplicationInfo fetchVaccineConditionApplicationInfo) {
        this.fetchVaccineSchemeInfo = fetchVaccineSchemeInfo;
        this.fetchVaccineConditionApplicationInfo = fetchVaccineConditionApplicationInfo;
    }

    @Override
    public VaccineConditionDto fetchVaccineConditionInfo(Short id) {
        VaccineConditionApplicationBo vaccineConditionApplicationBo = fetchVaccineConditionApplicationInfo.run(id);
        VaccineConditionDto result = new VaccineConditionDto();
        result.setId(id);
        result.setDescription(vaccineConditionApplicationBo.getDescription());
        return result;
    }

    @Override
    public VaccineSchemeInfoDto fetchVaccineSchemeInfo(Short id) {
        VaccineSchemeBo schemeBo = fetchVaccineSchemeInfo.run(id);
        VaccineSchemeInfoDto result = new VaccineSchemeInfoDto();
        result.setId(schemeBo.getId());
        result.setDescription(schemeBo.getDescription());
        return result;
    }
}
