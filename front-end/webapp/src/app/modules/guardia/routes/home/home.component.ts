import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { EmergencyCareEpisodeDto, EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { DateTimeDto } from '@api-rest/api-model';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { differenceInHours, differenceInMinutes } from 'date-fns';
import { EstadosEpisodio, Triages } from '../../constants/masterdata';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	readonly estadosEpisodio = EstadosEpisodio;
	readonly triages = Triages;
	readonly PACIENTE_TEMPORAL = 3;
	episodes: any[];

	constructor(
		private router: Router,
		private emergencyCareEpisodeService: EmergencyCareEpisodeService
		) {
	}

	ngOnInit(): void {
		this.emergencyCareEpisodeService.getAll().subscribe(episodes => {
			this.episodes = episodes.map(episode => {
				return {
					...episode,
					waitingTime: episode.state.id === this.estadosEpisodio.EN_ESPERA ?
						this.calculateWaitingTime(episode.creationDate) : undefined
				};
			});
		});

	}

	private calculateWaitingTime(dateTime: DateTimeDto): number {
		const creationDate = dateTimeDtoToDate(dateTime);
		const now = new Date();
		return differenceInMinutes(now, creationDate);
	}

	goToEpisode(id: number) {
		console.log(id);
	}

	goToMockup(): void {
		this.router.navigate([`${this.router.url}/mock`]);
	}

}
