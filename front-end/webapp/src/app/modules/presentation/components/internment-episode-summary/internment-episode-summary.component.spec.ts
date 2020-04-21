import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InternmentEpisodeSummaryComponent } from './internment-episode-summary.component';

describe('InternmentEpisodeSummaryComponent', () => {
  let component: InternmentEpisodeSummaryComponent;
  let fixture: ComponentFixture<InternmentEpisodeSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InternmentEpisodeSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InternmentEpisodeSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
