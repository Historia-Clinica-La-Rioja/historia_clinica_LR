import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientSummaryComponent } from './patient-summary.component';

describe('PatientSummaryComponent', () => {
  let component: PatientSummaryComponent;
  let fixture: ComponentFixture<PatientSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PatientSummaryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PatientSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
