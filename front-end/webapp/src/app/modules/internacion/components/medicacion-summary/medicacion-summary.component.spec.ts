import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicacionSummaryComponent } from './medicacion-summary.component';

describe('MedicacionSummaryComponent', () => {
  let component: MedicacionSummaryComponent;
  let fixture: ComponentFixture<MedicacionSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MedicacionSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicacionSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
