export const pushTo = <T>(data: T[], elem: T): T[] => {
	return data.concat([elem]);
}

export const removeFrom = <T>(data: T[], index: number): T[] => {
	return data.slice(0, index).concat(data.slice(index+1));
}
