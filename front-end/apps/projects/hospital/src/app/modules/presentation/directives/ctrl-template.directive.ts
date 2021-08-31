import { Directive, Input, TemplateRef } from '@angular/core';

@Directive({
	selector: '[appCtrlTemplate]'
})
export class CtrlTemplateDirective {
	@Input('appCtrlTemplate') name: string;

	constructor(public template: TemplateRef<any>) {
	}
}
