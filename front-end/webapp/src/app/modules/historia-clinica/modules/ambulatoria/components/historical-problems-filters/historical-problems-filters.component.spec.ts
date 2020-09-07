import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricalProblemsFiltersComponent } from './historical-problems-filters.component';

describe('HistoricalProblemsFiltersComponent', () => {
  let component: HistoricalProblemsFiltersComponent;
  let fixture: ComponentFixture<HistoricalProblemsFiltersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistoricalProblemsFiltersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoricalProblemsFiltersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
