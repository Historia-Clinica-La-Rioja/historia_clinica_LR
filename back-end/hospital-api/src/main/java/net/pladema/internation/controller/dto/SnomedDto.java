package net.pladema.internation.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class SnomedDto implements Serializable {

    @NotNull
    @NotEmpty
    private String id;

    @NotNull
    @NotEmpty
    private String fsn;

    private String parentId;

    private String parentFsn;
}
