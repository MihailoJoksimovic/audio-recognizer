package com.mihailojoksimovic.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Sorts.*;

import java.util.HashMap;

public class MatcherService {
    private static MatcherService instance;

    private final String COLLECTION_NAME                = "test";

    private final int NUM_NEIGHBOUR_BINS_TO_CONSIDER    = 5;

    private final int MIN_HITS_TO_CONSIDER_A_MATCH      = 20;

    public static MatcherService getInstance() {
        if (instance == null) {

            // TODO: How to do dependency injection?

            instance = new MatcherService();
        }

        return instance;
    }

    /**
     * Match a song against list of Fingerprints
     * @param fingerprints
     * @return
     */
    public HashMap<String,Integer> matchSong(double[] fingerprints) {
        HashMap<String,Integer> matches = new HashMap<>();
        HashMap<String,Integer> times   = new HashMap<>();

        MongoDatabase mongoDatabase = MongoManager.getDatabase();
        MongoCollection collection = mongoDatabase.getCollection(COLLECTION_NAME);

        int timeRef = 0;

        for (int i = 0; i < fingerprints.length; i++) {
            if ((i+1) == fingerprints.length) {
                // Did we hit the end ? Break if so
                break;
            }

            // Find better way to filter distincts
            HashMap<String, Boolean> distinctSongs = new HashMap<>();

            double hash     = fingerprints[i];

            double nextHash = fingerprints[i+1];

            // Get list of songs that have this exact match
            MongoCursor<Document> cursor = collection.find(
                    Filters.and(
                            Filters.eq("hash", hash),
                            Filters.eq("next_hash", nextHash)
                    )
            ).sort(Sorts.ascending("time")).iterator();

            if (!cursor.hasNext()) {
                // No matches for this hash. Try luck with next one ...
                continue;
            }

            // Iterate through match list, and increase as we go
            while (cursor.hasNext()) {
                Document point  = cursor.next();

                String song     = point.getString("song");
                int time        = point.getInteger("time");

                if (distinctSongs.containsKey(song)) {
                    continue;
                }

                // Do we have this song if times hashtable? If so, compare the times!

                if (!times.containsKey(song)) {
                    times.put(song, time);
                }

                if (times.get(song) > time) {
                    continue;
                }

                distinctSongs.put(song, true);
                times.put(song, time);

                // Well we've got a match, so add it to matches hashmap
                if (!matches.containsKey(song)) {
                    matches.put(song, 0);
                }

                int hits    = matches.get(song);

                System.out.println("Increasing hits for song "+song+" to "+hits+"; hit found for hash "+hash+", "+nextHash+" at time "+time);

                matches.put(song, ++hits);
            }
        }

        return matches;
    }
}
