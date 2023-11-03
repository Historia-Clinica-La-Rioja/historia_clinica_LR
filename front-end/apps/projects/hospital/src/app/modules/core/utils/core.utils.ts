export const deepClone = (object) => {
	return JSON.parse(JSON.stringify(object));
}

export const capitalize = (word: string): string => {
	return word[0].toUpperCase() + word.slice(1);
}

export function removeAccents(input: string): string {
	const accentsMap: { [key: string]: string } = {
		á: 'a',
		é: 'e',
		í: 'i',
		ó: 'o',
		ú: 'u',
		ü: 'u',
		ñ: 'n',
		Á: 'A',
		É: 'E',
		Í: 'I',
		Ó: 'O',
		Ú: 'U',
		Ü: 'U',
		Ñ: 'N',
	};

	return input.replace(/[áéíóúüñÁÉÍÓÚÜÑ]/g, (match) => accentsMap[match] || match);
}
