import { mapToFullName } from './user-person-dto.mapper';

describe('mapToFullName', function() {

	it('should be empty if undefined', function() {
	  expect(mapToFullName(undefined)).toBe('');
	});

	it('should be empty if undefined', function() {
		expect(mapToFullName({firstName: undefined, lastName: undefined})).toBe('');
	});

	it('should be firstName if only firstName is defined', function() {
		expect(mapToFullName({firstName: 'Mario', lastName: undefined})).toBe('Mario');
	});

	it('should be firstName if only firstName is defined', function() {
		expect(mapToFullName({firstName: undefined, lastName: 'Gomez'})).toBe('Gomez');
	});

	it('should be firstName if only firstName is defined', function() {
		expect(mapToFullName({firstName: 'Mario', lastName: 'Gomez'})).toBe('Mario Gomez');
	});
});
