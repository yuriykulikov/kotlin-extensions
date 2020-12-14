package aoc

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.putAll
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

sealed class Line

data class Mask(val mask: String) : Line() {
    val posMask = mask.replace('X', '0').toLong(2)
    private val negMask = mask.replace('X', '1').toLong(2)
    fun applyTo(value: Long): Long {
        return value.or(posMask).and(negMask)
    }
}

data class Write(val address: Int, val value: Long) : Line()

class Day14DockingData {
    @Test
    fun silverTest() {
        assertThat(runV1Program(testInput).values.sum()).isEqualTo(165)
    }

    @Test
    fun silver() {
        assertThat(runV1Program(taskInput).values.sum()).isEqualTo(15919415426101)
    }

    private fun runV1Program(input: String): PersistentMap<Int, Long> {
        val program: List<Line> = parseProgram(input)

        val (memory, _) = program.fold(persistentMapOf<Int, Long>() to Mask("0")) { (memory, mask), line ->
            when (line) {
                is Mask -> memory to line
                is Write -> memory.put(line.address, mask.applyTo(line.value)) to mask
            }
        }

        return memory
    }

    private fun parseProgram(input: String) = input.lines()
        .filter { it.isNotEmpty() }
        .map {
            val tail = it.substringAfterLast(' ')
            when {
                it.startsWith("mask") -> Mask(tail)
                else -> Write(it.substringAfter('[').substringBefore(']').toInt(), tail.toLong())
            }
        }

    @Test
    fun goldTest() {
        val input = """
            mask = 000000000000000000000000000000X1001X
            mem[42] = 100
            mask = 00000000000000000000000000000000X0XX
            mem[26] = 1
        """.trimIndent()
        assertThat(runV2Program(input).values.sum()).isEqualTo(208)
    }

    @Test
    fun gold() {
        assertThat(runV2Program(taskInput).values.sum()).isEqualTo(3443997590975L)
    }

    private fun runV2Program(input: String): PersistentMap<Long, Long> {
        val program: List<Line> = parseProgram(input)

        val (memory, _) = program.fold(persistentMapOf<Long, Long>() to Mask("0")) { (memory, mask), line ->
            when (line) {
                is Mask -> memory to line
                is Write -> {
                    val values = floatAddresses(line.address.toLong(), mask).map { address -> address to line.value }
                    memory.putAll(values) to mask
                }
            }
        }

        return memory
    }

    // If the bitmask bit is 0, the corresponding memory address bit is unchanged.
    // If the bitmask bit is 1, the corresponding memory address bit is overwritten with 1.
    // If the bitmask bit is X, the corresponding memory address bit is floating.
    private fun floatAddresses(address: Long, mask: Mask): List<Long> {
        val positionsOfX = mask.mask
            .reversed()
            .mapIndexedNotNull { index, char -> if (char == 'X') index else null }

        return (0..powerOf2(positionsOfX.size))
            .map { permutation -> permutation.moveBitsToPositions(positionsOfX) }
            .map { floatingBits -> address.or(mask.posMask).xor(floatingBits) }
    }

    /**
     * Given a list of [positionsOfX], moves bits of this [Long] to these positions.
     */
    private fun Long.moveBitsToPositions(positionsOfX: List<Int>): Long {
        return (0..positionsOfX.lastIndex).map { index ->
            val selectBit = this.and(1L shl index)
            val moveToPos0 = selectBit shr index
            val moveToPosOfX = moveToPos0 shl positionsOfX[index]
            moveToPosOfX
        }.sum()
    }

    @Test
    fun testFloatAddresses() {
        // address: 000000000000000000000000000000101010  (decimal 42)
        // mask:    000000000000000000000000000000X1001X
        // result:  000000000000000000000000000000X1101X
        // After applying the mask, four bits are overwritten, three of which are different, and two of which are floating.
        // Floating bits take on every possible combination of values; with two floating bits, four actual memory addresses are written:
        // 000000000000000000000000000000011010  (decimal 26)
        // 000000000000000000000000000000011011  (decimal 27)
        // 000000000000000000000000000000111010  (decimal 58)
        // 000000000000000000000000000000111011  (decimal 59)
        val floatAddresses = floatAddresses(
            0b000000000000000000000000000000101010,
            Mask("000000000000000000000000000000X1001X")
        )

        floatAddresses.forEach { println(it.toString(2)) }

        assertThat(floatAddresses).contains(
            0b000000000000000000000000000000011010,
            0b000000000000000000000000000000011011,
            0b000000000000000000000000000000111010,
            0b000000000000000000000000000000111011,
        )
    }

    private fun powerOf2(size: Int): Long {
        return when (size) {
            1 -> 0b1
            2 -> 0b11
            3 -> 0b111
            4 -> 0b1111
            5 -> 0b11111
            6 -> 0b111111
            7 -> 0b1111111
            8 -> 0b11111111
            9 -> 0b111111111
            else -> error("Too big a number: $size")
        }
    }

    private val testInput = """
        mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
        mem[8] = 11
        mem[7] = 101
        mem[8] = 0
    """.trimIndent()

    private val taskInput = """
        mask = 10X1101X10X111011001X00X0110101X11XX
        mem[32921] = 1577116
        mem[16647] = 3562548
        mem[52072] = 60801469
        mask = 10111X1X1X1111110X01X011010X11110101
        mem[55658] = 974
        mem[607] = 61211605
        mem[4456] = 662806
        mem[11846] = 120026
        mask = 111110X01010110110XX000001X0X1X00X00
        mem[1669] = 15784535
        mem[50105] = 12001
        mem[26856] = 39199
        mem[22775] = 212900
        mask = 1X011011100XX11011010010011000X11101
        mem[37951] = 13806
        mem[44608] = 13716
        mem[63750] = 29338990
        mem[28382] = 82795005
        mem[41705] = 879
        mask = 1X111011101111111X010XXX011100X01010
        mem[21237] = 3919330
        mem[15812] = 56330940
        mem[28382] = 16529059
        mem[24050] = 301146346
        mem[11117] = 59982689
        mask = 10111XX110001111X1110X011111010000X1
        mem[15453] = 902235609
        mem[55091] = 33053
        mem[3413] = 58203
        mem[8710] = 46113
        mem[7577] = 22129270
        mem[31498] = 19549608
        mask = 1X1110X110X11101110101X101X001000011
        mem[13382] = 720248
        mem[6723] = 74155887
        mem[27485] = 551
        mask = X0111X1110X110111001XX1X1100X1X1110X
        mem[49577] = 116309682
        mem[39711] = 7679600
        mem[18912] = 12667729
        mem[10498] = 467725804
        mask = 0X1111X1110X010010X100101011X100X001
        mem[13944] = 103511727
        mem[13842] = 4
        mem[29367] = 6900
        mask = 11111000X0X0X101X0X00000010XX00X1101
        mem[34038] = 2063296
        mem[60815] = 684882
        mem[56475] = 306382
        mem[50240] = 13465856
        mask = X11X100X10101X01XX0010XX0110110X0010
        mem[19287] = 11439740
        mem[9199] = 11671400
        mem[9730] = 260
        mask = X01011X00XX0X00X100X001X0100100101X0
        mem[13929] = 55780879
        mem[33994] = 997198
        mem[26447] = 190887316
        mem[8877] = 1970
        mem[6038] = 36059
        mask = 1X1110011010XXX111000XXX01101X000000
        mem[55266] = 510766
        mem[14921] = 29982772
        mem[9793] = 6152
        mask = X011X1111011X111100100X10100XXX11100
        mem[27946] = 19226
        mem[32375] = 11168968
        mem[53988] = 3104623
        mask = 10111X1X11X0X10010X100X10101X001X010
        mem[47965] = 11636
        mem[23252] = 23723760
        mem[17780] = 686
        mask = 100X101110X11X0X110101X1X1X001000011
        mem[32259] = 1383831
        mem[23552] = 69
        mem[48586] = 19905
        mem[61134] = 503010834
        mask = 001111XX10011011X0111010110001010XX1
        mem[2130] = 588003418
        mem[26856] = 406949
        mem[57669] = 1990
        mask = 1011101110111XX1100XXX0000X0101111X0
        mem[17105] = 11122
        mem[1161] = 182593087
        mem[23720] = 26835389
        mem[14040] = 926
        mask = 001111111100X10010X101X000X011000011
        mem[30458] = 72015
        mem[3656] = 1289576
        mem[3210] = 37704
        mask = 10X01011X0010X0110X11101010X0X11010X
        mem[28809] = 1158
        mem[16494] = 24906824
        mem[29316] = 1405
        mem[24881] = 1139
        mem[24650] = 1279
        mask = 10X111X1X1X0110010X100110111101X101X
        mem[9865] = 3086
        mem[35589] = 6928783
        mem[45013] = 1042589
        mem[58671] = 8186
        mem[63500] = 7958931
        mask = 1011X011110X110X1101X1001010110XX1X0
        mem[10498] = 1758
        mem[20464] = 118853
        mem[27136] = 513019
        mem[17473] = 2166
        mem[25277] = 62687
        mem[6977] = 384625
        mask = 101X11111001001X10X111X001000X101000
        mem[16215] = 258081716
        mem[2986] = 59837829
        mem[12113] = 174
        mem[54740] = 55764889
        mem[42568] = 530
        mem[6897] = 36626559
        mem[19789] = 103059
        mask = 1011101X1000111111111X10001X0101101X
        mem[24050] = 595
        mem[40007] = 85706
        mem[49577] = 18525
        mem[48597] = 11928402
        mask = 10X11011100X01101X1100100XX00110X001
        mem[1832] = 217686
        mem[25638] = 61078944
        mem[3333] = 603016089
        mask = 1X01101110111X1X1101011000X01110110X
        mem[58924] = 1076
        mem[63750] = 171727
        mem[38459] = 3322
        mask = 10X110X1X00X1110X1X100000110011X10X1
        mem[7430] = 22428
        mem[235] = 7041475
        mem[8532] = 7378
        mem[9309] = 151644785
        mask = 101X11XXX100X0XX100100100111100XX000
        mem[49498] = 4666356
        mem[40510] = 77776996
        mem[51572] = 580
        mem[30601] = 21011
        mem[14222] = 239980618
        mask = 1011XX11110X11XX100101001XX0110001XX
        mem[5705] = 109516
        mem[46471] = 12926
        mask = X01110X11011100X100011011100X0X01110
        mem[17946] = 4470872
        mem[65465] = 17190
        mem[32367] = 525883
        mem[22681] = 6636
        mem[54450] = 219825
        mask = 1X11X01111X11100110110X011101000X110
        mem[52072] = 1158
        mem[56253] = 359
        mem[9081] = 1523513
        mask = 1011X011100011111XX1X00001101110X0X0
        mem[2538] = 30333
        mem[46979] = 474504
        mem[58005] = 100229924
        mem[56253] = 179310
        mem[8361] = 2283081
        mask = 1010101110011101X101X01011XX00010001
        mem[36508] = 1327
        mem[22899] = 64878
        mem[62742] = 1062244854
        mem[39176] = 341840371
        mem[45344] = 4382862
        mask = XX110X11101101X11X11101X1100X101XX00
        mem[41389] = 502732580
        mem[28513] = 15192
        mem[29743] = 61127
        mem[55067] = 91617
        mem[41301] = 1157
        mem[37208] = 2310
        mem[19814] = 75281
        mask = 10111X1110001XX11101X1011111110XX1XX
        mem[38296] = 8078966
        mem[28151] = 14883289
        mem[40348] = 209223060
        mem[3425] = 59252
        mem[44883] = 412318
        mem[18800] = 61696
        mem[19814] = 3022
        mask = 1X1110111000111111X100100X1101XX0011
        mem[15812] = 38676
        mem[14062] = 242965
        mem[59113] = 872092
        mem[16215] = 2900618
        mem[1547] = 80517
        mem[33698] = 662443800
        mem[52580] = 1975
        mask = 1XX11011101110X110011011XX101011010X
        mem[11354] = 106444
        mem[49252] = 25118850
        mem[21077] = 45913721
        mask = 101110X100X111X01101X010011001101110
        mem[32170] = 3405
        mem[9797] = 1598064
        mem[58145] = 2086745
        mask = 100110010001111X11010X100X10001X010X
        mem[15268] = 50475
        mem[108] = 20703877
        mem[18512] = 371131
        mem[4847] = 87160733
        mem[45954] = 6969238
        mask = 10111011001X1111X11X10010001010001X0
        mem[63099] = 10007
        mem[53328] = 667684590
        mem[33006] = 120834715
        mem[39619] = 10203557
        mem[32728] = 249140
        mem[982] = 6701
        mem[3425] = 3969
        mask = 1000X01110111000110110X0111X11001XXX
        mem[33375] = 432278811
        mem[21640] = 535321544
        mem[42180] = 29447087
        mask = 1X111010XX1X1111010110X0X101XX010111
        mem[39868] = 43350
        mem[25988] = 1078
        mem[20505] = 7282
        mem[18319] = 2486
        mem[10567] = 192634
        mem[8552] = 858664
        mask = 10111011X00011111111010X1X110111X000
        mem[16769] = 699868753
        mem[16893] = 5067565
        mem[34360] = 366702897
        mem[49861] = 62668
        mem[58145] = 82185
        mem[47411] = 354500670
        mask = 1011X01101111111X100110000XX01001110
        mem[42180] = 1347
        mem[7021] = 2390862
        mem[235] = 494391
        mem[8972] = 23499
        mem[50105] = 258783709
        mask = 001111X1X0011011X01X1010100010X10011
        mem[38065] = 469
        mem[7550] = 194014
        mem[49119] = 859034872
        mask = 10110011110X11011001XXXX1111110X0XX0
        mem[21509] = 12684
        mem[50469] = 33553
        mem[53691] = 1655
        mem[27959] = 77572136
        mask = 10X110011010X101XX000X0XX01X01000110
        mem[58769] = 1000374
        mem[36657] = 194200105
        mem[32259] = 57432
        mem[46573] = 297487
        mem[19814] = 1656654
        mem[1156] = 2904
        mask = 100110111X01111011010000X01101101X1X
        mem[9473] = 372
        mem[1547] = 258186
        mem[34639] = 59618
        mem[19413] = 861071
        mask = 1X1110XX101011011X0XX0X0011011000X0X
        mem[711] = 5474
        mem[709] = 1676
        mem[46979] = 287494
        mask = 1010X0111X0111011X0101100X0X000X0X01
        mem[21027] = 714127137
        mem[7139] = 3582467
        mem[32921] = 14414961
        mem[11266] = 374
        mask = X010101XX001000X0111X01X011100010010
        mem[48135] = 2522226
        mem[13708] = 101387187
        mask = 101110X1110011XX100101101XX100000100
        mem[17506] = 1223182
        mem[61253] = 30411596
        mem[26324] = 2489
        mem[34616] = 7601781
        mem[3487] = 337
        mem[4456] = 19172
        mem[10701] = 108978
        mask = 111X101110X11111100XX110101111011010
        mem[39871] = 12246238
        mem[53638] = 2013
        mem[32278] = 2293085
        mem[51935] = 3361274
        mem[32375] = 311
        mem[29092] = 10573484
        mask = 10111011100011X111X100X0111011X11100
        mem[56916] = 2832108
        mem[34359] = 140876203
        mem[41182] = 138137434
        mem[5530] = 10601840
        mem[27959] = 13262011
        mem[34695] = 4932
        mem[64032] = 170314
        mask = 101110111X00XX0010111X0011X01101XXX0
        mem[11300] = 16142
        mem[8877] = 14076727
        mem[7745] = 73206542
        mem[7169] = 52530396
        mem[7124] = 4235
        mask = X0111X111100XX0010XX0101000X1000100X
        mem[19649] = 1640696
        mem[28382] = 62738
        mem[9865] = 2420801
        mem[22542] = 10447
        mem[64571] = 311216
        mask = 1X101X00101X1001000XX011011111X11011
        mem[21911] = 284330
        mem[38525] = 235680925
        mem[46324] = 389019
        mask = 10X1101X101X111X010XX0X0011011100X01
        mem[31498] = 42923710
        mem[46906] = 994
        mem[16215] = 3476631
        mem[1103] = 150
        mem[2081] = 69
        mask = 1011100X100101101101X10XX100X0110101
        mem[19287] = 303085
        mem[21629] = 97810
        mem[711] = 1558
        mem[6361] = 51918
        mask = 1010101110X100X1XX110X1100XXX0011110
        mem[10802] = 416816598
        mem[62710] = 359911653
        mem[30622] = 6531718
        mem[12011] = 106349062
        mem[29808] = 376137
        mem[10017] = 30740080
        mem[32170] = 496432824
        mask = 0X111X1X1X01X01100X1101010X0X0X11100
        mem[55273] = 445275933
        mem[9313] = 899
        mem[23828] = 6962
        mem[10943] = 122125
        mask = 101X1111XX00110XX0110X000011X0110011
        mem[15786] = 232372147
        mem[45106] = 24757
        mem[13944] = 354059
        mem[17066] = 259951
        mem[18378] = 172143
        mem[3647] = 175446
        mem[706] = 7605
        mask = X0111011X0111X1110X10X1000X01000X000
        mem[20446] = 40142997
        mem[16553] = 2578971
        mem[46324] = 2081979
        mask = 101X10111X0X11011X0100XX11000X00XX01
        mem[28678] = 696
        mem[47965] = 369
        mem[53503] = 158269
        mem[47795] = 57488
        mem[20104] = 5725
        mem[9925] = 11183
        mask = 10X11111110011111001XXXX00X011000001
        mem[56102] = 3125205
        mem[31622] = 441134
        mem[17473] = 2759655
        mem[60730] = 919
        mem[27527] = 4108263
        mem[7297] = 5602
        mask = 1X111011101X1011100101100100011X0X0X
        mem[20617] = 1746719
        mem[28382] = 21097
        mem[21044] = 331
        mem[35182] = 12498
        mask = 101100111010111X0101X00000111X1010X1
        mem[37578] = 766108350
        mem[26040] = 690
        mem[2518] = 61286
        mem[29996] = 1605
        mem[64605] = 831184
        mem[62207] = 36319147
        mem[28182] = 1183
        mask = 10111011XX11111111XX01000001X1000X10
        mem[3658] = 1011
        mem[41806] = 210956
        mem[19413] = 4718600
        mem[58005] = 96158
        mem[1706] = 2668743
        mem[48586] = 8067
        mask = 10110010X011X111010XX010XX1111100X01
        mem[63219] = 891744
        mem[63637] = 492772
        mem[11895] = 3136365
        mem[35414] = 26714009
        mem[48] = 443766036
        mem[35068] = 6013893
        mask = 1001101X101X1X1101X100000X10X01001X0
        mem[47386] = 53382651
        mem[13842] = 2320
        mem[11461] = 66053097
        mem[54450] = 2816089
        mem[27850] = 246605895
        mem[35494] = 46318
        mask = X0X11XX1110XXX111001011010000100X0X0
        mem[30927] = 4066158
        mem[26772] = 215126
        mem[20450] = 1278891
        mem[2501] = 593905327
        mask = 101X1011X1001101100X001011X001000011
        mem[32369] = 7798
        mem[20519] = 516155
        mem[8315] = 687523
        mem[47998] = 27320481
        mem[19522] = 813
        mask = 1011X011X011X111X1X1101111X0100X1010
        mem[235] = 75134079
        mem[61265] = 54840684
        mem[15898] = 1574547
        mem[14222] = 402919
        mem[19916] = 1323054
        mask = 0011111110011011X0X11010110XX001XXX1
        mem[35182] = 1173
        mem[38177] = 208624773
        mem[2000] = 9825
        mem[8162] = 435974
        mem[1095] = 272229
        mask = 101110011001X1101X01000001X0X11010X1
        mem[20636] = 1237013
        mem[9309] = 89700
        mem[64938] = 36232755
        mem[56970] = 22155
        mem[36557] = 31395
        mem[1603] = 14404
        mem[21907] = 60737300
        mask = 101111111001XX111X0100100100000000X0
        mem[52572] = 847010
        mem[26040] = 112147
        mem[60065] = 282387
        mem[58984] = 1846
        mem[14062] = 2024
        mem[55733] = 45541
        mask = 10111X1110X1X0X110011010010X01001101
        mem[53224] = 6800262
        mem[10614] = 3892
        mem[1669] = 809
        mask = 100110111011111XXX01001010001XX0000X
        mem[9857] = 28052126
        mem[7430] = 783815
        mem[65465] = 2281
        mem[11846] = 866063
        mem[35278] = 7634
        mask = 10XX1011X000XX111XX10X0001100100X001
        mem[44609] = 58299033
        mem[46754] = 1207031
        mem[30787] = 24820
        mem[34360] = 20225
        mask = 1X11X01X101X111001XX0X01X01101101X01
        mem[46324] = 46771
        mem[39551] = 1753306
        mem[5530] = 6405523
        mem[25794] = 745520
        mask = X0111X111100X1001011000X1100XXX01XX1
        mem[24195] = 16091502
        mem[15190] = 1663
        mem[61138] = 149535782
        mem[35865] = 70299
        mem[50240] = 1606
        mem[59568] = 20094
        mem[42918] = 148996
        mask = 101XX01110110X110111X11X001001001X10
        mem[32346] = 262626661
        mem[3118] = 3767
        mem[58994] = 3117092
        mem[29546] = 85126863
        mem[40836] = 82452571
        mask = 10X1101110XX11XX11010X00X110010X0X01
        mem[59113] = 27863486
        mem[28011] = 4625
        mem[11969] = 1041
        mem[12355] = 254786
        mem[47238] = 39575
        mask = 101110XX1X00X11011X10000011X011X1001
        mem[9231] = 7130151
        mem[27878] = 72823816
        mem[9473] = 49969835
        mem[1161] = 92012
        mem[16893] = 1918
        mask = 11XX10101111111101010000X101000X0100
        mem[33199] = 502459785
        mem[52985] = 442741214
        mem[8315] = 29329
        mem[107] = 851735654
        mask = 1011101110111X1X1101001011110X1110X1
        mem[9307] = 3186223
        mem[17723] = 2763
        mem[59345] = 37171
        mem[55870] = 1097
        mem[11194] = 30399
        mem[27362] = 294155760
        mask = 1011001XX0111X111111101X110X10001100
        mem[16661] = 903425931
        mem[26856] = 654
        mem[7465] = 20156
        mem[6918] = 4826
        mem[23811] = 3597736
        mem[21327] = 2045620
        mask = 00111111100110111011X00X111101X01X11
        mem[15201] = 394
        mem[6361] = 9161
        mem[9528] = 1020900739
        mask = X0X11X1X11101X001001X011110X010101X0
        mem[7769] = 3096083
        mem[11534] = 196638138
        mem[59579] = 14797
        mem[56277] = 354149
        mem[607] = 88
        mem[62993] = 129370
        mask = 101111111X1X1X11100101100X010X0011X0
        mem[26195] = 196295804
        mem[15356] = 3609025
        mem[11117] = 31867
        mem[21077] = 311754410
        mem[31796] = 1520952
        mask = 011110011010110X100010100X0110X0X100
        mem[1859] = 14559
        mem[58145] = 339865
        mem[11077] = 5592
        mem[32233] = 1555
        mask = 1X01111X1100111X10010XX1100011111001
        mem[60360] = 2353
        mem[39451] = 494433330
        mem[34042] = 51856609
        mem[5491] = 88680
        mem[42859] = 2372
        mem[59113] = 31443
        mask = X011X1X11100110X10111X0111X0000X0101
        mem[59428] = 14216455
        mem[15786] = 274670172
        mask = 111XX01X101101X11111001X0X00000X00X0
        mem[1161] = 50623
        mem[4177] = 62912
        mem[19173] = 445372863
        mem[39232] = 111216
        mem[29546] = 240004911
        mask = 10X1X01010111111010X00XX11X110X10111
        mem[14052] = 3542987
        mem[35685] = 2379477
        mem[2000] = 205124146
        mem[2160] = 5267504
        mask = 10111X1110001X10110X0010011X01011100
        mem[47998] = 257900927
        mem[10091] = 2
        mask = 0X111X111X11101X10010XX010X1XX110001
        mem[43237] = 15779
        mem[1485] = 49
        mem[43650] = 1902259
        mem[36611] = 46027068
        mask = 1X1XXX1111X0110010110001110X00001111
        mem[57557] = 242660778
        mem[53255] = 171366328
        mem[26977] = 115714407
        mem[32449] = 187176
        mem[12169] = 341885
        mask = 101X1XX11100X100X00100X110001X001100
        mem[24050] = 7816751
        mem[22594] = 700652
        mem[30927] = 779
        mask = 1X00101X1X0X001111X1000000000X1X0001
        mem[63450] = 856838736
        mem[34384] = 206890379
        mem[20104] = 662
    """.trimIndent()
}