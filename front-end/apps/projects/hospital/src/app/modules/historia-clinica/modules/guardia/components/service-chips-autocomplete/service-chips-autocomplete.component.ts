import { Component, forwardRef, OnInit } from '@angular/core';
import { EpisodeFilterService } from '../../services/episode-filter.service';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ChipsOption } from '@presentation/components/chips-autocomplete/chips-autocomplete.component';
import { EmergencyCareClinicalSpecialtySectorDto } from '@api-rest/api-model';
import { map, Observable } from 'rxjs';

@Component({
	selector: 'app-service-chips-autocomplete',
	templateUrl: './service-chips-autocomplete.component.html',
	styleUrls: ['./service-chips-autocomplete.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => ServiceChipsAutocompleteComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => ServiceChipsAutocompleteComponent),
		},
	],
})
export class ServiceChipsAutocompleteComponent extends AbstractCustomForm implements OnInit {

	form: FormGroup;
	services$: Observable<ChipsOption<EmergencyCareClinicalSpecialtySectorDto>[]>;

	constructor(
		private readonly episodeFilterService: EpisodeFilterService,
	) {
		super();
	}

	ngOnInit(): void {
		this.createForm();
		this.services$ = this.episodeFilterService.getServices().pipe(map(services => services.map(service => this.toChipsOptions(service))));
	}

	createForm() {
		this.form = new FormGroup({
			clinicalSpecialtySectorIds: new FormControl(null)
		});
	}

	setSelectedServices(services: ChipsOption<EmergencyCareClinicalSpecialtySectorDto>[]) {
		const selectedServices = services.map(service => service.identifier);
		this.form.controls.clinicalSpecialtySectorIds.setValue(selectedServices);
	}

	writeValue(obj: any): void {
		if (!obj)
			this.resetForm();
	}

	resetForm() {
		this.form.controls.clinicalSpecialtySectorIds.setValue(null);		
	}


	private toChipsOptions(service: EmergencyCareClinicalSpecialtySectorDto): ChipsOption<EmergencyCareClinicalSpecialtySectorDto> {
		return { value: service, compareValue: service.description, identifier: service.id }
	}

}
