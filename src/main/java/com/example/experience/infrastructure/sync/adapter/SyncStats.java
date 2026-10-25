package com.example.experience.infrastructure.sync.adapter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 计数器,统计有多少sync,挂载在FetexContext上
 */
public final class SyncStats {

    private final AtomicLong eventsFetched = new AtomicLong();
    private final AtomicLong eventsInserted = new AtomicLong();
    private final AtomicLong mediaDownloaded = new AtomicLong();
    private final AtomicLong mediaBytes = new AtomicLong();

    public void addFetched(long count) {
        eventsFetched.addAndGet(count);
    }

    public void recordEventWrite(WriteResult result) {
        if (result == WriteResult.CREATED) {
            eventsInserted.incrementAndGet();
        }
    }

    public void recordMedia(long bytes) {
        mediaDownloaded.incrementAndGet();
        mediaBytes.addAndGet(bytes);
    }

    public long eventsFetched() {
        return eventsFetched.get();
    }

    public long eventsInserted() {
        return eventsInserted.get();
    }

    public long mediaDownloaded() {
        return mediaDownloaded.get();
    }

    public long mediaBytes() {
        return mediaBytes.get();
    }
}
