<template>
  <div>
    <div>
      <input type="text" v-model="message" />
      <button @click="onPostMessageClick">PostMessage</button>
    </div>
    <div>
      <div :key="index" v-for="(chatMessage, index) in chatMessages">
        {{ chatMessage }}
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import { HubConnection, HubConnectionBuilder, LogLevel, JsonHubProtocol } from '@aspnet/signalr';

@Component
export default class HelloWorld extends Vue {
  public message: string = '';
  public chatMessages: string[] = [];

  private connection!: HubConnection;

  public async onPostMessageClick(): Promise<void> {
    if (this.connection == null) {
      return;
    }

    await fetch('https://okazukisignalr.azurewebsites.net/api/PostMessage', {
      method: 'POST',
      body: JSON.stringify({ text: this.message }),
    });

    this.message = '';
  }

  public async created(): Promise<void> {
    console.log('created called');
    this.connection = new HubConnectionBuilder()
      .withUrl('https://okazukisignalr.azurewebsites.net/api')
      .configureLogging(LogLevel.Information)
      .build();
    this.connection.on('receiveMessage', (message) => {
      console.log(JSON.stringify(message));
      this.chatMessages.push(message.text);
    });
    this.connection.onclose = this.connect;
    await this.connect();
    console.log('created finished: ' + this.connection);
  }

  private async connect(): Promise<void> {
    while(true) {
      try {
        await this.connection.start();
        break;
      } catch (e) {
        await new Promise(resolve => setTimeout(resolve, 1000));
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
