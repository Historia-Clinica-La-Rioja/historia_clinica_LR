package ar.lamansys.sgx.shared.masterdata.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;

import java.util.ArrayList;
import java.util.List;

public class EnumWriter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public EnumWriter(){}

    public static List<MasterDataDto> writeList(List<? extends Enum> list){
        List<MasterDataDto> result = new ArrayList<>();
        list.forEach(e -> {
                result.add(write(e));
        });
        return result;
    }

    public static <E extends Enum> MasterDataDto write(E item){
        try {
            return OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(item), MasterDataDto.class);
        } catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
        }
        return null;
    }
}
