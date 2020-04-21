package net.pladema.internation.service.domain.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class AnthropometricData implements Serializable {

    private String bloodType;

    private String heigth;

    private String weigth;
}
