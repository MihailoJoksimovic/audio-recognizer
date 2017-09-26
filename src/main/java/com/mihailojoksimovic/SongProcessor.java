package com.mihailojoksimovic;

import com.mihailojoksimovic.service.CantExtractSamplesException;
import com.mihailojoksimovic.service.FingerprintExtractor;
import com.mihailojoksimovic.service.MongoManager;
import com.mihailojoksimovic.service.SamplesExtractor;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Song Processor takes list of Songs and extracts fingerprints
 * from them.
 *
 *
 */
public class SongProcessor {
    /**
     * Mongo Collection name
     *
     * TODO: Has to be configured globally
     */
    private final String COLLECTION_NAME                = "test";

    public static void main(String[] args) throws Exception{
        if (args.length < 1) {
            throw new Exception("Missing path to song or songs folder!");
        }

        File file;

        try {
            file = new File(args[0]);
        } catch (NullPointerException ex) {
            throw new Exception("Invalid file path provided!");
        }

        SongProcessor sp = new SongProcessor();

        sp.processFile(file);

        return;
    }

    public void processFile(File file) {
        if (file.isDirectory()) {
            processDirectory(file);

            return;
        }

        AudioInputStream in;

        try {
            in         = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("File "+file+" doesn't seem to be an audio file! Skipping it ...");

            return;
        } catch (IOException ex) {
            System.out.println("File "+file+" can't be opened! Skipping it ...");

            return;
        }

        AudioFormat baseFormat      = in.getFormat();
        AudioFormat decodedFormat;

        // Sometimes, we don't manage to extract all info about the underlying file.
        // If that's the case, try & fallback to default values

        if (baseFormat.getSampleSizeInBits() == AudioSystem.NOT_SPECIFIED) {
            // Fall-back to default MP3 encoding
            decodedFormat   = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels()*2,
                    baseFormat.getSampleRate(),
                    false
            );
        } else {
            // Well file was decoded properly!
            decodedFormat = baseFormat;
        }

        DataLine.Info info      = new DataLine.Info(SourceDataLine.class, decodedFormat);

        // Get that audio stream!!
        AudioInputStream din    = AudioSystem.getAudioInputStream(decodedFormat, in);

        try {
            System.out.println("Processing song: "+file);

            short[] samples             = SamplesExtractor.getInstance().extractSamplesFromStream(din);

            double[] fingerprints       = FingerprintExtractor.getInstance().extractFingerprints(samples, decodedFormat);

            MongoCollection collection  = MongoManager.getDatabase().getCollection(COLLECTION_NAME);

            storeFingerprintsToDb(fingerprints, file, collection);
        } catch (CantExtractSamplesException ex) {
            System.out.println("I was unable to extract samples from "+file+" ! Skipping it ...");

            return;
        }

    }

    private void processDirectory(File directory) {
        for (File f : directory.listFiles()) {
            // Recurse me, baby!
            processFile(f);
        }
    }

    private void storeFingerprintsToDb(double[] fingerprints, File file, MongoCollection collection) {
        String song = file.getName();
        int timer   = 0;

        for (int i = 0; i < fingerprints.length; i++) {
            Document d = new Document();

            if (i + 1 == fingerprints.length) {
                break;
            }

            d.append("song", song);
            d.append("hash", fingerprints[i]);
            d.append("next_hash", fingerprints[i+1]);
            d.append("time", timer++);

            try {
                collection.insertOne(d);
            } catch (MongoWriteException ex) {
                // Who cares ...
            }
        }
    }

}
