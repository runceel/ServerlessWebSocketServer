import { AzureFunction, Context, HttpRequest } from "@azure/functions"
import { SignalRMessage } from "../signalr/signalrmessage"

const httpTrigger: AzureFunction = async function (context: Context, 
    req: HttpRequest, 
    signalRMessages: SignalRMessage[]): Promise<void> {
    signalRMessages.push({
        target: "receiveMessage",
        arguments: [
            req.body,
        ],
    });
};

export default httpTrigger;
