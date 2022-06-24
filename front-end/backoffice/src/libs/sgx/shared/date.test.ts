import { 
    dateFormatter, 
    dayFormatter,
} from './date';

test('dateFormatter should work with right values', () => {

    expect(
        dateFormatter('')
    ).toBe('');

    expect(
        dateFormatter('1999-09-09')
    ).toBe('09/09/1999');

    expect(
        dateFormatter('2001-01-13')
    ).toBe('13/01/2001');

});


test('dayFormatter should work with right values', () => {

    expect(
        dayFormatter('')
    ).toBe('');

    expect(
        dayFormatter('1999-09-09')
    ).toBe('9 de septiembre de 1999');

    expect(
        dayFormatter('2001-01-13')
    ).toBe('13 de enero de 2001');

});
