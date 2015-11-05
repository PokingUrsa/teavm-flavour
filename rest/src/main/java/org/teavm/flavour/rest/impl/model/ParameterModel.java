/*
 *  Copyright 2015 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.flavour.rest.impl.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.teavm.model.ValueType;

/**
 *
 * @author Alexey Andreev
 */
public class ParameterModel implements Cloneable {
    String name;
    int index;
    ValueType type;
    List<PropertyModel> pathToValue = new ArrayList<>();
    Usage usage;
    private List<PropertyModel> readonlyPathToValue = Collections.unmodifiableList(pathToValue);

    ParameterModel() {
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public ValueType getType() {
        return type;
    }

    public Usage getUsage() {
        return usage;
    }

    public List<PropertyModel> getPathToValue() {
        return readonlyPathToValue;
    }

    @Override
    public ParameterModel clone() {
        try {
            ParameterModel copy = (ParameterModel) super.clone();
            copy.pathToValue = new ArrayList<>(pathToValue);
            copy.readonlyPathToValue = Collections.unmodifiableList(copy.readonlyPathToValue);
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}