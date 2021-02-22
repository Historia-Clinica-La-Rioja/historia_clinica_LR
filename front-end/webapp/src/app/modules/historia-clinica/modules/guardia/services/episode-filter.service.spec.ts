import { EpisodeFilterService } from './episode-filter.service';
import { Episode } from '../routes/home/home.component';

describe('EpisodeFilterService', () => {

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
		expect(EpisodeFilterService.filterByTriage(episode, filters)).toBe(true);
	});

	it('it should return true if the episode does not passes the filter by emergency care type', () => {
		expect(EpisodeFilterService.filterByEmergencyCareType(episode, filters)).toBe(false);
	});

	it('it should return true if the episode passes the filter by patient id', () => {
		expect(EpisodeFilterService.filterByPatientId(episode, filters)).toBe(true);
	});

});
