package net.pladema.template.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TemplateNamesDto {

    private Long id;
    private String name;
}
