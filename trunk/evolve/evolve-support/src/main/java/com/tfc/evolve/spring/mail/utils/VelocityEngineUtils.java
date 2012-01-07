/*
 * Copyright 2011 Focus Technology, Co., Ltd. All rights reserved.
 */
package com.tfc.evolve.spring.mail.utils;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolboxFactory;
import org.apache.velocity.tools.config.XmlFactoryConfiguration;
import org.springframework.core.io.Resource;

/**
 * 扩展使其支持Velocity Tools功能的工具类
 * 
 * @author taofucheng
 */
@SuppressWarnings({"rawtypes", "deprecation"})
public class VelocityEngineUtils {
    private static Resource toolboxConfigResource;

    private static final Log logger = LogFactory.getLog(VelocityEngineUtils.class);

    public static void mergeTemplate(VelocityEngine velocityEngine, String templateLocation, Map model, Writer writer)
            throws VelocityException {
        try {
            ToolContext velocityContext = createVelocityContext(velocityEngine, model);
            velocityEngine.mergeTemplate(templateLocation, velocityContext, writer);
        }
        catch (VelocityException ex) {
            throw ex;
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
            logger.error("Why does VelocityEngine throw a generic checked exception, after all?", ex);
            throw new VelocityException(ex.toString());
        }
    }

    public static void mergeTemplate(VelocityEngine velocityEngine, String templateLocation, String encoding,
            Map model, Writer writer) throws VelocityException {
        try {
            ToolContext velocityContext = createVelocityContext(velocityEngine, model);
            velocityEngine.mergeTemplate(templateLocation, encoding, velocityContext, writer);
        }
        catch (VelocityException ex) {
            throw ex;
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
            logger.error("Why does VelocityEngine throw a generic checked exception, after all?", ex);
            throw new VelocityException(ex.toString());
        }
    }

    public static String mergeTemplateIntoString(VelocityEngine velocityEngine, String templateLocation, Map model)
            throws VelocityException {
        StringWriter result = new StringWriter();
        mergeTemplate(velocityEngine, templateLocation, model, result);
        return result.toString();
    }

    public static String mergeTemplateIntoString(VelocityEngine velocityEngine, String templateLocation,
            String encoding, Map model) throws VelocityException {
        StringWriter result = new StringWriter();
        mergeTemplate(velocityEngine, templateLocation, encoding, model, result);
        return result.toString();
    }

    protected static ToolContext createVelocityContext(VelocityEngine velocityEngine, Map model) throws Exception {

        // Create a ViewToolContext instance since ChainedContext is deprecated
        // in Velocity Tools 2.0.

        ToolContext velocityContext = new ToolContext(velocityEngine);

        velocityContext.putAll(model);

        // Load a Configuration and publish toolboxes to the context when
        // necessary

        if (toolboxConfigResource != null) {

            XmlFactoryConfiguration cfg = new XmlFactoryConfiguration();

            URL cfgUrl;
            cfgUrl = toolboxConfigResource.getURL();
            cfg.read(cfgUrl);

            ToolboxFactory factory = cfg.createFactory();

            velocityContext.addToolbox(factory.createToolbox(Scope.APPLICATION));

            velocityContext.addToolbox(factory.createToolbox(Scope.REQUEST));

            velocityContext.addToolbox(factory.createToolbox(Scope.SESSION));

        }

        return velocityContext;
    }

    public static void setToolboxConfigResource(Resource toolboxConfigResource) {
        VelocityEngineUtils.toolboxConfigResource = toolboxConfigResource;
    }
}
