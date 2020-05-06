package net.pladema.internation.repository.core.domain;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DocumentsSummaryVo {

    private AnamnesisSummaryVo anamnesis;

    private EpicrisisSummaryVo epicrisis;
}
