import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UiComponentListComponent } from './ui-component-list.component';

describe('UiComponentListComponent', () => {
  let component: UiComponentListComponent;
  let fixture: ComponentFixture<UiComponentListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UiComponentListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UiComponentListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
