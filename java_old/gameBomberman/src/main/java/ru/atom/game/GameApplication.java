package ru.atom.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.atom.game.databases.player.PlayerData;
import ru.atom.game.databases.player.PlayerDataRepository;
import ru.atom.game.gamesession.properties.GameSessionProperties;
import ru.atom.game.gamesession.properties.GameSessionPropertiesCreator;
import ru.atom.game.gamesession.session.GameSession;

@SpringBootApplication
public class GameApplication {
    private static final Logger log = LoggerFactory.getLogger(GameApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GameApplication.class, args);
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        int cores = Runtime.getRuntime().availableProcessors();
        pool.setCorePoolSize(cores * 2);
        pool.setMaxPoolSize(cores * 10);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        pool.initialize();
        return pool;
    }

    @Bean
    public SimpleAsyncTaskExecutor taskExe() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    @Qualifier("GameSession")
    @Scope(value = "prototype")
    @Lazy(value = true)
    public GameSession getGameSession(GameSessionProperties properties) {
        return new GameSession(properties);
    }

    @Bean
    @Qualifier("GameSessionProperties")
    @Scope(value = "prototype")
    @Lazy(value = true)
    public GameSessionProperties getGameSessionProperties(GameSessionPropertiesCreator creator) {
        return creator.createProperties();
    }

    @Bean
    @Qualifier("GameSessionPropertiesCreator")
    @Scope(value = "prototype")
    @Lazy(value = true)
    public GameSessionPropertiesCreator getGameSessionPropertiesCreator() {
        return new GameSessionPropertiesCreator();
    }

    @Bean
    public CommandLineRunner demo(PlayerDataRepository repository) {
        return (args) -> {

            // save a couple of customers
            /*
            repository.save(new PlayerData("Jack", "Bauer"));
            repository.save(new PlayerData("Chloe", "O'Brian"));
            repository.save(new PlayerData("Kim", "Bauer"));
            repository.save(new PlayerData("David", "Palmer"));
            repository.save(new PlayerData("Michelle", "Dessler"));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (PlayerData customer : repository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            repository.findById(1L)
                    .ifPresent(customer -> {
                        log.info("Customer found with findById(1L):");
                        log.info("--------------------------------");
                        log.info(customer.toString());
                        log.info("");
                    });

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findByName("Bauer").forEach(bauer -> {
                log.info(bauer.toString());
            });
            log.info("");

            // fetch customers by first name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findByName("Kim").forEach(bauer -> {
                log.info(bauer.toString());
            });
            // for (Customer bauer : repository.findByLastName("Bauer")) {
            // 	log.info(bauer.toString());
            // }
            log.info("");
            */
        };
    }
}
