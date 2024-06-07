import { Component, EventEmitter, Output } from '@angular/core';
import { EElectronicSignatureStatus, ElectronicJointSignatureInvolvedDocumentListFilterDto } from '@api-rest/api-model';
import { JointSignatureService } from '@api-rest/services/joint-signature.service';
import { deepClone } from '@core/utils/core.utils';
import { SIGNATURE_STATUS_FILTER, SIGNATURE_STATUS_KEY } from '../../constants/joint-signature.constants';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { Filter, SelectedFilterOption } from '@presentation/components/filters/filters.component';
import { DateRange } from '@presentation/components/date-range-picker/date-range-picker.component';

@Component({
  selector: 'app-joint-signature-documents-filters',
  templateUrl: './joint-signature-documents-filters.component.html',
  styleUrls: ['./joint-signature-documents-filters.component.scss']
})
export class JointSignatureDocumentsFiltersComponent {
  @Output() selectedFilters = new EventEmitter<ElectronicJointSignatureInvolvedDocumentListFilterDto>();
  selectedFilterOptions: ElectronicJointSignatureInvolvedDocumentListFilterDto = {};
  filter: Filter[] = SIGNATURE_STATUS_FILTER;
	initialDateRange: DateRange;

  constructor(private joinSignatureService: JointSignatureService) {
    this.joinSignatureService.getElectronicJointSignatureDocumentPossibleStatusesController().subscribe(documentsState => {
      let filterAux = SIGNATURE_STATUS_FILTER;
      filterAux[0].options = documentsState;
      filterAux[0].defaultValue = documentsState.filter(s => s.id === EElectronicSignatureStatus.PENDING).map(s => s.id);;
      this.filter = deepClone(filterAux);
      this.selectedFilterOptions.electronicSignaturesStatusIds = [EElectronicSignatureStatus.PENDING];
      this.emitFilters();
    })
  }

  handleFilterChange(event: SelectedFilterOption[]): void {
		this.selectedFilterOptions.electronicSignaturesStatusIds = event.length ? event.find(filter => filter.key === SIGNATURE_STATUS_KEY).value : null;
	}

  setDatesFilter(event) {
    this.selectedFilterOptions.startDate = event? dateToDateDto(event.start): null;
    this.selectedFilterOptions.endDate = event? dateToDateDto(event.end): null;
  }
  
  emitFilters(){
    this.selectedFilterOptions.patientFirstName = this.selectedFilterOptions.patientFirstName? this.selectedFilterOptions.patientFirstName: null;
    this.selectedFilterOptions.patientLastName = this.selectedFilterOptions.patientLastName? this.selectedFilterOptions.patientLastName: null;
    this.selectedFilters.emit(deepClone(this.selectedFilterOptions));
  }

}
