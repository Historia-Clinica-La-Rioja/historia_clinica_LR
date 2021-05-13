package ar.lamansys.odontology.domain;

import ar.lamansys.odontology.infrastructure.controller.dto.OdontologySnomedDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToothBo implements Comparable<ToothBo>{
    private OdontologySnomedBo snomed;
    private String code;

    public Integer getQuadrant() {
        return Integer.parseInt(String.valueOf(code.charAt(0)));
    }

    public Integer getPosition() {
        return Integer.parseInt(String.valueOf(code.charAt(1)));
    }

    @Override
    public int compareTo(ToothBo o) {
        return this.getPosition().compareTo(o.getPosition());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ToothBo) {
            return this.code.equals(((ToothBo)obj).getCode());
        }
        return false;
    }
}
