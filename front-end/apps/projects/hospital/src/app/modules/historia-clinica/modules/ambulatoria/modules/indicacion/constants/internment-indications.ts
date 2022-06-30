import { Title } from "@presentation/components/indication/indication.component"
import { DateTimeDto } from "@api-rest/api-model";
import { dateTimeDtoToStringDate } from "@api-rest/mapper/date-dto.mapper";
import { differenceInMinutes, differenceInHours, differenceInDays } from "date-fns";
import { ConfirmDialogComponent } from "@presentation/dialogs/confirm-dialog/confirm-dialog.component";
import { MatDialog } from "@angular/material/dialog";
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

const EVENT = "e";

const HOURS = "h";

export function showTimeElapsed(createdOn: DateTimeDto): string {
	const differenceInMin = differenceInMinutes(new Date(), new Date(dateTimeDtoToStringDate(createdOn)));
	if (differenceInMin === 1)
		return "Hace " + differenceInMin + " minuto"
	if (differenceInMin < 60)
		return "Hace " + differenceInMin + " minutos"

	const differenceInHs = differenceInHours(new Date(), new Date(dateTimeDtoToStringDate(createdOn)));
	if (differenceInHs === 1)
		return "Hace " + differenceInHs + " hora"
	if (differenceInHs <= 24)
		return "Hace " + differenceInHs + " horas"

	const difference = differenceInDays(new Date(), new Date(dateTimeDtoToStringDate(createdOn)));
	if (difference === 1)
		return "Hace " + difference + " día"
	return "Hace " + difference + " días"
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
