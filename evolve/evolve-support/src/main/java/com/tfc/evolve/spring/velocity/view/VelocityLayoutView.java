/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tfc.evolve.spring.velocity.view;

import java.io.StringWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.core.NestedIOException;

/**
 * VelocityLayoutView emulates the functionality offered by Velocity's
 * VelocityLayoutServlet to ease page composition from different templates.
 * 
 * <p>
 * The <code>url</code> property should be set to the content template for the
 * view, and the layout template location should be specified as
 * <code>layoutUrl</code> property. A view can override the configured layout
 * template location by setting the appropriate key (the default is "layout") in
 * the content template.
 * 
 * <p>
 * When the view is rendered, the VelocityContext is first merged with the
 * content template (specified by the <code>url</code> property) and then merged
 * with the layout template to produce the final output.
 * 
 * <p>
 * The layout template can include the screen content through a VelocityContext
 * variable (the default is "screen_content"). At runtime, this variable will
 * contain the rendered content template.
 * 
 * @author Darren Davison
 * @author Juergen Hoeller
 * @since 1.2
 * @see #setLayoutUrl
 * @see #setLayoutKey
 * @see #setScreenContentKey
 */
public class VelocityLayoutView extends VelocityToolboxView {
	public static final String DEFAULT_LAYOUT_URL = "layout.vm";
	public static final String DEFAULT_LAYOUT_KEY = "layout";
	public static final String DEFAULT_SCREEN_CONTENT_KEY = "screen_content";

	private String layoutUrl;
	private String layoutDir;
	private String layoutKey = DEFAULT_LAYOUT_KEY;
	private String screenContentKey = DEFAULT_SCREEN_CONTENT_KEY;

	@Override
	protected void initApplicationContext() throws BeansException {
		super.initApplicationContext();
		this.layoutUrl = (String) getVelocityEngine().getProperty(
				"tools.view.servlet.layout.default.template");
		this.layoutDir = (String) getVelocityEngine().getProperty(
				"tools.view.servlet.layout.directory");
		if (this.layoutUrl == "") {
			this.layoutUrl = DEFAULT_LAYOUT_URL;
		}
		if (!this.layoutDir.endsWith("/")) {
			this.layoutDir = this.layoutDir + "/";
		}
	}

	public void setLayoutUrl(String layoutUrl) {
		this.layoutUrl = layoutUrl;
	}

	public void setLayoutKey(String layoutKey) {
		this.layoutKey = layoutKey;
	}

	public void setScreenContentKey(String screenContentKey) {
		this.screenContentKey = screenContentKey;
	}

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		if (!super.checkResource(locale)) {
			return false;
		}
		try {
			getTemplate(getTotalLayoutUrl(this.layoutUrl));
			return true;
		} catch (ResourceNotFoundException ex) {
			throw new NestedIOException(
					"Cannot find Velocity template for URL ["
							+ getTotalLayoutUrl(this.layoutUrl)
							+ "]: Did you specify the correct resource loader path?",
					ex);
		} catch (Exception ex) {
			throw new NestedIOException(
					"Could not load Velocity template for URL ["
							+ getTotalLayoutUrl(this.layoutUrl) + "]", ex);
		}
	}

	@Override
	protected void doRender(Context context, HttpServletResponse response)
			throws Exception {
		renderScreenContent(context);

		String layoutUrlToUse = (String) context.get(this.layoutKey);
		if (layoutUrlToUse != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Screen content template has requested layout ["
						+ layoutUrlToUse + "]");
			}
		} else {
			// No explicit layout URL given -> use default layout of this view.
			layoutUrlToUse = this.layoutUrl;
		}

		mergeTemplate(getTemplate(getTotalLayoutUrl(layoutUrlToUse)), context,
				response);
	}

	private String getTotalLayoutUrl(String layoutUrl) {
		return layoutDir + layoutUrl;
	}

	/**
	 * The resulting context contains any mappings from render, plus screen
	 * content.
	 */
	private void renderScreenContent(Context velocityContext) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Rendering screen content template [" + getUrl() + "]");
		}

		StringWriter sw = new StringWriter();
		Template screenContentTemplate = getTemplate(getUrl());
		screenContentTemplate.merge(velocityContext, sw);

		// Put rendered content into Velocity context.
		velocityContext.put(this.screenContentKey, sw.toString());
	}
}
