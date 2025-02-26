# repository-browser-plugin

Serves Concord repositories as static resources.

## Usage

Add the plugin's JAR to the concord-server's classpath.

```
GET /api/plugin/repositorybrowser/v1/{orgName}/{projectName}/{repositoryName}/{path:*}
```

For example:

```
curl -X 'Authorization: <your_concord_token>' http://localhost:8001/api/plugin/repositorybrowser/v1/Acme/prod/my-assets/assets/logo.png
```

will fetch assets/logo.png from the configured repository named "my-assets",
located in the project "prod" and the organization "Acme".

File paths must be 

Results are cached for 5 minutes.
