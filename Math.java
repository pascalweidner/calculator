import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class Math {
    public static final double PI = java.lang.Math.PI;
    public static final double E = java.lang.Math.E;
    private static final double INFINITY = Double.POSITIVE_INFINITY;

    public static double nthRoot(double x, int n) {
        var y = 2.0;
        for(int i = 0; i <= 1000; i++) {
            y = ((n-1)*pow(y, n) + x) /(n*pow(y, n - 1));
        }

        return y;
    }

    public static double pow(double x, double y) {
        //TODO eigenes pow
        double z;
        double r, s, t, u, v, w;
        int i, j, k, n;

        // y == zero: x**0 = 1
        if (y == 0.0)
            return 1.0;

        // +/-NaN return x + y to propagate NaN significands
        if (Double.isNaN(x) || Double.isNaN(y))
            return x + y;

        final double y_abs = java.lang.Math.abs(y);
        double x_abs   = java.lang.Math.abs(x);
        // Special values of y
        if (y == 2.0) {
            return x * x;
        } else if (y == 0.5) {
            if (x >= -Double.MAX_VALUE) // Handle x == -infinity later
                return java.lang.Math.sqrt(x + 0.0); // Add 0.0 to properly handle x == -0.0
        } else if (y_abs == 1.0) {        // y is  +/-1
            return (y == 1.0) ? x : 1.0 / x;
        } else if (y_abs == INFINITY) {       // y is +/-infinity
            if (x_abs == 1.0)
                return  y - y;         // inf**+/-1 is NaN
            else if (x_abs > 1.0) // (|x| > 1)**+/-inf = inf, 0
                return (y >= 0) ? y : 0.0;
            else                       // (|x| < 1)**-/+inf = inf, 0
                return (y < 0) ? -y : 0.0;
        }

        final int hx = __HI(x);
        int ix = hx & 0x7fffffff;

        /*
         * When x < 0, determine if y is an odd integer:
         * y_is_int = 0       ... y is not an integer
         * y_is_int = 1       ... y is an odd int
         * y_is_int = 2       ... y is an even int
         */
        int y_is_int  = 0;
        if (hx < 0) {
            if (y_abs >= 0x1.0p53)   // |y| >= 2^53 = 9.007199254740992E15
                y_is_int = 2; // y is an even integer since ulp(2^53) = 2.0
            else if (y_abs >= 1.0) { // |y| >= 1.0
                long y_abs_as_long = (long) y_abs;
                if ( ((double) y_abs_as_long) == y_abs) {
                    y_is_int = 2 -  (int)(y_abs_as_long & 0x1L);
                }
            }
        }

        // Special value of x
        if (x_abs == 0.0 ||
                x_abs == INFINITY ||
                x_abs == 1.0) {
            z = x_abs;                 // x is +/-0, +/-inf, +/-1
            if (y < 0.0)
                z = 1.0/z;     // z = (1/|x|)
            if (hx < 0) {
                if (((ix - 0x3ff00000) | y_is_int) == 0) {
                    z = (z-z)/(z-z); // (-1)**non-int is NaN
                } else if (y_is_int == 1)
                    z = -1.0 * z;             // (x < 0)**odd = -(|x|**odd)
            }
            return z;
        }

        n = (hx >> 31) + 1;

        // (x < 0)**(non-int) is NaN
        if ((n | y_is_int) == 0)
            return (x-x)/(x-x);

        s = 1.0; // s (sign of result -ve**odd) = -1 else = 1
        if ( (n | (y_is_int - 1)) == 0)
            s = -1.0; // (-ve)**(odd int)

        double p_h, p_l, t1, t2;
        // |y| is huge
        if (y_abs > 0x1.00000_ffff_ffffp31) { // if |y| > ~2**31
            final double INV_LN2   =  0x1.7154_7652_b82fep0;   //  1.44269504088896338700e+00 = 1/ln2
            final double INV_LN2_H =  0x1.715476p0;            //  1.44269502162933349609e+00 = 24 bits of 1/ln2
            final double INV_LN2_L =  0x1.4ae0_bf85_ddf44p-26; //  1.92596299112661746887e-08 = 1/ln2 tail

            // Over/underflow if x is not close to one
            if (x_abs < 0x1.fffff_0000_0000p-1) // |x| < ~0.9999995231628418
                return (y < 0.0) ? s * INFINITY : s * 0.0;
            if (x_abs > 0x1.00000_ffff_ffffp0)         // |x| > ~1.0
                return (y > 0.0) ? s * INFINITY : s * 0.0;
            /*
             * now |1-x| is tiny <= 2**-20, sufficient to compute
             * log(x) by x - x^2/2 + x^3/3 - x^4/4
             */
            t = x_abs - 1.0;        // t has 20 trailing zeros
            w = (t * t) * (0.5 - t * (0.3333333333333333333333 - t * 0.25));
            u = INV_LN2_H * t;      // INV_LN2_H has 21 sig. bits
            v =  t * INV_LN2_L - w * INV_LN2;
            t1 = u + v;
            t1 =__LO(t1, 0);
            t2 = v - (t1 - u);
        } else {
            final double CP      =  0x1.ec70_9dc3_a03fdp-1;  //  9.61796693925975554329e-01 = 2/(3ln2)
            final double CP_H    =  0x1.ec709ep-1;           //  9.61796700954437255859e-01 = (float)cp
            final double CP_L    = -0x1.e2fe_0145_b01f5p-28; // -7.02846165095275826516e-09 = tail of CP_H

            double z_h, z_l, ss, s2, s_h, s_l, t_h, t_l;
            n = 0;
            // Take care of subnormal numbers
            if (ix < 0x00100000) {
                x_abs *= 0x1.0p53; // 2^53 = 9007199254740992.0
                n -= 53;
                ix = __HI(x_abs);
            }
            n  += ((ix) >> 20) - 0x3ff;
            j  = ix & 0x000fffff;
            // Determine interval
            ix = j | 0x3ff00000;          // Normalize ix
            if (j <= 0x3988E)
                k = 0;         // |x| <sqrt(3/2)
            else if (j < 0xBB67A)
                k = 1;         // |x| <sqrt(3)
            else {
                k = 0;
                n += 1;
                ix -= 0x00100000;
            }
            x_abs = __HI(x_abs, ix);

            // Compute ss = s_h + s_l = (x-1)/(x+1) or (x-1.5)/(x+1.5)

            final double BP[]    = {1.0,
                    1.5};
            final double DP_H[]  = {0.0,
                    0x1.2b80_34p-1};        // 5.84962487220764160156e-01
            final double DP_L[]  = {0.0,
                    0x1.cfde_b43c_fd006p-27};// 1.35003920212974897128e-08

            // Poly coefs for (3/2)*(log(x)-2s-2/3*s**3
            final double L1      =  0x1.3333_3333_33303p-1;  //  5.99999999999994648725e-01
            final double L2      =  0x1.b6db_6db6_fabffp-2;  //  4.28571428578550184252e-01
            final double L3      =  0x1.5555_5518_f264dp-2;  //  3.33333329818377432918e-01
            final double L4      =  0x1.1746_0a91_d4101p-2;  //  2.72728123808534006489e-01
            final double L5      =  0x1.d864_a93c_9db65p-3;  //  2.30660745775561754067e-01
            final double L6      =  0x1.a7e2_84a4_54eefp-3;  //  2.06975017800338417784e-01
            u = x_abs - BP[k];               // BP[0]=1.0, BP[1]=1.5
            v = 1.0 / (x_abs + BP[k]);
            ss = u * v;
            s_h = ss;
            s_h = __LO(s_h, 0);
            // t_h=x_abs + BP[k] High
            t_h = 0.0;
            t_h = __HI(t_h, ((ix >> 1) | 0x20000000) + 0x00080000 + (k << 18) );
            t_l = x_abs - (t_h - BP[k]);
            s_l = v * ((u - s_h * t_h) - s_h * t_l);
            // Compute log(x_abs)
            s2 = ss * ss;
            r = s2 * s2* (L1 + s2 * (L2 + s2 * (L3 + s2 * (L4 + s2 * (L5 + s2 * L6)))));
            r += s_l * (s_h + ss);
            s2  = s_h * s_h;
            t_h = 3.0 + s2 + r;
            t_h = __LO(t_h, 0);
            t_l = r - ((t_h - 3.0) - s2);
            // u+v = ss*(1+...)
            u = s_h * t_h;
            v = s_l * t_h + t_l * ss;
            // 2/(3log2)*(ss + ...)
            p_h = u + v;
            p_h = __LO(p_h, 0);
            p_l = v - (p_h - u);
            z_h = CP_H * p_h;             // CP_H + CP_L = 2/(3*log2)
            z_l = CP_L * p_h + p_l * CP + DP_L[k];
            // log2(x_abs) = (ss + ..)*2/(3*log2) = n + DP_H + z_h + z_l
            t = (double)n;
            t1 = (((z_h + z_l) + DP_H[k]) + t);
            t1 = __LO(t1, 0);
            t2 = z_l - (((t1 - t) - DP_H[k]) - z_h);
        }

        // Split up y into (y1 + y2) and compute (y1 + y2) * (t1 + t2)
        double y1  = y;
        y1 = __LO(y1, 0);
        p_l = (y - y1) * t1 + y * t2;
        p_h = y1 * t1;
        z = p_l + p_h;
        j = __HI(z);
        i = __LO(z);
        if (j >= 0x40900000) {                           // z >= 1024
            if (((j - 0x40900000) | i)!=0)               // if z > 1024
                return s * INFINITY;                     // Overflow
            else {
                final double OVT     =  8.0085662595372944372e-0017; // -(1024-log2(ovfl+.5ulp))
                if (p_l + OVT > z - p_h)
                    return s * INFINITY;   // Overflow
            }
        } else if ((j & 0x7fffffff) >= 0x4090cc00 ) {        // z <= -1075
            if (((j - 0xc090cc00) | i)!=0)           // z < -1075
                return s * 0.0;           // Underflow
            else {
                if (p_l <= z - p_h)
                    return s * 0.0;      // Underflow
            }
        }
        /*
         * Compute 2**(p_h+p_l)
         */
        // Poly coefs for (3/2)*(log(x)-2s-2/3*s**3
        final double P1      =  0x1.5555_5555_5553ep-3;  //  1.66666666666666019037e-01
        final double P2      = -0x1.6c16_c16b_ebd93p-9;  // -2.77777777770155933842e-03
        final double P3      =  0x1.1566_aaf2_5de2cp-14; //  6.61375632143793436117e-05
        final double P4      = -0x1.bbd4_1c5d_26bf1p-20; // -1.65339022054652515390e-06
        final double P5      =  0x1.6376_972b_ea4d0p-25; //  4.13813679705723846039e-08
        final double LG2     =  0x1.62e4_2fef_a39efp-1;  //  6.93147180559945286227e-01
        final double LG2_H   =  0x1.62e43p-1;            //  6.93147182464599609375e-01
        final double LG2_L   = -0x1.05c6_10ca_86c39p-29; // -1.90465429995776804525e-09
        i = j & 0x7fffffff;
        k = (i >> 20) - 0x3ff;
        n = 0;
        if (i > 0x3fe00000) {              // if |z| > 0.5, set n = [z + 0.5]
            n = j + (0x00100000 >> (k + 1));
            k = ((n & 0x7fffffff) >> 20) - 0x3ff;     // new k for n
            t = 0.0;
            t = __HI(t, (n & ~(0x000fffff >> k)) );
            n = ((n & 0x000fffff) | 0x00100000) >> (20 - k);
            if (j < 0)
                n = -n;
            p_h -= t;
        }
        t = p_l + p_h;
        t = __LO(t, 0);
        u = t * LG2_H;
        v = (p_l - (t - p_h)) * LG2 + t * LG2_L;
        z = u + v;
        w = v - (z - u);
        t  = z * z;
        t1  = z - t * (P1 + t * (P2 + t * (P3 + t * (P4 + t * P5))));
        r  = (z * t1)/(t1 - 2.0) - (w + z * w);
        z  = 1.0 - (r - z);
        j  = __HI(z);
        j += (n << 20);
        if ((j >> 20) <= 0)
            z = java.lang.Math.scalb(z, n); // subnormal output
        else {
            int z_hi = __HI(z);
            z_hi += (n << 20);
            z = __HI(z, z_hi);
        }
        return s * z;
    }

    private static int __HI(double x) {
        long transducer = Double.doubleToRawLongBits(x);
        return (int)(transducer >> 32);
    }

    private static double __HI(double x, int high) {
        long transX = Double.doubleToRawLongBits(x);
        return Double.longBitsToDouble((transX & 0x0000_0000_FFFF_FFFFL) |
                ( ((long)high)) << 32 );
    }

    private static int __LO(double x) {
        long transducer = Double.doubleToRawLongBits(x);
        return (int)transducer;
    }

    private static double __LO(double x, int low) {
        long transX = Double.doubleToRawLongBits(x);
        return Double.longBitsToDouble((transX & 0xFFFF_FFFF_0000_0000L) |
                (low    & 0x0000_0000_FFFF_FFFFL));
    }

    public static double abs(double a) {
        if(a < 0) {
            return a * -1;
        }
        else {
            return a;
        }
    }

    public static double exp(double x) {
        double result = 0;
        for(int i = 0; i < 100; i++) {
            result += pow(x, i) / fak(i);
        }

        return result;
    }

    public static double ln(double a, int n) {
        double res = a;
        ArrayList<Double> list = new ArrayList<>();
        while(res > 4) {
            list.add(2.0);
            res /= 2;
        }
        list.add(res);

        double result = 0.0;
        double ln2 = 0.69314718055994530941723212145818;
        Double[] A_e = {0.693147180559945297099404706000, 0.405465108108164392935428259000, 0.223143551314209769962616701000,
                0.117783035656383447138088388000, 0.060624621816434840186291518000, 0.030771658666753686222134530000,
                0.015504186535965253358272343000, 0.007782140442054949100825041000, 0.003898640415657322636221046000,
                0.001951220131261749216850870000, 0.000976085973055458892686544000, 0.000488162079501351186957460000,
                0.000244110827527362687853374000, 0.000122062862525677363338881000, 0.000061033293680638525913091000,
                0.000030517112473186377476993000, 0.000015258672648362398138404000, 0.000007629365427567572417821000,
                0.000003814689989685889381171000, 0.000001907346813825409407938000, 0.000000953673861659188260005000,
                0.000000476837044516323000000000, 0.000000238418550679858000000000, 0.000000119209282445354000000000,
                0.000000059604642999033900000000, 0.000000029802321943606100000000, 0.000000014901161082825400000000,
                0.000000007450580569168250000000, 0.000000003725290291523020000000, 0.000000001862645147496230000000,
                0.000000000931322574181798000000, 0.000000000465661287199319000000, 0.000000000232830643626765000000,
                0.000000000116415321820159000000, 0.000000000058207660911773300000, 0.000000000029103830456310200000,
                0.000000000014551915228261000000, 0.000000000007275957614156960000, 0.000000000003637978807085100000,
                0.000000000001818989403544200000, 0.000000000000909494701772515000, 0.000000000000454747350886361000,
                0.000000000000227373675443206000, 0.000000000000113686837721610000, 0.000000000000056843418860806400,
                0.000000000000028421709430403600, 0.000000000000014210854715201900, 0.000000000000007105427357600980,
                0.000000000000003552713678800490, 0.000000000000001776356839400250, 0.000000000000000888178419700125,
                0.000000000000000444089209850063, 0.000000000000000222044604925031, 0.000000000000000111022302462516,
                0.000000000000000055511151231258, 0.000000000000000027755575615629, 0.000000000000000013877787807815,
                0.000000000000000006938893903907, 0.000000000000000003469446951954, 0.000000000000000001734723475977,
                0.000000000000000000867361737988, 0.000000000000000000433680868994, 0.000000000000000000216840434497,
                0.000000000000000000108420217249, 0.000000000000000000054210108624, 0.000000000000000000027105054312
        };

        for(double i : list) {
            double x = 1.0;
            double y = 0.0;
            double s = 1.0;

            if(i == 2.0) {
                y = ln2;
            }
            else {
                for(int k = 0; k <= n; k++) {
                    double z = x + x*s;
                    if(z <= i) {
                        x = z;
                        y += A_e[k];
                    }
                    s *= 0.5;
                }
            }

            result += y;
        }

        return result;
    }

    public static double log(double b, double x) {
        return ln(x, 65) / ln(b, 65);
    }

    public static double fak(int n) {
        double result = 1;
        for(int i = 1; i <= n; i++) {
            result = result * i;
        }
        return result;
    }

    public static double sqrt(double a) {
        return nthRoot(a, 2);
    }


    public static double sin(double x) {
        double x1 = x;
        while(x1 >= 2*PI) {
            x1 -= (2 * PI);
        }

        if(x1 <= PI && x1 > 0.5 * PI) {
            x1 = java.lang.Math.abs(x1 - PI);
        }
        else if(x1 <= 1.5 * PI && x1 > PI) {
            //x1 - PI = a
            x1 = -1 * (x1 - PI);
        }
        else if(x1 <= 2*PI && x1 > 1.5 * PI) {
            //2PI - a = x1
            x1 = -1 * java.lang.Math.abs(x1 - 2*PI);
        }

        double result = 0.0;

        for(int n = 0; n < 85; n++) {
            result += pow(-1.0, n) * (pow(x1, 2 * n + 1) / fak(2 * n + 1));
        }

        return result;
    }

    public static double cos(double x) {
        //TODO negative values
        double x1 = x;
        while(x1 >= 2*PI) {
            x1 -= (2 * PI);
        }

        if(x1 <= PI && x1 > 0.5 * PI) {
            x1 = -1 * java.lang.Math.abs(x1 - PI);
        }
        else if(x1 <= 1.5 * PI && x1 > PI) {
            //x1 - PI = a
            x1 = -1 * (x1 - PI);
        }
        else if(x1 <= 2*PI && x1 > 1.5 * PI) {
            //2PI - a = x1
            x1 = java.lang.Math.abs(x1 - 2*PI);
        }

        double result = 0.0;

        for(int n = 0; n < 85; n++) {
            result += pow(-1.0, n) * (pow(x1, 2 * n) / fak(2 * n));
        }

        return result;
    }

    public static double tan(double x) {
        return sin(x) / cos(x);
    }

    public static double cot(double x) {
        return cos(x) / sin(x);
    }

    public static double sec(double x) {
        double result = 0.0;
        for(int k = 0; k < 4000; k++) {
            result += (pow(-1, k) * (2 * k + 1)) / (pow(2 * k + 1, 2) * pow(PI, 2) - 4 * pow(x, 2));
        }

        return result * 4 * PI;
    }

    public static double csc(double x) {
        double result = 0.0;
        for(int k = 1; k < 4000; k++){
            result += pow(-1, k) / (pow(k, 2) * pow(PI, 2) - pow(x, 2));
        }

        return (1 / x) - 2 * x * result;
    }


    public static double arcsin(double x) {
        if(x == 1.0) {
            return PI * 0.5;
        }
        double result = 0.0;
        for(int n = 0; n < 85; n++)
        {
            result += (fak(2 * n) / (pow(2, 2 * n) * pow(fak(n), 2) * (2 * n + 1))) * pow(x, 2 * n + 1);
        }

        return result;
    }

    public static double arccos(double x) {
        return PI / 2 - arcsin(x);
    }

    public static double arctan(double x) {
        double result = 0.0;
        for(int n = 0; n < 10000; n++) {
            result += pow(-1, n) * (1 / (2 * n + 1.0)) * pow(x, 2 * n + 1);
        }

        return result;
    }


    public static double sinh(double z) {
        double result = 0.0;
        for(int n = 0; n < 84; n++) {
            result += pow(z, 2 * n + 1) / fak(2 * n + 1);
        }

        return result;
    }

    public static double cosh(double z) {
        double result = 0.0;
        for(int n = 0; n < 84; n++) {
            result += pow(z, 2 * n) / fak(2 * n);
        }

        return result;
    }

    public static double tanh(double z) {
        return sinh(z) / cosh(z);
    }

    public static double coth(double z) {
        return cosh(z) / sinh(z);
    }

    public static double sech(double x) {
        double result = 0.0;
        for(int k = 0; k < 2000; k++) {
            result += pow(-1, k) * (((8 * k + 4) * PI) / (pow(2 * k + 1, 2) * pow(PI, 2) + 4 * pow(x, 2)));
        }
        return result;
    }

    public static double csch(double x) {
        double result = 1 / x;
        for(int k = 1; k < 2000; k++) {
            result += pow(-1, k) * ((2 * x) / (pow(k, 2) * pow(PI, 2) + pow(x, 2)));
        }

        return result;
    }

}
