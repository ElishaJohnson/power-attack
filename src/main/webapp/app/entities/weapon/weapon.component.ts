import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWeapon } from 'app/shared/model/weapon.model';
import { WeaponService } from './weapon.service';
import { WeaponDeleteDialogComponent } from './weapon-delete-dialog.component';

@Component({
  selector: 'jhi-weapon',
  templateUrl: './weapon.component.html'
})
export class WeaponComponent implements OnInit, OnDestroy {
  weapons?: IWeapon[];
  eventSubscriber?: Subscription;

  constructor(protected weaponService: WeaponService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.weaponService.query().subscribe((res: HttpResponse<IWeapon[]>) => (this.weapons = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInWeapons();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IWeapon): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInWeapons(): void {
    this.eventSubscriber = this.eventManager.subscribe('weaponListModification', () => this.loadAll());
  }

  delete(weapon: IWeapon): void {
    const modalRef = this.modalService.open(WeaponDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.weapon = weapon;
  }
}
