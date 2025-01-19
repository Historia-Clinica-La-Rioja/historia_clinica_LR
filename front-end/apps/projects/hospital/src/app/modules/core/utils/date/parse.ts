

import {
	parseISO
} from 'date-fns'

export function parseDateOnlyISO(date: string): Date {
	return parseISO(date);
}
