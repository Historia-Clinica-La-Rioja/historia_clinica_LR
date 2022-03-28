import { Title } from "@presentation/components/indication/indication.component"
import { DateTimeDto } from "@api-rest/api-model";
import { dateTimeDtoToStringDate } from "@api-rest/mapper/date-dto.mapper";
import { differenceInMinutes, differenceInHours, differenceInDays } from "date-fns";

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
