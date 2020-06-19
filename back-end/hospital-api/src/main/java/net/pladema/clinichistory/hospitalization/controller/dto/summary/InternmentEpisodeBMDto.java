package net.pladema.clinichistory.hospitalization.controller.dto.summary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.dto.InternmentEpisodeADto;

@Getter
@Setter
@NoArgsConstructor
public class InternmentEpisodeBMDto extends InternmentEpisodeADto {

    private Integer id;
}
