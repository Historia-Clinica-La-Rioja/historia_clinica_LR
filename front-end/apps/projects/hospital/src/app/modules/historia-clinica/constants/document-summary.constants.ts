import { IDENTIFIER_CASES } from "../../hsi-components/identifier-cases/identifier-cases.component"

export const VITAL_SIGNS_AND_RISK_FACTORS = {
    KG: 'Kg',
    PERCENTAJE: '%',
    MG_DL: 'mg/dl',
    MILIMITERS:'mm',
    MINUTE: '/min',
    TEMPERATURE: '°',
}

export const ANESTHESIC_CLINICAL_EVALUATION = {
    PERCENTAJE: '%',
}

export const PROCEDURES_DESCRIPTION_ITEM = {
    title: 'internaciones.documents-summary.procedure.TITLE',
    subtitle: 'internaciones.documents-summary.procedure.REGISTERED_PROCEDURES',
    icon: 'library_add',
}

export const ALLERGIES_DESCRIPTION_ITEM = {
    title: 'internaciones.documents-summary.allergies.TITLE',
    subtitle: 'internaciones.documents-summary.allergies.RECORDED_ALLERGIES',
    icon: 'cancel',
}

export const VACCINES_DESCRIPTION_ITEM = {
    title: 'internaciones.documents-summary.vaccines.TITLE',
    subtitle: 'internaciones.documents-summary.vaccines.RECORDED_VACCINES',
    icon: 'vaccines',
}

export const PERSONAL_HISTORIES_DESCRIPTION_ITEM = {
    title: 'internaciones.documents-summary.personal-histories.TITLE',
    subtitle: 'internaciones.documents-summary.personal-histories.RECORDED_PERSONAL_HISTORIES',
    icon: 'report',
}

export const FAMILY_HISTORIES_DESCRIPTION_ITEM = {
    title: 'internaciones.documents-summary.family-histories.TITLE',
    subtitle: 'internaciones.documents-summary.family-histories.RECORDED_FAMILY_HISTORIES',
    icon: 'report',
}

export const USUAL_MEDICATIONS_DESCRIPTION_ITEM = {
    title: 'internaciones.documents-summary.usual-medications.TITLE',
    subtitle: 'internaciones.documents-summary.usual-medications.RECORDED_USUAL_MEDICATIONS',
    icon: 'event_available',
}

export const OTHER_PROBLEMS_DESCRIPTION_ITEM = {
    title: 'internaciones.documents-summary.other-problems.TITLE',
    subtitle: 'internaciones.documents-summary.other-problems.REGISTERED_OTHER_PROBLEMS',
    icon: 'event_available',
}

export const PROPOSED_SURGERIES_DESCRIPTION_ITEM = {
    title: 'internaciones.anesthesic-report.proposed-surgery.TITLE',
    subtitle: 'internaciones.anesthesic-report.proposed-surgery.REGISTERED_PROPOSED_SURGERIES',
    icon: 'assignment',
}

export const ANESTHETIC_HISTORY_DESCRIPTION_ITEM = {
    title: 'internaciones.anesthesic-report.anesthetic-history.TITLE',
    subtitle: 'internaciones.anesthesic-report.anesthetic-history.PREVIOUS_ANESTHESIA',
    icon: 'assignment',
}

export const ANESTHETIC_PLAN_DESCRIPTION_ITEM = {
    title: 'internaciones.anesthesic-report.anesthetic-plan.TITLE',
    subtitle: 'internaciones.anesthesic-report.anesthetic-plan.PLAN_RECORDS',
    icon: 'assignment',
}

export const ANALGESIC_TECHNIQUE_DESCRIPTION_ITEM = {
    title: 'internaciones.anesthesic-report.analgesic-technique.TITLE',
    subtitle: 'internaciones.anesthesic-report.analgesic-technique.TECHNIQUE_RECORDS',
    icon: 'assignment',
}

export const ANESTHETIC_TECHNIQUE_DESCRIPTION_ITEM = {
    title: 'internaciones.anesthesic-report.anesthetic-technique.TITLE',
    subtitle: 'internaciones.anesthesic-report.anesthetic-technique.TECHNIQUE_RECORDS',
    icon: 'assignment',
}

export const FLUID_ADMINISTRATION_DESCRIPTION_ITEM = {
    title: 'internaciones.anesthesic-report.fluid-administration.TITLE',
    subtitle: 'internaciones.anesthesic-report.fluid-administration.FLUID_RECORDS',
    icon: 'assignment',
}

export const ANESTHETIC_AGENTS_DESCRIPTION_ITEM = {
    title: 'internaciones.anesthesic-report.anesthetic-agents.TITLE',
    subtitle: 'internaciones.anesthesic-report.anesthetic-agents.REGISTERED_ANESTHETIC_AGENTS',
    icon: 'assignment',
}

export const NON_ANESTHETIC_DRUGS_DESCRIPTION_ITEM = {
    title: 'internaciones.anesthesic-report.non-anesthetic-drugs.TITLE',
    subtitle: 'internaciones.anesthesic-report.non-anesthetic-drugs.REGISTERED_NON_ANESTHETIC_DRUGS',
    icon: 'assignment',
}

export const ANTIBIOTIC_PROPHYLAXIS_DESCRIPTION_ITEM = {
    title: 'internaciones.anesthesic-report.antibiotic-prophylaxis.TITLE',
    subtitle: 'internaciones.anesthesic-report.antibiotic-prophylaxis.REGISTERED_ANTIBIOTIC_PROPHYLAXIS',
    icon: 'assignment',
}

export const REASONS_DESCRIPTION_ITEM = {
    title: 'guardia.documents-summary.reasons.TITLE',
    subtitle: 'guardia.documents-summary.reasons.REGISTERED_REASONS',
    icon: 'feedback',
}

export const HEADER_DATA_SCOPE = {
    dataId: "scope",
    identifierCase: IDENTIFIER_CASES.SCOPE,
}

export const HEADER_DATA_SPECIALTY = {
    dataId: "specialty",
    identifierCase: IDENTIFIER_CASES.SPECIALTY
}

export const HEADER_DATA_DATE = {
    dataId: "dateTime",
    identifierCase: IDENTIFIER_CASES.DATE,
}

export const HEADER_DATA_PATIENT = {
    dataId: "patient",
    identifierCase: IDENTIFIER_CASES.PATIENT,
}

export const HEADER_DATA_PROFESSIONAL = {
    dataId: "professional",
    identifierCase: IDENTIFIER_CASES.PROFESSIONAL,
}

export const HEADER_DATA_INSTITUTION = {
    dataId: "institution",
    identifierCase: IDENTIFIER_CASES.INSTITUTION,
}

export const HEADER_DATA_SECTOR = {
    dataId: "sector",
    identifierCase: IDENTIFIER_CASES.SECTOR,
}

export const HEADER_DATA_ROOM = {
    dataId: "room",
    identifierCase: IDENTIFIER_CASES.ROOM,
}

export const HEADER_DATA_BED = {
    dataId: "bed",
    identifierCase: IDENTIFIER_CASES.BED,
}

export const ExternalCauseType = {
    ACCIDENT: "Accidente",
    SELF_INFLICTED_INJURY: "Lesión autoinfligida",
    AGRESSION: "Agresión",
    IGNORED: "Se ignora",
}

export const EventLocation = {
    DOMICILIO_PARTICULAR: "Domicilio particular",
    VIA_PUBLICA: "Vía pública",
    LUGAR_DE_TRABAJO: "Lugar de trabajo",
    OTRO: "Otro",
}

export const PregnancyTerminationType = {
    VAGINAL: "Vaginal",
    CESAREAN: "Cesárea",
    UNDEFINED: "Sin definir",
}

export const BirthConditionType = {
    BORN_ALIVE: "Vivo",
	FETAL_DEATH: "Defunción fetal",
}

export const Gender = {
    MALE: "Masculino",
	FEMALE: "Femenino",
	INDETERMINATE: "Indeterminado",
}