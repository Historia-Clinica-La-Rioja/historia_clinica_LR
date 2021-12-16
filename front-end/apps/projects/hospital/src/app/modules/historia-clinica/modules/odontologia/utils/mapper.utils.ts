import { OdontologyAllergyConditionDto, OdontologyConceptDto, OdontologyDentalActionDto, OdontologyDiagnosticDto, OdontologyMedicationDto, OdontologyPersonalHistoryDto, OdontologyProcedureDto } from "@api-rest/api-model";
import { dateToDateDto } from "@api-rest/mapper/date-dto.mapper";
import { DateFormat, momentParse } from "@core/utils/moment.utils";
import { Alergia } from "@historia-clinica/modules/ambulatoria/services/alergias-nueva-consulta.service";
import { Medicacion } from "@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service";
import { AntecedentePersonal } from "@historia-clinica/modules/ambulatoria/services/personal-histories-new-consultation.service";
import { Problema } from "@historia-clinica/services/problemas.service";
import { Procedimiento } from "@historia-clinica/services/procedimientos.service";
import { ActionType, ToothAction } from "../services/actions.service";
import { ActionedTooth } from "../services/odontogram.service";
import { ESurfacePositionDtoValues } from "./surfaces";

export const toOdontologyAllergyConditionDto = (alergia: Alergia): OdontologyAllergyConditionDto => {
	return {
		categoryId: null,
		snomed: alergia.snomed,
		startDate: null,
		criticalityId: alergia.criticalityId,
	};
}

export const toOdontologyMedicationDto = (medicacion: Medicacion): OdontologyMedicationDto => {
	return {
		note: medicacion.observaciones,
		snomed: medicacion.snomed,
		suspended: medicacion.suspendido,
	};
}

export const toOdontologyPersonalHistoryDto = (antecedente: AntecedentePersonal): OdontologyPersonalHistoryDto => {
	return {
		snomed: antecedente.snomed,
		startDate: antecedente.fecha ? dateToDateDto(antecedente.fecha.toDate()) : undefined
	};
}

export const toOdontologyDiagnosticDto = (problema: Problema): OdontologyDiagnosticDto => {
	return {
		severity: problema.codigoSeveridad,
		chronic: problema.cronico,
		endDate: problema.fechaFin ? dateToDateDto(problema.fechaFin.toDate()) : undefined,
		snomed: problema.snomed,
		startDate: problema.fechaInicio ? dateToDateDto(problema.fechaInicio.toDate()) : undefined
	};

}

export const toOdontologyProcedureDto = (procedimiento: Procedimiento): OdontologyProcedureDto => {
	return {
		performedDate: procedimiento.performedDate ? dateToDateDto((momentParse(procedimiento.performedDate, DateFormat.API_DATE)).toDate()) : undefined,
		snomed: procedimiento.snomed
	};

}

export const toDentalAction = (actionedTooth: ActionedTooth, concepts: OdontologyConceptDto[]): OdontologyDentalActionDto[] => {
	return actionedTooth.actions
		.map((toothAction: ToothAction) => {
			return {
				diagnostic: toothAction.action.type === ActionType.DIAGNOSTIC,
				snomed: concepts.find(c => c.snomed.sctid === toothAction.action.sctid).snomed,
				surfacePosition: ESurfacePositionDtoValues[toothAction.surfaceId],
				tooth: actionedTooth.tooth.snomed
			}
		});
}
