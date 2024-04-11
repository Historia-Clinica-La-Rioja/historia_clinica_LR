package ar.lamansys.sgx.shared.flavor.instances;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.states.InitialFeatureStates;

import java.util.EnumMap;
import java.util.Optional;

public class HospitalsFeatureStates implements InitialFeatureStates {

	private final EnumMap<AppFeature, Boolean> map; //

	public HospitalsFeatureStates() {
		map = new EnumMap<>(AppFeature.class);
		map.put(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS, false);
		map.put(AppFeature.MAIN_DIAGNOSIS_REQUIRED, true);
		map.put(AppFeature.RESPONSIBLE_DOCTOR_REQUIRED, true);
		map.put(AppFeature.HABILITAR_CARGA_FECHA_PROBABLE_ALTA, true);
		map.put(AppFeature.HABILITAR_GESTION_DE_TURNOS, true);
		map.put(AppFeature.HABILITAR_HISTORIA_CLINICA_AMBULATORIA, true);
		map.put(AppFeature.HABILITAR_EDITAR_PACIENTE_COMPLETO, false);
		map.put(AppFeature.HABILITAR_MODULO_GUARDIA, true);
		map.put(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE, true);
		map.put(AppFeature.HABILITAR_CONFIGURACION, true);
		map.put(AppFeature.HABILITAR_BUS_INTEROPERABILIDAD, false);
		map.put(AppFeature.HABILITAR_ODONTOLOGY, false);
		map.put(AppFeature.HABILITAR_REPORTES, false);
		map.put(AppFeature.HABILITAR_INFORMES, false);
		map.put(AppFeature.HABILITAR_LLAMADO, false);
		map.put(AppFeature.HABILITAR_HISTORIA_CLINICA_EXTERNA, false);
		map.put(AppFeature.HABILITAR_SERVICIO_RENAPER, true);
		map.put(AppFeature.RESTRINGIR_DATOS_EDITAR_PACIENTE, true);
		map.put(AppFeature.HABILITAR_INTERCAMBIO_TEMAS, false);
		map.put(AppFeature.HABILITAR_CREACION_USUARIOS, false);
		map.put(AppFeature.HABILITAR_REPORTE_EPIDEMIOLOGICO, false);
		map.put(AppFeature.AGREGAR_MEDICOS_ADICIONALES, false);
		map.put(AppFeature.HABILITAR_DESCARGA_DOCUMENTOS_PDF, true);
		map.put(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS, false);
		map.put(AppFeature.HABILITAR_VISUALIZACION_PROPIEDADES_SISTEMA, true);
		map.put(AppFeature.HABILITAR_GENERACION_ASINCRONICA_DOCUMENTOS_PDF, false);
		map.put(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS, false);
		map.put(AppFeature.HABILITAR_MAIL_RESERVA_TURNO, false);
		map.put(AppFeature.LIBERAR_API_RESERVA_TURNOS, false);
		map.put(AppFeature.BACKOFFICE_MOSTRAR_ABM_RESERVA_TURNOS, false);
		map.put(AppFeature.OCULTAR_LISTADO_PROFESIONES_WEBAPP, true);
		map.put(AppFeature.HABILITAR_MODULO_ENF_EN_DESARROLLO, false);
		map.put(AppFeature.HABILITAR_2FA, false);
		map.put(AppFeature.HABILITAR_EXTENSIONES_WEB_COMPONENTS, true);
		map.put(AppFeature.HABILITAR_NOTIFICACIONES_TURNOS, false);
		map.put(AppFeature.HABILITAR_GUARDADO_CON_CONFIRMACION_CONSULTA_AMBULATORIA, true);
		map.put(AppFeature.HABILITAR_REPORTES_ESTADISTICOS, false);
		map.put(AppFeature.HABILITAR_VISUALIZACION_DE_CARDS, true);
		map.put(AppFeature.HABILITAR_RECUPERAR_PASSWORD, true);
		map.put(AppFeature.HABILITAR_DESARROLLO_RED_IMAGENES, false);
		map.put(AppFeature.HABILITAR_SIP_PLUS, false);
		map.put(AppFeature.HABILITAR_VALIDACION_MATRICULAS_SISA, false);
		map.put(AppFeature.HABILITAR_RECETA_DIGITAL, false);
		map.put(AppFeature.HABILITAR_PRESCRIPCION_RECETA, false);
		map.put(AppFeature.HABILITAR_MODULO_AUDITORIA, false);
		map.put(AppFeature.HABILITAR_CAMPOS_CIPRES_EPICRISIS, false);
		map.put(AppFeature.HABILITAR_IMPRESION_HISTORIA_CLINICA_EN_DESARROLLO, false);
		map.put(AppFeature.HABILITAR_API_CONSUMER, true);
		map.put(AppFeature.HABILITAR_TELEMEDICINA, false);
		map.put(AppFeature.HABILITAR_REPORTE_REFERENCIAS_EN_DESARROLLO, true);
		map.put(AppFeature.HABILITAR_OBLIGATORIEDAD_UNIDADES_JERARQUICAS, false);
		map.put(AppFeature.HABILITAR_FIRMA_DIGITAL, false);
		map.put(AppFeature.HABILITAR_NUEVO_FORMATO_PDF_ORDENES_PRESTACION, false);
		map.put(AppFeature.HABILITAR_TURNOS_CENTRO_LLAMADO, false);
		map.put(AppFeature.HABILITAR_AUDITORIA_DE_ACCESO_EN_HC, false);
		map.put(AppFeature.HABILITAR_MONITOREO_CIPRES, false);
		map.put(AppFeature.HABILITAR_PARTE_ANESTESICO_EN_DESARROLLO, false);
		map.put(AppFeature.HABILITAR_AGENDA_DINAMICA, false);
		map.put(AppFeature.ROLES_API_PUBLICA_EN_DESARROLLO, false);
		map.put(AppFeature.HABILITAR_RECURRENCIA_EN_DESARROLLO, false);
		map.put(AppFeature.HABILITAR_FIRMA_CONJUNTA, false);
		map.put(AppFeature.HABILITAR_ACTUALIZACION_AGENDA, false);
		map.put(AppFeature.HABILITAR_ADMINISTRADOR_DATOS_PERSONALES, false);
		map.put(AppFeature.HABILITAR_ANEXO_II_MENDOZA, false);
		map.put(AppFeature.HABILITAR_GRAFICOS_EVOLUCIONES_ANTROPOMETRICAS_EN_DESARROLLO, false);
	}

	@Override
	public EnumMap<AppFeature, Boolean> getStates() {
		return map;
	}

	@Override
	public Optional<AppFeature> getAppFeatureByPropertyKey(String propertyKey) {
		if (propertyKey == null)
			return Optional.empty();
		return map.keySet().stream()
				.filter(appFeature -> appFeature.propertyNameFor().equals(propertyKey))
				.findFirst();
	}

}