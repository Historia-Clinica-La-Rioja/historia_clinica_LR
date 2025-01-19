import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { EmergencyCareAttentionPlaceService } from '../../services/emergency-care-attention-place.service';
import { EmergencyCareAttentionPlaceDto } from '@api-rest/api-model';
import { map, Observable } from 'rxjs';

const ORGANIZED_BY_SPECIALTY_ID = 1;
@Component({
	selector: 'app-emergency-care-change-attention-place-select-sector',
	templateUrl: './emergency-care-change-attention-place-select-sector.component.html',
	styleUrls: ['./emergency-care-change-attention-place-select-sector.component.scss']
})
export class EmergencyCareChangeAttentionPlaceSelectSectorComponent implements OnInit {

	form: FormGroup<SectorForm>;
	sectors$: Observable<MappedSector[]>;

	@Output() selectedSector = new EventEmitter<EmergencyCareAttentionPlaceDto>();

	constructor(
		private emergencyCareAttentionPlaceService: EmergencyCareAttentionPlaceService,
	) {
		this.createForm();
		this.subscribeToChangesAndEmit();
	}

	ngOnInit() {
		this.sectors$ = this.emergencyCareAttentionPlaceService.getSectors().pipe(
			map(sectors => {
				const mappedSectors = sectors.map(sector => this.mapSectorDescription(sector));
				if (mappedSectors.length === 1) {
					this.form.controls.sector.setValue(mappedSectors[0].originalSector);
					this.selectedSector.emit(mappedSectors[0].originalSector);
				}
				return mappedSectors;
			})
		);
	}

	private mapSectorDescription(sector: EmergencyCareAttentionPlaceDto): MappedSector {
		const services = this.getServicesDescription(sector);
		return {
			originalSector: sector,
			mappedDescription: services ? `${sector.description} (${services})` : sector.description
		};
	}

	private getServicesDescription(sector: EmergencyCareAttentionPlaceDto): string {
		return sector.sectorOrganizationId === ORGANIZED_BY_SPECIALTY_ID
			? sector.clinicalSpecialtySectors.map(service => service.description).join(', ')
			: '';
	}

	private createForm() {
		this.form = new FormGroup<SectorForm>({
			sector: new FormControl(null),
		});
	}

	private subscribeToChangesAndEmit() {
		this.form.controls.sector.valueChanges.subscribe(sector => this.selectedSector.emit(sector));
	}
}

interface SectorForm {
	sector: FormControl<EmergencyCareAttentionPlaceDto>;
}

interface MappedSector {
	originalSector: EmergencyCareAttentionPlaceDto;
	mappedDescription: string;
}
