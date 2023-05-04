import {Component, Input} from '@angular/core';
import {UIComponentDto, UILabelDto} from '@extensions/extensions-model';
import {
	ChartDefinitionService,
	FilterValue,
} from '@extensions/services/chart-definition.service';
import {HttpClient} from "@angular/common/http";
import {ContextService} from "@core/services/context.service";
import {FeatureFlagService} from "@core/services/feature-flag.service";
import {AppFeature} from "@api-rest/api-model";

export interface FilterDefinition {
	member: string;
	operator: string;
}

export interface FilterFormDefinition {
	filter: FilterDefinition;
	type: string;
	label: string;
}

export interface QueryFilters {
	[key: string]: FilterFormDefinition;
}

export interface FilterValues {
	[key: string]: string[];
}

const CUBEJS_CHART_COMPONENT = "cubejs-chart";
const TABS_COMPONENT = "tabs";
const PROFESSIONAL_FILTER_KEY = "professional";
const  MEASURE_PROFESSIONAL_SELF_DETERMINATION = "profesional_autopercibido";
@Component({
	selector: 'app-cubejs-dashboard',
	templateUrl: './cubejs-dashboard.component.html',
	styleUrls: ['./cubejs-dashboard.component.scss']
})
export class CubejsDashboardComponent {

	@Input() set content(content: UIComponentDto[]) {
		this.chartDefinitionService = new ChartDefinitionService(this.http, this.context);
		this.chartDefinitionService.next([]);
		this.setChartService(content);
		this._content = content;
	}

	@Input() set filters(filters: QueryFilters){
		this._filters = filters;
		this.params = {};
		this.configureSelfDeterminationFilter();
	};

	@Input() title?: UILabelDto;

	private params: FilterValues = {};
	public _content: UIComponentDto[];
	public _filters: QueryFilters;
	public chartDefinitionService: ChartDefinitionService;

	constructor(
		private http: HttpClient,
		private context: ContextService,
		private featureFlagService: FeatureFlagService
	) {
	}

	changeValue(key: string, values: string[]) {
		this.params[key] = values;

		let filtersToAdd: FilterValue[] = Object.entries(this.params)
			.map(([key, values]) => ({
				...this._filters[key].filter,
				values,
			}));
		filtersToAdd = filtersToAdd.filter(filter => filter.values);
		this.chartDefinitionService.next(filtersToAdd);
	}

	setChartService(content: UIComponentDto[]) {
		content.forEach(c => {
			if (c.type === CUBEJS_CHART_COMPONENT)
				c.args.service = this.chartDefinitionService;
			else if (c.args.content)
				this.setChartService(<UIComponentDto[]>c.args.content)
			else if (c.type === TABS_COMPONENT){
				c.args.tabs.forEach(tab=> this.setChartService(<UIComponentDto[]>tab.args.content));
			}
		});
	}

	configureSelfDeterminationFilter(): void {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			if (isOn) {
				const professionalFilter = this._filters[PROFESSIONAL_FILTER_KEY];
				if (professionalFilter)
					professionalFilter.filter.member = professionalFilter.filter.member.split(".")[0] + "." + MEASURE_PROFESSIONAL_SELF_DETERMINATION ;
			}
		});
	}
}
