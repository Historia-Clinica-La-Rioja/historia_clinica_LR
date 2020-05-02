package net.pladema.internation.controller.dto.core;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnamnesisSummaryDto implements Serializable {

    private Long id;

    private boolean confirmed = false;
}
