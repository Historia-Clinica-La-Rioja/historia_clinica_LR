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

export enum ENaturalNumbers {
	DIGIT_1 = "Digit1",
	DIGIT_2 = "Digit2",
	DIGIT_3 = "Digit3",
	DIGIT_4 = "Digit4",
	DIGIT_5 = "Digit5",
	DIGIT_6 = "Digit6",
	DIGIT_7 = "Digit7",
	DIGIT_8 = "Digit8",
	DIGIT_9 = "Digit9",
	NUMPAD_1 = "Numpad1",
    NUMPAD_2 = "Numpad2",
    NUMPAD_3 = "Numpad3",
    NUMPAD_4 = "Numpad4",
    NUMPAD_5 = "Numpad5",
    NUMPAD_6 = "Numpad6",
    NUMPAD_7 = "Numpad7",
    NUMPAD_8 = "Numpad8",
    NUMPAD_9 = "Numpad9"
}

export const NATURAL_NUMBERS: string[] = [ENaturalNumbers.DIGIT_1, ENaturalNumbers.DIGIT_2, ENaturalNumbers.DIGIT_3,
										ENaturalNumbers.DIGIT_4, ENaturalNumbers.DIGIT_5, ENaturalNumbers.DIGIT_6,
										ENaturalNumbers.DIGIT_7, ENaturalNumbers.DIGIT_8, ENaturalNumbers.DIGIT_9,
										ENaturalNumbers.NUMPAD_1, ENaturalNumbers.NUMPAD_2, ENaturalNumbers.NUMPAD_3,
										ENaturalNumbers.NUMPAD_4, ENaturalNumbers.NUMPAD_5, ENaturalNumbers.NUMPAD_6,
										ENaturalNumbers.NUMPAD_7, ENaturalNumbers.NUMPAD_8, ENaturalNumbers.NUMPAD_9]
