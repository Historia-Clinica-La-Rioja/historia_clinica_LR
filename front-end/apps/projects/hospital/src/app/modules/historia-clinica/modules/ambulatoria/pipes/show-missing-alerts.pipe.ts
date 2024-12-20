import { Pipe, PipeTransform } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Observable, of } from 'rxjs';

const ONE_ELEMENT = 1;

@Pipe({
	name: 'showMissingAlerts$'
})
export class ShowMissingAlertsPipe implements PipeTransform {

	constructor(
		private readonly translateService: TranslateService
	) { }

	transform(cant: number): Observable<string> {
		if (!cant)
			return of("");
		return cant === ONE_ELEMENT ? this.translateService.get('historia-clinica.isolation-alert.add_alerts.SINGULAR', { cant }) : this.translateService.get('historia-clinica.isolation-alert.add_alerts.PLURAL', { cant })
	}

}
