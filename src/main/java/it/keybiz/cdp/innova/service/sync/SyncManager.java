package it.keybiz.cdp.innova.service.sync;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
public class SyncManager {
    private final TreeMap<String, Synchronizator> synchronizators;

    public SyncManager(Map<String, Synchronizator> synchronizators) {
        this.synchronizators = new TreeMap<>(synchronizators); // per ordinare
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "${cognistreamer.sync-cron}")
    @SchedulerLock(name = "cognistreamer-sync", lockAtLeastFor = "15m")
    public void startSync() {
        log.info("Starting synchronization from APIs");
        StopWatch stopWatch = new StopWatch("Synchronizer");

        log.info("Start sync cognistreamer data");
        stopWatch.start("Sync data from APIs");
        synchronizators.forEach((k, v) -> v.sync());
        stopWatch.stop();
        log.info("Ended sync cognistreamer data.");

        log.info("Cleaning up non processed entities");
        stopWatch.start("Cleanup Database");
        synchronizators.descendingMap().forEach((k, v) -> v.cleanupDatabase());
        stopWatch.stop();
        log.info("Database cleaned");

        log.info("Synchronization finished in {} seconds.", stopWatch.getTotalTimeSeconds());
        log.info(stopWatch.prettyPrint());
    }

    @Async
    @Scheduled(cron = "${cognistreamer.clean-images-cron}")
    @SchedulerLock(name = "cognistreamer-clean-images", lockAtLeastFor = "15m")
    public void cleanOrphanImages() {
        log.info("Cleaning up orphan images");

        StopWatch stopWatch = new StopWatch("Synchronizer");
        stopWatch.start("Clean images");

        synchronizators.forEach((k, v) -> v.cleanupImages());

        stopWatch.stop();
        log.info("Cleaning images finished in {} seconds.", stopWatch.getTotalTimeSeconds());
        log.info(stopWatch.prettyPrint());
    }
}
