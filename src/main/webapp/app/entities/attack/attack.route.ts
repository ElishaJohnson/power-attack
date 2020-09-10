import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAttack, Attack } from 'app/shared/model/attack.model';
import { AttackService } from './attack.service';
import { AttackComponent } from './attack.component';
import { AttackDetailComponent } from './attack-detail.component';
import { AttackUpdateComponent } from './attack-update.component';

@Injectable({ providedIn: 'root' })
export class AttackResolve implements Resolve<IAttack> {
  constructor(private service: AttackService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttack> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((attack: HttpResponse<Attack>) => {
          if (attack.body) {
            return of(attack.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Attack());
  }
}

export const attackRoute: Routes = [
  {
    path: '',
    component: AttackComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Attacks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AttackDetailComponent,
    resolve: {
      attack: AttackResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Attacks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AttackUpdateComponent,
    resolve: {
      attack: AttackResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Attacks'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AttackUpdateComponent,
    resolve: {
      attack: AttackResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Attacks'
    },
    canActivate: [UserRouteAccessService]
  }
];
