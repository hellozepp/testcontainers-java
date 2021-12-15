/*
 * MIT License
 *
 * Copyright (c) 2021-present HiveMQ GmbH
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
package org.testcontainers.hivemq;

import com.google.common.collect.ImmutableList;
import com.hivemq.extension.sdk.api.ExtensionMain;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Yannick Weber
 */
public class HiveMQExtension {

    private final @NotNull String id;
    private final @NotNull String name;
    private final @NotNull String version;
    private final int priority;
    private final int startPriority;
    private final boolean disabledOnStartup;
    private final @NotNull Class<? extends ExtensionMain> mainClass;
    private final @NotNull ImmutableList<Class<?>> additionalClasses;

    private HiveMQExtension(
            final @NotNull String id,
            final @NotNull String name,
            final @NotNull String version,
            final int priority,
            final int startPriority,
            final boolean disabledOnStartup,
            final @NotNull Class<? extends ExtensionMain> mainClass,
            final @NotNull ImmutableList<Class<?>> additionalClasses) {

        this.id = id;
        this.name = name;
        this.version = version;
        this.priority = priority;
        this.startPriority = startPriority;
        this.disabledOnStartup = disabledOnStartup;
        this.mainClass = mainClass;
        this.additionalClasses = additionalClasses;
    }

    public @NotNull String getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getVersion() {
        return version;
    }

    public int getPriority() {
        return priority;
    }

    public int getStartPriority() {
        return startPriority;
    }

    public boolean isDisabledOnStartup() {
        return disabledOnStartup;
    }

    public @NotNull Class<? extends ExtensionMain> getMainClass() {
        return mainClass;
    }

    public @NotNull ImmutableList<Class<?>> getAdditionalClasses() {
        return additionalClasses;
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @Nullable String id;
        private @Nullable String name;
        private @Nullable String version;
        private int priority = 0;
        private int startPriority = 0;
        private boolean disabledOnStartup = false;
        private @Nullable Class<? extends ExtensionMain> mainClass;
        private final @NotNull ImmutableList.Builder<Class<?>> additionalClassesBuilder = ImmutableList.builder();

        public HiveMQExtension build() {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("extension id must not be null or empty");
            }
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("extension name must not be null or empty");
            }
            if (version == null || version.isEmpty()) {
                throw new IllegalArgumentException("extension version must not be null or empty");
            }
            if (mainClass == null) {
                throw new IllegalArgumentException("extension main class must not be null");
            }
            return new HiveMQExtension(
                    id,
                    name,
                    version,
                    priority,
                    startPriority,
                    disabledOnStartup,
                    mainClass,
                    additionalClassesBuilder.build()
            );
        }

        public @NotNull Builder id(final @NotNull String id) {
            this.id = id;
            return this;
        }

        public @NotNull Builder name(final @NotNull String name) {
            this.name = name;
            return this;
        }

        public @NotNull Builder version(final @NotNull String version) {
            this.version = version;
            return this;
        }

        public @NotNull Builder priority(final int priority) {
            this.priority = priority;
            return this;
        }

        public @NotNull Builder startPriority(final int startPriority) {
            this.startPriority = startPriority;
            return this;
        }

        public @NotNull Builder disabledOnStartup(final boolean disabledOnStartup) {
            this.disabledOnStartup = disabledOnStartup;
            return this;
        }

        public @NotNull Builder mainClass(final @NotNull Class<? extends ExtensionMain> mainClass) {
            this.mainClass = mainClass;
            return this;
        }

        public @NotNull Builder addAdditionalClass(final @NotNull Class<?> clazz) {
            this.additionalClassesBuilder.add(clazz);
            return this;
        }
    }
}