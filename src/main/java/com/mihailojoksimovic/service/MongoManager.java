package com.mihailojoksimovic.service;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoManager {
    private static MongoClient instance;

    public static MongoClient getClient() {
        if (instance != null) {
            return  instance;
        }

        instance = new MongoClient("localhost");

        return instance;
    }

    public static MongoDatabase getDatabase() {
        return getClient().getDatabase("shabaan");
    }
}
