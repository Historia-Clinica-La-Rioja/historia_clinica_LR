import { anyMatch, pushIfNotExists, sortBy } from './array.utils';

describe('anyMatch', () => {
	// true if any match, false if no coincidences

	it('should be false if both empty', () => {
		expect(anyMatch<string>([], [])).toBe(false);
	});

	it('should be false if one is empty', () => {
		expect(anyMatch<Keys>([Keys.ONE], [])).toBe(false);
		expect(anyMatch<Keys>([], [Keys.ONE])).toBe(false);
	});

	it('should be false if no match', () => {
		expect(anyMatch<Keys>([Keys.ONE], [Keys.TWO])).toBe(false);
		expect(anyMatch<Keys>([Keys.ONE], [Keys.TWO, Keys.THREE])).toBe(false);
		expect(anyMatch<Keys>([Keys.ONE, Keys.TWO, Keys.THREE], [Keys.FOUR, Keys.FIVE, Keys.SIX])).toBe(false);
	});

	it('should be true if something matches', () => {
		expect(anyMatch<Keys>([Keys.ONE], [Keys.ONE])).toBe(true);
		expect(anyMatch<Keys>([Keys.ONE, Keys.TWO, Keys.THREE], [Keys.ONE, Keys.TWO, Keys.THREE])).toBe(true);
	});

});

describe('sortBy', () => {

	it('should be undefined if undefined or null', () => {
		expect(sortBy('id')<MockClass>(undefined)).toBeUndefined();
		expect(sortBy('id')<MockClass>(null)).toBeUndefined();
	});

	it('should be same array if it hast less than two elements', () => {
		expect(sortBy('id')<MockClass>([])).toEqual([]);
		expect(sortBy('id')<MockClass>([MOCK_THREE])).toEqual([MOCK_THREE]);
	});

	it('should be same array if property does not exists', () => {
		expect(sortBy('notExistingProperty')<MockClass>([])).toEqual([]);
		expect(sortBy('notExistingProperty')<MockClass>([MOCK_THREE, MOCK_ONE])).toEqual([MOCK_THREE, MOCK_ONE]);
	});

	it('should be sorted in ascending order by a given existing field', () => {
		expect(sortBy('id')<MockClass>([MOCK_THREE, MOCK_FOUR, MOCK_ONE, MOCK_TWO])).toEqual([MOCK_ONE, MOCK_TWO, MOCK_THREE, MOCK_FOUR]);
		expect(sortBy('value')<MockClass>([MOCK_THREE, MOCK_FOUR, MOCK_ONE, MOCK_TWO])).toEqual([MOCK_FOUR, MOCK_ONE, MOCK_THREE, MOCK_TWO]);
	});

	it('should group undefined values at the end of array', () => {
		expect(sortBy('id')<MockClass>([undefined, MOCK_THREE, undefined, MOCK_ONE])).toEqual([MOCK_ONE, MOCK_THREE, undefined, undefined]);
	});
});


describe('pushIfNotExists', () => {

	it('should add the element if the array is empty', () => {
		expect(pushIfNotExists<MockClass>([], MOCK_ONE, equals)).toEqual([MOCK_ONE]);
	});

	it('should add the element if it is not present in the array', () => {
		expect(pushIfNotExists<MockClass>([MOCK_ONE], MOCK_TWO, equals)).toEqual([MOCK_ONE, MOCK_TWO]);
	});

	it('should not add the element if it is already in array', () => {
		expect(pushIfNotExists<MockClass>([MOCK_ONE], MOCK_ONE, equals)).toEqual([MOCK_ONE]);
	});
	
	it('should not add a null or undefined element', () => {
		expect(pushIfNotExists<MockClass>([MOCK_ONE], null, equals)).toEqual([MOCK_ONE]);
		expect(pushIfNotExists<MockClass>([MOCK_ONE], undefined, equals)).toEqual([MOCK_ONE]);
	});

});

function equals(obj1: MockClass, obj2: MockClass): boolean {
	if (!obj1 || !obj2)
		return true
	return obj1.id === obj2.id
}

const enum Keys {
	ONE,
	TWO,
	THREE,
	FOUR,
	FIVE,
	SIX,
}

const MOCK_ONE: MockClass = { id: 1, value: 'One' };
const MOCK_TWO: MockClass = { id: 2, value: 'Two' };
const MOCK_THREE: MockClass = { id: 3, value: 'Three' };
const MOCK_FOUR: MockClass = { id: 4, value: 'Four' };

class MockClass {
	id: number;
	value: string;
}
