import { EpisodesFilterService } from './episodes-filter.service';
import { Episode } from '../routes/home/home.component';
import { async, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

describe('EpisodesFilterService', () => {
	let service: EpisodesFilterService;

	const episode: Episode = {
		waitingTime: null,
		creationDate: null,
		doctorsOffice: null,
		id: null,
		patient: {
			id: 3,
			patientMedicalCoverageId: null,
			person: null,
			typeId: null
		},
		state: null,
		triage: {
			color: 'Rojo',
			id: 1,
			name: 'Nivel I'
		},
		type: {
			id: 2,
			description: 'Pediatria'
		},
	};

	const filters = {
		triage: 1,
		emergencyCareType: 1,
		patientId: 3
	};

	it('it should return true if the episode passes the filter by triage', () => {
		expect(EpisodesFilterService.filterByTriage(episode, filters)).toBe(true);
	});

	it('it should return true if the episode does not passes the filter by emergency care type', () => {
		expect(EpisodesFilterService.filterByEmergencyCareType(episode, filters)).toBe(false);
	});

	it('it should return true if the episode passes the filter by patient id', () => {
		expect(EpisodesFilterService.filterByPatientId(episode, filters)).toBe(true);
	});


	beforeEach(async(() => {
	  TestBed.configureTestingModule({
		  imports: [
			  ReactiveFormsModule,
			  HttpClientModule
		  ]
	  });
	  service = TestBed.inject(EpisodesFilterService);
	}));

	it('should be created', () => {
	  expect(service).toBeTruthy();
	});
});
