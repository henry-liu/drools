/*
 * Copyright 2010 JBoss Inc
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

package org.drools.core.util;

import java.util.NoSuchElementException;

import org.drools.core.common.InternalFactHandle;
import org.drools.core.util.ObjectHashMap.ObjectEntry;
import org.kie.runtime.ObjectFilter;

public class JavaIteratorAdapter
    implements
    java.util.Iterator {
    public static int    OBJECT = 0;
    public static int    FACT_HANDLE = 1;

    private Iterator     iterator;
    private ObjectEntry  nextEntry;
    //    private Object  nextObject;
    //    private InternalFactHandle  nextHandle;
    private ObjectFilter filter;
    private int          type;

    public JavaIteratorAdapter(Iterator iterator,
                               int type) {
        this( iterator,
              type,
              null );
    }

    public JavaIteratorAdapter(Iterator iterator,
                               int type,
                               ObjectFilter filter) {
        this.iterator = iterator;
        this.filter = filter;
        this.type = type;
        setNext();
    }

    public boolean hasNext() {
        return (this.nextEntry != null);
    }

    public Object next() {
        ObjectEntry current = this.nextEntry;

        if ( current != null ) {
            setNext();
        } else {
            throw new NoSuchElementException( "No more elements to return" );
        }

        if ( this.type == OBJECT ) {
            return ((InternalFactHandle) current.getKey()).getObject();
        } else {
            return current.getKey();
        }
    }

    private void setNext() {
        ObjectEntry entry = null;

        while ( entry == null ) {
            entry = (ObjectEntry) this.iterator.next();
            if ( entry == null ) {
                break;
            }
            if ( this.filter != null ) {
                Object object = ((InternalFactHandle) entry.getKey()).getObject();
                if ( this.filter.accept( object ) == false ) {
                    entry = null;
                }
            }
        }

        this.nextEntry = entry;
        //        this.nextHandle = handle;
        //        this.nextObject = object;
    }

    public void remove() {
        throw new UnsupportedOperationException( "remove() is not support" );
    }
}
