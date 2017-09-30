package com.mihailojoksimovic;

import com.mihailojoksimovic.service.CantExtractSamplesException;
import com.mihailojoksimovic.service.FingerprintExtractor;
import com.mihailojoksimovic.service.MongoManager;
import com.mihailojoksimovic.service.SamplesExtractor;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import org.apache.commons.cli.*;
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

        CommandLine commandLine;

        Options options = new Options();

        options.addOption("song", true, "[REQUIRED] File or Folder to be processed");
        options.addOption("print", false, "Prints the fingerprints");
        options.addOption("save", false, "If present, stores fingerprints to DB");

//        Option song = OptionBuilder.create();
//        song.setRequired(true);
//        song.setLongOpt("song");
//        song.setType(String.class);
//        song.setDescription("Song name");
//
//        Option printFingerprints = OptionBuilder.create();
//        printFingerprints.setRequired(false);
//        printFingerprints.setLongOpt("print-fingerprint");
//        printFingerprints.setDescription("If set to true, will print all fingerprints from song");
//        printFingerprints.setType(Boolean.class);
//
//        Options options = new Options();
//
//        options.addOption(song);
//        options.addOption(printFingerprints);

        CommandLineParser parser = new GnuParser();

        try {
            commandLine = parser.parse(options, args);

            if (!commandLine.hasOption("song")) {
                // Print usage info
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("SongProcessor", options);

                return;
            }

            File file = new File(commandLine.getOptionValue("song"));

            SongProcessor sp = new SongProcessor();

            sp.processFile(file, commandLine);
        } catch (ParseException ex) {
            System.out.println("Unable to parse command line arguments!");
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            throw new Exception("Invalid file path provided!");
        }
    }

    public void processFile(File file, CommandLine commandLine) {
        if (file.isDirectory()) {
            processDirectory(file, commandLine);

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

            if (commandLine.hasOption("write")) {
                System.out.println("Storing fingerprints to DB");

                MongoCollection collection  = MongoManager.getDatabase().getCollection(COLLECTION_NAME);

                storeFingerprintsToDb(fingerprints, file, collection);
            }

            if (commandLine.hasOption("print")) {
                for (double d : fingerprints) {
                    System.out.print((int)d+",");
                }
                System.out.println();
            }

        } catch (CantExtractSamplesException ex) {
            System.out.println("I was unable to extract samples from "+file+" ! Skipping it ...");

            return;
        }

    }

    private void processDirectory(File directory, CommandLine commandLine) {
        for (File f : directory.listFiles()) {
            // Recurse me, baby!
            processFile(f, commandLine);
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
