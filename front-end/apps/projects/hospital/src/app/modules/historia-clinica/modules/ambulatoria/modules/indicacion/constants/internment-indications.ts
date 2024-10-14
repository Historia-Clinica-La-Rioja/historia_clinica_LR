import { MatDialog } from "@angular/material/dialog";
import { EIndicationStatus, EIndicationType, ENursingRecordStatus } from "@api-rest/api-model";
import { Title } from "@presentation/components/indication/indication.component";
import { ConfirmDialogComponent } from "@presentation/dialogs/confirm-dialog/confirm-dialog.component";
import { Observable } from "rxjs";

export enum MONTHS_OF_YEAR {
	Enero,
	Febrero,
	Marzo,
	Abril,
	Mayo,
	Junio,
	Julio,
	Agosto,
	Septiembre,
	Octubre,
	Noviembre,
	Diciembre
}

export enum DAYS_OF_WEEK {
	Domingo,
	Lunes,
	Martes,
	Miércoles,
	Jueves,
	Viernes,
	Sábado
}

export const IndicationStatus = {
	[EIndicationStatus.INDICATED]: 'indicacion.internment-card.sections.label.INDICATED',
	[EIndicationStatus.SUSPENDED]: 'indicacion.internment-card.sections.label.SUSPENDED',
	[EIndicationStatus.IN_PROGRESS]: 'indicacion.internment-card.sections.label.IN_PROGRESS',
	[EIndicationStatus.COMPLETED]: 'indicacion.internment-card.sections.label.COMPLETED',
	[EIndicationStatus.REJECTED]: 'indicacion.internment-card.sections.label.REJECTED'
}

export const IndicationStatusScss = {
	[EIndicationStatus.INDICATED]: 'blue',
	[EIndicationStatus.SUSPENDED]: 'red',
	[EIndicationStatus.IN_PROGRESS]: 'yellow',
	[EIndicationStatus.COMPLETED]: 'green',
	[EIndicationStatus.REJECTED]: 'grey',
}

export const NursingRecordStatus = {
	[ENursingRecordStatus.PENDING]: 'indicacion.nursing-care.status.PENDING',
	[ENursingRecordStatus.COMPLETED]: 'indicacion.internment-card.sections.label.COMPLETED',
	[ENursingRecordStatus.REJECTED]: 'indicacion.internment-card.sections.label.REJECTED'
}

export const NursingRecordStatusScss = {
	[ENursingRecordStatus.PENDING]: 'red',
	[ENursingRecordStatus.COMPLETED]: 'green',
	[ENursingRecordStatus.REJECTED]: 'grey'
}

export const IndicationMatIcon = {
	[EIndicationType.DIET]: 'local_dining',
	[EIndicationType.OTHER_INDICATION]: 'assignment_late',
}

export const IndicationSvgIcon = {
	[EIndicationType.PHARMACO]: 'pharmaco',
	[EIndicationType.PARENTERAL_PLAN]: 'parenteral_plans',
}

export const DIET: Title = {
	title: 'indicacion.internment-card.sections.DIET',
	matIcon: 'local_dining',
}

export const PARENTERAL_PLAN: Title = {
	title: 'indicacion.internment-card.sections.PARENTERAL_PLAN',
	svgIcon: 'parenteral_plans',
}

export const PHARMACO: Title = {
	title: 'indicacion.internment-card.sections.PHARMACO',
	svgIcon: 'pharmaco',
}

export const OTHER_INDICATION: Title = {
	title: 'indicacion.internment-card.sections.OTHER_INDICATION',
	matIcon: 'assignment_late',
}

export function openConfirmDialog(dialog: MatDialog, date: Date): Observable<any> {
	const keyPrefix = 'indicacion.internment-card.buttons';
	const dateString = date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();
	const dateHTML = `<strong>${dateString}</strong>`;
	const messageHTML = `<strong>¿Desea confirmar la indicación?</strong>`;
	const dialogRef = dialog.open(ConfirmDialogComponent, {
		data: {
			content: `La indicación se estará realizando para el día ${dateHTML}. Si desea realizar la indicación para el dia de hoy, por favor
					seleccione la fecha indicada en el campo “Fecha de indicación”. ${messageHTML}`,
			okButtonLabel: `${keyPrefix}.CONFIRM`,
			cancelButtonLabel: `${keyPrefix}.CANCEL`,
			showMatIconError: true,
		},
		width: "40%",
		autoFocus: false,
		disableClose: true,
	});

	return dialogRef.afterClosed();
}
export const HOURS_LIST = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24];

export const OTHER_FREQUENCY = { title: "Otra Frecuencia", value: 0 };

export const OTHER_INDICATION_ID = 11;

export const INTERVALS_TIME = [4, 6, 8, 12, 24, OTHER_FREQUENCY.value];

export const PAGE_SIZE_OPTIONS = [5, 10, 25];

export const PAGE_MIN_SIZE = 5;

export const INDICATION_TYPE = {
	DIET: "DIET",
	PARENTERAL_PLAN: 'PARENTERAL_PLAN',
	PHARMACO: 'PHARMACO',
	OTHER_INDICATION: 'OTHER_INDICATION'
}
