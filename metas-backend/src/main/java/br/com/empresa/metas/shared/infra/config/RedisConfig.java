package br.com.empresa.metas.shared.infra.config;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * Configura o Redis como backend de cache do Spring (@Cacheable, usado
 * por exemplo pra guardar as permissões de um usuário por alguns
 * minutos, evitando bater no banco a cada requisição).
 *
 * TTL padrão de 5 minutos: dado que muda pouco (permissão de um CPF, por
 * exemplo) fica em cache por um tempo curto — rápido o bastante pra não
 * incomodar se alguém alterar uma permissão e o efeito demorar um
 * pouquinho pra refletir, mas ainda assim aliviando o banco.
 */
@Configuration
public class RedisConfig implements CachingConfigurer {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}
