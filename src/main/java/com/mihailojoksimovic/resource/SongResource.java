package com.mihailojoksimovic.resource;

import com.mihailojoksimovic.model.Peak;
import com.mihailojoksimovic.model.Point;
import com.mihailojoksimovic.service.*;

import javax.json.*;
import javax.sound.sampled.AudioFormat;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.ShortBuffer;
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
    @POST
    @Path("match-amplitude")
    public Response matchAgainstAmplitudes(@FormParam("amplitudes") String amplitudesCsv) {
        List<Short> list  = new ArrayList<>();

        int count           = 0;

        for (String amplitude : amplitudesCsv.split(",")) {
            list.add(Short.parseShort(amplitude));

            count++;
        }

        short[] amplitudeShorts = new short[count];

        Iterator it = list.iterator();

        for (int i = 0; i < count; i++) {
            amplitudeShorts[i] = list.get(i);
        }

        AudioFormat audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                11025,
                16,
                1,
                2,
                11025,
                false
        );

        double[][] timeFrequencyBins = TimeToFrequencyDomainConverter.getInstance().convertToFrequencyDomain(amplitudeShorts, audioFormat, 25);

        PeakExtractor peakExtractor = new PeakExtractor();

        Peak[] peaks = peakExtractor.extractPeaks(timeFrequencyBins);

        Point[] points = PointsFromPeaksCreator.makePointsFromPeaks(peaks, 3);

        HashMap<String, Integer> matches = MongoService.findMatches(points);


        Iterator<HashMap.Entry<String,Integer>> iter = matches.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry entry = iter.next();

            System.out.println(entry.getKey() + ": " + entry.getValue()+" matches");
        }


        return Response.status(200).entity(amplitudesCsv).build();
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
