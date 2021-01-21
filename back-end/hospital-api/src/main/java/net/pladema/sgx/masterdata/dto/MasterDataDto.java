package net.pladema.sgx.masterdata.dto;

import java.util.Objects;
import java.util.stream.Stream;

public class MasterDataDto extends AbstractMasterdataDto<Number> {

    public boolean hasNoValues() {
        return Stream.of(getId(), getDescription()).allMatch(Objects::isNull);
    }

}
