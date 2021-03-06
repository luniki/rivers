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

import com.google.common.base.Nullable;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * A high-performance, immutable, random-access {@code List} implementation.
 * Does not permit null elements.
 *
 * <p>Unlike {@link Collections#unmodifiableList}, which is a <i>view</i> of a
 * separate collection that can still change, an instance of {@code
 * ImmutableList} contains its own private data and will <i>never</i> change.
 * {@code ImmutableList} is convenient for {@code public static final} lists
 * ("constant lists") and also lets you easily make a "defensive copy" of a list
 * provided to your class by a caller.
 *
 * <p><b>Note</b>: Although this class is not final, it cannot be subclassed as
 * it has no public or protected constructors. Thus, the immutability guarantee
 * can be trusted.
 *
 * @see ImmutableSet
 * @author Kevin Bourrillion
 */
@SuppressWarnings("serial") // we're overriding default serialization
public abstract class ImmutableList<E> extends AbstractList<E>
    implements RandomAccess, Serializable {
  private static final ImmutableList<?> EMPTY_IMMUTABLE_LIST
      = new EmptyImmutableList();

  /**
   * Returns the empty immutable list. This set behaves and performs comparably
   * to {@link Collections#emptyList}, and is preferable mainly for consistency
   * and maintainability of your code.
   */
  // Casting to any type is safe because the set will never hold any elements.
  @SuppressWarnings({"unchecked"})
  public static <E> ImmutableList<E> of() {
    return (ImmutableList<E>) EMPTY_IMMUTABLE_LIST;
  }

  /**
   * Returns an immutable list containing a single element. This list behaves
   * and performs comparably to {@link Collections#singleton}, but will not
   * accept a null element. It is preferable mainly for consistency and
   * maintainability of your code.
   */
  public static <E> ImmutableList<E> of(E element) {
    // TODO: evaluate a specialized SingletonImmutableList
    return new RegularImmutableList<E>(new Object[] { checkNotNull(element) });
  }

  /**
   * Returns an immutable list containing the given elements, in order.
   *
   * @throws NullPointerException if any of {@code elements} is null
   */
  public static <E> ImmutableList<E> of(E... elements) {
    return (elements.length == 0)
        ? ImmutableList.<E>of()
        : new RegularImmutableList<E>(copyIntoArray(elements));
  }

  /**
   * Returns an immutable list containing the given elements, in order. Note
   * that if {@code list} is a {@code List<String>}, then {@code
   * ImmutableList.copyOf(list)} returns an {@code ImmutableList<String>}
   * containing each of the strings in {@code list}, while
   * ImmutableList.of(list)} returns an {@code ImmutableList<List<String>>}
   * containing one element (the given list itself).
   *
   * <p><b>Note:</b> Despite what the method name suggests, if {@code elements}
   * is an {@code ImmutableList}, no copy will actually be performed, and the
   * given list itself will be returned.
   *
   * @throws NullPointerException if any of {@code elements} are null
   */
  public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
    if (elements instanceof ImmutableList<?>) {
      @SuppressWarnings("unchecked") // all supported methods are covariant
      ImmutableList<E> list = (ImmutableList<E>) elements;
      return list;
    }
    int size = Iterables.size(elements);
    return (size == 0)
        ? ImmutableList.<E>of()
        : new RegularImmutableList<E>(copyIntoArray(elements, size));
  }

  private ImmutableList() {}

  // Mark these three methods with @Nullable

  @Override public int indexOf(@Nullable Object object) {
    return super.indexOf(object);
  }

  @Override public int lastIndexOf(@Nullable Object object) {
    return super.lastIndexOf(object);
  }

  @Override public boolean contains(@Nullable Object object) {
    return super.contains(object);
  }

  private static final class EmptyImmutableList extends ImmutableList<Object> {
    @Override public int size() {
      return 0;
    }

    @Override public boolean isEmpty() {
      return true;
    }

    @Override public boolean contains(Object target) {
      return false;
    }

    @Override public Iterator<Object> iterator() {
      return Iterators.emptyIterator();
    }

    @Override public Object[] toArray() {
      return ObjectArrays.EMPTY_ARRAY;
    }

    @Override public <T> T[] toArray(T[] a) {
      if (a.length > 0) {
        a[0] = null;
      }
      return a;
    }

    @Override public Object get(int index) {
      throw new IndexOutOfBoundsException("Index: " + index);
    }

    @Override public int indexOf(Object target) {
      return -1;
    }

    @Override public int lastIndexOf(Object target) {
      return -1;
    }

    @Override public ListIterator<Object> listIterator() {
      return Iterators.emptyListIterator();
    }

    @Override public ListIterator<Object> listIterator(int start) {
      checkArgument(start == 0);
      return Iterators.emptyListIterator();
    }

    @Override public boolean containsAll(Collection<?> targets) {
      return targets.isEmpty();
    }

    @Override public boolean equals(Object object) {
      return object == this
          || (object instanceof List<?> && ((List<?>) object).isEmpty());
    }

    @Override public int hashCode() {
      return 1;
    }

    @Override public String toString() {
      return "[]";
    }

    private Object readResolve() {
      return EMPTY_IMMUTABLE_LIST;
    }
  }

  private static final class RegularImmutableList<E> extends ImmutableList<E> {
    private final Object[] array;

    private RegularImmutableList(Object[] array) {
      this.array = array;
    }

    @Override public int size() {
      return array.length;
    }

    @Override public boolean isEmpty() {
      return false;
    }

    @Override public boolean contains(Object target) {
      return indexOf(target) != -1;
    }

    @SuppressWarnings("unchecked")
    @Override public Iterator<E> iterator() {
      return (Iterator<E>) Iterators.forArray(array);
    }

    @Override public Object[] toArray() {
      Object[] newArray = new Object[size()];
      System.arraycopy(array, 0, newArray, 0, size());
      return newArray;
    }

    @Override public <T> T[] toArray(T[] other) {
      int size = size();
      if (other.length < size) {
        other = ObjectArrays.newArray(other, size);
      } else if (other.length > size) {
        other[size] = null;
      }
      System.arraycopy(array, 0, other, 0, size);
      return other;
    }

    // The fake cast to E is safe because the creation methods only allow E's
    @SuppressWarnings("unchecked")
    @Override public E get(int index) {
      return (E) array[index];
    }

    @Override public int indexOf(Object target) {
      if (target != null) {
        for (int i = 0; i < size(); i++) {
          if (array[i].equals(target)) {
            return i;
          }
        }
      }
      return -1;
    }

    @Override public int lastIndexOf(Object target) {
      if (target != null) {
        for (int i = size() - 1; i >= 0; i--) {
          if (array[i].equals(target)) {
            return i;
          }
        }
      }
      return -1;
    }

    @Override public ListIterator<E> listIterator(final int start) {
      checkArgument(start >= 0);
      checkArgument(start <= size());
      return new ListIterator<E>() {
        int index = start;

        public boolean hasNext() {
          return index < size();
        }
        public boolean hasPrevious() {
          return index > 0;
        }

        public int nextIndex() {
          return index;
        }
        public int previousIndex() {
          return index - 1;
        }

        public E next() {
          E result;
          try {
            result = get(index);
          } catch (IndexOutOfBoundsException unused) {
            throw new NoSuchElementException();
          }
          index++;
          return result;
        }
        public E previous() {
          E result;
          try {
            result = get(index - 1);
          } catch (IndexOutOfBoundsException unused) {
            throw new NoSuchElementException();
          }
          index--;
          return result;
        }

        public void set(E o) {
          throw new UnsupportedOperationException();
        }
        public void add(E o) {
          throw new UnsupportedOperationException();
        }
        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }

    @Override public boolean equals(Object object) {
      if (object == this) {
        return true;
      }
      if (!(object instanceof List<?>)) {
        return false;
      }

      List<?> that = (List<?>) object;
      if (that.size() != size()) {
        return false;
      }

      int index = 0;
      if (object instanceof RegularImmutableList<?>) {
        RegularImmutableList<?> other = (RegularImmutableList<?>) object;
        for (Object element : other.array) {
          if (!array[index++].equals(element)) {
            return false;
          }
        }
      } else {
        for (Object element : that) {
          if (!array[index++].equals(element)) {
            return false;
          }
        }
      }
      return true;
    }
  
    @Override public int hashCode() {
      int hashCode = 1;
      for (Object element : array) {
        hashCode = 31 * hashCode + element.hashCode();
      }
      return hashCode;
    }

    @Override public String toString() {
      StringBuilder sb = new StringBuilder(size() * 16);
      sb.append('[').append(array[0]);
      for (int i = 1; i < size(); i++) {
        sb.append(", ").append(array[i]);
      }
      return sb.append(']').toString();
    }
  }

  private static Object[] copyIntoArray(Object[] source) {
    Object[] array = new Object[source.length];
    int index = 0;
    for (Object element : source) {
      if (element == null) {
        throw new NullPointerException("at index " + index);
      }
      array[index++] = element;
    }
    return array;
  }

  private static Object[] copyIntoArray(Iterable<?> source, int size) {
    Object[] array = new Object[size];
    int index = 0;
    for (Object element : source) {
      if (element == null) {
        throw new NullPointerException("at index " + index);
      }
      array[index++] = element;
    }
    return array;
  }

  private static final long serialVersionUID = 0;
}
