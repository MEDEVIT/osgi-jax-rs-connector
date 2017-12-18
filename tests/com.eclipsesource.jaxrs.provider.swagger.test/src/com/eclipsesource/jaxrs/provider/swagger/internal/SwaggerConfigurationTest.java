/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Holger Staudacher - initial API and
 * implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.swagger.internal;

import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_BASE_PATH;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_FILTER_CLASS;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_HOST;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_INFO_CONTACT_EMAIL;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_INFO_CONTACT_NAME;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_INFO_CONTACT_URL;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_INFO_DESCRIPTION;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_INFO_LICENSE_NAME;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_INFO_LICENSE_URL;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_INFO_TERMS;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_INFO_TITLE;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_INFO_VERSION;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.SECURITY_DEFINITION_PREFIX;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.SECURITY_DEFINITION_TYPE;
import static org.junit.Assert.assertEquals;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.osgi.service.cm.ConfigurationException;

import io.swagger.models.Info;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.OAuth2Definition;

public class SwaggerConfigurationTest {

  private SwaggerConfiguration configuration;
  private Dictionary<String, String> properties;

  @Before
  public void setUp() {
    configuration = new SwaggerConfiguration();
    properties = createProperties();
  }

  private Dictionary<String, String> createProperties() {
    Dictionary<String, String> properties = new Hashtable<>();
    properties.put( PROPERTY_HOST, "host" );
    properties.put( PROPERTY_BASE_PATH, "path" );
    properties.put( PROPERTY_FILTER_CLASS, "filter" );
    properties.put( PROPERTY_INFO_TITLE, "title" );
    properties.put( PROPERTY_INFO_DESCRIPTION, "desc" );
    properties.put( PROPERTY_INFO_VERSION, "version" );
    properties.put( PROPERTY_INFO_TERMS, "terms" );
    properties.put( PROPERTY_INFO_CONTACT_NAME, "name" );
    properties.put( PROPERTY_INFO_CONTACT_EMAIL, "email" );
    properties.put( PROPERTY_INFO_CONTACT_URL, "url" );
    properties.put( PROPERTY_INFO_LICENSE_NAME, "licenseName" );
    properties.put( PROPERTY_INFO_LICENSE_URL, "licenseUrl" );
    properties.put( SECURITY_DEFINITION_TYPE + "testoauth", "oauth2" );
    properties.put( SECURITY_DEFINITION_PREFIX + "testoauth.description", "oauth2Description" );
    properties.put( SECURITY_DEFINITION_PREFIX + "testoauth.flow", "password" );
    properties.put( SECURITY_DEFINITION_PREFIX + "testoauth.tokenUrl",
                    "http://host/login/oauth/token" );
    properties.put( SECURITY_DEFINITION_PREFIX + "testoauth.scopes.0", "sysadmin" );
    properties.put( SECURITY_DEFINITION_PREFIX + "testoauth.scopes.0.description", "system admin" );
    properties.put( SECURITY_DEFINITION_TYPE + "testApiKey", "apiKey" );
    properties.put( SECURITY_DEFINITION_PREFIX + "testApiKey.name", "apiKeyName" );
    return properties;
  }

  @Test
  public void testGetsHost() throws ConfigurationException {
    configuration.updated( properties );
    String host = configuration.getHost();
    assertEquals( "host", host );
  }

  @Test
  public void testGetsBasePath() throws ConfigurationException {
    configuration.updated( properties );
    String basePath = configuration.getBasePath();
    assertEquals( "path", basePath );
  }

  @Test
  public void testGetsFilterClass() throws ConfigurationException {
    configuration.updated( properties );
    String filterClass = configuration.getFilterClass();
    assertEquals( "filter", filterClass );
  }

  @Test
  public void testGetsInfo() throws ConfigurationException {
    configuration.updated( properties );
    Info info = configuration.getInfo();
    assertEquals( "title", info.getTitle() );
    assertEquals( "version", info.getVersion() );
    assertEquals( "desc", info.getDescription() );
    assertEquals( "terms", info.getTermsOfService() );
    assertEquals( "name", info.getContact().getName() );
    assertEquals( "email", info.getContact().getEmail() );
    assertEquals( "url", info.getContact().getUrl() );
    assertEquals( "licenseName", info.getLicense().getName() );
    assertEquals( "licenseUrl", info.getLicense().getUrl() );
  }

  @Test
  public void testSecurityDefinitions() throws ConfigurationException {
    configuration.updated( properties );
    OAuth2Definition oauth2Definition = ( OAuth2Definition )configuration.getSecurityDefinitions()
      .get( "testoauth" );
    assertEquals( "oauth2", oauth2Definition.getType() );
    assertEquals( "oauth2Description", oauth2Definition.getDescription() );
    assertEquals( "password", oauth2Definition.getFlow() );
    assertEquals( "http://host/login/oauth/token", oauth2Definition.getTokenUrl() );
    assertEquals( "system admin", oauth2Definition.getScopes().get( "sysadmin" ) );
    ApiKeyAuthDefinition apiKeyDefinition = ( ApiKeyAuthDefinition )configuration
      .getSecurityDefinitions()
      .get( "testApiKey" );
    assertEquals( "apiKeyName", apiKeyDefinition.getName() );
  }
}
