import { Component, EventEmitter, Output } from '@angular/core';
import { EElectronicSignatureStatus, ElectronicJointSignatureInvolvedDocumentListFilterDto } from '@api-rest/api-model';
import { JointSignatureService } from '@api-rest/services/joint-signature.service';
import { deepClone } from '@core/utils/core.utils';
import { SIGNATURE_STATUS_FILTER, SIGNATURE_STATUS_KEY } from '../../constants/joint-signature.constants';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { Filter, SelectedFilterOption } from '@presentation/components/filters/filters.component';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';
import { getElementAtPosition } from '@core/utils/array.utils';

@Component({
  selector: 'app-joint-signature-documents-filters',
  templateUrl: './joint-signature-documents-filters.component.html',
  styleUrls: ['./joint-signature-documents-filters.component.scss']
})
export class JointSignatureDocumentsFiltersComponent {
  @Output() selectedFilters = new EventEmitter<ElectronicJointSignatureInvolvedDocumentListFilterDto>();
  selectedFilterOptions: ElectronicJointSignatureInvolvedDocumentListFilterDto = {} as ElectronicJointSignatureInvolvedDocumentListFilterDto;
  statesFiltersOptions: Filter[] = SIGNATURE_STATUS_FILTER;
	initialDateRange: DateRange;

  constructor(private joinSignatureService: JointSignatureService) {
    this.joinSignatureService.getElectronicJointSignatureDocumentPossibleStatuses().subscribe(documentsState => {
      let filterAux = SIGNATURE_STATUS_FILTER;
      getElementAtPosition(filterAux,0).options = documentsState;
      getElementAtPosition(filterAux,0).defaultValue = documentsState.filter(s => s.id === EElectronicSignatureStatus.PENDING).map(s => s.id);;
      this.statesFiltersOptions = deepClone(filterAux);
      this.selectedFilterOptions.electronicSignaturesStatusIds = [EElectronicSignatureStatus.PENDING];
      this.emitFilters(this.selectedFilterOptions);
    })
  }

  setStatesFilter(event: SelectedFilterOption[]): void {
		this.selectedFilterOptions.electronicSignaturesStatusIds = event.length ? event.find(filter => filter.key === SIGNATURE_STATUS_KEY).value : null;
	}

  setDatesFilter(event: DateRange) {
    this.selectedFilterOptions.startDate = event? dateToDateDto(event.start): null;
    this.selectedFilterOptions.endDate = event? dateToDateDto(event.end): null;
  }
  
  emitFilters(selectedFilterOptions: ElectronicJointSignatureInvolvedDocumentListFilterDto){
    selectedFilterOptions.patientFirstName = this.selectedFilterOptions.patientFirstName? this.selectedFilterOptions.patientFirstName: null;
    selectedFilterOptions.patientLastName = this.selectedFilterOptions.patientLastName? this.selectedFilterOptions.patientLastName: null;
    this.selectedFilters.emit(deepClone(selectedFilterOptions));
  }

}
