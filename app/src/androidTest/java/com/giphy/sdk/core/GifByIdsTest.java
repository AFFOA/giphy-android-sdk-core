package com.giphy.sdk.core;

import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.api.GiphyApiClient;
import com.giphy.sdk.core.network.response.GifResponse;
import com.giphy.sdk.core.network.response.MultipleGifsResponse;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by bogdantmm on 4/21/17.
 */

public class GifByIdsTest {
    GiphyApiClient imp;

    @Before
    public void setUp() throws Exception {
        imp = new GiphyApiClient("dc6zaTOxFJmzC");
    }

    /**
     * Test if gif is returned using id
     * @throws Exception
     */
    @Test
    public void testBase() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);

        final List<String> gifIds = new ArrayList<>();
        gifIds.add("GSotmi2t5hEA");
        gifIds.add("darAMUceRAs0w");
        gifIds.add("l4FGF1Lk3GibtKchO");

        imp.gifByIds(gifIds, new CompletionHandler<MultipleGifsResponse>() {
            @Override
            public void onComplete(Throwable e, MultipleGifsResponse result) {
                lock.countDown();

                Assert.assertNull(e);
                Assert.assertNotNull(result);
                Assert.assertTrue(result.gifs.size() == gifIds.size());
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * Test if the gifs returned have the same id as the ones requested
     * @throws Exception
     */
    @Test
    public void testIdsMatch() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);

        final List<String> gifIds = new ArrayList<>();
        gifIds.add("GSotmi2t5hEA");
        gifIds.add("darAMUceRAs0w");
        gifIds.add("l4FGF1Lk3GibtKchO");

        imp.gifByIds(gifIds, new CompletionHandler<MultipleGifsResponse>() {
            @Override
            public void onComplete(Throwable e, MultipleGifsResponse result) {
                lock.countDown();

                Assert.assertNull(e);
                Assert.assertNotNull(result);
                Assert.assertTrue(result.gifs.size() == gifIds.size());
                for (int i = 0; i < gifIds.size(); i ++) {
                    Assert.assertEquals(result.gifs.get(i).id, gifIds.get(i));
                }
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
    }

    /**
     * Test the response when some ids are not found
     * @throws Exception
     */
    @Test
    public void testGifNotFound() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);

        final List<String> gifIds = new ArrayList<>();
        gifIds.add("GSotmi2t5hEA");
        gifIds.add("darAMUceRAs0w_ttttttttt");
        gifIds.add("l4FGF1Lk3GibtKchO");

        imp.gifByIds(gifIds, new CompletionHandler<MultipleGifsResponse>() {
            @Override
            public void onComplete(Throwable e, MultipleGifsResponse result) {
                lock.countDown();

                Assert.assertNull(e);
                Assert.assertNotNull(result);
                Assert.assertTrue(result.gifs.size() == gifIds.size() - 1);
                Assert.assertEquals(result.gifs.get(0).id, gifIds.get(0));
                Assert.assertEquals(result.gifs.get(1).id, gifIds.get(2));
            }
        });
        lock.await(2000, TimeUnit.MILLISECONDS);
    }
}
