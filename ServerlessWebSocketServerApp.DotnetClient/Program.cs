using System;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.SignalR.Client;
using Newtonsoft.Json;

namespace ServerlessWebSocketServerApp.DotnetClient
{
    class Program
    {
        static async Task Main(string[] args)
        {
            var client = new HttpClient();
            var connection = new HubConnectionBuilder()
                .WithUrl("https://okazukisignalr.azurewebsites.net/api")
                .Build();
            connection.On("receiveMessage", new[]{ typeof(ChatMessage) }, (arguments, state) =>
            {
                var chatMessage = (ChatMessage)arguments[0];
                Console.WriteLine($"Received a message: {chatMessage.Text}");
                return Task.CompletedTask;
            }, null);
            await connection.StartAsync();
            while (true)
            {
                var text = Console.ReadLine();
                await client.PostAsync("https://okazukisignalr.azurewebsites.net/api/PostMessage", 
                    new StringContent(JsonConvert.SerializeObject(new ChatMessage { Text = text }), Encoding.UTF8, "application/json"));
            }
        }
    }

    class ChatMessage
    {
        [JsonProperty("text")]
        public string Text { get; set; }
    }
}
