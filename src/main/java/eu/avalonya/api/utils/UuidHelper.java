package eu.avalonya.api.utils;

import com.google.protobuf.ByteString;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

public class UuidHelper {
    public static byte[] toByteArray(UUID uuid) {
        Objects.requireNonNull(uuid, "uuid cannot be null");

        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID fromByteArray(byte[] bytes) {
        Objects.requireNonNull(bytes, "bytes cannot be null");

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long mostSigBits = byteBuffer.getLong();
        long leastSigBits = byteBuffer.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    public static ByteString toByteString(UUID uuid) {
        Objects.requireNonNull(uuid, "uuid cannot be null");
        return ByteString.copyFrom(toByteArray(uuid));
    }

    public static UUID fromByteString(ByteString byteString) {
        Objects.requireNonNull(byteString, "byteString cannot be null");
        return fromByteArray(byteString.toByteArray());
    }
}
