package ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto;

import java.util.Objects;
import java.util.stream.Stream;

public class MasterDataDto extends AbstractMasterdataDto<Number> {

    public boolean hasNoValues() {
        return Stream.of(getId(), getDescription()).allMatch(Objects::isNull);
    }

    public static MasterDataDto fromShort(Short id, String description) {
    	var ret = new MasterDataDto();
    	ret.setId(id);
    	ret.setDescription(description);
    	return ret;
    }

}
