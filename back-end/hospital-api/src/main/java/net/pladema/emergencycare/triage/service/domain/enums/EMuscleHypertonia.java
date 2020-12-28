package net.pladema.emergencycare.triage.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.pladema.sgx.enums.MDSerializer;
import net.pladema.sgx.enums.MasterDataEnum;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonSerialize(using = MDSerializer.class)
public enum EMuscleHypertonia implements MasterDataEnum {

    HIPERTONIA(1, "Hipertonía"),
    HIPOTONIA(2, "Hipotonía"),
    NORMAL(3, "Normal");

    private final Short id;
    private final String description;

    EMuscleHypertonia(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static List<EMuscleHypertonia> getAll(){
        return Stream.of(EMuscleHypertonia.values()).collect(Collectors.toList());
    }
    @JsonValue
    public Short getId() {
        return id;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
