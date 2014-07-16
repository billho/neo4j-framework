package com.graphaware.runtime.manager;

import com.graphaware.runtime.config.RuntimeConfiguration;
import com.graphaware.runtime.metadata.DefaultTimerDrivenModuleMetadata;
import com.graphaware.runtime.metadata.ModuleMetadataRepository;
import com.graphaware.runtime.metadata.TimerDrivenModuleMetadata;
import com.graphaware.runtime.module.TimerDrivenModule;
import com.graphaware.runtime.schedule.RotatingTaskScheduler;
import com.graphaware.runtime.schedule.TaskScheduler;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Production implementation of {@link TimerDrivenModuleManager}. Must be backed by a {@link GraphDatabaseService},
 * as there is no support for using {@link TimerDrivenModule}s in batch mode (i.e. with {@link org.neo4j.unsafe.batchinsert.BatchInserter}s).
 */
public class ProductionTimerDrivenModuleManager extends BaseModuleManager<TimerDrivenModuleMetadata, TimerDrivenModule> implements TimerDrivenModuleManager {

    private final GraphDatabaseService database;
    private final TaskScheduler taskScheduler;

    /**
     * Construct a new manager.
     *
     * @param database           storing graph data.
     * @param metadataRepository for storing module metadata.
     * @param runtimeConfig      the {@link RuntimeConfiguration} of the Runtime in which this module manager works.
     */
    public ProductionTimerDrivenModuleManager(GraphDatabaseService database, ModuleMetadataRepository metadataRepository, RuntimeConfiguration runtimeConfig) {
        super(metadataRepository);
        this.database = database;
        taskScheduler = new RotatingTaskScheduler(database, metadataRepository, runtimeConfig.provideTimingStrategy());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TimerDrivenModuleMetadata createFreshMetadata(TimerDrivenModule module) {
        return new DefaultTimerDrivenModuleMetadata(module.createInitialContext(database));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TimerDrivenModuleMetadata acknowledgeMetadata(TimerDrivenModule module, TimerDrivenModuleMetadata metadata) {
        taskScheduler.registerModuleAndContext(module, metadata.lastContext());
        return metadata;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startModules() {
        taskScheduler.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdownModules() {
        super.shutdownModules();
        taskScheduler.stop();
    }
}
