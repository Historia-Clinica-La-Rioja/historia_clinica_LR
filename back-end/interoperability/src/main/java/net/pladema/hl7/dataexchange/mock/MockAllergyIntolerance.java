package net.pladema.hl7.dataexchange.mock;

import net.pladema.hl7.dataexchange.model.domain.AllergyIntoleranceVo;

import java.util.ArrayList;
import java.util.List;

public class MockAllergyIntolerance {

    public static List<AllergyIntoleranceVo> mock(){
        List<AllergyIntoleranceVo> resources = new ArrayList<>();
        resources.add(newResource());
        return resources;
    }

    private static AllergyIntoleranceVo newResource(){
        AllergyIntoleranceVo resource = new AllergyIntoleranceVo();
        resource.setId("ID-Allergy");
        resource.setClinicalStatus("55561003");
        resource.setVerificationStatus("59156000");
        return resource;
    }
}
