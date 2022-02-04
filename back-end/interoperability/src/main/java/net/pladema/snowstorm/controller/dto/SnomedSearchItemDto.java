package net.pladema.snowstorm.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SnomedSearchItemDto {

    private String conceptId;
    private String id;
    private FullySpecifiedNamesDto fsn;
    private PreferredTermDto pt;

}
