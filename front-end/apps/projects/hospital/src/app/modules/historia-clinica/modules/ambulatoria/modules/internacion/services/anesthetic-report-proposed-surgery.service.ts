import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SnomedDto, SnomedECL } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AnestheticReportProposedSurgeryService {

    private readonly ECL = SnomedECL.PROCEDURE;
    private form: FormGroup;
	snomedConcept: SnomedDto;
    private proposedSurgeries: ProposedSurgery[] = [];

    private dataEmitter = new BehaviorSubject<ProposedSurgery[]>(this.proposedSurgeries);
	private data$ = this.dataEmitter.asObservable();

    constructor(
        private readonly snomedService: SnomedService,
        private readonly snackBarService: SnackBarService,
    ) {
        this.form = new FormGroup<ProposedSurgeryForm>({
            snomed: new FormControl(null, Validators.required),
        });
    }

    private add(proposedSurgery: ProposedSurgery): boolean {
        const currentItems = this.proposedSurgeries.length;
        this.proposedSurgeries = pushIfNotExists<any>(this.proposedSurgeries, proposedSurgery, this.compareProposedSurgeries);
        this.dataEmitter.next(this.proposedSurgeries);
        return currentItems === this.proposedSurgeries.length;
    }

    private compareProposedSurgeries(proposedSurgery: ProposedSurgery, proposedSurgery2: ProposedSurgery): boolean {
        return proposedSurgery.snomed.sctid === proposedSurgery2.snomed.sctid;
    }

    addToList(): boolean {
        if (this.form.valid && this.snomedConcept) {
            const proposedSurgery: ProposedSurgery = {
                snomed: this.snomedConcept,
                isAdded: true,
            };
            if (this.add(proposedSurgery))
                this.snackBarService.showError("Cirugía propuesta duplicada");
            this.resetForm();
            return true;
        }
        return false;
    }

    getProposedSurgeries(): Observable<ProposedSurgery[]> {
        return this.data$;
    }
    
    remove(index: number): void {
        this.proposedSurgeries = removeFrom<ProposedSurgery>(this.proposedSurgeries, index);
        this.dataEmitter.next(this.proposedSurgeries);
    }
    
    isEmpty(): boolean {
        return (!this.proposedSurgeries || this.proposedSurgeries.length === 0);
    }
    
    getForm(): FormGroup {
        return this.form;
    }

    resetForm(): void {
        delete this.snomedConcept;
        this.form.reset();
    }

    getECL(): SnomedECL {
        return this.ECL;
    }

    openSearchDialog(searchValue: string): void {
        if (searchValue) {
            const search: SnomedSemanticSearch = {
                searchValue,
                eclFilter: this.ECL
            };
            this.snomedService.openConceptsSearchDialog(search)
                .subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
        }
    }

    setConcept(selectedConcept: SnomedDto): void {
        this.snomedConcept = selectedConcept;
        const pt = selectedConcept ? selectedConcept.pt : '';
        this.form.controls.snomed.setValue(pt);
    }
}

export interface ProposedSurgery {
    snomed: SnomedDto,
    isAdded: boolean,
}

export interface ProposedSurgeryForm {
    snomed: FormControl<SnomedDto>;
}