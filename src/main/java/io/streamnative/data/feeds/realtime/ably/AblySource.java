package io.streamnative.data.feeds.realtime.ably;

import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.types.AblyException;
import io.ably.lib.types.Message;
import org.apache.pulsar.io.core.PushSource;
import org.apache.pulsar.io.core.SourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AblySource extends PushSource<String> {

    public static final String API_KEY_PROPERTY = "apiKey";
    public static final String CHANNEL_NAME_PROPERTY = "channelName";

    public static final String MESSAGE_KEY_PROPERTY = "messageKey";

    private static final Logger LOG = LoggerFactory.getLogger(AblySource.class);

    private AblyRealtime ablyRealtime;

    private Channel channel;

    private String msgKey;

    @Override
    public void open(Map<String, Object> config, SourceContext srcCtx) throws Exception {
        ablyRealtime = new AblyRealtime(srcCtx.getSourceConfig()
                .getSecrets().get(API_KEY_PROPERTY).toString());

        ablyRealtime.connect();

        channel = ablyRealtime.channels.get(srcCtx.getSourceConfig()
                .getConfigs().get(CHANNEL_NAME_PROPERTY).toString());

        msgKey = srcCtx.getSourceConfig().getConfigs().get(MESSAGE_KEY_PROPERTY).toString();
        readMessages(this);
    }

    @Override
    public void close() throws Exception {
        ablyRealtime.close();
    }

    private void readMessages(PushSource src) throws AblyException {
        channel.subscribe(new Channel.MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message != null) {
                    LOG.info(String.format("Message [name: %s, data: %s]", message.name, message.data));
                    src.consume(new AblyRecord(message, msgKey));
                }
            }
        });
    }
}
