package br.com.empresa.metas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Ponto de entrada da aplicação. Igual ao "if __name__ == '__main__'" do
 * Python: é essa classe que sobe o servidor quando você roda
 * "./gradlew bootRun" (ou o .jar gerado pelo build).
 *
 * @EnableCaching liga o suporte a cache do Spring (usado com Redis, ver
 * shared/infra/config/RedisConfig).
 */
@SpringBootApplication
@EnableCaching
public class MetasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetasApplication.class, args);
    }
}
