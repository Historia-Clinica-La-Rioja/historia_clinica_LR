export const deepClone = (object) => {
	return JSON.parse(JSON.stringify(object));
}

export const capitalize = (word: string): string => {
	return word[0].toUpperCase() + word.slice(1);
}

export function capitalizeSentence(input: string): string {
	if (!input) {
	  return '';
	}

	const words = input.toLowerCase().split(' ');

	for (let i = 0; i < words.length; i++) {
	  words[i] = words[i].charAt(0).toUpperCase() + words[i].slice(1);
	}

	return words.join(' ');
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

export enum ENumbersEventCode {
	DIGIT_0 = "Digit0",
	DIGIT_1 = "Digit1",
	DIGIT_2 = "Digit2",
	DIGIT_3 = "Digit3",
	DIGIT_4 = "Digit4",
	DIGIT_5 = "Digit5",
	DIGIT_6 = "Digit6",
	DIGIT_7 = "Digit7",
	DIGIT_8 = "Digit8",
	DIGIT_9 = "Digit9",
	NUMPAD_0 = "Numpad0",
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

export const EVENT_CODE_NUMBERS: string[] = [ENumbersEventCode.DIGIT_0, ENumbersEventCode.DIGIT_1, ENumbersEventCode.DIGIT_2, 
										ENumbersEventCode.DIGIT_3, ENumbersEventCode.DIGIT_4, ENumbersEventCode.DIGIT_5, 
										ENumbersEventCode.DIGIT_6, ENumbersEventCode.DIGIT_7, ENumbersEventCode.DIGIT_8, 
										ENumbersEventCode.DIGIT_9, ENumbersEventCode.NUMPAD_0, ENumbersEventCode.NUMPAD_1, 
										ENumbersEventCode.NUMPAD_2, ENumbersEventCode.NUMPAD_3, ENumbersEventCode.NUMPAD_4, 
										ENumbersEventCode.NUMPAD_5, ENumbersEventCode.NUMPAD_6, ENumbersEventCode.NUMPAD_7, 
										ENumbersEventCode.NUMPAD_8, ENumbersEventCode.NUMPAD_9]

export function getValuesOfEnum<T>(enumeration: T): number[] {
	const enumKeys: string[] = Object.keys(enumeration).filter(key => isNaN(Number(key)));
	return enumKeys.map(key => enumeration[key]);
}