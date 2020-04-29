package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class GeneralHealthConditionBo implements Serializable {

    private List<HealthConditionBo> diagnosis = new ArrayList<>();

    private List<HealthHistoryCondition> personalHistories = new ArrayList<>();

    private List<HealthHistoryCondition> familyHistories = new ArrayList<>();
}
