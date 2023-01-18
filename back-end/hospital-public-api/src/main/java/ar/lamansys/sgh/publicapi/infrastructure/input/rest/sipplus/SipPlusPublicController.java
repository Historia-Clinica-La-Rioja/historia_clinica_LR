package ar.lamansys.sgh.publicapi.infrastructure.input.rest.sipplus;

import ar.lamansys.sgh.publicapi.application.sipplus.getmotherbasicdata.GetMotherBasicData;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.sipplus.MotherIdentificationInfoDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import net.minidev.json.JSONObject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/public-api/sip-plus")
@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Public Api", description = "Sip Plus")
public class SipPlusPublicController {

	private final GetMotherBasicData getMotherBasicData;

	@PostMapping(value = "/mother-basic-data")
	public JSONObject getMotherBasicData(@RequestBody MotherIdentificationInfoDto motherInfoDto) {
		log.debug("Input parameter -> motherInfoDto {} ", motherInfoDto);
		JSONObject result = getMotherBasicData.run(motherInfoDto.getIdentificationType(), motherInfoDto.getIdentificationNumber());
		log.debug("Get basic mother information", result.toJSONString());
		return result;
	}

}
