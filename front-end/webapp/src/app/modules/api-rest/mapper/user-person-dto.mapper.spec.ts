import { mapToFullName } from './user-person-dto.mapper';

describe('mapToFullName', () => {

	it('should be empty if undefined', () => {
	  expect(mapToFullName(undefined)).toBe('');
	});

	it('should be empty if undefined', () => {
		expect(mapToFullName({firstName: undefined, lastName: undefined})).toBe('');
	});

	it('should be firstName if only firstName is defined', () => {
		expect(mapToFullName({firstName: 'Mario', lastName: undefined})).toBe('Mario');
	});

	it('should be firstName if only firstName is defined', () => {
		expect(mapToFullName({firstName: undefined, lastName: 'Gomez'})).toBe('Gomez');
	});

	it('should be firstName if only firstName is defined', () => {
		expect(mapToFullName({firstName: 'Mario', lastName: 'Gomez'})).toBe('Mario Gomez');
	});
});
