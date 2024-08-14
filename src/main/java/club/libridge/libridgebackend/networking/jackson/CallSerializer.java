package club.libridge.libridgebackend.networking.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import scalabridge.Call;

public class CallSerializer extends StdSerializer<Call> {

    public CallSerializer() {
        this(null);
    }

    public CallSerializer(Class<Call> t) {
        super(t);
    }

    @Override
    public void serialize(Call call, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeStringField("call", call.toString());

        jgen.writeEndObject();
    }
}
