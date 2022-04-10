package com.didi.libnetwork;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.didi.libnetwork.cache.Cache;
import com.didi.libnetwork.cache.CacheDao;
import com.didi.libnetwork.cache.CacheDatabase;

import java.io.IOException;

import kotlin.jvm.Throws;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private CacheDao cacheDao;
    private CacheDatabase database;

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Before
    public void createDb() {
        // Context of the app under test.
        database = CacheDatabase.Companion.getDatabase();
        cacheDao = database.cacheDao();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void writeCacheAndRead() {
        /*Cache cache = new Cache("1234", "data1");
        cacheDao.save(cache);
        Cache cache1 = cacheDao.getCache("1234");
        Log.d("zangjin", cache1.getData());*/
    }
}