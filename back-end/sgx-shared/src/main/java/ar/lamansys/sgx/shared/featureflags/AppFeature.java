package ar.lamansys.sgx.shared.featureflags;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum AppFeature implements Feature {
 
    @Label("Indica si se puede dar de alta una internación sin tener una epicrisis asociada")
    HABILITAR_ALTA_SIN_EPICRISIS,

    @Label("Indica si el diagnostico principal en una internación es obligatorio")
    MAIN_DIAGNOSIS_REQUIRED,

    @Label("Indica si el médico responsable de una internación es obligatorio")
    RESPONSIBLE_DOCTOR_REQUIRED,

    @Label("Indica si es posible cargar la fecha probable de alta de una internación")
    HABILITAR_CARGA_FECHA_PROBABLE_ALTA,

    @Label("Indica si se debe habilitar la funcionalidad gestión de turnos")
    HABILITAR_GESTION_DE_TURNOS,

    @Label("Indica si se debe habilitar la funcionalidad historia clinica ambulatoria")
    HABILITAR_HISTORIA_CLINICA_AMBULATORIA,

    @Label("Indica si esta habilitada la funcionalidad para actualizar documentos de internación")
    HABILITAR_UPDATE_DOCUMENTS,

    @Label("Indica si esta habilitada la funcionalidad para editar un paciente")
    HABILITAR_EDITAR_PACIENTE_COMPLETO,

    @Label("Indica si esta habilitado el modulo de Guardia")
    HABILITAR_MODULO_GUARDIA,

    @Label("Indica si esta habilitado el módulo de Portal de Paciente")
    HABILITAR_MODULO_PORTAL_PACIENTE,

    @Label("Indica si esta habilitado el menu configuracion")
    HABILITAR_CONFIGURACION,

    @Label("Indica si está habilitado el Bus de Interoperabilidad")
    HABILITAR_BUS_INTEROPERABILIDAD,

    @Label("Indica si está habilitado el módulo de odontología")
    HABILITAR_ODONTOLOGY,

    @Label("Indica si está habilitado el módulo de Reportes")
    HABILITAR_REPORTES,

    @Label("Indica si está habilitado la nueva versión de aplicación de vacunas")
    HABILITAR_VACUNAS_V2,
    
    @Label("Indica si está habilitada la funcionalidad para descargar informes desde turnos y desde el perfil del paciente")
    HABILITAR_INFORMES,

    @Label("Habilita los llamados")
    HABILITAR_LLAMADO,

    ;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }
}