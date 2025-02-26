package ca.ibodrov.concord.repositorybrowser;

import com.walmartlabs.concord.server.jooq.enums.OutVariablesMode;
import com.walmartlabs.concord.server.jooq.enums.RawPayloadMode;
import com.walmartlabs.concord.server.org.OrganizationDao;
import com.walmartlabs.concord.server.org.project.ProjectDao;
import com.walmartlabs.concord.server.org.project.ProjectVisibility;
import com.walmartlabs.concord.server.org.project.RepositoryDao;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Named
public class TestingData {

    public static final String TEST_PROJECT_NAME = "walmartlabs";
    public static final String TEST_REPOSITORY_NAME = "concord";
    public static final String TEST_REPOSITORY_URL = "https://github.com/walmartlabs/concord.git";
    public static final String TEST_REPOSITORY_BRANCH = "master";

    private final OrganizationDao organizationDao;
    private final ProjectDao projectDao;
    private final RepositoryDao repositoryDao;

    @Inject
    public TestingData(OrganizationDao organizationDao,
                       ProjectDao projectDao,
                       RepositoryDao repositoryDao) {

        this.organizationDao = requireNonNull(organizationDao);
        this.projectDao = requireNonNull(projectDao);
        this.repositoryDao = requireNonNull(repositoryDao);
    }

    public void apply() {
        var orgId = Optional.ofNullable(organizationDao.getId("Default")).orElseThrow();

        var projectCfg = Map.<String, Object>of("_plugin",
                Map.of("repository-browser",
                        Map.of("allowList",
                                Map.of(TEST_REPOSITORY_NAME,
                                        List.of(".*\\.js")))));

        var projectId = projectDao.insert(orgId, TEST_PROJECT_NAME, "TestingData", null, projectCfg,
                ProjectVisibility.PRIVATE, RawPayloadMode.DISABLED, null, Map.of(), OutVariablesMode.DISABLED);

        repositoryDao.insert(projectId, TEST_REPOSITORY_NAME, TEST_REPOSITORY_URL, TEST_REPOSITORY_BRANCH, null,
                "/console2/public", null, false, Map.of(), true);
    }
}
