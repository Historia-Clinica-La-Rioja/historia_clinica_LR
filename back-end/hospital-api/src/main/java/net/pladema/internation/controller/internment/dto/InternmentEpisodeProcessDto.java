package net.pladema.internation.controller.internment.dto;


import lombok.*;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InternmentEpisodeProcessDto {

    @Nullable
    private Integer id;

    private boolean inProgress = false;

}
