package net.pladema.imagenetwork.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyFileInfoDto {
    private String token;
    private String url;
    private String uuid;
    private Integer pacServerId;
}
