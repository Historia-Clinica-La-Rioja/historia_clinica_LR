package net.pladema.hl7.dataexchange.mock;

import net.pladema.hl7.dataexchange.model.domain.ImmunizationVo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MockImmunization {

    public static List<ImmunizationVo> mock(){
        List<ImmunizationVo> resources = new ArrayList<>();
        resources.add(newResource());
        return resources;
    }

    private static ImmunizationVo newResource(){
        ImmunizationVo resource = new ImmunizationVo();
        resource.setId("ID-Immunization");
        resource.setStatus("255594003");
        resource.setImmunizationTerm("BCG Esquema Regular");
        resource.setImmunizationCode("62");
        resource.setAdministrationDate(LocalDate.of(2020, 10, 1));
        return resource;
    }
}
