package club.libridge.libridgebackend.app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import club.libridge.libridgebackend.core.rulesets.abstractrulesets.Ruleset;
import club.libridge.libridgebackend.networking.jackson.CallSerializer;
import club.libridge.libridgebackend.networking.jackson.RulesetSerializer;
import scalabridge.Call;

@Configuration
public class ServerObjectMapperConfiguration {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapperOnlyFields = new ObjectMapper();

        // Configure to use fields in all classes
        objectMapperOnlyFields.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        objectMapperOnlyFields.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        // Add custom Serializers
        SimpleModule module = new SimpleModule();
        module.addSerializer(Ruleset.class, new RulesetSerializer());
        module.addSerializer(Call.class, new CallSerializer());

        objectMapperOnlyFields.registerModule(module);

        return objectMapperOnlyFields;
    }
}
