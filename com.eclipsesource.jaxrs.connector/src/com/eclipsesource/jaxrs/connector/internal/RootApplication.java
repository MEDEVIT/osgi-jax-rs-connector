/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.connector.internal;


import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.sun.jersey.api.core.DefaultResourceConfig;


public class RootApplication extends DefaultResourceConfig {
  
  private List<Object> resourcePool;

  public RootApplication() {
    resourcePool = new LinkedList<Object>();
  }
  
  void addResource( Object resource ) {
    resourcePool.add( resource );
  }
  
  void removeResource( Object resource ) {
    resourcePool.remove( resource );
  }
  
  boolean hasResources() {
    return !resourcePool.isEmpty();
  }
  
  public List<Object> getResources() {
    return new LinkedList<Object>( resourcePool );
  }

  @Override
  public Set<Object> getSingletons() {
    Set<Object> singletons = super.getSingletons();
    singletons.addAll( resourcePool );
    return singletons;
  }
  
}
