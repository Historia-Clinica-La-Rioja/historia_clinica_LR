import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntryCallComponent } from './entry-call.component';

describe('EntryCallComponent', () => {
  let component: EntryCallComponent;
  let fixture: ComponentFixture<EntryCallComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EntryCallComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EntryCallComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
