package com.mihailojoksimovic.service;

import com.mihailojoksimovic.model.Point;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.HashMap;

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

        MongoCollection mongoCollection = MongoManager.getDatabase().getCollection(COLLECTION_NAME);

        int counter = 0;

        for (Point p : points) {
            try {
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
                }
            } catch (NullPointerException ex) {
                int b = 1;
            }
        }

        return results;
    }
}
