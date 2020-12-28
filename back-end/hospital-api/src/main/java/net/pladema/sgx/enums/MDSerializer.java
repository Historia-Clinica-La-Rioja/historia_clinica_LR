package net.pladema.sgx.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class MDSerializer extends StdSerializer<MasterDataEnum> {

    protected MDSerializer() {
        super(MasterDataEnum.class);
    }

    @Override
    public void serialize(MasterDataEnum value,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("id");
        jsonGenerator.writeNumber(value.getId());
        jsonGenerator.writeFieldName("description");
        jsonGenerator.writeString(value.getDescription());
        jsonGenerator.writeEndObject();
    }
}