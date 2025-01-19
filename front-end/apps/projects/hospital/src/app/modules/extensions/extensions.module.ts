import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { NgChartsModule } from 'ng2-charts';
import { CubejsClientModule } from '@cubejs-client/ngx';
// deps
import { PresentationModule } from '@presentation/presentation.module';
// routing
import { InstitutionRoutedExternalComponent } from './institution-routed-external/institution-routed-external.component';

// components
import { AppointmentStateInputComponent } from './components/appointment-state-input/appointment-state-input.component';
import { CubejsCardComponent } from './components/cubejs-card/cubejs-card.component';
import { CubejsChartComponent } from './components/cubejs-chart/cubejs-chart.component';
import { CubejsDashboardComponent } from './components/cubejs-dashboard/cubejs-dashboard.component';
import { DateOnlyIsoFormInputComponent } from './components/date-only-iso-form-input/date-only-iso-form-input.component';
import { DownloadCsvButtonComponent } from './components/download-csv-button/download-csv-button.component';
import { FreeTextFormInputComponent } from './components/free-text-form-input/free-text-form-input.component';
import { HtmlComponent } from './components/html/html.component';
import { JsonComponent } from './components/json/json.component';
import { MultiselectCubejsDashboardComponent } from './components/multiselect-cubejs-dashboard/multiselect-cubejs-dashboard.component';
import { PageLayoutComponent } from './components/page-layout/page-layout.component';
import { ProfessionalFormInputComponent } from './components/professional-form-input/professional-form-input.component';
import { QueryCardRendererComponent } from './components/query-card-renderer/query-card-renderer.component';
import { QueryChartRendererComponent } from './components/query-chart-renderer/query-chart-renderer.component';
import { QueryRendererComponent } from './components/query-renderer/query-renderer.component';
import { QueryTableRendererComponent } from './components/query-table-renderer/query-table-renderer.component';
import { RoutedExternalComponent } from './components/routed-external/routed-external.component';
import { SpecialtyFormInputComponent } from './components/specialty-form-input/specialty-form-input.component';
import { TabsComponent } from './components/tabs/tabs.component';
import { UiCardComponent } from './components/ui-card/ui-card.component';
import { UiChartComponent } from './components/ui-chart/ui-chart.component';
import { UiComponentComponent } from './components/ui-component/ui-component.component';
import { UiComponentListComponent } from './components/ui-component-list/ui-component-list.component';
import { UiExternalComponentComponent } from './components/ui-external-component/ui-external-component.component';


// config options
import { cubejsOptions } from './extensions-cubejs';

@NgModule({
	imports: [
		CommonModule,
		NgChartsModule,
		ReactiveFormsModule,
		CubejsClientModule.forRoot(cubejsOptions),
		// deps
		PresentationModule,
	],
	declarations: [
		// routing
		InstitutionRoutedExternalComponent,
		// components
		AppointmentStateInputComponent,
		CubejsCardComponent,
		CubejsChartComponent,
		CubejsDashboardComponent,
		DateOnlyIsoFormInputComponent,
		DownloadCsvButtonComponent,
		FreeTextFormInputComponent,
		HtmlComponent,
		JsonComponent,
		MultiselectCubejsDashboardComponent,
		PageLayoutComponent,
		ProfessionalFormInputComponent,
		QueryCardRendererComponent,
		QueryChartRendererComponent,
		QueryRendererComponent,
		QueryTableRendererComponent,
		RoutedExternalComponent,
		SpecialtyFormInputComponent,
		TabsComponent,
		UiCardComponent,
		UiChartComponent,
		UiComponentComponent,
		UiComponentListComponent,
		UiExternalComponentComponent,
	],
	exports: [
		// components
		PageLayoutComponent,
		UiExternalComponentComponent,
		UiComponentComponent,
		JsonComponent,
	]
})
export class ExtensionsModule { }
