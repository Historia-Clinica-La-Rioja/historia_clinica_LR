import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { SearchField } from '../components/documents-summary/documents-summary.component';
import { EDocumentSearch } from '@api-rest/api-model';

export const INTERNACION: SummaryHeader = {
	title: 'Resumen de internación',
	matIcon: 'single_bed'
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
	title: 'Factores de riesgo',
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
	matIcon: 'queue'
};

export const VACUNAS: SummaryHeader = {
	title: 'Vacunas aplicadas',
	matIcon: 'event_available'
};

export const DOCUMENTS: SummaryHeader = {
	title: 'Evoluciones',
	matIcon: 'assignment'
};

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

export const NURSING_CARE: SummaryHeader = {
	title: 'indicacion.nursing-care.TITLE_CARD',
	matIcon: 'event_available',
}
