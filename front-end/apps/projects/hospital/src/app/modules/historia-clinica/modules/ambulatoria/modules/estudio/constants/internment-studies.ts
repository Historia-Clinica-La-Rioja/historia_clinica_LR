import { Title } from "@presentation/components/indication/indication.component";

export enum StudyCategories {
	IMAGE = 'ambulatoria.paciente.studies.sections.IMAGES',
	LABORATORY = 'ambulatoria.paciente.studies.sections.LABORATORY',
	PATHOLOGIC_ANATOMY = 'ambulatoria.paciente.studies.sections.PATHOLOGIC_ANATOMY',
	HEMOTHERAPY = 'ambulatoria.paciente.studies.sections.HEMOTHERAPY',
	SURGICAL_PROCEDURE = 'ambulatoria.paciente.studies.sections.SURGICAL_PROCEDURE',
	OTHER_PROCEDURES_OR_PRACTICE = 'ambulatoria.paciente.studies.sections.OTHER_PROCEDURES_AND_PRACTICES',
	ADVICE = 'ambulatoria.paciente.studies.sections.ADVICE',
	EDUCATION = 'ambulatoria.paciente.studies.sections.EDUCATION'
}

export const IMAGES: Title = {
	title: StudyCategories.IMAGE,
	svgIcon: 'images',
}

export const LABORATORY: Title = {
	title: StudyCategories.LABORATORY,
	svgIcon: 'laboratory',
}

export const PATHOLOGIC_ANATOMY: Title = {
	title: StudyCategories.PATHOLOGIC_ANATOMY,
	svgIcon: 'pathologic_anatomy',
}

export const HEMOTHERAPY: Title = {
	title: StudyCategories.HEMOTHERAPY,
	svgIcon: 'hemotherapy',
}

export const SURGICAL_PROCEDURE: Title = {
	title: StudyCategories.SURGICAL_PROCEDURE,
	svgIcon: 'surgical_procedure',
}

export const OTHER_PROCEDURES_AND_PRACTICES: Title = {
	title: StudyCategories.OTHER_PROCEDURES_OR_PRACTICE,
	svgIcon: 'other_procedures_and_practices',
}

export const ADVICE: Title = {
	title: StudyCategories.ADVICE,
	svgIcon: 'advice',
}

export const EDUCATION: Title = {
	title: StudyCategories.EDUCATION,
	svgIcon: 'education',
}
