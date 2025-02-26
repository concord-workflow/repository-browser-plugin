package ca.ibodrov.concord.repositorybrowser;

import com.google.inject.Module;
import com.typesafe.config.Config;
import com.walmartlabs.concord.it.testingserver.TestingConcordServer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class LocalServer {

    private static final String TEST_ADMIN_TOKEN = "test";

    public static void main(String[] args) throws Exception {
        var apiPort = 8001;
        try (var db = new PostgreSQLContainer<>("postgres:15-alpine");
                var server = new TestingConcordServer(db, apiPort, createConfig(), extraModules())) {
            db.start();
            server.start();

            var injector = server.getServer().getInjector();
            var testingData = injector.getInstance(TestingData.class);
            testingData.apply();

            System.out.printf(
                    """
                            ==============================================================

                              UI (hosted): http://localhost:%s
                              DB:
                                JDBC URL: %s
                                username: %s
                                password: %s
                              API:
                                admin key: %s

                              curl -i -H 'Authorization: %s' http://localhost:8001/api/plugin/repositorybrowser/v1/Default/walmartlabs/concord/cfg.js

                            ==============================================================
                            %n""",
                    apiPort, db.getJdbcUrl(), db.getUsername(), db.getPassword(), TEST_ADMIN_TOKEN, TEST_ADMIN_TOKEN);
            Thread.currentThread().join();
        }
    }

    private static Map<String, String> createConfig() {
        return ImmutableMap.<String, String>builder()
                .put("db.changeLogParameters.defaultAdminToken", TEST_ADMIN_TOKEN)
                .build();
    }

    private static List<Function<Config, Module>> extraModules() {
        return List.of(_cfg -> new RepositoryBrowserModule());
    }
}
