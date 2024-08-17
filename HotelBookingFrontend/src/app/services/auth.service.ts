import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationRequest, UserType } from '../registration/registration.model';
import { loginRequest, loginResponse } from '../login/loginRequest.model';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private httpClient:HttpClient;
  private url = 'http://localhost:8081'
  private role:UserType=UserType.USER;
  private userId: string | null = null;
  private isLoggedInSubject = new BehaviorSubject<boolean>(false);
  isLoggedIn$ = this.isLoggedInSubject.asObservable();
  private jwtToken: String | null = null;
  constructor(httpClient: HttpClient) {
    this.httpClient = httpClient;
   }

   setRole(role:UserType){
      this.role = role;
   }

   setUserId(userId:string | null){
    this.userId = userId;
   }

   getUserId(){
    return this.userId;
   }

   setJwtToken(token: String | null){
    this.jwtToken = token;
   }

   getJwtToken<String>(){
    return this.jwtToken;
   }

   activateSignIn(){
    this.isLoggedInSubject.next(true);
   }

   activateLogout(){
    this.jwtToken = null;
    this.userId = null;
    this.role = UserType.USER;
    this.isLoggedInSubject.next(false);
   }

   

   postUserRegistration(requestBody: RegistrationRequest){
    return this.httpClient.post(
      `${this.url}/auth/register`,requestBody
    );
   }

   invokeActivateAccount(token:String){
    let reqUrl = `${this.url}/auth/activate-account?activationCode=${token}`
    return this.httpClient.get(reqUrl);
   }

   postUserAuthentication(requestBody: loginRequest){
      let reqUrl = `${this.url}/auth/authenticate`;
      return this.httpClient.post<loginResponse>(reqUrl,requestBody);
   }
}
