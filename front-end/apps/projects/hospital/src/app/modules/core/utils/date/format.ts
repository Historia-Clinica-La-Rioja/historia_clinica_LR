

import {
	formatISO
} from 'date-fns';
import { Moment, isMoment } from 'moment';

export function formatDateOnlyISO(date: Date): string {
	return formatISO(fixDate(date), { representation: 'date' });
}


export const fixDate = (date: Date | Moment): Date => {
	if (isMoment(date)) {
		return (date as Moment).toDate();
	}
	return date;
}
