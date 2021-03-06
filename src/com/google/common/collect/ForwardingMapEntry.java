/*
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import java.util.Map;

/**
 * A map entry which forwards all its method calls to another map entry.
 * Subclasses should override one or more methods to modify the behavior of the
 * backing map entry as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 * 
 * @see ForwardingObject
 * @author Mike Bostock
 */
public abstract class ForwardingMapEntry<K, V> extends ForwardingObject
    implements Map.Entry<K, V> {

  /**
   * Constructs a forwarding map entry that forwards to the provided delegate.
   */
  protected ForwardingMapEntry(Map.Entry<K, V> delegate) {
    super(delegate);
  }

  @SuppressWarnings("unchecked")
  @Override protected Map.Entry<K, V> delegate() {
    return (Map.Entry<K, V>) super.delegate();
  }

  public K getKey() {
    return delegate().getKey();
  }

  public V getValue() {
    return delegate().getValue();
  }

  public V setValue(V value) {
    return delegate().setValue(value);
  }

  @Override public boolean equals(Object obj) {
    return delegate().equals(obj);
  }

  @Override public int hashCode() {
    return delegate().hashCode();
  }
}
