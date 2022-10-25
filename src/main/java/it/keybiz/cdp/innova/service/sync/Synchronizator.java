package it.keybiz.cdp.innova.service.sync;

interface Synchronizator {
    void sync();

    void cleanupDatabase();

    void cleanupImages();
}
