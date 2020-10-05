/*
 * Ultralight Java - Java wrapper for the Ultralight web engine
 * Copyright (C) 2020 LabyMedia and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.labymedia.ultralight;

import net.labymedia.ultralight.annotation.NativeCall;
import net.labymedia.ultralight.annotation.NativeType;
import net.labymedia.ultralight.annotation.Unsigned;
import net.labymedia.ultralight.ffi.ObjectWithHandle;
import net.labymedia.ultralight.ffi.RefPtr;

/**
 * The core of Ultralight. You should initialize it after setting up
 * your Platform config and drivers.
 * <p>
 * This singleton class manages the lifetime of all Views (@see View) and
 * coordinates all painting, rendering, network requests, and event dispatch.
 * <p>
 * TODO: Implement the AppCore API for Java
 */
// Not implemented yet:
// You don't have to create this instance directly if you use the
// AppCore API. The App class will automatically create a Renderer and
// perform all rendering within its run loop.
@NativeType("ultralight::RefPtr<ultralight::Renderer>")
public class UltralightRenderer implements ObjectWithHandle {
    /**
     * Create the Renderer singleton. You should set up all your Platform config,
     * file-system, and drivers before calling this function. @see Platform
     * <p>
     * You should only create one Renderer per application lifetime.
     *
     * @return A ref-pointer to a new Renderer instance. You should
     * assign it to either a Ref&lt;Renderer&gt; (non-nullable) or
     * RefPt&lt;Renderer&gt; (nullable).
     */
    public static native UltralightRenderer create();

    // C++ RefPtr of the object
    private final RefPtr<UltralightRenderer> ptr;

    /**
     * Constructs a new {@link UltralightRenderer} from the given native
     * {@link RefPtr}.
     *
     * @param ptr The pointer managing the renderer instance
     */
    @NativeCall
    private UltralightRenderer(RefPtr<UltralightRenderer> ptr) {
        this.ptr = ptr;
    }

    // TODO: Session API

    /**
     * Create a new View.
     *
     * @param width       The initial width, in pixels.
     * @param height      The initial height, in pixels.
     * @param transparent Whether or not the view background is transparent.
     * @return The created view
     */
    public UltralightView createView(
            @NativeType("uint32_t") @Unsigned long width,
            @NativeType("uint32_t") @Unsigned long height,
            boolean transparent
    ) {
        return createView(width, height, transparent, false);
    }

    /**
     * Create a new View.
     *
     * @param width       The initial width, in pixels.
     * @param height      The initial height, in pixels.
     * @param transparent Whether or not the view background is transparent.
     *
     * @return The created view
     */
    public native UltralightView createView(
            @NativeType("uint32_t") @Unsigned long width,
            @NativeType("uint32_t") @Unsigned long height,
            boolean transparent,
            boolean forceCpuRenderer
    );

    /**
     * Update timers and dispatch internal callbacks. You should call this often
     * from your main application loop.
     */
    public native void update();

    /**
     * Render all active views to display lists and dispatch calls to GPUDriver.
     */
    public native void render();

    /**
     * Attempt to release as much memory as possible. <b>Don't call this from any
     * callbacks or driver code.</b>
     */
    public native void purgeMemory();

    /**
     * Print detailed memory usage statistics to the log.
     */
    public native void logMemoryUsage();

    @Override
    public @NativeType("void *") @Unsigned long getHandle() {
        return ptr.getHandle();
    }
}
