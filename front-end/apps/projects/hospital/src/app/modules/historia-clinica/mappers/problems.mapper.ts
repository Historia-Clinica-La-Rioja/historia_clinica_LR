import { capitalizeSentence } from "@core/utils/core.utils"
import { dateToViewDate } from "@core/utils/date.utils"
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
