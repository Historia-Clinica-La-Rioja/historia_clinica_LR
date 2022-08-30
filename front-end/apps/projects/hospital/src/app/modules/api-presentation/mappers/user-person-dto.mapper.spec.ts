import { mapToFullName } from './user-person-dto.mapper';
import {unsupported} from "@angular/compiler/src/render3/view/util";

describe('mapToFullName', () => {

	it('should be empty if undefined', () => {
	  expect(mapToFullName(undefined, false)).toBe('');
	});

	it('should be empty if undefined', () => {
		expect(mapToFullName({firstName: undefined, lastName: undefined, nameSelfDetermination: undefined}, false)).toBe('');
	});

	it('should be firstName if only firstName is defined', () => {
		expect(mapToFullName({firstName: 'Mario', lastName: undefined, nameSelfDetermination: undefined}, false)).toBe('Mario');
	});

	it('should be firstName if only firstName is defined', () => {
		expect(mapToFullName({firstName: undefined, lastName: 'Gomez', nameSelfDetermination: undefined}, false)).toBe('Gomez');
	});

	it('should be firstName if only firstName is defined', () => {
		expect(mapToFullName({firstName: 'Mario', lastName: 'Gomez', nameSelfDetermination: undefined}, false)).toBe('Mario Gomez');
	});

	it('should be empty if undefined', () => {
		expect(mapToFullName(undefined, true)).toBe('');
	});

	it('should be empty if undefined', () => {
		expect(mapToFullName({firstName: undefined, lastName: undefined, nameSelfDetermination: undefined}, true)).toBe('');
	});

	it('should be firstName if only firstName is defined', () => {
		expect(mapToFullName({firstName: 'Mario', lastName: undefined, nameSelfDetermination: undefined}, true)).toBe('Mario');
	});

	it('should be firstName if only firstName is defined', () => {
		expect(mapToFullName({firstName: undefined, lastName: 'Gomez', nameSelfDetermination: undefined}, true)).toBe('Gomez');
	});

	it('should be firstName if only firstName is defined', () => {
		expect(mapToFullName({firstName: undefined, lastName: undefined, nameSelfDetermination: 'Maria'}, true)).toBe('Maria');
	});

	it('should be firstName if only firstName is defined', () => {
		expect(mapToFullName({firstName: undefined, lastName: 'Gomez', nameSelfDetermination: 'Maria'}, true)).toBe('Maria Gomez');
	});
});
