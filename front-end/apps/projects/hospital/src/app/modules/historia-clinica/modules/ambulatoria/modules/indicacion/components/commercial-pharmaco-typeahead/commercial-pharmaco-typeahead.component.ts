import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UntypedFormControl } from '@angular/forms';
import { GetCommercialMedicationSnomedDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { debounceTime, distinctUntilChanged, mergeMap, Observable, of, startWith } from 'rxjs';

@Component({
	selector: 'app-commercial-pharmaco-typeahead',
	templateUrl: './commercial-pharmaco-typeahead.component.html',
	styleUrls: ['./commercial-pharmaco-typeahead.component.scss']
})
export class CommercialPharmacoTypeaheadComponent {

	snomedConcept: SnomedDto;
	@Input() ecl: SnomedECL;
	@Input() placeholder = '';
	@Input() debounceTime = 300;
	@Input() showSearchIcon = false;

	@Output() conceptSelected = new EventEmitter<SnomedDto>();

	myControl = new UntypedFormControl();
	filteredOptions: Observable<any[]>;

	COMMERCIAL_PHARMACOS: GetCommercialMedicationSnomedDto[] = [
		{
			commercialPt: "PAXON D 100/12.5 [HIDROCLOROTIAZIDA 12,5 MG + LOSARTAN POTASICO]",
			genericMedication: {
				pt: "hidroclorotiazida 12,5 mg y losartán potásico 100 mg",
				sctid: "1"
			}
		},
		{
			commercialPt: "PAXON D 100/12.5 [HIDROCLOROTIAZIDA 12,5 MG + LOSARTAN POTASICO]",
			genericMedication: {
				pt: "hidroclorotiazida 12,5 mg y losartán potásico 100 mg",
				sctid: "1"
			}
		},{
			commercialPt: "PAXON D 100/12.5 [HIDROCLOROTIAZIDA 12,5 MG + LOSARTAN POTASICO]",
			genericMedication: {
				pt: "hidroclorotiazida 12,5 mg y losartán potásico 100 mg",
				sctid: "1"
			}
		},{
			commercialPt: "PAXON D 100/12.5 [HIDROCLOROTIAZIDA 12,5 MG + LOSARTAN POTASICO]",
			genericMedication: {
				pt: "hidroclorotiazida 12,5 mg y losartán potásico 100 mg",
				sctid: "1"
			}
		},{
			commercialPt: "PAXON D 100/12.5 [HIDROCLOROTIAZIDA 12,5 MG + LOSARTAN POTASICO]",
			genericMedication: {
				pt: "hidroclorotiazida 12,5 mg y losartán potásico 100 mg",
				sctid: "1"
			}
		},
	]

	constructor(
	) {

		this.filteredOptions = this.myControl.valueChanges.pipe(
			startWith(''),
			debounceTime(this.debounceTime),
			distinctUntilChanged(),
			mergeMap(searchValue => {
				return this.filter(searchValue || '')
			})
		)
	}

	private filter(searchValue: string): Observable<GetCommercialMedicationSnomedDto[]> {
		return this.searchConcepts(searchValue)
	}

	private searchConcepts(searchValue): Observable<GetCommercialMedicationSnomedDto[]> {
		const filteredPharmacos = this.COMMERCIAL_PHARMACOS.filter(pharmaco =>
			pharmaco.commercialPt.includes(searchValue)
		);

		return of(filteredPharmacos);
	}

	getDisplayName(option: GetCommercialMedicationSnomedDto): string {
		return option && option.genericMedication.pt ? option.genericMedication.pt : '';
	}

	handleOptionSelected(event: any) {
		const selectedOption = event.option?.value;
		if (selectedOption) {
			this.snomedConcept = selectedOption;
			this.conceptSelected.emit(this.snomedConcept);
		}
	}

	clear() {
		this.snomedConcept = null;
		this.myControl.reset();
		this.conceptSelected.emit(this.snomedConcept);
	}
}
