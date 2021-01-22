import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalSummaryCardComponent } from './external-summary-card.component';

describe('ExternalSummaryCardComponent', () => {
  let component: ExternalSummaryCardComponent;
  let fixture: ComponentFixture<ExternalSummaryCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExternalSummaryCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExternalSummaryCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
