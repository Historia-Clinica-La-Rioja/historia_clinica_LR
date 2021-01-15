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

    public List<MasterDataDto> writeList(List<? extends Enum> list){
        List<MasterDataDto> result = new ArrayList<>();
        list.forEach(e -> {
                result.add(write(e));
        });
        return result;
    }

    public <E extends Enum> MasterDataDto write(E item){
        try {
            return jackson.readValue(jackson.writeValueAsString(item), MasterDataDto.class);
        } catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
        }
        return null;
    }
}
