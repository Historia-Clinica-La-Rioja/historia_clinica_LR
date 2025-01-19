import { CompletedFormSummaryDto, CompletedParameterSummaryDto, EParameterType, HCEEvolutionSummaryDto } from "@api-rest/api-model"
import { dateTimeDtoToDate } from "@api-rest/mapper/date-dto.mapper"
import { capitalizeSentence } from "@core/utils/core.utils"
import { dateToViewDate } from "@core/utils/date.utils"
import { AssociatedParameterizedFormInformation } from "@historia-clinica/modules/ambulatoria/components/associated-parameterized-form-information/associated-parameterized-form-information.component"
import { HistoricalProblems } from "@historia-clinica/modules/ambulatoria/services/historical-problems-facade.service"
import { Detail } from "@presentation/components/details-section-custom/details-section-custom.component"


export const buildProblemHeaderInformation = (problem: HistoricalProblems): Detail[] => {
	const result = [
		{
			title: 'ambulatoria.paciente.problemas.historical-problems.CONSULTATION_DATE',
			text: dateToViewDate(problem.consultationDate)
		},
		{
			title: 'ambulatoria.paciente.problemas.historical-problems.PROFESSIONAL',
			text: capitalizeSentence(problem.professionalFullName)
		},
		{
			title: 'ambulatoria.paciente.problemas.historical-problems.CONSULTATION_INSTITUTION',
			text: problem.institutionName
		},
	]
	if (problem.professionalsThatSignedNames?.length) {
		const signedNames = {
			title: 'ambulatoria.paciente.problemas.historical-problems.SIGNING_PROFESSIONALS',
			text: capitalizeSentence(problem.professionalsThatSignedNames.join(", ")),
		}
		result.push(signedNames);
	}
	if (problem.professionalsThatDidNotSignAmount) {
		const notSignAmount = {
			title: 'ambulatoria.paciente.problemas.historical-problems.PROFESSIONALS_PENDING_SIGNATURE',
			text: problem.professionalsThatDidNotSignAmount.toString()
		}
		result.push(notSignAmount);
	}
	return result;
}

const getNumericalParameterValue = (oldValue: string, newValue: string): string => {
	const hasOldValue = !!oldValue;
	const hasNewValue = !!newValue;

	if (hasOldValue) {
		return hasNewValue ? `${oldValue} | ${newValue}` : oldValue;
	} else {
		return hasNewValue ? newValue : null;
	}
}

const toAssociatedParameterInformation = (parameters: CompletedParameterSummaryDto[]): Map<string, string> => {
	return parameters.reduce((parameterSectionMap: Map<string, string>, parameter: CompletedParameterSummaryDto) => {

		if (!parameterSectionMap.has(parameter.description))
			parameterSectionMap.set(parameter.description, null);

		let valueToSet = parameter.completedValue;

		if (parameter.type == EParameterType.NUMERIC) {
			const oldValue = parameterSectionMap.get(parameter.description);
			valueToSet = getNumericalParameterValue(oldValue, valueToSet);
		}

		parameterSectionMap.set(parameter.description, valueToSet);

		return parameterSectionMap;
	}, new Map<string, string>());
}

const toAssociatedParameterizedFormInformation = (completedFormSummaryDto: CompletedFormSummaryDto[]): AssociatedParameterizedFormInformation[] => {
	return completedFormSummaryDto.map(form => {
		return {
			name: form.formName,
			parameters: toAssociatedParameterInformation(form.parameters)
		}
	});
}

export const toHistoricalProblems = (hceEvolutionSummaryDto: HCEEvolutionSummaryDto[]): HistoricalProblems[] => {
	return hceEvolutionSummaryDto.reduce((historicalProblemsList, currentOutpatientEvolutionSummary) => {
		currentOutpatientEvolutionSummary.healthConditions.length ?
			historicalProblemsList = [...historicalProblemsList, ...currentOutpatientEvolutionSummary.healthConditions.map(problem => ({
				consultationDate: currentOutpatientEvolutionSummary.startDate ? dateTimeDtoToDate(currentOutpatientEvolutionSummary.startDate) : null,
				consultationEvolutionNote: currentOutpatientEvolutionSummary.evolutionNote,
				professionalFullName: currentOutpatientEvolutionSummary.professional.person.fullName,
				consultationProfessionalId: currentOutpatientEvolutionSummary.professional.id,
				consultationProfessionalPersonId: currentOutpatientEvolutionSummary.professional.person.id,
				document: currentOutpatientEvolutionSummary.document,
				institutionName: currentOutpatientEvolutionSummary.institutionName,
				problemId: problem.snomed.sctid,
				problemPt: problem.snomed.pt,
				specialtyId: currentOutpatientEvolutionSummary.clinicalSpecialty?.id,
				specialityPt: currentOutpatientEvolutionSummary.clinicalSpecialty?.name,
				consultationReasons: currentOutpatientEvolutionSummary.reasons?.map(r => ({ reasonId: r.snomed.sctid, reasonPt: r.snomed.pt })),
				consultationProcedures: currentOutpatientEvolutionSummary.procedures.map(p => ({ procedureDate: p.performedDate, procedureId: p.snomed.sctid, procedurePt: p.snomed.pt })),
				reference: problem.references?.length > 0 ? problem.references : null,
				markedAsError: problem.isMarkedAsError,
				color: problem.isMarkedAsError ? 'grey-text' : 'primary',
				errorProblem: problem.errorProblem,
				professionalsThatDidNotSignAmount: currentOutpatientEvolutionSummary.electronicJointSignatureProfessionals?.professionalsThatDidNotSignAmount,
				professionalsThatSignedNames: currentOutpatientEvolutionSummary.electronicJointSignatureProfessionals?.professionalsThatSignedNames,
				...currentOutpatientEvolutionSummary.completedForms && { parameterizedForm: toAssociatedParameterizedFormInformation(currentOutpatientEvolutionSummary.completedForms) }
			}))] : historicalProblemsList = [...historicalProblemsList, {
				consultationDate: currentOutpatientEvolutionSummary.startDate ? dateTimeDtoToDate(currentOutpatientEvolutionSummary.startDate) : null,
				consultationEvolutionNote: currentOutpatientEvolutionSummary.evolutionNote,
				professionalFullName: currentOutpatientEvolutionSummary.professional.person.fullName,
				consultationProfessionalId: currentOutpatientEvolutionSummary.professional.id,
				consultationProfessionalPersonId: currentOutpatientEvolutionSummary.professional.person.id,
				document: currentOutpatientEvolutionSummary.document,
				institutionName: currentOutpatientEvolutionSummary.institutionName,
				problemId: 'Problema no informado',
				problemPt: 'Problema no informado',
				specialtyId: currentOutpatientEvolutionSummary.clinicalSpecialty?.id,
				specialityPt: currentOutpatientEvolutionSummary.clinicalSpecialty?.name,
				consultationReasons: currentOutpatientEvolutionSummary.reasons.map(r => ({ reasonId: r.snomed.sctid, reasonPt: r.snomed.pt })),
				consultationProcedures: currentOutpatientEvolutionSummary.procedures.map(p => ({ procedureDate: p.performedDate, procedureId: p.snomed.sctid, procedurePt: p.snomed.pt })),
				reference: null,
				color: 'primary',
				professionalsThatDidNotSignAmount: currentOutpatientEvolutionSummary.electronicJointSignatureProfessionals?.professionalsThatDidNotSignAmount,
				professionalsThatSignedNames: currentOutpatientEvolutionSummary.electronicJointSignatureProfessionals?.professionalsThatSignedNames,
				...currentOutpatientEvolutionSummary.completedForms && { parameterizedForm: toAssociatedParameterizedFormInformation(currentOutpatientEvolutionSummary.completedForms) }
			}];
		return historicalProblemsList;
	}, []);
}
