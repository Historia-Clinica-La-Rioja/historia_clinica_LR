import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewEpisodePediatricTriageComponent } from './new-episode-pediatric-triage.component';

describe('NewEpisodePediatricTriageComponent', () => {
  let component: NewEpisodePediatricTriageComponent;
  let fixture: ComponentFixture<NewEpisodePediatricTriageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewEpisodePediatricTriageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewEpisodePediatricTriageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
