package net.pladema.cipres.infrastructure.output.rest.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CipresRegisterResponseDeserializer extends StdDeserializer<CipresRegisterResponse> {

	public CipresRegisterResponseDeserializer() {
		this(null);
	}

	public CipresRegisterResponseDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public CipresRegisterResponse deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException {
		JsonNode saludNode = jp.getCodec().readTree(jp);
		return new CipresRegisterResponse(saludNode.get("detail") == null ? null : saludNode.get("detail").textValue(),
				saludNode.get("type") == null ? null : saludNode.get("type").textValue(),
				saludNode.get("title") == null ? null : saludNode.get("title").textValue());
	}
}