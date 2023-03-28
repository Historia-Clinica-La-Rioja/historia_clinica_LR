import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SnomedDto } from '@api-rest/api-model';
import { ExternalCauseDto } from '@api-rest/api-model';
import { EEventLocation } from '@api-rest/api-model';
import { EExternalCauseType } from '@api-rest/api-model';
import { Subscription } from 'rxjs/internal/Subscription';
import { ExternalCauseService } from '../../services/external-cause.service';

@Component({
	selector: 'app-external-cause',
	templateUrl: './external-cause.component.html',
	styleUrls: ['./external-cause.component.scss']
})

export class ExternalCauseComponent implements OnInit {
	snomedConceptEvent: SnomedDto;
	ACCIDENT = EExternalCauseType.ACCIDENT;
	SELF_INFLICTED_INJURY = EExternalCauseType.SELF_INFLICTED_INJURY;
	AGRESSION = EExternalCauseType.AGRESSION;
	IGNORED = EExternalCauseType.IGNORED;
	DOMICILIO_PARTICULAR: EEventLocation.DOMICILIO_PARTICULAR;
	VIA_PUBLICA: EEventLocation.VIA_PUBLICA;
	LUGAR_DE_TRABAJO: EEventLocation.LUGAR_DE_TRABAJO;
	OTRO: EEventLocation.OTRO;
	eventLocation: EEventLocation;
	selectedOptionEventLocation: EEventLocation;
	selectedOptionExternalCauseType: EExternalCauseType;
	idExternalCause = 0;
	@Input() searchConceptsLocallyFF = false;
	@Output() event = new EventEmitter<ExternalCauseDto>();
	private externalCauseServise$: Subscription;

	constructor(
		readonly externalCauseService: ExternalCauseService,
	) { }

	ngOnInit(): void {
		this.externalCauseServise$ = this.externalCauseService.getValue().subscribe((externalCause: ExternalCauseDto) => {
			this.selectedOptionExternalCauseType = externalCause?.externalCauseType;
			this.selectedOptionEventLocation = externalCause?.eventLocation;
			this.idExternalCause = externalCause?.id;
			this.snomedConceptEvent = externalCause?.snomed;
			this.emmitEvent();
		});

	}

	emmitEvent() {
		this.event.emit({
			eventLocation: this.selectedOptionEventLocation,
			externalCauseType: this.selectedOptionExternalCauseType,
			id: this?.idExternalCause,
			snomed: this?.snomedConceptEvent,
		})
	}

	onEventSelected($event) {
		this.snomedConceptEvent = $event;
		this.emmitEvent();
	}
}
