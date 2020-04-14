import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PartialMatchesTableComponent } from './partial-matches-table.component';

describe('PartialMatchesTableComponent', () => {
  let component: PartialMatchesTableComponent;
  let fixture: ComponentFixture<PartialMatchesTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PartialMatchesTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PartialMatchesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
