/*
 * Copyright 2014 Blazebit.
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

package com.blazebit.persistence.view.impl.metamodel;

import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.MappingParameter;
import com.blazebit.persistence.view.metamodel.MappingConstructor;
import com.blazebit.persistence.view.metamodel.ParameterAttribute;
import com.blazebit.persistence.view.metamodel.ViewType;
import com.blazebit.reflection.ReflectionUtils;
import java.lang.annotation.Annotation;

/**
 *
 * @author cpbec
 */
public class ParameterAttributeImpl<X, Y> implements ParameterAttribute<X, Y> {
    
    private final int index;
    private final MappingConstructor<X> declaringConstructor;
    private final Class<Y> javaType;
    private final String mapping;
    private final boolean mappingParameter;
    
    public ParameterAttributeImpl(MappingConstructor<X> constructor, int index) {
        this.index = index;
        this.declaringConstructor = constructor;
        this.javaType = (Class<Y>) constructor.getJavaConstructor().getParameterTypes()[index];
        
        Annotation[] annotations = constructor.getJavaConstructor().getParameterAnnotations()[index];
        Mapping mappingAnnotation = null;
        
        for (int i = 0; i < annotations.length; i++) {
            if (ReflectionUtils.isSubtype(annotations[i].annotationType(), Mapping.class)) {
                mappingAnnotation = (Mapping) annotations[i];
                break;
            }
        }
        
        if (mappingAnnotation == null) {
            MappingParameter mappingParameterAnnotation = null;
        
            for (int i = 0; i < annotations.length; i++) {
                if (ReflectionUtils.isSubtype(annotations[i].annotationType(), MappingParameter.class)) {
                    mappingParameterAnnotation = (MappingParameter) annotations[i];
                    break;
                }
            }
        
            if (mappingParameterAnnotation == null) {
                throw new IllegalArgumentException("No mapping annotation could be found for the parameter of the constructor '" + declaringConstructor.getJavaConstructor().toString() +  "' at the index '" + index + "'!");
            } else {
                this.mapping = mappingParameterAnnotation.value();
                this.mappingParameter = true;
            }
        } else {
            this.mapping = mappingAnnotation.value();
                this.mappingParameter = false;
        }
        
        if (mapping.isEmpty()) {
            throw new IllegalArgumentException("Illegal empty mapping for the parameter of the constructor '" + declaringConstructor.getJavaConstructor().toString() +  "' at the index '" + index + "'!");
        }
    }

    @Override
    public int getIndex() {
        return index;
    }
    
    @Override
    public boolean isMappingParameter() {
        return mappingParameter;
    }

    @Override
    public MappingConstructor<X> getDeclaringConstructor() {
        return declaringConstructor;
    }

    @Override
    public ViewType<X> getDeclaringType() {
        return declaringConstructor.getDeclaringType();
    }

    @Override
    public Class<Y> getJavaType() {
        return javaType;
    }

    @Override
    public String getMapping() {
        return mapping;
    }
    
}
