import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VacunasSummaryComponent } from './vacunas-summary.component';

describe('VacunasSummaryComponent', () => {
  let component: VacunasSummaryComponent;
  let fixture: ComponentFixture<VacunasSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VacunasSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VacunasSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
