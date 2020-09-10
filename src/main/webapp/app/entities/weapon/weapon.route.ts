import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IWeapon, Weapon } from 'app/shared/model/weapon.model';
import { WeaponService } from './weapon.service';
import { WeaponComponent } from './weapon.component';
import { WeaponDetailComponent } from './weapon-detail.component';
import { WeaponUpdateComponent } from './weapon-update.component';

@Injectable({ providedIn: 'root' })
export class WeaponResolve implements Resolve<IWeapon> {
  constructor(private service: WeaponService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWeapon> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((weapon: HttpResponse<Weapon>) => {
          if (weapon.body) {
            return of(weapon.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Weapon());
  }
}

export const weaponRoute: Routes = [
  {
    path: '',
    component: WeaponComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Weapons'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: WeaponDetailComponent,
    resolve: {
      weapon: WeaponResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Weapons'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: WeaponUpdateComponent,
    resolve: {
      weapon: WeaponResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Weapons'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: WeaponUpdateComponent,
    resolve: {
      weapon: WeaponResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Weapons'
    },
    canActivate: [UserRouteAccessService]
  }
];
