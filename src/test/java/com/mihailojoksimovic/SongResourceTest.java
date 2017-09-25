package com.mihailojoksimovic;

import com.mihailojoksimovic.resource.MyResource;
import com.mihailojoksimovic.resource.SongResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;

import static org.junit.Assert.assertEquals;

public class SongResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(SongResource.class);
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        final String responseMsg   = target().path("song/match/100,200").request().get(String.class);

//        final String responseMsg = target().path("myresource").request().get(String.class);

        assertEquals("Hello, Heroku!", responseMsg);
    }
}
