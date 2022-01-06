package net.pladema.snvs.infrastructure.output.rest.report.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class SnvsEventRegisterResponseDeserializer extends StdDeserializer<SnvsEventRegisterResponse> {

    public SnvsEventRegisterResponseDeserializer() {
        this(null);
    }

    public SnvsEventRegisterResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public SnvsEventRegisterResponse deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException {
        JsonNode snvsNode = jp.getCodec().readTree(jp);
        return new SnvsEventRegisterResponse(snvsNode.get("errors").textValue(),
                snvsNode.get("status").textValue(),
                snvsNode.get("timestamp").textValue(),
                snvsNode.get("resultado").textValue(),
                snvsNode.get("id_caso").intValue(),
                snvsNode.get("description").textValue());
    }
}