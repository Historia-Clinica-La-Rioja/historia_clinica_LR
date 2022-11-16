package ar.lamansys.sgx.shared.flavor.instances;

import java.util.EnumMap;
import java.util.Optional;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.states.InitialFeatureStates;

public class HospitalsFeatureStates implements InitialFeatureStates {

	private final EnumMap<AppFeature, Boolean> map;

	public HospitalsFeatureStates() {
		map = new EnumMap<>(AppFeature.class);
		map.put(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS, false);
		map.put(AppFeature.MAIN_DIAGNOSIS_REQUIRED, true);
		map.put(AppFeature.RESPONSIBLE_DOCTOR_REQUIRED, true);
		map.put(AppFeature.HABILITAR_CARGA_FECHA_PROBABLE_ALTA, true);
		map.put(AppFeature.HABILITAR_GESTION_DE_TURNOS, true);
		map.put(AppFeature.HABILITAR_HISTORIA_CLINICA_AMBULATORIA, true);
		map.put(AppFeature.HABILITAR_EDITAR_PACIENTE_COMPLETO, false);
		map.put(AppFeature.HABILITAR_MODULO_GUARDIA, false);
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
		map.put(AppFeature.HABILITAR_DESCARGA_DOCUMENTOS_PDF, false);
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
		map.put(AppFeature.HABILITAR_EXTENSIONES_WEB_COMPONENTS, false);
		map.put(AppFeature.HABILITAR_NOTIFICACIONES_TURNOS, false);
		map.put(AppFeature.HABILITAR_GUARDADO_CON_CONFIRMACION_CONSULTA_AMBULATORIA, true);
		map.put(AppFeature.HABILITAR_REPORTES_ESTADISTICOS, false);
		map.put(AppFeature.HABILITAR_VISUALIZACION_DE_CARDS, false);
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