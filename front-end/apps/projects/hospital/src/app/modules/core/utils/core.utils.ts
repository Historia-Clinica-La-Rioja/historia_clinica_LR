export const deepClone = (object) => {
	return JSON.parse(JSON.stringify(object));
}

export const capitalize = (word: string): string => {
	return word[0].toUpperCase() + word.slice(1);
}

export const isNumberOrDot = (value: string) => {
	const pattern = /^[\d\.]{1}$/;
	return pattern.test(value);
}
