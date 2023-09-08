package ar.lamansys.sgh.clinichistory.domain.ips;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ConclusionBo extends HealthConditionBo {
    public ConclusionBo(SnomedBo snomedBo) {
        super(snomedBo);
    }

    @Override
    @JsonIgnore
    public boolean isActive() {
        return super.isActive();
    }

    @Override
    @JsonIgnore
    public boolean isError() {
        return super.isError();
    }

    @Override
    @JsonIgnore
    public boolean isDiscarded() {
        return super.isDiscarded();
    }

    @Override
    @JsonIgnore
    public boolean isMain() {
        return super.isMain();
    }

    @Override
    @JsonIgnore
    public String getSnomedSctid() {
        return super.getSnomedSctid();
    }

    @Override
    @JsonIgnore
    public String getSnomedPt() {
        return super.getSnomedPt();
    }


}
