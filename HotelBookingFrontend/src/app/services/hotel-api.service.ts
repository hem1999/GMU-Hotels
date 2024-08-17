import { HttpClient,HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Route, Router } from '@angular/router';
import { Room } from '../rooms/room.model';

@Injectable({
  providedIn: 'root'
})
export class HotelApiService {

  httpClient:HttpClient = inject(HttpClient);
  route: Router = inject(Router);
  url:String = 'http://localhost:8081';
  authService = inject(AuthService);

  getAllRooms(){
    //No need of authorization header, since we have permitted all to rooms!
    return this.httpClient.get<Room[]>(`${this.url}/rooms`);
  }

  getRoom(roomId:String | null){
    if(roomId == null){
      return null;
    }
    //No need of authorization header, since we have permitted all to rooms!
    return this.httpClient.get<Room>(`${this.url}/rooms/${roomId}`);
  }

  getUserDetails(userId: String | null){
    //Comment this out while developing or changing UI
    if(this.authService.getJwtToken() == null){
      this.route.navigateByUrl('login');
    }
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getJwtToken()}`
    })
    return this.httpClient.get(`${this.url}/user/${userId}`,{
        headers
    })
  }

  userUpdate(requestBody:any){
    if(this.authService.getJwtToken() == null){
      this.route.navigateByUrl('login');
    }
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getJwtToken()}`
    })
    return this.httpClient.put(
      `${this.url}/user/updateUser`,requestBody,{
        headers
      }
    )
  }

  postAddRoom(requestBody:FormData){
    if(this.authService.getJwtToken() == null){
      this.route.navigateByUrl('login');
    }
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.authService.getJwtToken()}`
    })
    return this.httpClient.post(
      `${this.url}/rooms/add`,requestBody,{
        headers
      }
    );
  }

  getIsRoomAvailable(roomId:String | null, startDate: String, endDate: String){
    
    return this.httpClient.get<boolean>(
      `${this.url}/available?roomId=${roomId}&startDate=${startDate}&endDate=${endDate}`
    );
  }

 
}
