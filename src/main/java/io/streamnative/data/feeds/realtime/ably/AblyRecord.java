package io.streamnative.data.feeds.realtime.ably;

import io.ably.lib.types.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.pulsar.functions.api.Record;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AblyRecord implements Record<String> {

    private final String key;
    private final String value;
    private final Map<String, String> properties;

    public AblyRecord(Message msg, String key) {
        this.key = key;
        this.value = msg.data.toString();
        this.properties = new HashMap<>();
        this.properties.put("timestamp", Long.toString(msg.timestamp));

        if (StringUtils.isNotBlank(msg.id)) {
            this.properties.put("id", msg.id);
        }

        if (StringUtils.isNotBlank(msg.name)) {
            this.properties.put("name", msg.name);
        }

        if (StringUtils.isNotBlank(msg.encoding)) {
            this.properties.put("encoding", msg.encoding);
        }
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public Optional<String> getKey() {
        return Optional.ofNullable(key);
    }
}
