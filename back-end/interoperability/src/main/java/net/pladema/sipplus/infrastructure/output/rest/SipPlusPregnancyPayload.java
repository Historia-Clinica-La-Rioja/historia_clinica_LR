package net.pladema.sipplus.infrastructure.output.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

import java.util.Map;

@Slf4j
@Builder
@ToString
public class SipPlusPregnancyPayload {

	@JsonProperty("pregnancies")
	private Map<String, JSONObject> pregnancies;

}