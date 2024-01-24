import { format } from "date-fns";

export const API_FORMAT = 'yyyy-MM-dd';
export const FILE_FORMAT = 'dd-MM-yyyy';

export function toApiFormat(date: Date): string {
	return format(date, API_FORMAT);
}
export function toFileFormat(date: Date): string {
	return format(date, FILE_FORMAT);
}
