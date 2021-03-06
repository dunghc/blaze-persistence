/*
 * Copyright 2014 - 2020 Blazebit.
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

package com.blazebit.persistence.view.impl.metamodel.attribute;

import com.blazebit.persistence.view.impl.collection.CollectionInstantiatorImplementor;
import com.blazebit.persistence.view.impl.collection.MapInstantiatorImplementor;
import com.blazebit.persistence.view.impl.metamodel.AbstractMethodPluralAttribute;
import com.blazebit.persistence.view.impl.metamodel.EmbeddableOwner;
import com.blazebit.persistence.view.impl.metamodel.ManagedViewTypeImplementor;
import com.blazebit.persistence.view.impl.metamodel.MetamodelBuildingContext;
import com.blazebit.persistence.view.impl.metamodel.MethodAttributeMapping;
import com.blazebit.persistence.view.metamodel.ManagedViewType;
import com.blazebit.persistence.view.metamodel.MethodMapAttribute;
import com.blazebit.persistence.view.metamodel.Type;

import java.util.Map;

/**
 *
 * @author Christian Beikov
 * @since 1.2.0
 */
public abstract class AbstractMethodMapAttribute<X, K, V> extends AbstractMethodPluralAttribute<X, Map<K, V>, V> implements MethodMapAttribute<X, K, V> {

    private final Type<K> keyType;
    private final Map<ManagedViewType<? extends K>, String> keyInheritanceSubtypes;
    private final MapInstantiatorImplementor<?, ?> mapInstantiator;

    @SuppressWarnings("unchecked")
    public AbstractMethodMapAttribute(ManagedViewTypeImplementor<X> viewType, MethodAttributeMapping mapping, MetamodelBuildingContext context, int attributeIndex, int dirtyStateIndex, EmbeddableOwner embeddableMapping) {
        super(viewType, mapping, context, attributeIndex, dirtyStateIndex, embeddableMapping);
        this.keyType = (Type<K>) mapping.getKeyType(context, embeddableMapping);
        this.keyInheritanceSubtypes = (Map<ManagedViewType<? extends K>, String>) (Map<?, ?>) mapping.getKeyInheritanceSubtypes(context, embeddableMapping);
        this.mapInstantiator = createMapInstantiator(context, createMapFactory(context), isSorted(), isOrdered(), getComparator());
    }

    @Override
    public Type<K> getKeyType() {
        return keyType;
    }

    @Override
    public Map<ManagedViewType<? extends K>, String> getKeyInheritanceSubtypeMappings() {
        return keyInheritanceSubtypes;
    }

    @SuppressWarnings("unchecked")
    protected Map<ManagedViewTypeImplementor<?>, String> keyInheritanceSubtypeMappings() {
        return (Map<ManagedViewTypeImplementor<?>, String>) (Map<?, ?>) keyInheritanceSubtypes;
    }

    @Override
    public boolean isKeySubview() {
        return keyType.getMappingType() != Type.MappingType.BASIC;
    }

    @Override
    public CollectionType getCollectionType() {
        return CollectionType.MAP;
    }

    @Override
    public CollectionInstantiatorImplementor<?, ?> getCollectionInstantiator() {
        throw new UnsupportedOperationException("Map attribute");
    }

    @Override
    public MapInstantiatorImplementor<?, ?> getMapInstantiator() {
        return mapInstantiator;
    }

    @Override
    public boolean isIndexed() {
        return true;
    }

}
