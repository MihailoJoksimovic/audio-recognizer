package com.mihailojoksimovic;

import com.mihailojoksimovic.model.Peak;
import com.mihailojoksimovic.model.Point;
import com.mihailojoksimovic.service.*;
import sun.misc.IOUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by mihailojoksimovic on 10/15/17.
 */
public class FromTxtFile {
    public static void main(String[] args) throws FileNotFoundException, LineUnavailableException, IOException {
        String file = "/Users/mihailojoksimovic/Downloads/vlado-10s.pcm";

        File file1 = new File(file);

        FileInputStream fileInputStream = new FileInputStream(file1);

        ByteBuffer byteBuffer = ByteBuffer.allocate(11025*20).order(ByteOrder.LITTLE_ENDIAN);

        int pos = 0;

        while (true) {
            byte[] buffer = new byte[1024];

            int n = fileInputStream.read(buffer, 0, buffer.length);

            if (n==-1) {
                break;
            }

            byteBuffer.put(buffer);
        }

//        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();

//        ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

//        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();

//        short[] amplitudes = shortBuffer.array();




        AudioFormat audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                11025,
                16,
                1,
                2,
                11025,
                false
        );

//        for (int i = 0; i+buffer.length < wholeSong.length; i++) {
//            System.arraycopy(wholeSong, i, buffer, 0, buffer.length);
//
//            PlayerService.getInstance().play(data, audioFormat);
//        }
//
//
//        System.out.println("Starting to play now");

        short[] amplitudes = new short[11025 * 10];

        byteBuffer.rewind();


        for (int i = 0, idx = 0; i < byteBuffer.capacity() / 2; i++, idx++) {
            short amp = byteBuffer.getShort();

            System.out.println(amp);

            amplitudes[idx] = amp;
        }

//        amplitudes = AudioCutter.cutAudio(amplitudes, audioFormat, 0, 50);

        double[][] timeFrequencyBins = TimeToFrequencyDomainConverter.getInstance().convertToFrequencyDomain(amplitudes, audioFormat, 25);

        PeakExtractor peakExtractor = new PeakExtractor();

        Peak[] peaks = peakExtractor.extractPeaks(timeFrequencyBins);

//        for (Peak p : peaks) {
//            if (p != null) {
//                System.out.println(p.getTimeBin()+"\t\t"+p.getFrequencyBin());
//            }
//
//        }

        Point[] points = PointsFromPeaksCreator.makePointsFromPeaks(peaks, 3);

        // Now query the DB nigga!

        HashMap<String, Integer> matches = MongoService.findMatches(points);


        Iterator<HashMap.Entry<String,Integer>> it = matches.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = it.next();

            System.out.println(entry.getKey() + ": " + entry.getValue()+" matches");
        }





//        short[] data    = shortBuffer.order(ByteOrder.LITTLE_ENDIAN);

//        short[] data = shortBuffer.array();

//        PlayerService.getInstance().play(amplitudes, audioFormat);
    }
}
