import { atom } from 'recoil';

export const authState = atom({
  key: 'authState',
  default: {
    isAuthenticated: false,
    adminInfo: null,
    token: null,
  },
});

export const loadingState = atom({
  key: 'loadingState',
  default: false,
});

export const errorState = atom({
  key: 'errorState',
  default: null,
});