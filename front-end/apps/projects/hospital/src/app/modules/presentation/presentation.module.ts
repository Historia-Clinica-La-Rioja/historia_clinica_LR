import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule, FlexModule } from '@angular/flex-layout';

import { CoreModule } from '@core/core.module';

import { PatientCardComponent } from './components/patient-card/patient-card.component';
import { SummaryCardComponent } from './components/summary-card/summary-card.component';
import { InternmentEpisodeSummaryComponent } from './components/internment-episode-summary/internment-episode-summary.component';
import { NoDataComponent } from './components/no-data/no-data.component';
import { TableComponent } from './components/table/table.component';
import { SignoVitalCurrentPreviousComponent } from './components/signo-vital-current-previous/signo-vital-current-previous.component';
import { DetailBoxComponent } from './components/detail-box/detail-box.component';
import { FullHouseAddressPipe } from './pipes/fullHouseAddress.pipe';
import { PersonalInformationComponent } from './components/personal-information/personal-information.component';
import { PatientTypeLogoComponent } from './components/patient-type-logo/patient-type-logo.component';
import { MessageSnackbarComponent } from './components/message-snackbar/message-snackbar.component';
import { MainLayoutComponent } from './components/main-layout/main-layout.component';
import { BarComponent } from './components/bar/bar.component';
import { MenuComponent } from './components/menu/menu.component';
import { LabelComponent } from './components/label/label.component';
import { FooterComponent } from './components/footer/footer.component';
import { DayTimeRangePipe } from './pipes/day-time-range.pipe';
import { ViewDatePipe } from './pipes/view-date.pipe';
import { ViewHourMinutePipe } from './pipes/view-hour-minute.pipe';
import { PersonIdentificationPipe } from './pipes/person-identification.pipe';
import { EditableFieldComponent } from './components/editable-field/editable-field.component';
import { LogoComponent } from './components/logo/logo.component';
import { DockPopupComponent } from './components/dock-popup/dock-popup.component';
import { FiltersCardComponent } from './components/filters-card/filters-card.component';
import { ViewDateDtoPipe } from './pipes/view-date-dto.pipe';
import { ImgUploaderComponent } from './components/img-uploader/img-uploader.component';
import { SignoVitalComponent } from './components/signo-vital-current/signo-vital.component';
import { ContentTitleComponent } from './components/content-title/content-title.component';
import { PageComponent } from './components/page/page.component';
import { PageComponentsComponent } from './components/page-components/page-components.component';
import { UiComponentComponent } from './components/ui-component/ui-component.component';
import { NewDocumentSectionComponent } from './components/new-document-section/new-document-section-component.component';
import { DocumentSectionTableComponent } from './components/document-section-table/document-section-table.component';
import { CtrlTemplateDirective } from './directives/ctrl-template.directive';
import { CellTemplatesComponent } from './components/cell-templates/cell-templates.component';
import { TypeaheadComponent } from './components/typeahead/typeahead.component';

import { ConfirmDialogComponent } from './dialogs/confirm-dialog/confirm-dialog.component';
import { DatePickerComponent } from './dialogs/date-picker/date-picker.component';

@NgModule({
	declarations: [
		BarComponent,
		InternmentEpisodeSummaryComponent,
		NoDataComponent,
		PatientCardComponent,
		SignoVitalCurrentPreviousComponent,
		SummaryCardComponent,
		TableComponent,
		DetailBoxComponent,
		InternmentEpisodeSummaryComponent,
		FullHouseAddressPipe,
		PersonalInformationComponent,
		PatientTypeLogoComponent,
		MessageSnackbarComponent,
		MainLayoutComponent,
		MenuComponent,
		LabelComponent,
		FooterComponent,
		DayTimeRangePipe,
		ViewDatePipe,
		ViewHourMinutePipe,
		PersonIdentificationPipe,
		EditableFieldComponent,
		DockPopupComponent,
		LogoComponent,
		FiltersCardComponent,
		ViewDateDtoPipe,
		ImgUploaderComponent,
		SignoVitalComponent,
		ContentTitleComponent,
		PageComponent,
		PageComponentsComponent,
		UiComponentComponent,
		NewDocumentSectionComponent,
		DocumentSectionTableComponent,
		CtrlTemplateDirective,
		CellTemplatesComponent,
		TypeaheadComponent,
		// dialogs
		ConfirmDialogComponent,
		DatePickerComponent,
	],
	imports: [
		CommonModule,
		FlexModule,
		FlexLayoutModule,
		CoreModule,
	],
	exports: [
		BarComponent,
		InternmentEpisodeSummaryComponent,
		NoDataComponent,
		PatientCardComponent,
		SignoVitalCurrentPreviousComponent,
		SummaryCardComponent,
		TableComponent,
		DetailBoxComponent,
		InternmentEpisodeSummaryComponent,
		FullHouseAddressPipe,
		PersonalInformationComponent,
		PatientTypeLogoComponent,
		MainLayoutComponent,
		MenuComponent,
		LabelComponent,
		FlexModule,
		FlexLayoutModule,
		DayTimeRangePipe,
		ViewDatePipe,
		ViewHourMinutePipe,
		PersonIdentificationPipe,
		EditableFieldComponent,
		DockPopupComponent,
		FiltersCardComponent,
		ViewDateDtoPipe,
		ImgUploaderComponent,
		SignoVitalComponent,
		ContentTitleComponent,
		PageComponent,
		NewDocumentSectionComponent,
		DocumentSectionTableComponent,
		CtrlTemplateDirective,
		CellTemplatesComponent,
		TypeaheadComponent,
	],
	entryComponents: [
		DockPopupComponent
	]
})
export class PresentationModule { }
