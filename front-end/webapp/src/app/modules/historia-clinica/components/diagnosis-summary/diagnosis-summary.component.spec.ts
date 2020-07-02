import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiagnosisSummaryComponent } from './diagnosis-summary.component';

describe('DiagnosisSummaryComponent', () => {
  let component: DiagnosisSummaryComponent;
  let fixture: ComponentFixture<DiagnosisSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiagnosisSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiagnosisSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
