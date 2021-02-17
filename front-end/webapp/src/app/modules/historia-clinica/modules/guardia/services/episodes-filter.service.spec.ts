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
		patient: null,
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
		emergencyCareType: 1
	};

	it('it should return true if the episode passes the filter by triage', () => {
		expect(EpisodesFilterService.filterByTriage(episode, filters)).toBe(true);
	});

	it('it should return true if the episode does not passes the filter by emergency care type', () => {
		expect(EpisodesFilterService.filterByEmergencyCareType(episode, filters)).toBe(false);
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
