package net.pladema.hl7.dataexchange.mock;

import net.pladema.hl7.dataexchange.model.domain.ConditionVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockCondition {

    public static List<ConditionVo> mock(){
        List<ConditionVo> resources = new ArrayList<>();
        resources.add(newResource());
        return resources;
    }

    private static ConditionVo newResource(){
        ConditionVo resource = new ConditionVo();
        resource.setId("ID-Condition");
        resource.setClinicalStatus("55561003");
        resource.setVerificationStatus("59156000");
        resource.setSeverityCode("LA6751-7");
        resource.setSctidCode("195967001");
        resource.setSctidTerm("asma (trastorno)");
        resource.setCreatedOn(LocalDateTime.now());
        return resource;
    }
}
