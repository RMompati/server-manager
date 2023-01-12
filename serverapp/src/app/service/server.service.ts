import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from "@angular/common/http"
import { CustomResponse } from '../interface/custom-response';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { Server } from '../interface/server';
import { Status } from '../enum/status.enum';

@Injectable({ providedIn: 'root' })
export class ServerService {
  private apiUrl = "http://localhost:8080/server";

  constructor(private http: HttpClient) { }

  servers$ = <Observable<CustomResponse>>
    this.http.get<CustomResponse>(`${this.apiUrl}/list`)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  save$ = (server: Server) => <Observable<CustomResponse>>
    this.http.post<CustomResponse>(`${this.apiUrl}/save`, server)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  ping$ = (ipAddress: string) => <Observable<CustomResponse>>
    this.http.get<CustomResponse>(`${this.apiUrl}/ping/${ipAddress}`)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  filter$ = (status: Status, response: CustomResponse) => <Observable<CustomResponse>>
    new Observable<CustomResponse>(
      subscriber => {
        console.log(response);
        subscriber.next(
          status === Status.ALL ? { ...response, message: `Servers filtered by '${status}' status` } :
            {
              ...response, message: this.filteredServers(response, status).length > 0 ?
                `Servers filtered by '${status === Status.SERVER_UP ? 'SERVER UP' : 'SERVER DOWN'}' status` :
                `No servers of ${status} found.`,
              data: { servers: this.filteredServers(response, status) }
            }
        );
        subscriber.complete();
      }
    )
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  delete$ = (serverId: number) => <Observable<CustomResponse>>
    this.http.delete<CustomResponse>(`${this.apiUrl}/delete/${serverId}`)
      .pipe(
        tap(console.log),
        catchError(this.handleError)
      );

  private filteredServers(response: CustomResponse, status: Status): Server[] {
    return response.data.servers?.filter(srv => srv.status === status) ?? [];
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.log(error);
    return throwError(() => new Error(`An error occured - Error code: ${error.status}`));
  }
}
