export interface SignalRMessage {
    userId?: string
    groupName?: string
    target: string
    arguments: {[key:string]: any}
}
