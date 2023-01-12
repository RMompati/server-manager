import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { BehaviorSubject, catchError, map, Observable, of, startWith } from 'rxjs';
import { DataState } from './enum/data-state.enum';
import { Status } from './enum/status.enum';
import { AppState } from './interface/app.state';
import { emptyResponse, CustomResponse } from './interface/custom-response';
import { Server } from './interface/server';
import { NotificationService } from './service/notification.service';
import { ServerService } from './service/server.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  appState$: Observable<AppState<CustomResponse>> | undefined;
  readonly DataState = DataState;
  readonly Status = Status;
  private filterSubject = new BehaviorSubject<string>('');
  private dataSubject = new BehaviorSubject<CustomResponse>(emptyResponse);
  filterStatus$ = this.filterSubject.asObservable();


  private isLoading = new BehaviorSubject<boolean>(false);
  isLoading$ = this.isLoading.asObservable();

  constructor(private serverService: ServerService, private notifier: NotificationService) {  }

  ngOnInit(): void {
    this.appState$ = this.serverService.servers$
      .pipe(
        map(response => {
          this.notifier.onDefault(response.message);
          this.dataSubject.next(response);
          return { dataState: DataState.LOADED_STATE, appData: response }
        }),
        startWith({ dataState: DataState.LOADING_STATE }),
        catchError((error: string) => {
          this.notifier.onError(error);
          return of({ dataState: DataState.ERROR_STATE, error })
        })
      );
  }

  pingServer(ipAddress: string): void {
    this.filterSubject.next(ipAddress);
    this.appState$ = this.serverService.ping$(ipAddress)
      .pipe(
        map(response => {
          this.notifier.onDefault(response.message);
          if (this.dataSubject.value.data.servers != undefined && response.data.server  != undefined) {
            const index = this.dataSubject.value.data.servers.findIndex(server => server.id === response.data.server?.id);
            this.dataSubject.value.data.servers[index] = response.data.server;
          }
          this.filterSubject.next('');

          return { dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((error: string) => {
          this.notifier.onError(error);
          this.filterSubject.next('');
          return of({ dataState: DataState.ERROR_STATE, error })
        })
      );
  }

  filterServers(status: Status): void {
    this.appState$ = this.serverService.filter$(status, this.dataSubject.value)
      .pipe(
        map(response => {
          this.notifier.onDefault(response.message);
          return { dataState: DataState.LOADED_STATE, appData: response }
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((error: string) => {
          this.notifier.onError(error);
          return of({ dataState: DataState.ERROR_STATE, error })
        })
      );
  }

  saveServer(serverForm: NgForm): void {
    this.isLoading.next(true);
    this.appState$ = this.serverService.save$(serverForm.value as Server)
      .pipe(
        map(response => {
          this.notifier.onDefault(response.message);
          this.dataSubject.next(
            {...response, data: {servers: [response.data.server!, ...this.dataSubject.value.data.servers!]}}
          )
          document.getElementById("closeModal")?.click(); // Close the module.
          this.isLoading.next(false);

          serverForm.resetForm({ status: this.Status.SERVER_DOWN });
         
          return { dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((error: string) => {
          this.notifier.onError(error);
          this.isLoading.next(false);
          return of({ dataState: DataState.ERROR_STATE, error })
        })
      );
  }

  deleteServer(server: Server): void {
    this.appState$ = this.serverService.delete$(server.id)
      .pipe(
        map(response => {
          this.notifier.onDefault(response.message);
          this.dataSubject.next(
            { ...response, data:
              {
                servers: this.dataSubject.value.data.servers?.filter(srv => srv.id !== server.id)
              }}
          )
          return { dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }
        }),
        startWith({ dataState: DataState.LOADED_STATE, appData: this.dataSubject.value }),
        catchError((error: string) => {
          this.notifier.onError(error);
          return of({ dataState: DataState.ERROR_STATE, error })
        })
      );
  }

  statusToString(status: Status): string {
    return status === Status.SERVER_UP ? "SERVER UP" : "SERVER DOWN";
  }

  printReport(): void {

    let dataType = 'application/vnd.ms.excel.sheet.macroEnabled.12';
    let tableSelect = document.getElementById('servers');
    let tableHtml = tableSelect?.innerHTML.replace(/ /g, '%20');
    let downloadLink = document.createElement('a');
    document.body.appendChild(downloadLink);
    downloadLink.href = `data:${dataType}, ${tableHtml}`;
    downloadLink.download = 'servers-report.xls';
    downloadLink.click();
    document.body.removeChild(downloadLink);
    
    this.notifier.onDefault("Report Downloaded...");
  }
}
