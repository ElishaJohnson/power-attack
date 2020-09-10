import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICharacter } from 'app/shared/model/character.model';
import { CharacterService } from './character.service';
import { CharacterDeleteDialogComponent } from './character-delete-dialog.component';

@Component({
  selector: 'jhi-character',
  templateUrl: './character.component.html'
})
export class CharacterComponent implements OnInit, OnDestroy {
  characters?: ICharacter[];
  eventSubscriber?: Subscription;

  constructor(protected characterService: CharacterService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.characterService.query().subscribe((res: HttpResponse<ICharacter[]>) => (this.characters = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCharacters();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICharacter): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCharacters(): void {
    this.eventSubscriber = this.eventManager.subscribe('characterListModification', () => this.loadAll());
  }

  delete(character: ICharacter): void {
    const modalRef = this.modalService.open(CharacterDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.character = character;
  }
}
