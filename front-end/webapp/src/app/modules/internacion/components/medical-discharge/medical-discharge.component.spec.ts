import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicalDischargeComponent } from './medical-discharge.component';

describe('MedicalDischargeComponent', () => {
  let component: MedicalDischargeComponent;
  let fixture: ComponentFixture<MedicalDischargeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MedicalDischargeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicalDischargeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
