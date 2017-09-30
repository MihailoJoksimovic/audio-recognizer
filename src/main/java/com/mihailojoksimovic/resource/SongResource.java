package com.mihailojoksimovic.resource;


import com.mihailojoksimovic.service.MatcherService;
import com.mihailojoksimovic.util.MapUtil;

import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;


/**
 * Root resource (exposed at "request" path)
 *
 * Represents a SongResource for identifying the song.
 */
@Path("song")
@Produces(MediaType.APPLICATION_JSON)
public class SongResource {
    /**
     *
     * @return
     */
    @GET
    public String gotIt() {
        return "Got it!";
    }

    /**
     *
     * @return
     */
    @GET
    @Path("match/{fingerprints:.+}")
    public Response match(@PathParam("fingerprints") String fingerprintsCsv) {
        List<Double> list  = new ArrayList<>();

        for (String fingerprint : fingerprintsCsv.split(",")) {
            list.add(Double.parseDouble(fingerprint));
        }

        double[] fingerprints   = new double[list.size()];

        int i = 0;

        for (double d : list) {
            fingerprints[i++] = d;
        }

        HashMap matches = MatcherService.getInstance().matchSong(fingerprints);

        JsonArrayBuilder songs = Json.createArrayBuilder();

        Iterator<Map.Entry> it = matches.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = it.next();

            songs.add(
                    Json.createObjectBuilder().add("name", entry.getKey().toString()).add("hits", entry.getValue().toString()).build()
            );
        }

        JsonObject response = Json.createObjectBuilder().add("status", true).add("matches", songs.build()).build();


        return Response.status(200).entity(response).build();
    }

}
