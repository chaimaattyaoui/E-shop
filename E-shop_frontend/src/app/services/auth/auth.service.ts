import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { map } from 'rxjs';
import { UserStorageService } from '../storage/user-storage.service';


const BASIC_URL = "http://localhost:8089/products";
 

@Injectable({
  providedIn: 'root'
})
export class AuthService {

 

  constructor( private http: HttpClient,
    private userStorageService: UserStorageService) { }

  register(sigunupRequest:any): Observable<any>{
    return this.http.post(`${BASIC_URL}/sign-up`,sigunupRequest)

  }
  login(username: string, password: string): any {
  const headers = new HttpHeaders().set('Content-Type', 'application/json');
  const body = { username, password };
  return this.http.post(`${BASIC_URL}/authenticate`, body, { headers, observe: 'response' }).pipe(
    map((res) => {
      console.log('Response:', res); // Debug the full response
      const token = res.headers.get('authorization')?.substring(7);
      const user = res.body;
      console.log('Token:', token, 'User:', user); // Debug token and user
      if (token && user) {
        this.userStorageService.saveToken(token);
        this.userStorageService.saveUser(user); // Fix: Pass user object, not string 'user'
        return { success: true }; // Return success object
      }
      return { success: false };
    })
  );
}


getOrderByTrackingId(trackingId: number): Observable<any>{
  return this.http.get(`${BASIC_URL}/order/${trackingId}`)
}
}


