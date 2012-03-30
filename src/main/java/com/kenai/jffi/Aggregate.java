/*
 * Copyright (C) 2009 Wayne Meissner
 *
 * This file is part of jffi.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 
 * Alternatively, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.kenai.jffi;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Aggregate extends Type {
    /** The FFI type of this type */
    private final int type;
    /** The size in bytes of this type */
    private final int size;

    /** The minimum alignment of this type */
    private final int align;

    /** The address of this type's ffi_type structure */
    private final long handle;

    /** A handle to the foreign interface to keep it alive as long as this object is alive */
    private final Foreign foreign;

    Aggregate(Foreign foreign, long handle) {
        if (handle == 0L) {
            throw new NullPointerException("Invalid ffi_type handle");
        }
        this.foreign = foreign;
        this.handle = handle;
        this.type = foreign.getTypeType(handle);
        this.size = foreign.getTypeSize(handle);
        this.align = foreign.getTypeAlign(handle);
    }
    
    final long handle() {
        return handle;
    }

    public final int type() {
        return type;
    }

    public final int size() {
        return size;
    }

    public final int alignment() {
        return align;
    }

    public synchronized final void dispose() {}

    @Override
    protected void finalize() throws Throwable {
        try {
            foreign.freeAggregate(handle);
        } catch (Throwable t) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, 
                    "Exception when freeing FFI aggregate: %s", t.getLocalizedMessage());
        } finally {
            super.finalize();
        }
    }
}
