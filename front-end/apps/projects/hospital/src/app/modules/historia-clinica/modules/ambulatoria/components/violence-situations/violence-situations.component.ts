import { Component, OnInit } from '@angular/core';
import { ViolenceSituationDockPopupComponent } from '../../dialogs/violence-situation-dock-popup/violence-situation-dock-popup.component';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { ActivatedRoute } from '@angular/router';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { VIOLENCE_SITUATION_HISTORY, VIOLENCE_SITUATION_LIST } from '@historia-clinica/constants/summaries';
import { PageDto, ViolenceReportSituationDto, ViolenceReportSituationEvolutionDto } from '@api-rest/api-model';
import { ViolenceReportFacadeService } from '@api-rest/services/violence-report-facade.service';
import { map } from 'rxjs';
import { ItemListCard } from '@presentation/components/selectable-card/selectable-card.component';
import { dateDtoToDate, dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { dateTimeToViewDateHourMinute, dateToViewDate } from '@core/utils/date.utils';

@Component({
	selector: 'app-violence-situations',
	templateUrl: './violence-situations.component.html',
	styleUrls: ['./violence-situations.component.scss']
})
export class ViolenceSituationsComponent implements OnInit {
	
	constructor(private readonly dockPopupService: DockPopupService, 
				private route: ActivatedRoute,
				private violenceSituationReportFacadeService: ViolenceReportFacadeService) { }

	patientId: number;
	violenceListHeader: SummaryHeader = VIOLENCE_SITUATION_LIST;
	violenceHistoryHeader: SummaryHeader = VIOLENCE_SITUATION_HISTORY;
	violenceSituations: PageDto<ViolenceReportSituationDto>;
	evolutions: ItemListCard[] = [];
	showSeeAll: boolean = true;

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
			});
		this.setPatientViolenceSituations(true);
		this.violenceSituationReportFacadeService.violenceSituations$
			.subscribe((result: PageDto<ViolenceReportSituationDto>) => {
				this.violenceSituations = result;
				this.showSeeAll = result?.content.length !== result?.totalElementsAmount;
			});
		this.setEvolutions();
	}

	openViolenceSituationDockPopUp() {
		this.dockPopupService.open(ViolenceSituationDockPopupComponent, {
			data: {
				patientId: this.patientId,
			}
		});
	}

	setPatientViolenceSituations(mustBeLimited: boolean) {
		this.violenceSituationReportFacadeService.setAllPatientViolenceSituations(this.patientId, mustBeLimited);
	}	

	private setEvolutions = () => {
		this.violenceSituationReportFacadeService.setEvolutions(this.patientId, null);
		this.violenceSituationReportFacadeService.evolutions$
			.pipe(
				map((result: ViolenceReportSituationEvolutionDto[]) => {
					const itemListCard = result.map((evolution: ViolenceReportSituationEvolutionDto) => this.mapToItemListCard(evolution));
					return itemListCard;
				})
			).subscribe((result: ItemListCard[]) => this.evolutions = result);
	}

	private mapToItemListCard = (evolution: ViolenceReportSituationEvolutionDto): ItemListCard => {
		return {
			id: evolution.evolutionId,
			icon: 'assignment',
			title: `Situación #${evolution.situationId} (${dateToViewDate(dateDtoToDate(evolution.episodeDate))})`,
			options: [
				{
					title: `Evolución ${evolution.evolutionId}`,
					isImportant: true
				},
				{
					title: evolution.professionalFullName
				},
				{
					title: `${dateTimeToViewDateHourMinute(dateTimeDtoToDate(evolution.createdOn))} hs`
				},
			]
		}
	}
}