import { Pipe, PipeTransform } from '@angular/core';
import differenceInDays from 'date-fns/differenceInDays';
import differenceInHours from 'date-fns/differenceInHours';
import differenceInMinutes from 'date-fns/differenceInMinutes';

@Pipe({
	name: 'showTimeElapsed'
})
export class ShowTimeElapsedPipe implements PipeTransform {

	transform(createdOn: Date): string {
		const differenceInMin = differenceInMinutes(new Date(), createdOn);

		if (differenceInMin < 60)
			return `Hace ${differenceInMin} minuto${differenceInMin === 1 ? "" : "s"}`;

		const differenceInHs = differenceInHours(new Date(), createdOn);
		if (differenceInHs <= 24)
			return `Hace ${differenceInHs} hora${differenceInHs === 1 ? "" : "s"}`;


		const differenceDays = differenceInDays(new Date(), createdOn);
		return `Hace ${differenceDays} día${differenceDays === 1 ? "" : "s"}`;
	}

}
