package ar.lamansys.odontology.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DrawingsDto implements Serializable {

    @Nullable
    private String whole;

    @Nullable
    private String internal;

    @Nullable
    private String external;

    @Nullable
    private String central;

    @Nullable
    private String left;

    @Nullable
    private String right;

}
