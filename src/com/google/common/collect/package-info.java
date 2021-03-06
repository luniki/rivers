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

/**
 * This package contains generic collection interfaces and implementations, and
 * other utilities for working with collections.
 *
 * <h2>Collection Types</h2>
 *
 * <dl>
 * <dt>{@link com.google.common.collect.BiMap}
 * <dd>An extension of {@link java.util.Map} that guarantees the uniqueness of
 *     its values as well as that of its keys. This is sometimes called an
 *     "invertible map," since the restriction on values enables it to support
 *     an {@linkplain com.google.common.collect.BiMap#inverse inverse view} --
 *     which is another instance of {@code BiMap}.
 *
 * <dt>{@link com.google.common.collect.Multiset}
 * <dd>An extension of {@link java.util.Collection} that may contain duplicate
 *     values like a {@link java.util.List}, yet has order-independent equality
 *     like a {@link java.util.Set}.  One typical use for a multiset is to
 *     represent a histogram.
 *
 * <dt>{@link com.google.common.collect.Multimap}
 * <dd>A new type, which is similar to {@link java.util.Map}, but may contain
 *     multiple entries with the same key. Some behaviors of
 *     {@link com.google.common.collect.Multimap} are left unspecified and are
 *     provided only by the two subtypes mentioned next.
 *
 * <dt>{@link com.google.common.collect.SetMultimap}
 * <dd>An extension of {@link com.google.common.collect.Multimap} which has
 *     order-independent equality and does not allow duplicate entries; that is,
 *     while a key may appear twice in a {@code SetMultimap}, each must map to a
 *     different value.  {@code SetMultimap} takes its name from the fact that
 *     the {@linkplain com.google.common.collect.SetMultimap#get collection of
 *     values} associated with a fixed key fulfills the {@link java.util.Set}
 *     contract.
 *
 * <dt>{@link com.google.common.collect.ListMultimap}
 * <dd>An extension of {@link com.google.common.collect.Multimap} which permits
 *     duplicate entries, supports random access of values for a particular key,
 *     and has <i>partially order-dependent equality</i> as defined by
 *     {@link com.google.common.collect.ListMultimap#equals(Object)}. {@code
 *     ListMultimap} takes its name from the fact that the {@linkplain
 *     com.google.common.collect.ListMultimap#get collection of values}
 *     associated with a fixed key fulfills the {@link java.util.List} contract.
 *
 * <dt>{@link com.google.common.collect.SortedSetMultimap}
 * <dd>An extension of {@link com.google.common.collect.SetMultimap} for which
 *     the {@linkplain com.google.common.collect.SortedSetMultimap#get
 *     collection values} associated with a fixed key is a
 *     {@link java.util.SortedSet}.
 * </dl>
 *
 * <h2>Collection Implementations</h2>
 *
 * <h3>of {@link java.util.List}</h3>
 * <dl>
 * <dt>{@link com.google.common.collect.ImmutableList}
 * </ul>
 *
 * <h3>of {@link java.util.Set}</h3>
 * <dl>
 * <dt>{@link com.google.common.collect.ImmutableSet}
 * </ul>
 *
 * <h3>of {@link java.util.SortedSet}</h3>
 * <dl>
 * <dt>{@link com.google.common.collect.SortedArraySet}
 * </dl>
 *
 * <h3>of {@link java.util.Map}</h3>
 * <dl>
 * <dt>{@link com.google.common.collect.ReferenceMap}
 * </ul>
 *
 * <h3>of {@link com.google.common.collect.BiMap}</h3>
 * <dl>
 * <dt>{@link com.google.common.collect.HashBiMap}
 * <dt>{@link com.google.common.collect.EnumBiMap}
 * <dt>{@link com.google.common.collect.EnumHashBiMap}
 * </dl>
 *
 * <h3>of {@link com.google.common.collect.Multiset}</h3>
 * <dl>
 * <dt>{@link com.google.common.collect.EnumMultiset}
 * <dt>{@link com.google.common.collect.HashMultiset}
 * <dt>{@link com.google.common.collect.LinkedHashMultiset}
 * <dt>{@link com.google.common.collect.TreeMultiset}
 * </dl>
 *
 * <h3>of {@link com.google.common.collect.Multimap}</h3>
 * <dl>
 * <dt>{@link com.google.common.collect.ArrayListMultimap}
 * <dt>{@link com.google.common.collect.LinkedListMultimap}
 * <dt>{@link com.google.common.collect.HashMultimap}
 * <dt>{@link com.google.common.collect.LinkedHashMultimap}
 * <dt>{@link com.google.common.collect.TreeMultimap}
 * </dl>
 *
 * <h2>Skeletal implementations</h2>
 * <dl>
 * <dt>{@link com.google.common.collect.AbstractIterator}
 * <dt>{@link com.google.common.collect.AbstractIterable}
 * <dt>{@link com.google.common.collect.AbstractMapEntry}
 * <dt>{@link com.google.common.collect.AbstractMultiset}
 * <dt>{@link com.google.common.collect.AbstractMultisetEntry}
 * </dl>
 *
 * <h2>Classes of static utility methods</h2>
 *
 * <dl>
 * <dt>{@link com.google.common.collect.Comparators}
 * <dt>{@link com.google.common.collect.Iterators}
 * <dt>{@link com.google.common.collect.Iterables}
 * <dt>{@link com.google.common.collect.Lists}
 * <dt>{@link com.google.common.collect.Maps}
 * <dt>{@link com.google.common.collect.Sets}
 * <dt>{@link com.google.common.collect.Multisets}
 * <dt>{@link com.google.common.collect.Multimaps}
 * <dt>{@link com.google.common.collect.ObjectArrays}
 * <dt>{@link com.google.common.collect.PrimitiveArrays}
 * </dl>
 *
 * <h2>Builders</h2>
 *
 * <h2>Constraints stuff</h2>
 *
 * <h2>Forwarding objects</h2>
 * 
 * <dl>
 * <dt>{@link com.google.common.collect.ForwardingCollection }
 * <dt>{@link com.google.common.collect.ForwardingIterator }
 * <dt>{@link com.google.common.collect.ForwardingList }
 * <dt>{@link com.google.common.collect.ForwardingListIterator }
 * <dt>{@link com.google.common.collect.ForwardingMap }
 * <dt>{@link com.google.common.collect.ForwardingMapEntry }
 * <dt>{@link com.google.common.collect.ForwardingMultimap }
 * <dt>{@link com.google.common.collect.ForwardingMultiset }
 * <dt>{@link com.google.common.collect.ForwardingObject }
 * <dt>{@link com.google.common.collect.ForwardingQueue }
 * <dt>{@link com.google.common.collect.ForwardingSet }
 * <dt>{@link com.google.common.collect.ForwardingSortedMap }
 * <dt>{@link com.google.common.collect.ForwardingSortedSet }
 * </dl>
 *
 * <h2>Common Behavior</h2>
 *
 * <p>The methods of this package always throw {@link
 * java.lang.NullPointerException} in response to a null value being supplied
 * for any parameter that is not explicitly annotated as being {@link
 * com.google.common.base.Nullable @Nullable}.
 *
 * @author Mike Bostock
 * @author Kevin Bourrillion
 * @author Jared Levy
 */
package com.google.common.collect;
