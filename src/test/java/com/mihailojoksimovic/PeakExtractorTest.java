package com.mihailojoksimovic;

import com.mihailojoksimovic.service.PeakExtractor;
import com.mihailojoksimovic.service.TimeToFrequencyDomainConverter;
import junit.framework.*;
import org.junit.*;

/**
 * Created by mihailojoksimovic on 10/14/17.
 */
public class PeakExtractorTest extends TestCase {

    @org.junit.Test
    public void testVoidAssertTrue() {
        PeakExtractor peakExtractor = new PeakExtractor();

//        double[] amplitudes = new double[]{4904,2599.506493,2214.314527,3078.049431,165.0635966,2669.063859,2440.80507,1171.537677,617.192665,779.8130111,467.9162356,369.6783856,688.4620815,728.9524004,607.9263157,468.107255,486.0253792,502.3932898,39.39771962,298.9614084,166.1692719,260.2049743,114.9642637,186.1688513,333.2545416,78.53751522,858.8017696,1434.473041,726.2295808,369.7165935,137.0632786,843.9436891,734.0690992,382.513641,450.8261624,248.4663872,79.56347555,672.9205011,693.5659855,1714.546972,2268.813742,1633.291156,472.4014321,510.2249503,632.0863862,160.050593,570.6509671,712.9747253,314.5418181,174.8783142,78.02577201,467.5332382,616.0184738,1020.791067,1430.804216,605.5865306,1130.391887,874.0280055,1306.733074,470.1839003,731.6367299,679.5787359,1713.012447,1022.602261,861.0612749,818.5397153,676.0014269,846.3647376,202.4141678,99.89614842,127.6798999,202.8274529,589.5772458,228.735644,286.337897,96.98104086,213.8712568,37.80276456,208.9351977,102.5881293,179.3295351,239.0869223,365.9274096,60.298454,252.2682871,202.51191,295.1734011,208.0845681,404.3997503,53.80634025,283.8619971,64.2474348,306.3938351,137.961462,197.9841053,196.8617915,474.5073191,484.5985652,318.321574,362.7334727,297.549055,106.8115204,19.51661608,213.1199712,309.1679257,214.8900159,109.8699205,331.9816149,240.3895985,291.1124171,399.831529,152.2674057,252.6259793,64.09855968,170.7815609,171.0315932,294.6360104,276.6798095,397.3015442,55.49260278,375.6615561,205.3653756,158.6302683,363.8007827,418.5168603,28.61820249,252.9051969,254.2664613,166.0120478,303.8460797,225.1472529,190.6056612,194.97256,205.7660408,35.50888531,174.795897,142.5392831,234.2645644,96.53026496,308.7651444,269.330549,142.4823618,290.5008871,64.0252952,312.9005497,228.5167467,259.4171639,161.394069,256.9964473,62.09533837,207.7910689,34.30940346,206.1287686,93.73288033,149.4351798,241.7075787,221.8365985,146.7610469,38.57599031,62.40423379,154.8950569,262.682623,78.66870642,17.5711118,172.7582235,121.5611092,160.4718982,115.1562341,120.3245908,56.4682041,286.1117209,175.7563116,278.9585317,122.0851946,257.5368242,43.34111561,173.7474881,205.3851008,393.474615,213.6816495,106.9248323,222.9947024,124.9906822,345.9034528,45.03410923,338.7665093,163.4424444,305.4368363,158.2525224,201.9254354,490.1147381,385.6596157,507.7060969,508.8288408,400.703211,596.3652804,362.4216543,114.5883395,429.8611781,325.0601495,555.4559207,262.9817336,407.813395,858.9173571,496.6973355,1298.407584,888.086665,1155.834023,162.5252443,2372.684744,603.0260524,3362.169392,3093.751956,14892.7451,17461.70195,5918.739673,4630.897481,2584.986214,5445.919447,4634.737893,3071.124712,2545.089921,160.2705549,622.4713742,1285.731264,322.9739867,1234.72292,742.3176672,529.4299953,877.5406562,152.8022541,835.6091487,224.8250297,988.6955246,547.7923511,908.1621254,596.2796127,447.5582761,252.3662135,518.8476719,272.2102297,518.6851935,99.05448388,464.8557127,157.9719695,382.6845392,526.7676385,439.0048853,556.5900358,409.0619259,414.2903104,429.2535661,478.8915029,393.8841157,513.5766655,306.4862014};
        double[] amplitudes = new double[]{2410,1299.949218,5768.139765,3085.477325,8824.917156,11665.05486,6187.90485,2397.387618,4805.05654,2504.975767,4593.845858,9573.888785,7865.504701,2123.000733,2357.952329,4055.446684,2831.533651,1443.734753,2299.005548,815.6041964,2985.844581,1368.166081,6938.176429,13354.0198,9368.404337,1719.3386,2795.926352,1508.925327,2497.540567,2364.093857,3111.829003,1006.746009,4008.522912,2887.576244,7408.871544,4128.501993,2958.9622,2738.988004,2482.062194,1676.370546,1558.631291,7465.277734,6570.020828,6472.280643,1728.285177,38971.5363,60033.71406,32289.85255,8248.992966,13658.12896,4368.568869,7255.928825,4082.563614,4506.865223,3105.817079,3013.743978,2845.679705,876.5062944,2036.022589,1243.038599,2324.490319,1757.279402,1962.619336,2386.376211,3058.12077,822.8669619,1015.447513,3364.516116,2608.324514,1771.3655,1993.15004,1920.983189,1761.471819,2035.751338,1066.454436,336.9018619,1213.030653,1328.8608,734.2426354,1291.175075,953.6406738,1494.136644,2107.971927,783.5825618,2025.885762,998.8256625,337.6335491,1099.487661,877.5401957,875.1642879,668.6316312,2260.01328,6305.017723,6583.848298,2265.372141,2395.444063,874.1220731,1666.653675,643.5397562,1291.864008,488.5389858,987.8204846,754.272174,611.5788742,2131.78168,1554.524952,1269.948149,1161.007893,1102.419534,766.3953401,1411.774294,618.4902977,2055.33073,744.3535806,342.4873455,1493.644975,2643.913321,621.5547056,1416.401398,460.0417408,525.9042925,699.1133613,1026.470677,467.694023,700.6207931,520.3177238,1002.635725,307.0362477,1384.931045,2265.563185,831.6541918,954.2440773,766.0230742,918.0323305,685.0452153,759.5092507,474.9005505,1137.340301,1055.608309,595.7428361,910.5300318,1055.270264,834.7774436,787.8588386,837.4850911,935.525286,919.9369009,250.7725042,1000.125901,787.4859729,1148.23201,291.9428719,803.5319688,653.2065279,611.0362736,69.17489341,509.4293221,334.476026,195.6319295,654.4834537,627.3219429,536.817183,162.0819288,642.5911643,862.2683641,1302.42602,395.5605291,595.3743254,59.01513863,467.8143931,444.4910502,943.6585662,110.4823621,781.2867692,279.4115124,425.2852909,102.0808904,149.4318896,407.0599454,478.6552249,170.5395293,372.1782511,401.3424165,319.2921544,425.5717131,155.6502585,183.6675382,514.7073713,873.5833257,1773.96427,872.0049041,1555.603539,847.3142,681.3020559,500.1873373,475.3402078,495.1048279,560.0714582,692.5008892,229.0040853,1420.308924,985.2556122,997.7896517,693.9046585,727.3713216,1146.646487,1482.804142,1341.253859,852.9157245,1289.870695,1340.981704,4336.847782,3480.299378,11321.21637,13591.17375,4522.099644,4458.196794,1875.106368,2251.925613,422.2457359,1806.392463,298.6848128,1561.03007,462.0077787,915.8699958,1148.310673,1177.12936,818.4354469,777.8642951,760.5530993,332.2611168,1030.532377,42.33291169,858.1269415,769.2006947,1027.38521,595.0068669,771.0877729,750.6507653,929.2163474,658.3329523,548.7302019,541.8276781,181.688152,834.7885405,397.7906095,754.1964547,158.4074255,564.1052398,521.1914113,566.3514046,590.7726442,634.3721968,709.3857535,309.6283785,841.6111876};

        double[][] testData = new double[1][];

        testData[0] = amplitudes;

//        int[][] peaks = peakExtractor.extractPeaks(testData);
//
//        assertTrue(true);

    }

}