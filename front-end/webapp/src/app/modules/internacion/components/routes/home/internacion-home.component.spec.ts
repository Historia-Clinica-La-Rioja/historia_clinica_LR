import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InternacionHomeComponent } from './internacion-home.component';

describe('InternacionHomeComponent', () => {
  let component: InternacionHomeComponent;
  let fixture: ComponentFixture<InternacionHomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InternacionHomeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InternacionHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
