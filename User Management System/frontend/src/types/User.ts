export interface User {
  id: number;
  firstName: string;
  lastName: string;
  ssn: string;
  email: string;
  age: number;
  role: string;
  phone?: string;
  username?: string;
  birthDate?: string;
  gender?: string;
}

export interface ApiResponse<T> {
  data: T;
  message?: string;
  success?: boolean;
}