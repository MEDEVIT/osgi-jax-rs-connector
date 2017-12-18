package com.eclipsesource.jaxrs.provider.swagger;

public class SwaggerConfigurationConstants {

  public static final String SWAGGER_PREFIX = "swagger.";
  public static final String INFO_PREFIX = SWAGGER_PREFIX + "info.";
  public static final String PROPERTY_BASE_PATH = SWAGGER_PREFIX + "basePath";
  public static final String PROPERTY_HOST = SWAGGER_PREFIX + "host";
  public static final String PROPERTY_FILTER_CLASS = SWAGGER_PREFIX + "filterClass";
  public static final String PROPERTY_SCHEME_PREFIX = SWAGGER_PREFIX + "scheme.";
  public static final String PROPERTY_SCHEME_HTTP = PROPERTY_SCHEME_PREFIX + "http";
  public static final String PROPERTY_SCHEME_HTTPS = PROPERTY_SCHEME_PREFIX + "https";
  public static final String PROPERTY_SCHEME_WS = PROPERTY_SCHEME_PREFIX + "ws";
  public static final String PROPERTY_SCHEME_WSS = PROPERTY_SCHEME_PREFIX + "wss";
  public static final String PROPERTY_INFO_TITLE = INFO_PREFIX + "title";
  public static final String PROPERTY_INFO_DESCRIPTION = INFO_PREFIX + "description";
  public static final String PROPERTY_INFO_VERSION = INFO_PREFIX + "version";
  public static final String PROPERTY_INFO_TERMS = INFO_PREFIX + "termsOfService";
  public static final String PROPERTY_INFO_CONTACT_NAME = INFO_PREFIX + "contact.name";
  public static final String PROPERTY_INFO_CONTACT_URL = INFO_PREFIX + "contact.url";
  public static final String PROPERTY_INFO_CONTACT_EMAIL = INFO_PREFIX + "contact.email";
  public static final String PROPERTY_INFO_LICENSE_NAME = INFO_PREFIX + "license.name";
  public static final String PROPERTY_INFO_LICENSE_URL = INFO_PREFIX + "license.url";
  public static final String SECURITY_DEFINITION_PREFIX = SWAGGER_PREFIX + "securityDefinition.";
  public static final String SECURITY_DEFINITION_TYPE = SECURITY_DEFINITION_PREFIX + "type.";
  public static final String[] INFO_KEYS = new String[]{
    PROPERTY_INFO_TITLE,
    PROPERTY_INFO_DESCRIPTION,
    PROPERTY_INFO_VERSION,
    PROPERTY_INFO_TERMS,
    PROPERTY_INFO_CONTACT_NAME,
    PROPERTY_INFO_CONTACT_URL,
    PROPERTY_INFO_CONTACT_EMAIL,
    PROPERTY_INFO_LICENSE_NAME,
    PROPERTY_INFO_LICENSE_URL
  };
}
