/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors:<br>
 * Holger Staudacher - initial API and implementation <br>
 * Marco Descher - extract constants, add security definitions configuration
 ******************************************************************************/
package com.eclipsesource.jaxrs.provider.swagger.internal;

import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.INFO_KEYS;
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
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_SCHEME_HTTP;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_SCHEME_HTTPS;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_SCHEME_WS;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.PROPERTY_SCHEME_WSS;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.SECURITY_DEFINITION_PREFIX;
import static com.eclipsesource.jaxrs.provider.swagger.SwaggerConfigurationConstants.SECURITY_DEFINITION_TYPE;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Scheme;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.auth.SecuritySchemeDefinition;

public class SwaggerConfiguration implements ManagedService {

  public static final String SERVICE_PID = "com.eclipsesource.jaxrs.swagger.config";
  private Dictionary<String, ?> configuration;

  @Override
  public void updated( Dictionary<String, ?> configuration ) throws ConfigurationException {
    this.configuration = configuration;
  }

  public Info getInfo() {
    if( hasInfoValues() ) {
      return buildInfo();
    }
    return null;
  }

  private boolean hasInfoValues() {
    for( String infoKey : INFO_KEYS ) {
      if( hasConfig( infoKey ) ) {
        return true;
      }
    }
    return false;
  }

  private Info buildInfo() {
    Info info = new Info();
    info.setTitle( getConfig( PROPERTY_INFO_TITLE ) );
    info.setDescription( getConfig( PROPERTY_INFO_DESCRIPTION ) );
    info.setVersion( getConfig( PROPERTY_INFO_VERSION ) );
    info.setTermsOfService( getConfig( PROPERTY_INFO_TERMS ) );
    info.setContact( buildContact() );
    info.setLicense( buildLicense() );
    return info;
  }

  private Contact buildContact() {
    if( hasConfig( PROPERTY_INFO_CONTACT_NAME )
        || hasConfig( PROPERTY_INFO_CONTACT_URL )
        || hasConfig( PROPERTY_INFO_CONTACT_EMAIL ) )
    {
      Contact contact = new Contact();
      contact.setEmail( getConfig( PROPERTY_INFO_CONTACT_EMAIL ) );
      contact.setName( getConfig( PROPERTY_INFO_CONTACT_NAME ) );
      contact.setUrl( getConfig( PROPERTY_INFO_CONTACT_URL ) );
      return contact;
    }
    return null;
  }

  private License buildLicense() {
    if( hasConfig( PROPERTY_INFO_LICENSE_NAME ) || hasConfig( PROPERTY_INFO_LICENSE_URL ) ) {
      License license = new License();
      license.setName( getConfig( PROPERTY_INFO_LICENSE_NAME ) );
      license.setUrl( getConfig( PROPERTY_INFO_LICENSE_URL ) );
      return license;
    }
    return null;
  }

  public String getBasePath() {
    return getConfig( PROPERTY_BASE_PATH );
  }

  public String getHost() {
    return getConfig( PROPERTY_HOST );
  }

  public String getFilterClass() {
    return getConfig( PROPERTY_FILTER_CLASS );
  }

  private String getConfig( String key ) {
    if( hasConfig( key ) ) {
      return ( String )configuration.get( key );
    }
    return null;
  }

  private boolean hasConfig( String key ) {
    if( configuration != null ) {
      return configuration.get( key ) != null;
    }
    return false;
  }

  public List<Scheme> getSchemes() {
    List<Scheme> schemes = new ArrayList<>();
    if( hasConfig( PROPERTY_SCHEME_HTTP ) ) {
      schemes.add( Scheme.HTTP );
    }
    if( hasConfig( PROPERTY_SCHEME_HTTPS ) ) {
      schemes.add( Scheme.HTTPS );
    }
    if( hasConfig( PROPERTY_SCHEME_WS ) ) {
      schemes.add( Scheme.WS );
    }
    if( hasConfig( PROPERTY_SCHEME_WSS ) ) {
      schemes.add( Scheme.WSS );
    }
    return schemes;
  }

  public Map<String, SecuritySchemeDefinition> getSecurityDefinitions() {
    Map<String, SecuritySchemeDefinition> securityDefinitions = new HashMap<>();
    if( configuration != null ) {
      determineConfiguredSecuritySchemeDefinitions( securityDefinitions );
      Set<Entry<String, SecuritySchemeDefinition>> entrySet = securityDefinitions.entrySet();
      for( Iterator<Entry<String, SecuritySchemeDefinition>> iterator = entrySet
        .iterator(); iterator.hasNext(); )
      {
        Entry<String, SecuritySchemeDefinition> entry = ( Entry<String, SecuritySchemeDefinition> )iterator
          .next();
        String authSchemeName = entry.getKey();
        SecuritySchemeDefinition authSchemeDefinition = entry.getValue();
        authSchemeDefinition.setDescription( ( String )configuration
          .get( SECURITY_DEFINITION_PREFIX + authSchemeName + ".description" ) );
        // TODO vendor extension
        if( authSchemeDefinition instanceof OAuth2Definition ) {
          OAuth2Definition oauth2Def = ( OAuth2Definition )authSchemeDefinition;
          configureOAuth2SecurityDefinition( oauth2Def, authSchemeName );
        } else if( authSchemeDefinition instanceof ApiKeyAuthDefinition ) {
          ApiKeyAuthDefinition apiKeyDef = ( ApiKeyAuthDefinition )authSchemeDefinition;
          apiKeyDef.setName( ( String )configuration
            .get( SECURITY_DEFINITION_PREFIX + authSchemeName + ".name" ) );
        }
      }
    }
    return securityDefinitions;
  }

  private void configureOAuth2SecurityDefinition( OAuth2Definition def, String authSchemeName ) {
    def.setAuthorizationUrl( ( String )configuration
      .get( SECURITY_DEFINITION_PREFIX + authSchemeName + ".authorizationUrl" ) );
    def.setFlow( ( String )configuration
      .get( SECURITY_DEFINITION_PREFIX + authSchemeName + ".flow" ) );
    def.setTokenUrl( ( String )configuration
      .get( SECURITY_DEFINITION_PREFIX + authSchemeName + ".tokenUrl" ) );
    Map<String, String> scopeMap = new HashMap<>();
    int scopeInt = 0;
    while( hasConfig( SECURITY_DEFINITION_PREFIX + authSchemeName + ".scopes." + scopeInt ) ) {
      String scopeKey = getConfig( SECURITY_DEFINITION_PREFIX
                                   + authSchemeName
                                   + ".scopes."
                                   + scopeInt );
      String scopeDescription = getConfig( SECURITY_DEFINITION_PREFIX
                                           + authSchemeName
                                           + ".scopes."
                                           + scopeInt
                                           + ".description" );
      scopeMap.put( scopeKey, scopeDescription );
      scopeInt++;
    }
    def.setScopes( scopeMap );
  }

  /**
   * Find all swagger.securityDefinition.type.xxxx entries and instantiate the respective map entry
   * 
   * @param securityDefinitions
   */
  private void determineConfiguredSecuritySchemeDefinitions( Map<String, SecuritySchemeDefinition> securityDefinitions ) {
    Enumeration<String> keys = configuration.keys();
    while( keys.hasMoreElements() ) {
      String key = keys.nextElement();
      if( key.startsWith( SECURITY_DEFINITION_TYPE ) ) {
        String authSchemeName = key.substring( SECURITY_DEFINITION_TYPE.length() );
        String authSchemeType = ( String )configuration.get( key );
        SecuritySchemeDefinition securitySchemeDefinition = null;
        if( new OAuth2Definition().getType().equals( authSchemeType ) ) {
          securitySchemeDefinition = new OAuth2Definition();
        } else if( new BasicAuthDefinition().getType().equals( authSchemeType ) ) {
          securitySchemeDefinition = new BasicAuthDefinition();
        } else if( new ApiKeyAuthDefinition().getType().equals( authSchemeType ) ) {
          securitySchemeDefinition = new ApiKeyAuthDefinition();
        }
        securityDefinitions.put( authSchemeName, securitySchemeDefinition );
      }
    }
  }
}
