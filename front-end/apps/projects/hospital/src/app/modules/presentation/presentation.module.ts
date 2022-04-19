import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule, FlexModule } from '@angular/flex-layout';
// deps
import { CoreModule } from '@core/core.module';
import { AppMaterialModule } from '@material/app.material.module';
// components
import { BarComponent } from './components/bar/bar.component';
import { CellTemplatesComponent } from './components/cell-templates/cell-templates.component';
import { ContentTitleComponent } from './components/content-title/content-title.component';
import { DatepickerComponent } from "@presentation/components/datepicker/datepicker.component";
import { DetailBoxComponent } from './components/detail-box/detail-box.component';
import { DockPopupComponent } from './components/dock-popup/dock-popup.component';
import { DocumentSectionTableComponent } from './components/document-section-table/document-section-table.component';
import { EditableFieldComponent } from './components/editable-field/editable-field.component';
import { FiltersCardComponent } from './components/filters-card/filters-card.component';
import { FooterComponent } from './components/footer/footer.component';
import { ImgUploaderComponent } from './components/img-uploader/img-uploader.component';
import { IndicationComponent } from "./components/indication/indication.component";
import { LabelComponent } from './components/label/label.component';
import { LocationBadgeComponent } from './components/location-badge/location-badge.component';
import { LogoComponent } from './components/logo/logo.component';
import { MainLayoutComponent } from './components/main-layout/main-layout.component';
import { MenuComponent } from './components/menu/menu.component';
import { MessageSnackbarComponent } from './components/message-snackbar/message-snackbar.component';
import { NewDocumentSectionComponent } from './components/new-document-section/new-document-section-component.component';
import { NoDataComponent } from './components/no-data/no-data.component';
import { PatientCardComponent } from './components/patient-card/patient-card.component';
import { PatientTypeLogoComponent } from './components/patient-type-logo/patient-type-logo.component';
import { PersonalInformationComponent } from './components/personal-information/personal-information.component';
import { FactorDeRiesgoComponent } from './components/factor-de-riesgo-current/factor-de-riesgo.component';
import { FactorDeRiesgoCurrentPreviousComponent } from './components/factor-de-riesgo-current-previous/factor-de-riesgo-current-previous.component';
import { SummaryCardComponent } from './components/summary-card/summary-card.component';
import { TableComponent } from './components/table/table.component';
import { TypeaheadComponent } from './components/typeahead/typeahead.component';
import { UserBadgeComponent } from './components/user-badge/user-badge.component';
// dialogs
import { ConfirmDialogComponent } from './dialogs/confirm-dialog/confirm-dialog.component';
import { DatePickerComponent } from './dialogs/date-picker/date-picker.component';
import { DocumentSectionComponent } from './components/document-section/document-section.component';
import { MedicalCoverageComponent } from './dialogs/medical-coverage/medical-coverage.component';
import { DiscardWarningComponent } from './dialogs/discard-warning/discard-warning.component';
// directives
import { CtrlTemplateDirective } from './directives/ctrl-template.directive';
// pipes
import { DayTimeRangePipe } from './pipes/day-time-range.pipe';
import { FullHouseAddressPipe } from './pipes/fullHouseAddress.pipe';
import { PersonIdentificationPipe } from './pipes/person-identification.pipe';
import { ViewDateDtoPipe } from './pipes/view-date-dto.pipe';
import { ViewDatePipe } from './pipes/view-date.pipe';
import { ViewHourMinutePipe } from './pipes/view-hour-minute.pipe';


@NgModule({
	declarations: [
		// components
		BarComponent,
		CellTemplatesComponent,
		ContentTitleComponent,
		DatepickerComponent,
		DetailBoxComponent,
		DockPopupComponent,
		DocumentSectionTableComponent,
		EditableFieldComponent,
		FiltersCardComponent,
		FooterComponent,
		ImgUploaderComponent,
		IndicationComponent,
		LabelComponent,
		LocationBadgeComponent,
		LogoComponent,
		MainLayoutComponent,
		MenuComponent,
		MessageSnackbarComponent,
		NewDocumentSectionComponent,
		NoDataComponent,
		PatientCardComponent,
		PatientTypeLogoComponent,
		PersonalInformationComponent,
		FactorDeRiesgoComponent,
		FactorDeRiesgoCurrentPreviousComponent,
		SummaryCardComponent,
		TableComponent,
		TypeaheadComponent,
		UserBadgeComponent,
		// dialogs
		ConfirmDialogComponent,
		DatePickerComponent,
		DocumentSectionComponent,
		DiscardWarningComponent,
		MedicalCoverageComponent,
		// directives
		CtrlTemplateDirective,
		// pipes
		DayTimeRangePipe,
		FullHouseAddressPipe,
		PersonIdentificationPipe,
		ViewDateDtoPipe,
		ViewDatePipe,
		ViewHourMinutePipe
	],
	imports: [
		CommonModule,
		FlexModule,
		FlexLayoutModule,
		// deps
		CoreModule,
		AppMaterialModule,
	],
	exports: [
		FlexModule,
		FlexLayoutModule,
		// deps
		CoreModule,
		AppMaterialModule,
		// components
		BarComponent,
		CellTemplatesComponent,
		ContentTitleComponent,
		DatepickerComponent,
		DetailBoxComponent,
		DockPopupComponent,
		DocumentSectionComponent,
		DocumentSectionTableComponent,
		EditableFieldComponent,
		FiltersCardComponent,
		ImgUploaderComponent,
		IndicationComponent,
		LabelComponent,
		LocationBadgeComponent,
		MainLayoutComponent,
		MenuComponent,
		NewDocumentSectionComponent,
		NoDataComponent,
		PatientCardComponent,
		PatientTypeLogoComponent,
		PersonalInformationComponent,
		FactorDeRiesgoComponent,
		FactorDeRiesgoCurrentPreviousComponent,
		SummaryCardComponent,
		TableComponent,
		TypeaheadComponent,
		UserBadgeComponent,
		// dialogs
		MedicalCoverageComponent,
		// directives
		CtrlTemplateDirective,
		// pipes
		DayTimeRangePipe,
		FullHouseAddressPipe,
		PersonIdentificationPipe,
		ViewDateDtoPipe,
		ViewDatePipe,
		ViewHourMinutePipe
	],
	entryComponents: [
		DockPopupComponent
	]
})
export class PresentationModule { }
