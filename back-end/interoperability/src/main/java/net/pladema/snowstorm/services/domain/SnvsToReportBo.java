package net.pladema.snowstorm.services.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.snowstorm.controller.dto.SnvsToReportDto;
import net.pladema.snowstorm.repository.entity.SnvsReport;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SnvsToReportBo {

	private String apellido;

	private String nombre;

	private Integer tipoDocumento;

	private String numeroDocumento;

	private String fechaNacimiento;

	private String sexo;

	private String telefono;

	private String mail;

	private Integer paisEmisionTipoDocumento;

	private String seDeclaraPuebloIndigena;

	private String calle;

	private Integer idDepartamento;

	private Integer idLocalidad;

	private Integer idPais;

	private Integer idProvincia;

	private Integer idGrupoEvento;

	private Integer idEvento;

	private Integer idClasificacionManualCaso;

	private LocalDate fechaPapel;

	private Integer patientId;

	private Integer professionalId;

	private String status;

	private String institutionSisaCode;

	private Short responseCode;

	private Integer sisaRegisteredId;

	public SnvsToReportBo(SnvsToReportDto snvsToReportDto){
		this.idGrupoEvento = snvsToReportDto.getGroupEventId();
		this.idEvento = snvsToReportDto.getEventId();
		this.idClasificacionManualCaso = snvsToReportDto.getManualClassificationId();
		this.patientId = snvsToReportDto.getPatientId();
		this.professionalId = snvsToReportDto.getProfessionalId();
		this.institutionSisaCode = snvsToReportDto.getInstitutionSisaCode();
		//mock
		this.apellido = "Prueba";
		this.nombre = "Test1";
		this.tipoDocumento = 1;
		this.numeroDocumento = "34000001";
		this.sexo = "M";
		this.fechaNacimiento = "01-01-2000";
		this.seDeclaraPuebloIndigena = "No";
		this.calle = "Calle número 8000";
		this.idDepartamento = 4;
		this.idLocalidad = 6007010;
		this.idProvincia = 2;
		this.idPais = 200;
		this.telefono = "011-4224-0099";
		this.mail = "mail_mail@dominio.com";
	}

	public SnvsToReportBo(SnvsReport snvsReport){
		this.idGrupoEvento = snvsReport.getGroupEventId();
		this.idEvento = snvsReport.getEventId();
		this.idClasificacionManualCaso = snvsReport.getManualClassificationId();
		this.patientId = snvsReport.getPatientId();
		this.status = snvsReport.getStatus();
		this.responseCode = snvsReport.getResponseCode();
		this.professionalId = snvsReport.getProfessionalId();
		this.institutionSisaCode = snvsReport.getInstitutionId();
		this.sisaRegisteredId = snvsReport.getSisaRegisteredId();
		this.fechaPapel = snvsReport.getLastUpdate();
	}
}
