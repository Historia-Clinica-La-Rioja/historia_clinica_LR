import { OutpatientReasonDto } from "@api-rest/api-model";
import { MotivoConsulta } from "@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service";

const toOutpatientReason = (reason: MotivoConsulta): OutpatientReasonDto => {
	return {
		snomed: reason.snomed
	}
}

export const toOutpatientReasons = (reasons: MotivoConsulta[]): OutpatientReasonDto[] => {
	return reasons.map(reason => toOutpatientReason(reason));
}

const toMotive = (motive: OutpatientReasonDto): MotivoConsulta => {
	return { snomed: motive.snomed }
}

export const toMotives = (motives: OutpatientReasonDto[]): MotivoConsulta[] => {
	return motives.map(motive => toMotive(motive));
}