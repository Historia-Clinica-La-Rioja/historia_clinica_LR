package net.pladema.hl7.dataexchange.mock;

import net.pladema.hl7.dataexchange.model.domain.MedicationVo;

import java.util.ArrayList;
import java.util.List;

public class MockMedication {

    public static List<MedicationVo> mock(){
        List<MedicationVo> resources = new ArrayList<>();
        resources.add(newResource());
        return resources;
    }

    private static MedicationVo newResource(){
        MedicationVo resource = new MedicationVo();
        resource.setId("ID-Medication");
        resource.setStatementId("ID-MedicationStatement");
        resource.setStatus("55561003");
        return resource;
    }




}
