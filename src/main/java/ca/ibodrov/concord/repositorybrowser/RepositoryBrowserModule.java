package ca.ibodrov.concord.repositorybrowser;

import com.google.inject.Binder;
import com.google.inject.Module;

import javax.inject.Named;

import static com.walmartlabs.concord.server.Utils.bindJaxRsResource;

@Named
public class RepositoryBrowserModule implements Module {

    @Override
    public void configure(Binder binder) {
        bindJaxRsResource(binder, RepositoryBrowserResource.class);
    }
}
