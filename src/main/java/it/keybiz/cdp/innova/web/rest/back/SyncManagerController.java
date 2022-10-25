package it.keybiz.cdp.innova.web.rest.back;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.keybiz.cdp.innova.service.sync.SyncManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "Synchronizator")
@Tag(name = "Synchronizator")
public class SyncManagerController {
    private final SyncManager syncManager;

    @GetMapping("/sync")
    @ApiOperation("Synchronize data with Cognistreamer APIs")
    public void sync() {
        syncManager.startSync();
    }

    @GetMapping("/clean")
    @ApiOperation("Clean image not referenced by entities")
    public void cleanImages() {
        syncManager.cleanOrphanImages();
    }
}
