import { Episode } from '../components/emergency-care-patients-summary/emergency-care-patients-summary.component';
import { EpisodeFilterService } from './episode-filter.service';

describe('EpisodeFilterService', () => {

	const episode = {
		waitingTime: null,
		waitingHours: null,
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
			emergencyCareEpisodeListTriageDto: {
				color: 'Rojo',
				id: 1,
				name: 'Nivel I'
			}
		},
		type: {
			id: 2,
			description: 'Pediatria'
		},
	} as Episode;

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
