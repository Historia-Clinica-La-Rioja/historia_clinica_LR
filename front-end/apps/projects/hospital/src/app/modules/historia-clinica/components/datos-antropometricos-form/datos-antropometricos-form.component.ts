import { DatePipe } from '@angular/common';
import { Component, Input, forwardRef } from '@angular/core';
import { FormBuilder, NG_VALUE_ACCESSOR } from '@angular/forms';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { DatosAntropometricosNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/datos-antropometricos-nueva-consulta.service';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-datos-antropometricos-form',
	templateUrl: './datos-antropometricos-form.component.html',
	styleUrls: ['./datos-antropometricos-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => DatosAntropometricosFormComponent),
			multi: true,
		}
	]
})
export class DatosAntropometricosFormComponent {

	datosAntropometricosNuevaConsultaService =
		new DatosAntropometricosNuevaConsultaService(this.formBuilder, this.hceGeneralStateService,
			null, this.internacionMasterDataService, this.translateService, this.datePipe); // Quitar el null si vamos a precargar datos
	antropometricos = this.datosAntropometricosNuevaConsultaService.form;
	onChangeSub: Subscription;

	@Input() patientId: number;

	constructor(
		private formBuilder: FormBuilder,
		private readonly datePipe: DatePipe,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly translateService: TranslateService,
	) { }

	onTouched = () => { };

	writeValue(obj: any): void {
		if (obj)
			this.antropometricos.setValue(obj);
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.antropometricos.valueChanges
			.subscribe(value => {
				const toEmit = this.antropometricos.valid ? value : null;
				fn(toEmit);
			})
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	setDisabledState?(isDisabled: boolean): void {
		isDisabled ? this.antropometricos.disable() : this.antropometricos.enable();
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}

}