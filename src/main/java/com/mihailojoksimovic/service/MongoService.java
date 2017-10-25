package com.mihailojoksimovic.service;

import com.mihailojoksimovic.model.Point;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by mihailojoksimovic on 10/17/17.
 */
public class MongoService {
    private static String COLLECTION_NAME = "songs";

    public static void storeToMongo(Point[] points) {
        MongoCollection mongoCollection = MongoManager.getDatabase().getCollection(COLLECTION_NAME);

        for (Point p : points) {
            Document doc = new Document();
            doc.put("point_a",      p.getPeak1().getFrequencyBin());
            doc.put("point_b",      p.getPeak2().getFrequencyBin());
            doc.put("delta_time",   p.getDeltaTimebin());
            doc.put("timebin",      p.getTimeBin());
            doc.put("song",         p.getSong());

            try {
                mongoCollection.insertOne(doc);
            } catch (MongoException ex) {
                // Who cares ...
            }

        }
    }

    public static HashMap<String,Integer> findMatches(Point[] points) {
        int matches = 0;

        HashMap<String, Integer> results = new HashMap<>();
        HashMap<String, HashMap<Integer, Integer>> results2 = new HashMap<>();

        MongoCollection mongoCollection = MongoManager.getDatabase().getCollection(COLLECTION_NAME);

        int counter = 0;

        for (Point p : points) {

            try {
                if (p.getPeak1().getFrequencyBin() == p.getPeak2().getFrequencyBin()) {
                    continue;
                }

                MongoCursor<Document> cursor = mongoCollection.find(Filters.and(
                        Filters.eq("point_a",       p.getPeak1().getFrequencyBin()),
                        Filters.eq("point_b",       p.getPeak2().getFrequencyBin()),
                        Filters.eq("delta_time",    p.getDeltaTimebin())
                )).iterator();

                if (!cursor.hasNext()) {
                    continue;
                }

                while (cursor.hasNext()) {
                    Document match  = cursor.next();

                    String song     = match.getString("song");

                    if (!results.containsKey(song)) {
                        results.put(song, 0);
                    }

                    results.put(song, (results.get(song) + 1));

                    // Calc. offsets
                    int deltaOffset = match.getInteger("timebin") - p.getTimeBin();

                    if (!results2.containsKey(song)) {
                        results2.put(song, new HashMap<Integer, Integer>());
                    }

                    if (!results2.get(song).containsKey(deltaOffset)) {
                        results2.get(song).put(deltaOffset, 0);
                    }

                    results2.get(song).put(deltaOffset, results2.get(song).get(deltaOffset) + 1);
                }
            } catch (NullPointerException ex) {
                int b = 1;
            }
        }

        String highestSong  = null;
        int highestMatches  = 0;

        Iterator it = results2.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, HashMap<Integer, Integer>> pair = (Map.Entry)it.next();

            Iterator it2 = pair.getValue().entrySet().iterator();

            while (it2.hasNext()) {
                Map.Entry<Integer, Integer> pair2 = (Map.Entry)it2.next();

                if (pair2.getValue() > highestMatches) {
                    highestMatches = pair2.getValue();

                    highestSong     = pair.getKey();
                }
            }
        }

        System.out.println("Time-wise best match seems to be: " + highestSong);

        Iterator it2    = results.entrySet().iterator();

        highestSong  = null;
        highestMatches  = 0;

        while (it2.hasNext()) {
            Map.Entry<String, Integer> pair = (Map.Entry)it2.next();

            if (pair.getValue() > highestMatches) {
                highestMatches  = pair.getValue();
                highestSong     = pair.getKey();
            }
        }

        System.out.println("Point-wise best match seems to be: " + highestSong);

        System.out.println("\n\n\n");

        return results;
    }
}
