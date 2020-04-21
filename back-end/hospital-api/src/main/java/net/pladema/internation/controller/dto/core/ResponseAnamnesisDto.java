package net.pladema.internation.controller.dto.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.controller.dto.ObservationsDto;
import net.pladema.internation.controller.dto.ips.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class ResponseAnamnesisDto extends AnamnesisDto {

    public Integer anamnesisId;

}
