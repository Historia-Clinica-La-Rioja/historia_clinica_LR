package net.pladema.sgx.masterdata.service.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.pladema.sgx.masterdata.dto.MasterDataDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnumWriter {

    private final ObjectMapper jackson;

    public EnumWriter(ObjectMapper jackson){
        this.jackson=jackson;
    }

    public List<MasterDataDto> write(List<? extends Enum> list){
        List<MasterDataDto> result = new ArrayList<>();
        list.forEach(e -> {
            try {
                result.add(jackson.readValue(jackson.writeValueAsString(e), MasterDataDto.class));
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
        });
        return result;
    }
}
