/*
 * MIT License
 *
 * Copyright (c) 2021 Imanity
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

package org.fairy;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fairy.aspect.AsyncAspect;
import org.fairy.bean.BeanContext;
import org.fairy.bean.details.SimpleBeanDetails;
import org.fairy.cache.CacheableAspect;
import org.fairy.config.BaseConfiguration;
import org.fairy.library.Library;
import org.fairy.library.LibraryHandler;
import org.fairy.plugin.PluginManager;
import org.fairy.task.ITaskScheduler;
import org.fairy.util.terminable.composite.CompositeClosingException;
import org.fairy.util.terminable.composite.CompositeTerminable;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class FairyBootstrap {

    public static FairyBootstrap INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger();

    private final FairyPlatform platform;
    private final ITaskScheduler taskScheduler;
    private final CompositeTerminable compositeTerminable;
    private final AtomicBoolean loadedDependencies;

    private BaseConfiguration baseConfiguration;
    private LibraryHandler libraryHandler;
    private BeanContext beanContext;

    public FairyBootstrap(FairyPlatform platform) {
        INSTANCE = this;

        this.platform = platform;
        this.taskScheduler = this.platform.createTaskScheduler();

        this.compositeTerminable = CompositeTerminable.create();
        this.loadedDependencies = new AtomicBoolean();
    }

    public void enable() {
        this.loadDependencies();
        this.loadBindable();

        this.baseConfiguration = new BaseConfiguration();
        this.baseConfiguration.loadAndSave();

        this.beanContext = new BeanContext();
        this.beanContext.registerBean(new SimpleBeanDetails(this, "fairyBootstrap", this.getClass()));
        this.beanContext.init();
    }

    public void disable() {
        try {
            this.compositeTerminable.close();
        } catch (CompositeClosingException ex) {
            ex.printStackTrace();
        }

        this.beanContext.stop();
        PluginManager.INSTANCE.callFrameworkFullyDisable();
    }

    private void loadBindable() {
        this.bind(AsyncAspect.EXECUTOR);
        this.bind(CacheableAspect.CLEANER_SERVICE);
        this.bind(CacheableAspect.UPDATER_SERVICE);
    }

    public void loadDependencies() {
        if (!this.loadedDependencies.compareAndSet(false, true)) {
            return;
        }

        LOGGER.info("Loading Dependencies...");
        this.libraryHandler = new LibraryHandler();

        Set<Library> main = ImmutableSet.of(
                // SQL
                Library.MARIADB_DRIVER,
                Library.HIKARI,
                Library.MYSQL_DRIVER,
                Library.POSTGRESQL_DRIVER,

                // MONGO
                Library.MONGO_DB_SYNC,
                Library.MONGO_DB_CORE,
                Library.MONGOJACK,
                Library.BSON,

                // Caffeine
                Library.CAFFEINE,

                // Spring
                Library.SPRING_CORE,
                Library.SPRING_EL
        );
        this.libraryHandler.downloadLibraries(true, main);

        Set<Library> isolated = ImmutableSet.of(
                Library.H2_DRIVER
        );
        this.libraryHandler.downloadLibraries(false, isolated);

        final Set<Library> platformDependencies = this.platform.getDependencies();
        if (!platformDependencies.isEmpty()) {
            this.libraryHandler.downloadLibraries(true, platformDependencies);
        }

        Fairy.getLibraryHandler().downloadLibraries(true, Library.REDISSON);
    }

    public <T extends AutoCloseable> T bind(T t) {
        return this.compositeTerminable.bind(t);
    }

    public AutoCloseable bind(ExecutorService executorService) {
        return this.bind(executorService::shutdown);
    }

}
