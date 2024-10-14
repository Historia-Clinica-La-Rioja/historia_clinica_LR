import { Component, OnInit, forwardRef } from '@angular/core';
import { FormBuilder, NG_VALUE_ACCESSOR } from '@angular/forms';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { FactoresDeRiesgoFormService } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { TranslateService } from '@ngx-translate/core';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-factores-de-riesgo-form-v2',
	templateUrl: './factores-de-riesgo-form-v2.component.html',
	styleUrls: ['./factores-de-riesgo-form-v2.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => FactoresDeRiesgoFormV2Component),
			multi: true,
		}
	]
})
export class FactoresDeRiesgoFormV2Component implements OnInit {

	onChangeSub: Subscription;
	searchConceptsLocallyFFIsOn = false;
	factoresDeRiesgoFormService = new FactoresDeRiesgoFormService(this.formBuilder, this.translateService, this.hceGeneralStateService, null, this.dateFormatPipe);

	formMotivo = this.factoresDeRiesgoFormService.form;
	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly translateService: TranslateService,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly dateFormatPipe: DateFormatPipe,
	) { }

	ngOnInit(): void {
	}

	onTouched = () => { };

	writeValue(obj: any): void {
		if (obj)
			this.formMotivo.setValue(obj);
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.formMotivo.valueChanges
			.subscribe(value => {
				const toEmit = this.formMotivo.valid ? value : null;
				fn(toEmit);
			})
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	setDisabledState?(isDisabled: boolean): void {
		isDisabled ? this.formMotivo.disable() : this.formMotivo.enable();
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}

}
