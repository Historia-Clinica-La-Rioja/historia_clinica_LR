import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchPatientDialogComponent } from './search-patient-dialog.component';

describe('SearchPatientDialogComponent', () => {
  let component: SearchPatientDialogComponent;
  let fixture: ComponentFixture<SearchPatientDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchPatientDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchPatientDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
