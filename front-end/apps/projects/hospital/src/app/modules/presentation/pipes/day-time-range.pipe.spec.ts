import { DayTimeRangePipe } from './day-time-range.pipe';
import { DatePipe, registerLocaleData } from '@angular/common';
import localeEsAr from '@angular/common/locales/es';
import localeEsArExtras from '@angular/common/locales/extra/es-AR';

describe('DayTimeRangePipe', () => {
    registerLocaleData(localeEsAr, localeEsArExtras);
    const pipe = new DayTimeRangePipe();
    pipe.datePipe = new DatePipe('es-AR');
    const initDate = new Date('1995-12-19T03:24:00');
    const endDate = new Date('1995-12-18T04:24:00');

    it(`should keep first day and show time of both days spread by 'a' `, () => {
        let result = pipe.transform(initDate, endDate);
        expect(result).toBe(`Martes, 3:24hs a 4:24hs`);

        pipe.datePipe = new DatePipe('en-CA');
        result = pipe.transform(initDate, endDate);
        expect(result).toBe(`Tuesday, 3:24 AMhs a 4:24 AMhs`);

    });

    it(`should transform to undefined if some date is missing `, () => {
        const result = pipe.transform(initDate, null);
        expect(result).toBeUndefined();

        const result2 = pipe.transform(null, endDate);
        expect(result2).toBeUndefined();

    });

});