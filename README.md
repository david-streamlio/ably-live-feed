# Ably Realtime Data Connector

A generic Pulsar source connector that can be configured to read from most, if not all of the data feeds 
available on the [Ably Hub](https://ably.com/hub).

---
Configuration


You need to specify the following properties in the Source configuration,

```bash
sourceConfig:
  - channelName: "[product:ably-coindesk/crypto-pricing]xrp:usd"
  - messageKey: "xrp:usd"
```

You need to pass the API Key as a secret with the name `apiKey`

---
References

- https://ably.com/blog/ably-open-data-streaming-program
- https://ably.com/api-streamer