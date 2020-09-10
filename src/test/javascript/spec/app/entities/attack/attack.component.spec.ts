import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PowerattackTestModule } from '../../../test.module';
import { AttackComponent } from 'app/entities/attack/attack.component';
import { AttackService } from 'app/entities/attack/attack.service';
import { Attack } from 'app/shared/model/attack.model';

describe('Component Tests', () => {
  describe('Attack Management Component', () => {
    let comp: AttackComponent;
    let fixture: ComponentFixture<AttackComponent>;
    let service: AttackService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PowerattackTestModule],
        declarations: [AttackComponent]
      })
        .overrideTemplate(AttackComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AttackComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AttackService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Attack(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.attacks && comp.attacks[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
