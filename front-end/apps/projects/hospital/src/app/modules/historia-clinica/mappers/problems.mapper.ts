import { DateFormat } from "@core/utils/date.utils"
import { HistoricalProblems } from "@historia-clinica/modules/ambulatoria/services/historical-problems-facade.service"
import { Detail } from "@presentation/components/details-section-custom/details-section-custom.component"
import { format } from "date-fns"

export const buildProblemHeaderInformation = (problem: HistoricalProblems): Detail[] => {
	let result = [
		{
			title: 'ambulatoria.paciente.problemas.historical-problems.CONSULTATION_DATE',
			text: format(problem.consultationDate, DateFormat.VIEW_DATE)
		},
		{
			title: 'ambulatoria.paciente.problemas.historical-problems.PROFESSIONAL',
			text: problem.professionalFullName
		},
		{
			title: 'ambulatoria.paciente.problemas.historical-problems.CONSULTATION_INSTITUTION',
			text: problem.institutionName
		},
	]
	if (problem.professionalsThatSignedNames?.length > 0) {
		let signedNames = {
			title: 'ambulatoria.paciente.problemas.historical-problems.SIGNING_PROFESSIONALS',
			text: problem.professionalsThatSignedNames.join(", "),
		}
		result.push(signedNames);
	}
	if (problem.professionalsThatDidNotSignAmount > 0) {
		let notSignAmount = {
			title: 'ambulatoria.paciente.problemas.historical-problems.PROFESSIONALS_PENDING_SIGNATURE',
			text: problem.professionalsThatDidNotSignAmount.toString()
		}
		result.push(notSignAmount);
	}
	return result;
}					