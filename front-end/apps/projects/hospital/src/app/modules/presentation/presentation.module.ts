import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule, FlexModule } from '@angular/flex-layout';
// deps
import { CoreModule } from '@core/core.module';
import { AppMaterialModule } from '@material/app.material.module';
// components
import { BarComponent } from './components/bar/bar.component';
import { CategoryHeaderDividerComponent } from './components/category-header-divider/category-header-divider.component';
import { CellTemplatesComponent } from './components/cell-templates/cell-templates.component';
import { ContentTitleComponent } from './components/content-title/content-title.component';
import { DatepickerComponent } from './components/datepicker/datepicker.component';
import { DetailBoxComponent } from './components/detail-box/detail-box.component';
import { DockPopupComponent } from './components/dock-popup/dock-popup.component';
import { DocumentSectionComponent } from './components/document-section/document-section.component';
import { DocumentSectionTableComponent } from './components/document-section-table/document-section-table.component';
import { EditableFieldComponent } from './components/editable-field/editable-field.component';
import { FactorDeRiesgoComponent } from './components/factor-de-riesgo-current/factor-de-riesgo.component';
import { FactorDeRiesgoCurrentPreviousComponent } from './components/factor-de-riesgo-current-previous/factor-de-riesgo-current-previous.component';
import { FiltersCardComponent } from './components/filters-card/filters-card.component';
import { FooterComponent } from './components/footer/footer.component';
import { ImgUploaderComponent } from './components/img-uploader/img-uploader.component';
import { IndicationComponent } from './components/indication/indication.component';
import { LabelComponent } from './components/label/label.component';
import { ListElementItemComponent } from './components/list-element-item/list-element-item.component';
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
import { SummaryCardComponent } from './components/summary-card/summary-card.component';
import { TableComponent } from './components/table/table.component';
import { TypeaheadComponent } from './components/typeahead/typeahead.component';
import { UserBadgeComponent } from './components/user-badge/user-badge.component';
import { TitledContentCardComponent } from './components/titled-content-card/titled-content-card.component';
import { ColoredLabelComponent } from './colored-label/colored-label.component';
import { CardComponent } from './components/card/card.component';
// dialogs
import { DatePickerComponent } from './dialogs/date-picker/date-picker.component';
import { DiscardWarningComponent } from './dialogs/discard-warning/discard-warning.component';
import { ConfirmDialogComponent } from './dialogs/confirm-dialog/confirm-dialog.component';
// directives
import { CtrlTemplateDirective } from './directives/ctrl-template.directive';
// pipes
import { DayTimeRangePipe } from './pipes/day-time-range.pipe';
import { FullHouseAddressPipe } from './pipes/fullHouseAddress.pipe';
import { PatientToPersonPipe } from "./pipes/PatientToPersonPipe";
import { PersonIdentificationPipe } from './pipes/person-identification.pipe';
import { ViewDateDtoPipe } from './pipes/view-date-dto.pipe';
import { ViewDatePipe } from './pipes/view-date.pipe';
import { ViewHourMinutePipe } from './pipes/view-hour-minute.pipe';

@NgModule({
	declarations: [
		// components
		BarComponent,
		CategoryHeaderDividerComponent,
		CardComponent,
		CellTemplatesComponent,
		ColoredLabelComponent,
		ContentTitleComponent,
		DatepickerComponent,
		DetailBoxComponent,
		DockPopupComponent,
		DocumentSectionComponent,
		DocumentSectionTableComponent,
		EditableFieldComponent,
		FactorDeRiesgoComponent,
		FactorDeRiesgoCurrentPreviousComponent,
		FiltersCardComponent,
		FooterComponent,
		ImgUploaderComponent,
		IndicationComponent,
		LabelComponent,
		ListElementItemComponent,
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
		SummaryCardComponent,
		TableComponent,
		TypeaheadComponent,
		UserBadgeComponent,
		TitledContentCardComponent,
		// dialogs
		ConfirmDialogComponent,
		DatePickerComponent,
		DiscardWarningComponent,
		// directives
		CtrlTemplateDirective,
		// pipes
		DayTimeRangePipe,
		FullHouseAddressPipe,
		PatientToPersonPipe,
		PersonIdentificationPipe,
		ViewDateDtoPipe,
		ViewDatePipe,
		ViewHourMinutePipe,
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
		TitledContentCardComponent,
		BarComponent,
		CategoryHeaderDividerComponent,
		CardComponent,
		CellTemplatesComponent,
		ColoredLabelComponent,
		ContentTitleComponent,
		DatepickerComponent,
		DetailBoxComponent,
		DockPopupComponent,
		DocumentSectionComponent,
		DocumentSectionTableComponent,
		EditableFieldComponent,
		FactorDeRiesgoComponent,
		FactorDeRiesgoCurrentPreviousComponent,
		FiltersCardComponent,
		ImgUploaderComponent,
		IndicationComponent,
		LabelComponent,
		ListElementItemComponent,
		LocationBadgeComponent,
		MainLayoutComponent,
		MenuComponent,
		NewDocumentSectionComponent,
		NoDataComponent,
		PatientCardComponent,
		PatientTypeLogoComponent,
		PersonalInformationComponent,
		SummaryCardComponent,
		TableComponent,
		TypeaheadComponent,
		UserBadgeComponent,
		// dialogs
		// directives
		CtrlTemplateDirective,
		// pipes
		DayTimeRangePipe,
		FullHouseAddressPipe,
		PatientToPersonPipe,
		PersonIdentificationPipe,
		ViewDateDtoPipe,
		ViewDatePipe,
		ViewHourMinutePipe,
	],
	entryComponents: [
		DockPopupComponent
	]
})
export class PresentationModule { }
