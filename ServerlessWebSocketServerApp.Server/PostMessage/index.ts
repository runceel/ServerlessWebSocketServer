import { AzureFunction, Context, HttpRequest } from "@azure/functions"
import { SignalRMessage } from "../signalr/signalrmessage"

const httpTrigger: AzureFunction = async function (context: Context, 
    req: HttpRequest): Promise<void> {
    context.log(JSON.stringify(req.body));
    context.bindings.signalRMessages = new Array<SignalRMessage>({
        target: "receiveMessage",
        arguments: [
            req.body,
        ],
    });
};

export default httpTrigger;
