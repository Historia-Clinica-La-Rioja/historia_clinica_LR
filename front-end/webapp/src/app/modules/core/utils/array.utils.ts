export const pushTo = <T>(data: T[], elem: T): T[] => {
	return data.concat([elem]);
};

export const removeFrom = <T>(data: T[], index: number): T[] => {
	return data.slice(0, index).concat(data.slice(index+1));
};

export const anyMatch = <T>(a1: T[], a2: T[]): boolean => {
	return (a1.some(e1 => a2.some(e2 => e2 === e1)));
};
