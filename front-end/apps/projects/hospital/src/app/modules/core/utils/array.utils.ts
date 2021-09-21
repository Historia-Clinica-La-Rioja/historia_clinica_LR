export const pushTo = <T>(data: T[], elem: T): T[] => {
	return data.concat([elem]);
};

export const removeFrom = <T>(data: T[], index: number): T[] => {
	return data.slice(0, index).concat(data.slice(index + 1));
};

export const anyMatch = <T>(a1: T[], a2: T[]): boolean => {
	return (a1.some(e1 => a2.some(e2 => e2 === e1)));
};

export const uniqueItems = <T>(data: T[]): T[] => {
	return data.filter((elem, index, self) => {
		return index === self.indexOf(elem);
	});
};

export const pushIfNotExists = <T>(data: T[], obj: T, compareFunction: (obj1: T, obj2: T) => boolean): T[] => {
	if (!data.some(e => compareFunction(e, obj))) {
		return data.concat([obj]);
	}
	return data;
};

export const sortBy = (field: string) => <T>(data: T[]): T[] => {
	return data?.sort((a, b) => a[field] < b[field] ? -1 : 1);
};
