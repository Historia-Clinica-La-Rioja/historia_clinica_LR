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
public enum ERespiratoryRetraction implements MasterDataEnum {

    INTERCOSTAL(1, "Intercostal"),
    GENERALIZADO(2, "Generalizado"),
    NO((short)3, "No");

    private final Short id;
    private final String description;

    ERespiratoryRetraction(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static List<ERespiratoryRetraction> getAll(){
        return Stream.of(ERespiratoryRetraction.values()).collect(Collectors.toList());
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
