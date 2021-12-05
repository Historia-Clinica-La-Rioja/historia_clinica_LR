package net.pladema.snowstorm.services.domain.semantics;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EclOperators {

    @ToString.Include
    private String or;

    @ToString.Include
    private String childrenOf;

    @ToString.Include
    private String childrenAndSelfOf;

    @ToString.Include
    private String memberOf;

    @ToString.Include
    private String minus;
}
