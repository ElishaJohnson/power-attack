import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';

import { ICharacter } from 'app/shared/model/character.model';
import { CharacterService } from 'app/entities/character/character.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  eventSubscriber?: Subscription;
  characters?: ICharacter[];
  selectedCharacter: ICharacter | null = null;

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    protected characterService: CharacterService,
    protected eventManager: JhiEventManager
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.loadAll();
    this.registerChangeInCharacters();
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    // clear character variables before next user logs in
    if (this.characters && this.characters.length > 0) {
      this.characters = [];
      this.selectedCharacter = null;
    }

    this.loginModalService.open();
  }

  loadAll(): void {
    this.characterService.query().subscribe((res: HttpResponse<ICharacter[]>) => (this.characters = res.body || []));
  }

  registerChangeInCharacters(): void {
    this.eventSubscriber = this.eventManager.subscribe('characterListModification', () => this.loadAll());
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }
}
