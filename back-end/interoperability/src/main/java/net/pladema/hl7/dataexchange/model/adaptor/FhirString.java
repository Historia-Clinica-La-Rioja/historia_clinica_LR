package net.pladema.hl7.dataexchange.model.adaptor;

import lombok.experimental.UtilityClass;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class FhirString {

    public static String joining(String...args){
        return joining(' ', args);
    }

    public static String joining(Character delimiter, String...args){
        return Stream.of(args)
                .filter(data-> data != null && !data.isBlank())
                .collect(Collectors.joining(delimiter.toString()));
    }
}
