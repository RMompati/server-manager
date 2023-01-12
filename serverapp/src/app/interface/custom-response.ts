import { Server } from "./server";

export interface CustomResponse {
    timeStamp: Date;
    statusCode: number;
    status: string;
    reason: string;
    message: string;
    developerMessage: string;
    data: { servers?: Server[], server?: Server };
}

export let emptyResponse: CustomResponse = {
    timeStamp: new Date(),
    statusCode: -1,
    status: '',
    reason: '',
    message: '',
    developerMessage: '',
    data: {}
};