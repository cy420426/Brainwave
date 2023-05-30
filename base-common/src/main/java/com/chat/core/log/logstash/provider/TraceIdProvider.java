package com.chat.core.log.logstash.provider;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import com.chat.core.log.constant.TraceLogConstant;
import net.logstash.logback.composite.AbstractJsonProvider;

import java.io.IOException;

/**
 * @classDesc: 功能描述:(logstash encoder)
 * @author: cy
 * @date: 2022/11/21 15:30
 */
public class TraceIdProvider extends AbstractJsonProvider<ILoggingEvent> {
    @Override
    public void writeTo(JsonGenerator generator, ILoggingEvent iLoggingEvent) throws IOException {
        String traceId = TraceLogConstant.getTraceId();
        if (traceId == null) {
            return;
        }

        writeStringField(generator, "traceId", traceId);
    }

    public void writeStringField(JsonGenerator generator, String fieldName, String fieldValue) throws IOException {
        if (fieldValue != null){
            generator.writeStringField(fieldName, fieldValue);
        }else {
            generator.writeNullField(fieldName);
        }
    }
}
