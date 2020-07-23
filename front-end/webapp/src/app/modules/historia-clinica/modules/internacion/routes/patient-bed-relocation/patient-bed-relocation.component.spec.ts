import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientBedRelocationComponent } from './patient-bed-relocation.component';

describe('PatientBedRelocationComponent', () => {
  let component: PatientBedRelocationComponent;
  let fixture: ComponentFixture<PatientBedRelocationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PatientBedRelocationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PatientBedRelocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
