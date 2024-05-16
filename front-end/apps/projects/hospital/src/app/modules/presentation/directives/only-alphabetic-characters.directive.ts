import { Directive, HostListener } from '@angular/core';
import { BACKSPACE, WHITESPACE, STRING_PATTERN } from '@core/utils/form.utils';

@Directive({
    selector: '[onlyAlphabeticCharacters]'
})
export class OnlyAlphabeticCharactersDirective {

    constructor() { }
  
    @HostListener('keydown', ['$event'])
    checkValue(event: KeyboardEvent) {
        if (event.key === BACKSPACE || event.key === WHITESPACE) return;
        const char = event.key;
        const allowedCharacters = STRING_PATTERN;
        return allowedCharacters.test(char);
    }
}
