/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018.  Georg Beier. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.geobe.tbp.core.dto

import de.geobe.util.vaadin.helper.IdProvider

/**
 * A Query Data Transfer Object that holds a list of NodeDtos to display in ListViews or similar
 * Created by georg beier on 09.01.2018.
 *
 */
class ListDto {
    LinkedHashMap<Tuple2<String, Long>, NodeDto> all = [:]

    Tuple2<String, Long> getFirstId() {
        if (all) {
            all.keySet().iterator().next()
        } else {
            [:]
        }
    }
}

/**
 * A Query Data Transfer Object that holds an identifying display string (for humans)
 * and the key of the underlying domain object and a map with lists of QNodes of all
 * kinds of related objects to display associations
 * @param <K>
 */
class NodeDto implements IdProvider<Tuple2<String, Serializable>> {
    Tuple2<String, Long> id = new Tuple2<String, Long>('', 0)
    String tag
    Map<String, List<NodeDto>> related = [:]

    @Override
    String toString() {
        def c = id.first.toString()?.charAt(0)
        "$c: ${this.@tag}"
    }

    def getType() {id.first}
}

/**
 * A Data Transfer Object for Query and Command operations that holds the key of the
 * underlying domain object, its typename, a map with all its attribute names and values and a map
 * with lists of QNodes of all related objects to display associations
 */
class FullDto {
    Tuple2<String, Long> id = new Tuple2<String, Long>('', 0)
    String tag
    LinkedHashMap<String, Object> args = [:]
    Map<String, List<NodeDto>> related = [:]
}

