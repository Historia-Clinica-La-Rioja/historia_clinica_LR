package net.pladema.cipres.infrastructure.output.rest.domain.consultation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class CipresConsultationPayload {

	private Long idPaciente;

	private String fecha;

	private String motivoConsulta;

	private String especialidad;

	private String contactoMedicoPaciente;

	private String establecimiento;

	private List<CipresDatosClinicosPayload> datosClinico;

	private List<CipresSnomedPayload> diagnosticosSnomed;

	private List<CipresSnomedPayload> prestacionesSnomed;

	private List<CipresSnomedPayload> medicacionsSnomed;

}
