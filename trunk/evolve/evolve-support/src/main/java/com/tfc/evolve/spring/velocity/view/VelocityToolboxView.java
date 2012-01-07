package com.tfc.evolve.spring.velocity.view;

import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolboxFactory;
import org.apache.velocity.tools.config.XmlFactoryConfiguration;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;

/**
 * Configures Velocity Toolbox View to use new XML Configuration in Spring
 * 
 * @author Gregor "hrax" Magdolen
 */

public class VelocityToolboxView extends org.springframework.web.servlet.view.velocity.VelocityToolboxView {
    private Resource toolboxConfigResource;

    public void setToolboxConfigResource(Resource toolboxConfigResource) {
        this.toolboxConfigResource = toolboxConfigResource;
    }

    public Resource getToolboxConfigResource() {
        return this.toolboxConfigResource;
    }

    @Override
    protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        // Create a ViewToolContext instance since ChainedContext is deprecated
        // in Velocity Tools 2.0.

        ViewToolContext velocityContext = new ViewToolContext(

        getVelocityEngine(), request, response, getServletContext());

        velocityContext.putAll(model);

        // Load a Configuration and publish toolboxes to the context when
        // necessary

        if (getToolboxConfigLocation() != null || getToolboxConfigResource() != null) {

            XmlFactoryConfiguration cfg = new XmlFactoryConfiguration();

            URL cfgUrl;
            if (getToolboxConfigLocation() != null) {
                cfgUrl = new ServletContextResource(getServletContext(), getToolboxConfigLocation()).getURL();
                cfg.read(cfgUrl);
            }
            else if (getToolboxConfigResource() != null) {
                cfgUrl = getToolboxConfigResource().getURL();
                cfg.read(cfgUrl);
            }

            ToolboxFactory factory = cfg.createFactory();

            velocityContext.addToolbox(factory.createToolbox(Scope.APPLICATION));

            velocityContext.addToolbox(factory.createToolbox(Scope.REQUEST));

            velocityContext.addToolbox(factory.createToolbox(Scope.SESSION));

        }

        return velocityContext;

    }
}
