package io.streamnative.data.feeds.realtime.ably;

import org.apache.pulsar.common.functions.FunctionConfig;
import org.apache.pulsar.common.io.SourceConfig;
import org.apache.pulsar.functions.LocalRunner;

import java.util.HashMap;
import java.util.Map;

public class AblyFeedLocalRunner {

    private static final Map<String, Object> CONFIGS = new HashMap<>();
    private static final Map<String, Object> SECRETS = new HashMap<>();

    static {
        CONFIGS.put("CHANNEL_NAME", "[product:ably-coindesk/crypto-pricing]xrp:usd");
        CONFIGS.put("MESSAGE_KEY", "xrp:usd");
        SECRETS.put("API_KEY", "<YOUR API KEY>");
    }

    public static void main(String[] args) throws Exception {

        SourceConfig sourceConfig =
                SourceConfig.builder()
                        .className(AblySource.class.getName())
                        .name("ably-feed")
                        .topicName("persistent://public/default/ably")
                        .processingGuarantees(FunctionConfig.ProcessingGuarantees.ATMOST_ONCE)
                        .schemaType("string")
                        .configs(CONFIGS)
                        .secrets(SECRETS)
                        .build();

        LocalRunner localRunner =
                LocalRunner.builder()
                        .brokerServiceUrl("pulsar://192.168.1.120:6650")
                        .sourceConfig(sourceConfig)
                        .build();

        localRunner.start(false);
        Thread.sleep(120 * 1000);
        localRunner.stop();
    }
}
