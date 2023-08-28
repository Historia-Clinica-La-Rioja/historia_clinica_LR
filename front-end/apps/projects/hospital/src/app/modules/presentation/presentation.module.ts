import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule, FlexModule } from '@angular/flex-layout';
// deps
import { CoreModule } from '@core/core.module';
import { AppMaterialModule } from '@material/app.material.module';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { QuillModule } from 'ngx-quill';
// components
import { AsignPatientButtonComponent } from './components/asign-patient-button/asign-patient-button.component';
import { BarComponent } from './components/bar/bar.component';
import { CardComponent } from './components/card/card.component';
import { CategoryHeaderDividerComponent } from './components/category-header-divider/category-header-divider.component';
import { CellTemplatesComponent } from './components/cell-templates/cell-templates.component';
import { ColoredDivPatientStateComponent } from './components/colored-div-patient-state/colored-div-patient-state.component';
import { ColoredIconTextComponent } from './components/colored-icon-text/colored-icon-text.component';
import { ColoredLabelComponent } from './colored-label/colored-label.component';
import { ContentTitleComponent } from './components/content-title/content-title.component';
import { DatepickerComponent } from './components/datepicker/datepicker.component';
import { DateRangePickerComponent } from './components/date-range-picker/date-range-picker.component';
import { DetailBoxComponent } from './components/detail-box/detail-box.component';
import { DockPopupComponent } from './components/dock-popup/dock-popup.component';
import { DocumentSectionComponent } from './components/document-section/document-section.component';
import { DocumentSectionTableComponent } from './components/document-section-table/document-section-table.component';
import { EditableFieldComponent } from './components/editable-field/editable-field.component';
import { EmergencyCareTemporaryPatientHeader } from './components/emergency-care-temporary-patient-header/emergency-care-temporary-patient-header.component';
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
import { MessageFlaggedForAuditComponent } from './components/message-flagged-for-audit/message-flagged-for-audit.component';
import { MessageSnackbarComponent } from './components/message-snackbar/message-snackbar.component';
import { NewDocumentSectionComponent } from './components/new-document-section/new-document-section-component.component';
import { NoDataComponent } from './components/no-data/no-data.component';
import { PatientCardComponent } from './components/patient-card/patient-card.component';
import { PatientCardHeaderComponent } from './components/patient-card-header/patient-card-header.component';
import { PatientTypeLogoComponent } from './components/patient-type-logo/patient-type-logo.component';
import { PersonalInformationComponent } from './components/personal-information/personal-information.component';
import { RichTextEditorComponent } from './components/rich-text-editor/rich-text-editor.component';
import { SummaryCardComponent } from './components/summary-card/summary-card.component';
import { TableComponent } from './components/table/table.component';
import { TitledContentCardComponent } from './components/titled-content-card/titled-content-card.component';
import { TitledContentComponent } from './components/titled-content/titled-content.component';
import { TypeaheadComponent } from './components/typeahead/typeahead.component';
import { UserBadgeComponent } from './components/user-badge/user-badge.component';
import { FilterButtonComponent } from './components/filter-button/filter-button.component';
import { FloatingDivComponent } from './components/floating-div/floating-div.component';
import { PrioritySelectComponent } from './components/priority-select/priority-select.component';
import { PersonShortDescriptionComponent } from './components/person-short-description/person-short-description.component';
import { IconedTextComponent } from './components/iconed-text/iconed-text.component';
import { PriorityComponent } from './components/priority/priority.component';
// dialogs
import { ConfirmDialogComponent } from './dialogs/confirm-dialog/confirm-dialog.component';
import { DatePickerComponent } from './dialogs/date-picker/date-picker.component';
import { DiscardWarningComponent } from './dialogs/discard-warning/discard-warning.component';
import { ViewPdfComponent } from './dialogs/view-pdf/view-pdf.component';
// directives
import { CtrlTemplateDirective } from './directives/ctrl-template.directive';
// pipes
import { DayTimeRangePipe } from './pipes/day-time-range.pipe';
import { FullHouseAddressPipe } from './pipes/fullHouseAddress.pipe';
import { FullMedicalCoveragePipe } from './pipes/full-medical-coverage.pipe';
import { PaginatePipe } from './pipes/paginate.pipe';
import { PatientToPersonPipe } from "./pipes/PatientToPersonPipe";
import { PersonIdentificationPipe } from './pipes/person-identification.pipe';
import { TimeDtoToLocalTimePipe } from './pipes/time-dto-to-local-time.pipe';
import { ViewDateDtoPipe } from './pipes/view-date-dto.pipe';
import { ViewDatePipe } from './pipes/view-date.pipe';
import { ViewHourMinutePipe } from './pipes/view-hour-minute.pipe';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { CallDetailsComponent } from './components/call-details/call-details.component';

@NgModule({
	declarations: [
		// components
		AsignPatientButtonComponent,
		BarComponent,
		CallDetailsComponent,
		CardComponent,
		CategoryHeaderDividerComponent,
		CellTemplatesComponent,
		ColoredDivPatientStateComponent,
		ColoredIconTextComponent,
		ColoredLabelComponent,
		ContentTitleComponent,
		DatepickerComponent,
		DateRangePickerComponent,
		DetailBoxComponent,
		DockPopupComponent,
		DocumentSectionComponent,
		DocumentSectionTableComponent,
		EditableFieldComponent,
		EmergencyCareTemporaryPatientHeader,
		FactorDeRiesgoComponent,
		FactorDeRiesgoCurrentPreviousComponent,
		FiltersCardComponent,
		FloatingDivComponent,
		FooterComponent,
		ImgUploaderComponent,
		IndicationComponent,
		LabelComponent,
		ListElementItemComponent,
		LocationBadgeComponent,
		LogoComponent,
		MainLayoutComponent,
		MenuComponent,
		MessageFlaggedForAuditComponent,
		MessageSnackbarComponent,
		NewDocumentSectionComponent,
		NoDataComponent,
		PatientCardComponent,
		PatientCardHeaderComponent,
		PatientTypeLogoComponent,
		PersonalInformationComponent,
		RichTextEditorComponent,
		SummaryCardComponent,
		TableComponent,
		TitledContentCardComponent,
		TitledContentComponent,
		TypeaheadComponent,
		UserBadgeComponent,
		FilterButtonComponent,
		PrioritySelectComponent,
		// dialogs
		ConfirmDialogComponent,
		DatePickerComponent,
		DiscardWarningComponent,
		ViewPdfComponent,
		// directives
		CtrlTemplateDirective,
		// pipes
		DayTimeRangePipe,
		FullHouseAddressPipe,
		FullMedicalCoveragePipe,
		PaginatePipe,
		PatientToPersonPipe,
		PersonIdentificationPipe,
		TimeDtoToLocalTimePipe,
		ViewDateDtoPipe,
		ViewDatePipe,
		ViewHourMinutePipe,
		PersonShortDescriptionComponent,
		IconedTextComponent,
		PriorityComponent,
	],
	imports: [
		CommonModule,
		FlexModule,
		FlexLayoutModule,
		// deps
		CoreModule,
		AppMaterialModule,
		PdfViewerModule,
		QuillModule.forRoot(),
		DragDropModule
	],
	exports: [
		FlexModule,
		FlexLayoutModule,
		// deps
		CoreModule,
		AppMaterialModule,
		// components
		AsignPatientButtonComponent,
		BarComponent,
		CallDetailsComponent,
		CardComponent,
		CategoryHeaderDividerComponent,
		CellTemplatesComponent,
		ColoredDivPatientStateComponent,
		ColoredIconTextComponent,
		ColoredLabelComponent,
		ContentTitleComponent,
		DatepickerComponent,
		DateRangePickerComponent,
		DetailBoxComponent,
		DockPopupComponent,
		DocumentSectionComponent,
		DocumentSectionTableComponent,
		EditableFieldComponent,
		EmergencyCareTemporaryPatientHeader,
		FactorDeRiesgoComponent,
		FactorDeRiesgoCurrentPreviousComponent,
		FiltersCardComponent,
		FloatingDivComponent,
		IconedTextComponent,
		ImgUploaderComponent,
		IndicationComponent,
		LabelComponent,
		ListElementItemComponent,
		LocationBadgeComponent,
		LogoComponent,
		MainLayoutComponent,
		MenuComponent,
		MessageFlaggedForAuditComponent,
		NewDocumentSectionComponent,
		NoDataComponent,
		PatientCardComponent,
		PatientCardHeaderComponent,
		PatientTypeLogoComponent,
		PersonalInformationComponent,
		PersonShortDescriptionComponent,
		PriorityComponent,
		RichTextEditorComponent,
		SummaryCardComponent,
		TableComponent,
		TitledContentCardComponent,
		TitledContentComponent,
		TypeaheadComponent,
		UserBadgeComponent,
		FilterButtonComponent,
		PrioritySelectComponent,
		// dialogs
		// directives
		CtrlTemplateDirective,
		// pipes
		DayTimeRangePipe,
		FullHouseAddressPipe,
		FullMedicalCoveragePipe,
		PaginatePipe,
		PatientToPersonPipe,
		PersonIdentificationPipe,
		TimeDtoToLocalTimePipe,
		ViewDateDtoPipe,
		ViewDatePipe,
		ViewHourMinutePipe,
	],
	entryComponents: [
		DockPopupComponent,
	]
})
export class PresentationModule { }
