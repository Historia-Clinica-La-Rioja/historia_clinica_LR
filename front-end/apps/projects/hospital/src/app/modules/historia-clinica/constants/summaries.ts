import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { SearchField } from '../components/documents-summary/documents-summary.component';
import { EDocumentSearch } from '@api-rest/api-model';

export const INTERNACION: SummaryHeader = {
	title: 'Resumen de internación',
	matIcon: 'single_bed'
};

export const GUARDIA: SummaryHeader = {
	title: 'Resumen de guardia',
	matIcon: 'local_hotel'
};

export const DIAGNOSTICO_PRINCIPAL: SummaryHeader = {
	title: 'internaciones.internacion-paciente.main-diagnosis.TITLE',
	matIcon: 'local_hospital'
};

export const DIAGNOSTICOS: SummaryHeader = {
	title: 'internaciones.anamnesis.diagnosticos.TITLE',
	matIcon: 'queue'
};

export const FACTORES_DE_RIESGO: SummaryHeader = {
	title: 'Signos vitales y factores de riesgo',
	matIcon: 'favorite_border'
};

export const ANTROPOMETRICOS: SummaryHeader = {
	title: 'Información antropométrica',
	matIcon: 'accessibility_new'
};

export const ANTECEDENTES_PERSONALES: SummaryHeader = {
	title: 'Antecedentes personales',
	matIcon: 'error_outline'
};

export const ANTECEDENTES_FAMILIARES: SummaryHeader = {
	title: 'Antecedentes familiares',
	matIcon: 'error_outline'
};

export const MEDICACION: SummaryHeader = {
	title: 'Medicación',
	matIcon: 'event_available'
};

export const MEDICACION_HABITUAL: SummaryHeader = {
	title: 'Medicación habitual',
	matIcon: 'event_available'
};

export const ALERGIAS: SummaryHeader = {
	title: 'Alergias',
	matIcon: 'cancel'
};

export const VACUNAS: SummaryHeader = {
	title: 'Vacunas aplicadas',
	matIcon: 'event_available'
};

export const DOCUMENTS: SummaryHeader = {
	title: 'Evoluciones',
	matIcon: 'assignment'
};

export const VIOLENCE_SITUATION_LIST: SummaryHeader = {
	title: 'ambulatoria.paciente.violence-situations.violence-situation-list.TITLE',
	matIcon: 'history'
}

export const VIOLENCE_SITUATION_HISTORY: SummaryHeader = {
	title: 'ambulatoria.paciente.violence-situations.violence-situation-history.TITLE',
	matIcon: 'history'
}

export const DOCUMENTS_SEARCH_FIELDS: SearchField[] = [
	{
		field: EDocumentSearch.DIAGNOSIS,
		label: 'internaciones.documents-summary.search-fields.DIAGNOSIS',
	},
	{
		field: EDocumentSearch.DOCTOR,
		label: 'internaciones.documents-summary.search-fields.PROFESSIONAL',
	},
	{
		field: EDocumentSearch.CREATED_ON,
		label: 'internaciones.documents-summary.search-fields.CREATEDON',
	},
	{
		field: EDocumentSearch.DOCUMENT_TYPE,
		label: 'internaciones.documents-summary.search-fields.DOCUMENT_TYPE',
	}
];

export const PROBLEMAS_ACTIVOS: SummaryHeader = {
	title: 'ambulatoria.paciente.problemas.ACTIVOS',
	matIcon: 'error_outline'
};

export const PROBLEMAS_CRONICOS: SummaryHeader = {
	title: 'ambulatoria.paciente.problemas.CRONICOS',
	matIcon: 'report_problem'
};

export const PROBLEMAS_RESUELTOS: SummaryHeader = {
	title: 'ambulatoria.paciente.problemas.RESUELTOS',
	matIcon: 'check'
};

export const PROBLEMAS_ANTECEDENTES: SummaryHeader = {
	title: 'Problemas activos y crónicos',
	matIcon: 'error_outline'
};

export const PROBLEMAS_POR_ERROR: SummaryHeader = {
	title: 'Registros incorrectos',
	matIcon: 'report_problem'
};

export const PROBLEMAS_INTERNACION: SummaryHeader = {
	title: 'ambulatoria.paciente.problemas.INTERNACION',
	matIcon: 'check'
};

export const ORDENES_MEDICACION: SummaryHeader = {
	title: 'ambulatoria.paciente.ordenes_prescripciones.MEDICACION',
	matIcon: 'event_available'
};

export const INDICACIONES: SummaryHeader = {
	title: 'ambulatoria.paciente.ordenes_prescripciones.RECOMENDACIONES',
	matIcon: 'error_outline'
};

export const ESTUDIOS: SummaryHeader = {
	title: 'ambulatoria.paciente.ordenes_prescripciones.ESTUDIOS',
	matIcon: 'medical_services'
};

export const INTERNMENT_INDICATIONS: SummaryHeader = {
	title: 'indicacion.internment-card.TITLE',
	matIcon: 'event_available',
}

export const EMERGENCY_CARE_INDICATIONS: SummaryHeader = {
	title: 'Indicaciones de Guardia',
	matIcon: 'event_available',
}

export const NURSING_CARE: SummaryHeader = {
	title: 'indicacion.nursing-care.TITLE_CARD',
	matIcon: 'event_available',
}

export enum PatientType {
	PERMANENT = 1,
	VALIDATED = 2,
	TEMPORARY = 3,
	HISTORICAL = 4,
	PHONE = 5,
	REJECTED = 6,
	PERMANENT_NO_VALIDATED = 7,
	EMERGENCY_CARE_TEMPORARY = 8,
}

export enum AttentionPlace {
	CONSULTORIO = 1,
	SHOCKROOM = 2,
	HABITACION = 3,
}
