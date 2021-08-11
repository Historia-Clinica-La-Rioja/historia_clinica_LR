export const deepClone = (object) => {
	return JSON.parse(JSON.stringify(object));
}
