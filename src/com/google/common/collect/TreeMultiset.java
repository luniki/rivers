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

import java.util.Comparator;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Multiset implementation backed by a TreeMap.
 *
 * @author Neal Kanodia
 */
public final class TreeMultiset<E> extends AbstractMapBasedMultiset<E> {
  /**
   * Constructs a new, empty multiset, sorted according to the elements' natural
   * order. All elements inserted into the multiset must implement the
   * {@code Comparable} interface. Furthermore, all such elements must be
   * <i>mutually comparable</i>: {@code e1.compareTo(e2)} must not throw a
   * {@code ClassCastException} for any elements {@code e1} and {@code e2} in
   * the multiset. If the user attempts to add an element to the multiset that
   * violates this constraint (for example, the user attempts to add a string
   * element to a set whose elements are integers), the {@code add(Object)}
   * call will throw a {@code ClassCastException}.
   *
   * @see Comparable
   * @see java.util.TreeSet
   */
  public TreeMultiset() {
    super(new TreeMap<E, AtomicInteger>());
  }

  /**
   * Constructs a new, empty multiset, sorted according to the specified
   * comparator. All elements inserted into the multiset must be <i>mutually
   * comparable</i> by the specified comparator: {@code comparator.compare(e1,
   * e2)} must not throw a {@code ClassCastException} for any elements {@code
   * e1} and {@code e2} in the multiset. If the user attempts to add an element
   * to the multiset that violates this constraint, the {@code add(Object)} call
   * will throw a {@code ClassCastException}.
   *
   * @param comparator the comparator that will be used to sort this multiset. A
   *     null value indicates that the elements' <i>natural ordering</i> should
   *     be used.
   */
  public TreeMultiset(Comparator<? super E> comparator) {
    super(new TreeMap<E, AtomicInteger>(comparator));
  }

  /**
   * Constructs an empty multiset containing the given initial elements, sorted
   * according to the elements' natural order.
   */
  public TreeMultiset(Iterable<? extends E> elements) {
    this();
    Iterables.addAll(this, elements); // careful if we make this class non-final
  }

  
  /**
   * {@inheritDoc}
   *
   * <p>In {@code TreeMultiset}, the return type of this method is narrowed
   * from {@link Set} to {@link SortedSet}.
   */
  @Override public SortedSet<E> elementSet() {
    return (SortedSet<E>) super.elementSet();
  }
  
  @Override protected Set<E> createElementSet() {
     return new SortedMapBasedElementSet(
         (SortedMap<E, AtomicInteger>) backingMap());
  }
  
  private class SortedMapBasedElementSet extends MapBasedElementSet
      implements SortedSet<E> {

    SortedMapBasedElementSet(SortedMap<E, AtomicInteger> map) {
      super(map);
    }

    SortedMap<E, AtomicInteger> sortedMap() {
      return (SortedMap<E, AtomicInteger>) getMap();
    }

    public Comparator<? super E> comparator() {
      return sortedMap().comparator();
    }

    public E first() {
      return sortedMap().firstKey();
    }

    public E last() {
      return sortedMap().lastKey();
    }

    public SortedSet<E> headSet(E toElement) {
      return new SortedMapBasedElementSet(sortedMap().headMap(toElement));
    }

    public SortedSet<E> subSet(E fromElement, E toElement) {
      return new SortedMapBasedElementSet(
          sortedMap().subMap(fromElement, toElement));
    }

    public SortedSet<E> tailSet(E fromElement) {
      return new SortedMapBasedElementSet(sortedMap().tailMap(fromElement));
    }
  }
  
  /*
   * TODO: Decide whether entrySet() should return entries with an equals()
   * method that calls the comparator to compare the two keys. If that change
   * is made, AbstractMultiset.equals() can simply check whether two multisets
   * have equal entry sets.
   */
  
  private static final long serialVersionUID = 980261132547708887L;
}
