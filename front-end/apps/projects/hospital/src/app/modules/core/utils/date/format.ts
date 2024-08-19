

import {
	formatISO
} from 'date-fns';

export function formatDateOnlyISO(date: Date): string {
	return formatISO(fixDate(date), { representation: 'date' });
}

export const fixDate = (date: Date): Date => {
	return date;
}
