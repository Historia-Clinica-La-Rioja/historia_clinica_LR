package net.pladema.hsi.addons.billing.infrastructure.output.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillProceduresErrorResponseDto {
	private String error;
	private String message;
	private String path;
	private Integer status;


//	{
//	"timestamp":"2024-01-26T19:04:44.393Z",
//	"status":500,
//	"error":"Internal Server Error",
//	"message":"El cuit de la obra social es obligatorio",
//	"path":"/api/addons/external/encounter"}

}
