package net.pladema.template.domain;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIncludeProperties({ "text" })
public class TextTemplateBo extends DocumentTemplateBo {

    private String text;

    @Override
    public String toString() {
        return "ConclusionTemplateBo{" + "text=" + text +
                ", super=" + super.toString() +
                '}';
    }
}
