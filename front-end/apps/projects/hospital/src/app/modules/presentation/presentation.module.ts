import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule, FlexModule } from '@angular/flex-layout';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { QuillModule } from 'ngx-quill';
// deps
import { CoreModule } from '@core/core.module';
import { AppMaterialModule } from '@material/app.material.module';
// components
import { AddObservationsComponent } from './components/add-observations/add-observations.component';
import { AsignPatientButtonComponent } from './components/asign-patient-button/asign-patient-button.component';
import { BarComponent } from './components/bar/bar.component';
import { ButtonComponent } from './components/button/button.component';
import { CardComponent } from './components/card/card.component';
import { CategoryHeaderDividerComponent } from './components/category-header-divider/category-header-divider.component';
import { CellTemplatesComponent } from './components/cell-templates/cell-templates.component';
import { ChipsAutocompleteComponent } from './components/chips-autocomplete/chips-autocomplete.component';
import { ColoredDivPatientStateComponent } from './components/colored-div-patient-state/colored-div-patient-state.component';
import { ColoredIconTextComponent } from './components/colored-icon-text/colored-icon-text.component';
import { ColoredLabelComponent } from './colored-label/colored-label.component';
import { ColorSelectorComponent } from './components/color-selector/color-selector.component';
import { ContentTitleComponent } from './components/content-title/content-title.component';
import { DatepickerComponent } from './components/datepicker/datepicker.component';
import { DateRangePickerComponent } from './components/date-range-picker/date-range-picker.component';
import { DetailBoxComponent } from './components/detail-box/detail-box.component';
import { DetailedInformationComponent } from './components/detailed-information/detailed-information.component';
import { DetailsSectionCustomComponent } from './components/details-section-custom/details-section-custom.component';
import { DescriptionItemComponent } from './components/description-item/description-item.component';
import { DockPopupComponent } from './components/dock-popup/dock-popup.component';
import { DocumentSectionComponent } from './components/document-section/document-section.component';
import { DocumentSectionTableComponent } from './components/document-section-table/document-section-table.component';
import { DownloadButtonComponent } from './components/download-button/download-button.component';
import { EditableFieldComponent } from './components/editable-field/editable-field.component';
import { EmergencyCareTemporaryPatientHeader } from './components/emergency-care-temporary-patient-header/emergency-care-temporary-patient-header.component';
import { ExpansionSectionComponent } from './components/expansion-section/expansion-section.component';
import { FactorDeRiesgoComponent } from './components/factor-de-riesgo-current/factor-de-riesgo.component';
import { FactorDeRiesgoCurrentPreviousComponent } from './components/factor-de-riesgo-current-previous/factor-de-riesgo-current-previous.component';
import { FilterButtonComponent } from './components/filter-button/filter-button.component';
import { FiltersCardComponent } from './components/filters-card/filters-card.component';
import { FiltersComponent } from './components/filters/filters.component';
import { FiltersSelectComponent } from './components/filters-select/filters-select.component';
import { FiltersSelectV2Component } from './components/filters-select-v2/filters-select-v2.component';
import { FiltersTypeaheadComponent } from '../presentation/components/filters-typeahead/filters-typeahead.component';
import { FloatingDivComponent } from './components/floating-div/floating-div.component';
import { FooterComponent } from './components/footer/footer.component';
import { HideShowToggleComponent } from './components/hide-show-toggle/hide-show-toggle.component';
import { IconedTextComponent } from './components/iconed-text/iconed-text.component';
import { IconedTitledSectionComponent } from './components/iconed-titled-section/iconed-titled-section.component';
import { IdentifierComponent } from './components/identifier/identifier.component';
import { ImgCarouselComponent } from './components/img-carousel/img-carousel.component';
import { ImgUploaderComponent } from './components/img-uploader/img-uploader.component';
import { IndicationComponent } from './components/indication/indication.component';
import { ItemSummaryComponent } from './components/item-summary/item-summary.component';
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
import { PaginatorComponent } from './components/paginator/paginator.component';
import { PatientCardComponent } from './components/patient-card/patient-card.component';
import { PatientCardHeaderComponent } from './components/patient-card-header/patient-card-header.component';
import { PatientTypeLogoComponent } from './components/patient-type-logo/patient-type-logo.component';
import { PersonalInformationComponent } from './components/personal-information/personal-information.component';
import { PersonShortDescriptionComponent } from './components/person-short-description/person-short-description.component';
import { PriorityComponent } from './components/priority/priority.component';
import { PrioritySelectComponent } from './components/priority-select/priority-select.component';
import { ProfessionalSelectComponent } from './components/professional-select/professional-select.component';
import { RadioGroupComponent } from './components/radio-group/radio-group.component';
import { RegisterEditorInfoComponent } from './components/register-editor-info/register-editor-info.component';
import { RichTextEditorComponent } from './components/rich-text-editor/rich-text-editor.component';
import { RouteBackComponent } from './components/route-back/route-back.component';
import { RouteMenuComponent } from './components/route-menu/route-menu.component';
import { RouteMenuItemComponent } from './components/route-menu-item/route-menu-item.component';
import { SearchComponent } from './components/search/search.component';
import { SelectableCardComponent } from './components/selectable-card/selectable-card.component';
import { SummaryCardComponent } from './components/summary-card/summary-card.component';
import { TableComponent } from './components/table/table.component';
import { TimePickerComponent } from './components/time-picker/time-picker.component';
import { TitledContentCardComponent } from './components/titled-content-card/titled-content-card.component';
import { TitledContentComponent } from './components/titled-content/titled-content.component';
import { TitleDescriptionComponent } from './components/title-description/title-description.component';
import { TitleDescriptionListComponent } from './components/title-description-list/title-description-list.component';
import { TitledSingleContentComponent } from './components/titled-single-content/titled-single-content.component';
import { TypeaheadComponent } from './components/typeahead/typeahead.component';
import { TypeaheadFilterOptionsComponent } from './components/typeahead-filter-options/typeahead-filter-options.component';
import { TypeaheadV2Component } from './components/typeahead-v2/typeahead-v2.component';
import { UserBadgeComponent } from './components/user-badge/user-badge.component';
// dialogs
import { ConfirmDialogComponent } from './dialogs/confirm-dialog/confirm-dialog.component';
import { DatePickerComponent } from './dialogs/date-picker/date-picker.component';
import { DiscardWarningComponent } from './dialogs/discard-warning/discard-warning.component';
import { FileDownloadComponent } from './dialogs/view-pdf/file-download/file-download.component';
import { FileDownloadCardComponent } from './dialogs/view-pdf/file-download-card/file-download-card.component';
import { FileViewerPdfComponent } from './dialogs/view-pdf/file-viewer-pdf/file-viewer-pdf.component';
import { ViewPdfComponent } from './dialogs/view-pdf/view-pdf.component';
// directives
import { CtrlTemplateDirective } from './directives/ctrl-template.directive';
// pipes
import { DateFormatPipe } from './pipes/date-format.pipe';
import { DayTimeRangePipe } from './pipes/day-time-range.pipe';
import { FullHouseAddressPipe } from './pipes/fullHouseAddress.pipe';
import { FullMedicalCoveragePipe } from './pipes/full-medical-coverage.pipe';
import { IsoToDatePipe } from './pipes/iso-to-date.pipe';
import { PaginatePipe } from './pipes/paginate.pipe';
import { PatientToPersonPipe } from './pipes/PatientToPersonPipe';
import { PersonIdentificationPipe } from './pipes/person-identification.pipe';
import { ShowMoreConceptsPipe } from './pipes/show-more-concepts.pipe';
import { TimeDtoToLocalTimePipe } from './pipes/time-dto-to-local-time.pipe';
import { ViewDateDtoPipe } from './pipes/view-date-dto.pipe';
import { ViewHourMinutePipe } from './pipes/view-hour-minute.pipe';

@NgModule({
	declarations: [
		// components
		AddObservationsComponent,
		AsignPatientButtonComponent,
		BarComponent,
		ButtonComponent,
		CardComponent,
		CategoryHeaderDividerComponent,
		CellTemplatesComponent,
		ChipsAutocompleteComponent,
		ColoredDivPatientStateComponent,
		ColoredIconTextComponent,
		ColoredLabelComponent,
		ColorSelectorComponent,
		ContentTitleComponent,
		DatepickerComponent,
		DateRangePickerComponent,
    	DescriptionItemComponent,
		DetailBoxComponent,
		DetailedInformationComponent,
		DetailsSectionCustomComponent,
		DockPopupComponent,
		DocumentSectionComponent,
		DocumentSectionTableComponent,
		DownloadButtonComponent,
		EditableFieldComponent,
		EmergencyCareTemporaryPatientHeader,
		ExpansionSectionComponent,
		FactorDeRiesgoComponent,
		FactorDeRiesgoCurrentPreviousComponent,
		FilterButtonComponent,
		FiltersCardComponent,
		FiltersComponent,
		FiltersSelectComponent,
		FiltersSelectV2Component,
		FiltersTypeaheadComponent,
		FloatingDivComponent,
		FooterComponent,
		HideShowToggleComponent,
		IconedTextComponent,
		IconedTitledSectionComponent,
		IdentifierComponent,
		ImgCarouselComponent,
		ImgUploaderComponent,
		IndicationComponent,
		ItemSummaryComponent,
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
		PaginatorComponent,
		PatientCardComponent,
		PatientCardHeaderComponent,
		PatientTypeLogoComponent,
		PersonalInformationComponent,
		PersonShortDescriptionComponent,
		PriorityComponent,
		PrioritySelectComponent,
		ProfessionalSelectComponent,
		RadioGroupComponent,
		RegisterEditorInfoComponent,
		RichTextEditorComponent,
		RouteBackComponent,
		RouteMenuComponent,
		RouteMenuItemComponent,
		SearchComponent,
		SelectableCardComponent,
		SummaryCardComponent,
		TableComponent,
		TimePickerComponent,
		TitledContentCardComponent,
		TitledContentComponent,
		TitleDescriptionComponent,
		TitledSingleContentComponent,
		TitleDescriptionComponent,
		TitleDescriptionListComponent,
		TypeaheadComponent,
		TypeaheadFilterOptionsComponent,
		TypeaheadV2Component,
		UserBadgeComponent,
		// dialogs
		ConfirmDialogComponent,
		DatePickerComponent,
		DiscardWarningComponent,
		FileDownloadComponent,
		FileDownloadCardComponent,
		FileViewerPdfComponent,
		ViewPdfComponent,
		// directives
		CtrlTemplateDirective,
		// pipes
		DateFormatPipe,
		DayTimeRangePipe,
		FullHouseAddressPipe,
		FullMedicalCoveragePipe,
		IsoToDatePipe,
		PaginatePipe,
		PatientToPersonPipe,
		PersonIdentificationPipe,
		ShowMoreConceptsPipe,
		TimeDtoToLocalTimePipe,
		ViewDateDtoPipe,
		ViewHourMinutePipe
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
		AddObservationsComponent,
		AsignPatientButtonComponent,
		BarComponent,
		ButtonComponent,
		CardComponent,
		CategoryHeaderDividerComponent,
		CellTemplatesComponent,
		ChipsAutocompleteComponent,
		ColoredDivPatientStateComponent,
		ColoredIconTextComponent,
		ColoredLabelComponent,
		ColorSelectorComponent,
		ContentTitleComponent,
		DatepickerComponent,
		DateRangePickerComponent,
		DetailBoxComponent,
		DetailedInformationComponent,
		DetailsSectionCustomComponent,
    	DescriptionItemComponent,
		DockPopupComponent,
		DocumentSectionComponent,
		DocumentSectionTableComponent,
		DownloadButtonComponent,
		EditableFieldComponent,
		EmergencyCareTemporaryPatientHeader,
		ExpansionSectionComponent,
		FactorDeRiesgoComponent,
		FactorDeRiesgoCurrentPreviousComponent,
		FilterButtonComponent,
		FiltersCardComponent,
		FiltersComponent,
		FiltersSelectComponent,
		FiltersSelectV2Component,
		FloatingDivComponent,
		HideShowToggleComponent,
		IconedTextComponent,
		IconedTitledSectionComponent,
		IdentifierComponent,
		ImgCarouselComponent,
		ImgUploaderComponent,
		IndicationComponent,
		ItemSummaryComponent,
		LabelComponent,
		ListElementItemComponent,
		LocationBadgeComponent,
		LogoComponent,
		MainLayoutComponent,
		MenuComponent,
		MessageFlaggedForAuditComponent,
		NewDocumentSectionComponent,
		NoDataComponent,
		PaginatorComponent,
		PatientCardComponent,
		PatientCardHeaderComponent,
		PatientTypeLogoComponent,
		PersonalInformationComponent,
		PersonShortDescriptionComponent,
		PriorityComponent,
		PrioritySelectComponent,
		ProfessionalSelectComponent,
		RadioGroupComponent,
		RegisterEditorInfoComponent,
		RichTextEditorComponent,
		RouteBackComponent,
		RouteMenuComponent,
		SearchComponent,
		SelectableCardComponent,
		SummaryCardComponent,
		TableComponent,
		TimePickerComponent,
		TitledContentCardComponent,
		TitledContentComponent,
		TitleDescriptionComponent,
		TitleDescriptionListComponent,
		TitledSingleContentComponent,
		TypeaheadComponent,
		TypeaheadV2Component,
		UserBadgeComponent,
		// dialogs
		// directives
		CtrlTemplateDirective,
		// pipes
		DateFormatPipe,
		DayTimeRangePipe,
		FullHouseAddressPipe,
		FullMedicalCoveragePipe,
		IsoToDatePipe,
		PaginatePipe,
		PatientToPersonPipe,
		PersonIdentificationPipe,
		ShowMoreConceptsPipe,
		TimeDtoToLocalTimePipe,
		ViewDateDtoPipe,
		ViewHourMinutePipe,
	],
	entryComponents: [
		DockPopupComponent,
	]
})
export class PresentationModule { }
