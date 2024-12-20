import { Component, forwardRef, OnInit } from '@angular/core';
import { EpisodeFilterService } from '../../services/episode-filter.service';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ChipsOption } from '@presentation/components/chips-autocomplete/chips-autocomplete.component';
import { EmergencyCareClinicalSpecialtySectorDto } from '@api-rest/api-model';
import { map, Observable, take } from 'rxjs';

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
	servicesSelected: ChipsOption<EmergencyCareClinicalSpecialtySectorDto>[] = [];

	constructor(
		private readonly episodeFilterService: EpisodeFilterService,
	) {
		super();
	}

	ngOnInit() {
		this.createForm();
		this.loadServices();
		this.loadSavedValues();
	}

	createForm() {
		this.form = new FormGroup({
			clinicalSpecialtySectorIds: new FormControl(null)
		});
	}

	loadServices() {
		this.services$ = this.episodeFilterService.getServices().pipe(
			map(services => services.map(service => this.toChipsOptions(service)))
		);
	}

	loadSavedValues() {
		const preloadedServices = this.episodeFilterService.getFilterValue('clinicalSpecialtySectorIds') || [];

		if (preloadedServices.length) {
			this.services$.pipe(
				take(1),
				map(services =>
					services.filter(service => preloadedServices.includes(service.identifier))
				)
			).subscribe(selectedServices => {
				this.servicesSelected = selectedServices;
				this.setSelectedServices(selectedServices);
			});
		}
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
		this.servicesSelected = [];
	}


	private toChipsOptions(service: EmergencyCareClinicalSpecialtySectorDto): ChipsOption<EmergencyCareClinicalSpecialtySectorDto> {
		return { value: service, compareValue: service.description, identifier: service.id }
	}

}
