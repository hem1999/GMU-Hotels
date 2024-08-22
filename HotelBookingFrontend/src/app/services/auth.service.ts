import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { inject, Injectable, OnInit } from '@angular/core';
import { RegistrationRequest, UserType } from '../registration/registration.model';
import { loginRequest, loginResponse } from '../login/loginRequest.model';
import { BehaviorSubject, catchError, map } from 'rxjs';
import { NotExpr } from '@angular/compiler';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private httpClient:HttpClient;
  private url = 'http://localhost:8081'
  private isLoggedIn:BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  isLoggedIn$ = this.isLoggedIn.asObservable();
  isCurrentTokenValid = true; //For now let it be always true! later change it accordingly!
  private router = inject(Router);

  constructor(httpClient: HttpClient) {
    this.httpClient = httpClient;
    this.checkLoginStatus();
   }

   

   public saveAuth(jwtToken:string, userId:string, role:UserType){
    sessionStorage.setItem('gmu_hotels_jwt_token',jwtToken);
    sessionStorage.setItem('gmu_hotels_user_id',userId);
    sessionStorage.setItem('gmu_hotels_user_role',role);
   }

   

   getUserId(){
    return sessionStorage.getItem('gmu_hotels_user_id');
   }


   getJwtToken<String>(){
    return sessionStorage.getItem('gmu_hotels_jwt_token');
   }

   checkLoginStatus(): boolean {
    //TODO: Later! this.checkTokenValidity();
    const isAuthenticated = sessionStorage.getItem('gmu_hotels_jwt_token') != null && this.isCurrentTokenValid;
    this.isLoggedIn.next(isAuthenticated);
    return isAuthenticated;
  }

  checkTokenValidity(){
    // TODO: After long time of not using it should check for token validation and only it should respond!
    if(sessionStorage.getItem('gmu_hotels_jwt_token') != null){
      let jwtToken = sessionStorage.getItem('gmu_hotels_jwt_token');
      this.isCurrentTokenValid = false;
      this.httpClient.post(`${this.url}/auth/validate`,{jwtToken},{observe: 'response'}).pipe(
        map(
          res => {
            console.log(res.status);
            if(res.status==200){
              this.isCurrentTokenValid = true;
            }
            if(res.status == 403){
              this.activateLogout();
              this.router.navigateByUrl("login");
            }
          }
        )
      ).subscribe();
    }
  }

   activateSignIn(){
    this.isLoggedIn.next(true);
   }

   activateLogout(){
    sessionStorage.removeItem('gmu_hotels_jwt_token');
    sessionStorage.removeItem('gmu_hotels_user_id');
    sessionStorage.removeItem('gmu_hotels_user_role');
    this.isLoggedIn.next(false);
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
