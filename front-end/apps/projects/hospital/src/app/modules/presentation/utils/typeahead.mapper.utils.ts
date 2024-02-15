import { TypeaheadOption } from "@presentation/components/typeahead/typeahead.component";

export function listToTypeaheadOptions<T>(list: T[], key: string): TypeaheadOption<T>[] {
    return list?.map(item => objectToTypeaheadOption(item, key)) || [];
}

export function objectToTypeaheadOption<T>(obj: T, key: string): TypeaheadOption<T> {
	return {
		compareValue: obj[key],
		value: obj,
	};
}
