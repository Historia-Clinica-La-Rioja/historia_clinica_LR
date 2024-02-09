import { Component, OnInit } from '@angular/core';
import { ViolenceSituationDockPopupComponent } from '../../dialogs/violence-situation-dock-popup/violence-situation-dock-popup.component';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { ActivatedRoute } from '@angular/router';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { VIOLENCE_SITUATION_HISTORY, VIOLENCE_SITUATION_LIST } from '@historia-clinica/constants/summaries';
import { ViolenceReportFacadeService } from '@api-rest/services/violence-report-facade.service';
import { map } from 'rxjs';
import { ItemListCard, SelectableCardIds } from '@presentation/components/selectable-card/selectable-card.component';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { dateToViewDate } from '@core/utils/date.utils';
import { DetailedInformation } from '@presentation/components/detailed-information/detailed-information.component';
import { DateTimeDto, PageDto, ViolenceReportSituationDto, ViolenceReportSituationEvolutionDto } from '@api-rest/api-model';
import { ViewDateDtoPipe } from '@presentation/pipes/view-date-dto.pipe';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

@Component({
	selector: 'app-violence-situations',
	templateUrl: './violence-situations.component.html',
	styleUrls: ['./violence-situations.component.scss']
})
export class ViolenceSituationsComponent implements OnInit {

	constructor(private readonly dockPopupService: DockPopupService,
		private route: ActivatedRoute,
		private violenceSituationReportFacadeService: ViolenceReportFacadeService,
		private readonly dateFormatPipe: DateFormatPipe) { }

	patientId: number;
	violenceListHeader: SummaryHeader = VIOLENCE_SITUATION_LIST;
	violenceHistoryHeader: SummaryHeader = VIOLENCE_SITUATION_HISTORY;
	violenceSituations: PageDto<ViolenceReportSituationDto>;
	evolutions: ItemListCard[] = [];
	showSeeAll: boolean = true;
	selectedViolenceEvolution: DetailedInformation;
	viewDateDtoPipe: ViewDateDtoPipe = new ViewDateDtoPipe();

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
		this.violenceSituationReportFacadeService.detailedInformation$
			.subscribe((result: DetailedInformation) => this.selectedViolenceEvolution = result);
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

	seeDetails = (ids: SelectableCardIds) => {
		this.violenceSituationReportFacadeService.getEvolutionData(this.patientId, ids.id, ids.relatedId);
	}

	download(ids: SelectableCardIds) {
		this.violenceSituationReportFacadeService.download(this.patientId, ids.id, ids.relatedId);
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
			id: evolution.situationId,
			relatedId: evolution.evolutionId,
			icon: 'assignment',
			title: `Situación #${evolution.situationId} (${dateToViewDate(dateDtoToDate(evolution.episodeDate))})`,
			options: [
				{
					title: this.violenceSituationReportFacadeService.parseEvolutionText(evolution.evolutionId),
					isImportant: true
				},
				{
					title: evolution.professionalFullName
				},
				{
					title: `${this.parseDate(evolution.createdOn)}`
				},
			]
		}
	}

	private parseDate(dateTimeDto: DateTimeDto): string {
		const date: Date = this.viewDateDtoPipe.transform(dateTimeDto, 'localdatetime');
		return this.dateFormatPipe.transform(date, 'datetime');
	}

}
