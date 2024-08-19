import { Directive, HostListener } from '@angular/core';
import { BACKSPACE, NUMBER_PATTERN } from '@core/utils/form.utils';

@Directive({
    selector: '[onlyNumericCharacters]'
})
export class OnlyNumericCharactersDirective {

    constructor() { }

    @HostListener('keydown', ['$event'])
    checkValue(event: KeyboardEvent) {
        if (event.key === BACKSPACE) return;
        const char = event.key;
        const allowedCharacters = NUMBER_PATTERN;
        return allowedCharacters.test(char);
    }

}
