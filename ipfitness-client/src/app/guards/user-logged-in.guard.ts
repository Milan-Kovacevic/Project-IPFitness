import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { UserContextService } from '../shared/context/user-context.service';

export const userLoggedInGuard: CanActivateFn = (route, state) => {
  let userContext = inject(UserContextService);
  return userContext.isLoggedIn();
};
