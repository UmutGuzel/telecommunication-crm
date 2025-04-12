package com.gygy.customerservice.infrastructure.persistence.config;

import org.bson.types.Binary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        List<Object> converters = new ArrayList<>();
        converters.add(UuidToBinaryConverter.INSTANCE);
        converters.add(BinaryToUuidConverter.INSTANCE);
        return new MongoCustomConversions(converters);
    }

    private enum UuidToBinaryConverter implements Converter<UUID, Binary> {
        INSTANCE;

        @Override
        public Binary convert(UUID source) {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(source.getMostSignificantBits());
            bb.putLong(source.getLeastSignificantBits());
            return new Binary((byte) 0x04, bb.array()); // subtype 4
        }
    }

    private enum BinaryToUuidConverter implements Converter<Binary, UUID> {
        INSTANCE;

        @Override
        public UUID convert(Binary source) {
            ByteBuffer bb = ByteBuffer.wrap(source.getData());
            return new UUID(bb.getLong(), bb.getLong());
        }
    }
}
