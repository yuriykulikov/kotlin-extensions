import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day6UniversalOrbitMap {
    private fun parseMap(input: String): Map<String, List<String>> = input.lines()
        .map { line -> line.substringBefore(")") to line.substringAfter(")") }
        .groupBy(
            { (name, _) -> name },
            { (_, orbitedBy) -> orbitedBy }
        )

    private fun calculateAmountOfOrbits(map: Map<String, List<String>>): Int {
        fun List<String>.recursiveSearch(level: Int): Int {
            return map { name ->
                map.getOrDefault(name, emptyList()).recursiveSearch(level + 1)
            }.sum() + level + size
        }

        return map.getValue("COM").recursiveSearch(-1) + 1
    }

    private fun parseTree(input: String): Map<String, String> {
        return input.lines()
            .map { line -> line.substringAfter(")") to line.substringBefore(")") }
            .toMap()
    }

    private fun stepsToSanta(tree: Map<String, String>): Int {
        fun String.toRoot(): List<String> {
            return tree[this]?.toRoot()?.plus(this) ?: listOf(this)
        }

        val you = "YOU".toRoot()
        val san = "SAN".toRoot()
        val common = you.intersect(san)
        val path = you.plus(san.reversed()).minus(common)
        return path.size - 2
    }

    @Test
    fun verifySilverTestInput() {
        assertThat(calculateAmountOfOrbits(parseMap(testInput))).isEqualTo(42)
    }

    @Test
    fun silverStar() {
        assertThat(calculateAmountOfOrbits(parseMap(taskInput))).isEqualTo(224901)
    }


    @Test
    fun verifyGoldTestInput() {
        assertThat(stepsToSanta(parseTree(testInputGold))).isEqualTo(4)
    }

    @Test
    fun goldStar() {
        assertThat(stepsToSanta(parseTree(taskInput))).isEqualTo(334)
    }

    private val testInputGold = """
COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L
K)YOU
I)SAN
    """.trimIndent()

    private val testInput = """
COM)B
B)C
C)D
D)E
E)F
B)G
G)H
D)I
E)J
J)K
K)L
    """.trimIndent()
    private val taskInput = """
MZP)PKY
MZ2)51N
QTV)K66
GMR)5YQ
V3S)C2K
HSR)Z9G
4VZ)KT6
6FJ)YM5
PBH)T3S
RKV)3WT
2BF)V5G
65B)N2Q
KL3)MQ3
WDK)K1Z
3GT)9KW
MG5)LJY
27Z)8LK
G7F)DBD
ZRD)22M
PJT)2GY
R2B)P7W
PXH)38X
11P)SCT
SLG)SVG
MHC)75W
2DY)GJ4
X1H)P4R
HSL)SC1
MW2)HTK
745)3X9
YH3)7WW
7BT)HL2
3D6)4R1
XZ1)JB5
49V)N4J
T8V)Y3Z
VCS)GDC
CC4)M2V
Y2P)LWT
T9W)8WY
PLC)84V
96D)3NT
CT4)M2H
K1Z)VRH
9YL)848
3C6)DRL
PLT)J4S
RN6)C16
745)PVF
1VF)53X
J5X)59C
LG2)QTV
1F7)V73
TJR)NZV
FBY)62C
W72)FP2
WRQ)FRQ
25J)CKT
X8R)18Q
HVT)3HQ
Z5X)CT4
YQJ)HC5
6B8)PHM
6MX)54Z
Q4T)16R
HWQ)WJJ
YFQ)2RS
4K6)4Z1
MTQ)GW5
G41)3BG
1XB)R67
5HN)PBM
DBD)H34
RFP)H7Y
84F)TVG
9R3)Y59
FT1)V5W
M15)4VN
K5J)VF5
18Q)76Y
CMV)7HF
GVV)54G
4WL)W3L
4NB)4G3
XMN)S2M
H7P)NP8
L4D)415
JQ4)5PL
YM9)D91
VLC)RLF
9M1)3S3
81N)4ZM
DJH)LR1
8J6)28F
6XD)BSP
8LK)36Q
CTG)D8B
9RC)4JM
MTV)KGY
HSR)SSV
51R)83P
6M7)ZKC
S1T)CZN
W3P)62H
LBJ)XZ1
L48)ZP2
QFJ)FTR
38C)RZK
XLD)DMP
DNB)XYB
LVX)NZ6
FG5)LP1
G8D)TC2
2PY)8DQ
8Z6)CFY
JBM)FQY
2YW)32T
KJW)JNN
3HQ)Z5X
K4Z)K6V
SMW)9K4
KTH)MGD
37V)8Z6
3ND)DWX
DBG)RLV
XJ6)J5X
BH7)2C4
1F9)PFS
9C9)CMR
ZYG)L25
79B)MSN
ZDD)FNZ
32G)YKS
93G)3QQ
VK8)233
YHJ)T9W
KT1)399
RDZ)TWK
YNZ)D5J
QTL)GF4
G85)79C
V3Z)XVR
21B)G41
9QB)VX7
WYG)Q6V
NPX)DB9
4F2)4VQ
W5S)SJ2
GN3)7SY
RKS)L61
751)K1M
1KT)F8P
LBF)6MX
QWC)VPJ
T3P)NXF
XXM)212
NQZ)KPT
V5W)5Z8
ZWC)TS5
VRH)GQ6
2PT)21S
1ZH)VSN
283)4K3
4N8)D9B
GS5)DND
27M)V23
Y7S)361
63Z)G22
9XK)41H
DV3)NX4
VSF)P14
C7Z)V7F
NYS)75M
PGD)W2V
963)PFL
KF1)XMN
RNX)7GV
55Y)G1Z
WF4)PXG
L2L)WMP
WZ8)B73
RF8)18W
KBG)Y9M
G16)R6D
TSM)WJ2
9TV)87D
3M2)Y7S
D66)MC5
6KL)BM4
7SX)8BV
L83)F7P
K3D)T97
LCJ)4XH
N2Q)6DP
MX5)P32
B19)LRY
VX7)L1K
XG9)XH8
T8F)ZHY
3W7)HC4
5MW)4H5
PJ3)25L
F7Q)B5W
QCQ)QY7
F97)DQM
GXL)CTG
CQQ)6FJ
68Y)C2L
L9C)M5V
ZTW)2SG
ZXT)L4D
KCT)HF1
5TJ)4CS
2FD)FY4
ZL9)MZP
FBV)44M
8R7)QH7
5SK)L5S
TMY)PQ4
T1Q)81N
6SC)JPJ
46P)L1P
D48)Q58
B7W)L2H
LZ9)13H
4SG)1K1
QQ5)SYD
TZB)65D
BVZ)MTB
5RL)4VZ
LJC)BVJ
8XM)DPH
9WM)3VY
8C4)Z2D
XVR)2ZD
RWQ)12S
GWG)9X1
X3N)HWX
9M1)2C2
7M6)386
1P9)TJR
HKX)VJR
7CP)FVD
943)4C3
5W2)CMV
BYS)Q7M
RFW)QG7
YSL)T9K
VF5)JPZ
2XW)HVS
5NM)QX5
HT3)1RQ
H7Y)LBJ
TGP)BYW
XYR)1FH
JB5)LKV
2SG)GZG
Y4F)Z6M
SNZ)CJX
13C)H1Y
SPZ)TBV
XB7)3B9
Z62)QKC
8Y2)WM9
BTW)CM9
VJF)D2G
8Z1)VZ6
9BX)8JP
PJF)YYK
JDR)LM9
812)HDD
7YN)7CP
H8P)GY4
KJR)LZX
368)HJ5
BYW)LV2
KCW)1GK
79M)7LG
TSB)3DZ
D9B)ZY7
CM9)M15
QWM)15L
S1F)VW6
3WP)B19
9WF)18J
889)99C
J5X)2HT
KRK)LBF
PCN)XB7
77Z)LV5
P7W)1Q1
34N)V3S
9TY)GWP
MW3)SDV
PJS)Y4M
DGN)CW3
R99)TGC
623)XGF
3SK)32G
1WZ)9BX
Y7S)8BN
8CP)KL3
C2Y)8L6
M4C)43K
Q78)TW8
5PL)L83
QJK)PXH
L4H)FMC
D5M)RN6
MYD)2DY
7XV)YK3
SPR)D5M
954)JWT
1C8)QRH
TW2)3DQ
M2Y)T16
D8F)VXL
BV1)PVP
WMP)8L7
HM7)NVK
QHP)5MJ
NDF)J6B
1DB)1JZ
JFB)Q92
Z2C)YTK
7H1)QJK
ZX9)1L8
Q9D)VJF
9KW)T4X
SBV)12B
V22)ZBF
F7M)SYQ
9QP)LT4
1C8)ZNV
Y59)H45
X3P)787
3QM)ZXT
6MS)5PX
W51)LZ9
R9Y)9HR
1QZ)1VJ
WZG)6XT
SCT)BVZ
3H1)Y99
L1K)SV2
J9J)H41
W3W)RGT
WRK)9TP
GN6)DQR
7LG)YT9
NTB)3D8
3T2)FT7
WDD)28S
RMW)TJZ
6KP)4H8
SZD)L2Q
MQC)R12
NNY)H4Z
JJM)DGN
FY2)64P
592)9QH
5Z1)QBM
VQN)3C6
892)CYS
83P)8XM
KXG)6D4
3GJ)XG9
FLF)5RL
YYK)7CJ
MW3)W3W
SJT)9R1
JFY)YXH
M2Y)D4Y
YKS)BHX
X6L)DB3
36Q)H7L
MZD)5G3
FTR)839
54G)DD7
BDH)2F4
P14)9L2
G2Y)X3P
QLW)YFQ
CRQ)Y5Q
5RQ)JLR
NLV)7YQ
GHP)XPK
8L7)F97
YDL)GWG
D4K)NPX
415)19L
1V2)62R
87D)YCS
Q92)943
7WW)9C9
Z8K)9G6
3V8)5PM
G2S)XG5
M1Z)TMY
MJY)GWT
5RY)BFL
8BN)Y2G
DXD)SM9
HQ8)554
HRT)Q78
9K4)YKF
DQT)43D
XQ7)FSD
42J)HMT
K4C)F1Y
QKC)DR1
6C5)MLR
YGJ)J8D
HBQ)SKJ
3DD)66L
ZS3)F6P
7LB)95L
X6L)PFM
J29)2PP
TJZ)2WG
2JM)M74
DT5)VZB
HKD)596
JNN)428
VJM)WHY
25J)XJ8
G7F)BCK
K1K)WZG
Q49)CFD
T3S)77Z
3TJ)YCN
L5L)42J
BSP)QXY
V23)HKX
HC4)QLW
62C)4H2
76Y)368
Q8M)XLW
XMR)Z2M
QLX)GS9
Q6S)HL3
4R1)82F
JRD)X1H
4H8)49V
KY1)SV3
9QV)HBQ
2LL)MTV
CRD)ZH3
8PW)VT3
HC4)GVX
4VN)2DW
ZNV)3M2
JF9)Y46
2XJ)96D
S4Z)8P5
YK3)N3J
GJ4)9R3
KJR)T8V
K6P)Q6S
DGB)38C
N1T)MY3
28S)YW9
FFY)KTP
6ZG)K1K
SV7)ZS3
HL3)DLV
1M8)K4Z
5D1)C8H
C1Y)WQD
6H9)YMJ
9XT)2YW
581)QFH
X78)TTM
LJY)PVB
43K)FR1
SZ8)1QR
FT7)BGQ
8XM)X59
J6B)283
HJ5)7Y9
5PR)G84
XYB)YGJ
Z5L)KXK
M3V)VCS
M2V)GKH
L1V)8CP
G44)H8P
WZS)XSM
99C)11P
YGQ)1LC
Q7M)CFZ
VQX)QBW
PKY)7G8
NZV)12M
Y9G)YGQ
WRN)KY1
VW6)Z5L
F4W)SL3
X9F)1C8
9G6)WWQ
1KT)RNX
H4Z)KJR
78P)QQS
SYQ)XYQ
DB9)SPN
HGD)TJL
9TP)TQS
T9K)4K6
M55)GTX
YW9)59P
GDD)Y9K
3VY)DBG
M57)GL2
CFX)MBJ
H2B)7JB
H5Y)5MW
TWQ)8DZ
XDT)MHC
8R7)B95
3S8)D8K
CD6)HKD
4JX)7RG
9R1)HX3
51N)JLY
W2V)1VF
G8N)K4C
DGQ)VQP
DLR)2LL
3D8)1DT
4H5)WYG
59P)Q8M
HTK)WRN
7FQ)T3P
LX4)8R7
41B)2BF
LV2)3M5
H45)3QM
2JF)T5W
1JC)YMX
B73)L1V
DTW)FNX
BBD)XQ7
QLT)PH6
4DV)SLD
ZBF)31F
C2Y)YQJ
S4S)ZF4
8NR)M3V
YQL)J5F
PWB)KT1
5PH)DV3
V5R)6G2
KQN)WRQ
H3J)GSQ
VYG)WZ8
HT6)DCX
DB3)CF9
DPY)7TH
6KG)1XB
4XH)QVB
935)TQM
7Z7)5ZP
LZ3)1KX
17V)8LM
HS5)6NZ
LQZ)JV6
8BN)W1P
DQR)26L
HL2)481
WM9)R4P
QTL)751
DYR)B9Z
VFW)RGG
MQT)GQ7
9QV)43G
2KP)1YH
CSZ)5DR
R46)ZJ5
18W)YQL
7W8)C39
ZF4)PJ4
SXY)4FG
YCN)LXZ
D8G)1WZ
9TJ)71B
LZW)Q2R
RDS)KTH
GKH)6M7
YMZ)5W2
BVK)C1Y
DR9)S6B
T97)SG6
FFY)8XZ
2WC)LVX
GWP)QYK
9NW)HTQ
7GK)QQ5
7HF)P1Q
Z3Q)PK1
56L)FQ4
5X8)QSB
4RT)CFX
GVX)346
21S)RL7
YVT)JFY
JV6)935
5XQ)JZK
W4Z)MJY
QH7)13C
GZG)YHJ
481)C8B
8JP)5SL
79L)CZQ
6DN)3GT
2VK)TH2
ZN5)8J6
9X1)H5Y
QC7)CKX
7G8)Z8K
DQM)MQC
C2L)VJM
FY4)QLX
6G2)PW3
T2P)12Z
18J)HWW
3TR)PWB
D66)DYR
1QR)HWB
Q4C)V3Z
6VJ)FY2
D2J)XRW
FMC)C8T
CT6)GJK
1XG)Q8H
751)Z3Q
RJ1)SNQ
MY3)D3T
899)VYK
QQ1)51R
53X)Q6W
P46)43S
MZT)V4M
D2G)ZTJ
TWQ)MZQ
X5B)ZBX
C8H)34N
9KV)1F9
2C4)RXH
HKZ)MV7
D8K)6XD
PH9)GRT
61P)KB4
3K1)Q49
JR1)5R2
M7Q)CXV
399)W8B
SS7)NQT
D24)VS2
C16)4Y8
2GB)QFJ
MGP)5SV
4CS)LNL
PHM)XBN
2TS)5D1
H3J)WZS
ZGJ)PX2
1DB)YMZ
DFH)W81
SC1)C7Z
YFQ)KZH
RVQ)ZDD
SKJ)FBL
C9X)7H1
ZJ3)6T8
YX1)F4W
1RQ)1ZS
PPZ)37V
2WJ)CJF
2F4)KQN
T16)KXG
FY4)V2B
S1Z)SQN
DWQ)635
4CF)Q8Y
V5Z)NVL
FWD)LX5
DMS)TLF
L2H)MQT
P4R)JD3
NQT)JRD
652)5CM
G8N)HD7
H1Y)6SC
W3L)TRY
ZVQ)GNX
XJ8)5PH
HTQ)6FF
4Z1)SV7
WQ4)LX9
W8B)XST
N3R)2TM
ZWT)SPZ
N67)C3X
GW5)WFZ
JYY)4YH
62R)6J2
SYD)GJ3
7MZ)GWS
BM4)PGD
KH6)YN4
W62)RJ1
38X)3S8
5PX)8JJ
NX4)GHH
169)VR4
78P)96C
H34)M57
FNX)62L
GDC)SS7
NGM)JR1
28L)9TY
25L)SQK
3S3)PHT
ZSG)5BZ
NXF)BWV
ZY7)L5L
4Y8)HN1
FNZ)4T5
D3D)NNQ
TH2)4SG
L25)4CF
KWG)5Z1
FWB)NYX
HWB)HBM
TC2)2PY
Q4N)X9F
MWG)LXH
MVQ)DPY
PN7)XCS
464)93Q
TLF)YNZ
L61)89G
G22)ZVD
VMM)T8F
82F)LQZ
RCV)8PW
DQQ)GQR
JKT)M55
WZX)Z7X
2RS)GLF
43G)7M6
PWL)WGX
GS9)G8N
Z9G)9MW
361)Q4T
DD7)N8Y
DR1)52B
4T5)S5G
4KZ)KHK
WHY)DR9
T5W)QF7
M3L)4F2
LV5)R2W
JGS)DQT
TQR)BBD
YN4)1P9
VQ3)TXF
1LT)7BS
QVB)9XK
CKX)QR3
4N7)HQ8
BJ6)4TT
44V)PRJ
1LC)27M
PVB)DXD
HXF)3WP
MQ3)L9L
4TT)D8F
22M)R4X
1QR)K6P
6TK)8TZ
M3K)JF9
596)PBH
4C3)S4K
LM9)SPR
P4R)DR3
6LM)592
RMW)2XW
1VJ)411
2V5)MV2
LWP)3QX
5XP)HSL
1K1)XT6
3D8)ZQS
9R1)MZT
9G1)DHP
CZQ)TQQ
JDR)HTR
6NW)LG2
R6D)YD8
38X)QWC
YMJ)95Q
V5W)WCN
2HT)HBG
ZJ5)3V8
SQK)J13
ZFH)8Z1
95Y)SHZ
8Y2)YH3
ZKC)42R
X27)CT6
G2J)S1F
DF1)745
Q6W)955
7JB)L9C
C9N)Z1B
ZQS)G2Y
BQJ)DQQ
LCR)GMR
GW5)GC9
8DQ)61V
2BF)P9G
WRN)XDT
KZR)L48
SJB)RCV
J3F)BDH
XZ1)K5J
R2W)W64
RC2)W72
C7Z)7X1
954)F17
G1Z)1XG
YZJ)JBM
BZZ)C5X
YZC)66K
G2M)5SN
V73)5M6
TQM)W4Z
SJ2)SPC
B5Q)6VJ
B9L)2JM
Z2D)CX6
VXT)81K
33H)4RT
ZVD)VQ3
S4K)7PD
TPN)C9N
Y5Q)BX1
BVM)97B
C5G)QLT
7M4)TNZ
233)C8R
HLY)21B
QVB)NDF
6NZ)XHW
ZS7)ZQN
P8Q)T2P
31F)954
1KX)Y5H
DND)KRK
4RT)9QV
1ZS)KXZ
MPX)YZC
Y9G)FMZ
LKV)XV8
44M)RDS
C3C)191
3QX)9NW
FQ4)DWQ
QVH)DTW
8P5)Z18
NVK)RC2
X2W)YY9
MXF)N3W
P2W)PWL
KB4)Q1R
96C)T7R
QMR)B5Q
3GT)MWF
9X1)XPR
51R)6MS
P32)Y9G
47N)G1S
X59)WQ4
JT2)Y29
Y9M)F3G
HWW)LV7
QQS)MZD
BNS)F2L
V7F)27Z
GQR)SXY
NVD)CDN
ZHY)9GB
41H)M1Z
VS2)NM3
84V)SZ8
17M)RBR
S71)P8Q
NP8)6NW
LP1)Z3G
TS7)169
635)J29
6D4)628
CMV)PPZ
24K)7LB
DV3)F7M
81K)17V
X5R)VQN
HG6)VYG
W1P)CGR
SWQ)7W8
JBM)X3S
KND)SMW
178)QMR
1FH)M2Y
ZS7)YJS
C8T)911
WFZ)XXM
YCS)G4W
DD1)KJW
Q1R)MD7
XYQ)1KT
P9G)ZVQ
5SL)BQJ
HXF)MWL
MJY)GDD
DPH)PN7
G1S)MXF
YMX)31V
RFP)C9X
GFL)9WM
ZSW)HRT
ZBX)G2S
DT5)QCQ
W64)47N
S5G)BN3
B44)SWQ
YJS)6PH
DHP)X6L
5ZZ)FBV
6DP)418
P4W)TDT
H7L)KZ6
YC8)TZB
NM3)MVQ
16H)SR1
YTK)C18
HGT)4NB
HXZ)BVM
RBR)3ND
L5S)548
HD7)T9P
D2G)YOU
CXV)ZWT
R4X)CKN
8L6)5SK
CRQ)G7F
YD1)YBP
HZY)94R
LQ3)642
P74)S4Z
4TZ)VSF
61C)4VR
4FG)5GZ
ZYG)SLG
YMN)WWN
K66)5RR
8FV)K49
2ZD)VS4
7JJ)XXG
SVG)VK8
CDN)RDZ
XV8)JFB
4G3)DW9
Q8Y)W6Q
N2J)W49
SV2)RMW
5WL)GXZ
PX2)X72
WGX)D4K
LX5)1S3
RXH)H26
FRQ)Y6M
GLF)FWD
955)K3N
12M)5HN
5ZP)KZR
F1Y)68Y
H3N)WMQ
J7J)8PM
B5S)LF8
TVG)DC2
NYX)889
R12)29V
V26)MTQ
PF1)6KG
4H8)MPX
KK9)3DD
25H)N1T
BFL)7FQ
71B)ZJ3
75W)LX4
88Y)M3K
12S)JKT
Z3Q)DT5
418)FWB
XT6)PLC
G8Q)BTW
6J2)G8Q
TXF)3GG
F7P)B7W
911)2XJ
3CT)HZY
SPL)JTD
QBM)464
4BZ)NK2
V4M)X78
4N6)XRF
XBN)632
MWF)2TS
Y5M)41B
BVJ)W5S
XPK)RFP
YL1)7BT
1X9)6KP
WKR)4N6
J13)178
RGG)89P
MC4)V8L
ZH3)QTD
ZX2)B4P
HRH)VQX
CJX)W82
FQY)D48
YR4)TQR
Z2M)8SF
F17)BJ6
212)Y5J
XQY)4S5
KT6)D24
BQJ)812
7X1)VFW
D3T)Q2D
R41)WKR
MBJ)R46
TPN)3SK
5PM)MX1
S1T)V1S
26Q)PCN
PRJ)2KP
CFY)9YL
15L)YD1
W9W)BZZ
JC7)S71
346)382
HWX)D8G
632)SJB
MBJ)F7Q
CMR)1QZ
ZZK)BYP
CP9)1X9
H41)RYR
428)J7J
N8Y)DD1
2C2)T1Q
RZK)HDR
5CM)H6D
66K)JRP
MGD)KWG
93Q)YSL
FMZ)BH7
QG7)7MZ
JB5)3TR
RGT)W62
X72)Z7W
RWQ)J3F
M8D)3W7
D8B)GS5
HYS)KND
K1M)ZL9
FVD)DTC
4H5)Y2P
ZQN)8C4
1DT)3CT
JRP)GFL
LX9)BVK
2QN)HXZ
WJJ)3SV
GJZ)G44
HWQ)JJM
JTD)MC4
1LC)YL1
83P)JFF
9VH)HT3
TJL)Q6C
JLY)ZFH
8ZG)3GJ
VQP)6TK
B3T)NVC
LRY)P74
3M5)SAN
MZQ)V5R
2WN)D1B
RYR)JVC
PMC)3D6
PJ4)2GB
MV7)JT2
SPN)QFP
4CF)HLY
BCK)L2L
32T)4BZ
4YW)2CC
4G3)Y5M
VYK)QC7
TRY)HRH
SV3)RFW
2SX)5KL
5M6)6XR
XTS)44V
Y42)8Y2
382)DLR
CFD)LCR
5LS)1DB
LXH)HVT
9GX)WF4
787)VQF
1JZ)GDS
YT2)NGM
52B)CD6
G4W)H7P
T9P)TSB
Y2G)QRR
QRH)XLD
LNL)2WJ
5Z8)QVH
GTX)7JJ
368)6KL
31W)WDD
1L8)JDR
GBX)2XT
4VQ)DNB
SPN)K9G
MDX)46P
Y5J)KK9
X3S)4TZ
GF4)JTC
R4P)RVQ
GXZ)XNT
V5G)JC7
QKC)25J
QBW)4KZ
6MX)W51
SQN)PJ3
6XT)DW6
XLW)N2J
C5X)97K
7ZT)95Y
XHW)16H
168)H3N
KXZ)8XG
Q6V)N3R
4YH)G88
QFP)79M
8LM)BVF
S4S)VXT
Y29)5XP
FCP)SZD
CYS)3TH
V9T)7Z7
26L)MWG
NM3)1S1
FYB)SPL
4K3)XFG
7SY)9QP
5KL)CRD
F6P)W6X
M2H)581
1SH)W9W
GNX)LZW
L9L)YX1
2FM)ZGJ
ZGJ)7LL
JWT)9TJ
7XG)G8D
DMP)PHX
M1N)S1T
R1J)XMR
Y99)Y73
8TZ)5XQ
642)37L
6DP)2Y7
B5W)VLC
KTP)RF8
MG6)CP9
7LL)M8D
8JJ)33H
7H1)512
PFM)5RQ
QJK)6DD
T6R)28L
SV3)8VZ
K6V)D9Z
QXY)LQ3
848)2FM
5BZ)LCJ
HM7)RCK
D9Z)RT4
B4P)PF1
ZBH)BVD
2FH)1V2
9HR)FFY
Q4C)1M7
34M)C86
P4W)N7T
94R)NLV
HC1)ZSW
8C2)H76
ZTJ)SK3
HBM)V5Z
C8B)Q4N
1GK)GMP
Y73)7M4
Z7W)S3C
386)FYB
464)RWQ
BHX)MW3
84F)YZJ
79C)N67
6XR)48T
G88)4WL
SXX)VWD
54Z)Q9D
CGR)FCP
16R)B47
5BK)H3J
5GZ)Q4C
XH8)K6Q
VJR)P46
1Q1)DMS
3BG)2KX
V81)TPN
SMP)NGZ
31V)MX5
N3W)VMM
NK2)HYS
8DZ)HG6
XNT)1F7
3QQ)1SH
JKT)1ZH
CKT)G85
5G3)ZX9
B47)C5G
Q8H)17M
VS4)4YW
C3X)S1Z
89P)168
5RR)QQ1
8PM)VW4
QF7)CC4
19L)7ZT
BYP)YT2
LF8)JYY
F2L)8JS
3WT)BYS
HVS)93G
LZ9)FT1
MD7)X5B
DTC)CSZ
9SW)G2J
PHX)JGS
HN1)2FH
D91)QTL
5Z8)1LT
8PM)L4H
CJF)CQQ
9WH)FLF
DND)GN3
XRW)SNZ
YGQ)HKZ
TQQ)3H1
MRD)PH9
TSF)26Q
KHK)WMY
DWX)ZTR
B5S)2JF
LT4)8DL
3MY)HXF
TQQ)B9L
3NT)5X8
64P)GBX
DRL)2PT
TG9)G5R
SK3)SMP
PWB)MG5
GSQ)DGB
5SN)NQZ
T9P)HM7
BN3)2V5
CKN)SXX
ZK2)X5R
97K)Z62
889)MDX
RT4)1LX
2KX)Q4G
95Q)R5Y
9GB)88Y
R67)MW2
CZN)8FV
MLR)7XB
65D)5TJ
VXL)BNS
GWS)SJT
YN4)P2W
MWL)R2B
Y46)CRQ
7Y9)25H
S3C)NTB
V2B)LZ3
27M)HS5
XG5)YMN
2TM)V26
Z1B)WVP
BVM)HGL
PXG)ZYR
J4S)TG9
P46)W3P
Z6M)7YN
6FF)HC1
R2W)S5H
ZP2)G16
7VB)M4C
JVC)84F
3DZ)HT6
QDT)6ZG
3SV)433
XRF)92Z
GWT)HN7
SSV)Z2C
13H)2WC
PFS)6H9
DPV)M7Q
W63)WDK
191)4N7
Y7Y)96N
YT9)VDK
75M)T29
PK1)R9Y
F8P)652
JT2)9GX
HDD)899
554)TGP
ND1)GN6
LLH)QJJ
SK2)TSF
BX1)MDD
Y6M)623
42J)FBY
97B)7XV
K3N)RKV
S5H)KF1
T7R)3TJ
RLF)ZWC
HTR)Y4F
G82)6DN
HMT)SK2
44L)SBV
1S1)PMC
8SF)GJZ
WVP)QWM
NVD)X2W
GQ6)35G
MYD)BJT
H6D)KBG
2CC)9RC
T29)44L
C86)DJH
HGL)75Z
K9G)9VH
5BZ)HGT
8XZ)XJ6
48T)WRK
2DW)9WH
LHH)79B
F35)V22
PBM)CM4
COM)XQY
6PH)PJF
N3J)5NM
W9K)2VK
PF1)4N8
DW9)ZYG
C39)X27
W4Z)D3D
W82)TSM
CHM)K3D
Y3Z)FG5
TNZ)B5S
5SV)ZTW
3X9)XYR
SG6)LTF
PHT)9XT
62Z)9M1
VSF)ZRD
LSZ)XTS
MC5)R1J
JD3)S4S
NVL)GVV
ZYR)TS7
HN7)24K
2WG)ZZK
5NC)5PR
V38)DRQ
6DD)LV1
43K)W9K
TTM)7VB
JTC)6LM
2ZD)3MY
YD8)9SW
RCK)J9J
8WY)YP3
PW3)5WL
DD1)KCT
SQK)ZN5
CX6)X9J
GQ7)5RY
WMY)1JC
GHH)ZK2
QRR)DPV
NNQ)G82
LZW)9WF
BJT)4WY
W49)NHX
9YL)61C
Z18)WZX
MVQ)3K1
5YQ)HGD
KXK)ND1
42R)H2B
SR1)6C5
3TH)63Z
WJ2)34M
35G)3T2
K49)8ZG
628)P4W
DLV)9KV
XCS)Y7Y
M74)585
Q8M)1M8
QX5)ZBH
DC2)2NJ
6T8)BV1
NYX)12F
512)M3L
F97)V38
G2M)C2Y
PFL)DF1
TDT)YVT
CFZ)V9T
NQZ)892
2XT)T6R
59C)DFH
RP3)MG6
NGZ)TW2
92Z)YC8
YVX)KCW
YXH)LJC
S2M)2WN
FBL)LLH
1LX)NB7
D79)R41
V5Z)YDL
7XB)W4L
Q6C)LSZ
T4X)PJS
T9W)6B8
C9N)78P
TJL)4JX
SLD)FRW
DRQ)ZX2
Z7X)YM9
JLR)9TV
9QH)GHP
XST)B3T
YC8)MGP
75Z)77B
VPJ)XTJ
YM5)KH6
4ZM)LHH
LXZ)PJT
VT3)FLT
RL7)7XG
9MW)5NC
12F)G2M
ZTR)D2J
96C)B44
585)LWP
FSD)8C2
WCN)QDT
7BS)D66
3SK)NNY
PH6)2B1
Y9K)MRD
XTJ)5ZZ
M55)2QN
YY9)JQ4
7TH)K4D
548)963
WRZ)Y42
LV1)ZS7
4S5)X3N
5R2)ZSG
64P)9G1
5MJ)X8R
43S)8NR
V8L)2VG
HX3)YR4
HC5)2FD
VSN)V81
TGC)79L
89G)5PQ
31F)D79
CM4)GXL
FRW)QHP
CW3)7GK
MDD)MZ2
LV7)RKS
2VG)F35
VZB)HWQ
C8R)NYS
TW8)DGQ
NZ6)KHL
DR3)HSR
CF9)NVD
KPT)CHM
HBG)65B
R5Y)QNX
NVC)JMB
5PQ)WSH
MSN)LN2
95L)T83
K4D)5BK
JPJ)YVX
XXG)MYD
T3S)M1N
T83)4DV
PQ4)917
2GY)R99
TQS)55Y
NGM)RP3
MV2)C3C
V1S)PLT
12B)31W
QFH)9QB
B7W)7SX
433)FYY
JFF)2SX
M5V)2PG
1M7)56L
LR1)62Z
YKF)WRZ
WMQ)TWQ
28F)61P
PVF)5LS
MQ3)W63
    """.trimIndent()
}