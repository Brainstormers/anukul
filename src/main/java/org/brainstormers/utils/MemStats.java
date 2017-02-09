package org.brainstormers.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Memory statistics for program instrumentation
 *
 */
public class MemStats {
	private static final Logger log = LoggerFactory.getLogger(MemStats.class);
    public static long prevHeapSize = 0;

    /**
     * print out some statistics about heap size
     */
    public static void memStats ()  {
    // Get current size of heap in bytes
    long heapSize = MemoryUtils.totalMemory();

    // Get maximum size of heap in bytes. The heap cannot grow beyond this size.
// Any attempt will result in an OutOfMemoryException.
    long heapMaxSize = MemoryUtils.maxMemory();

    // Get amount of free memory within the heap in bytes. This size will increase
// after garbage collection and decrease as new objects are created.
    long heapFreeSize = MemoryUtils.freeMemory();
    long diff = heapSize - prevHeapSize;
    prevHeapSize = heapSize;
    log.info("Heap "+heapSize+" MaxSize "+heapMaxSize+" Free "+heapFreeSize+" Diff "+diff);

    }

}
