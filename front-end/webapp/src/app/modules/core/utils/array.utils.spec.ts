import { anyMatch } from './array.utils';

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

const enum Keys {
	ONE,
	TWO,
	THREE,
	FOUR,
	FIVE,
	SIX,
}
