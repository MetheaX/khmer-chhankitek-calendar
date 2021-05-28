package io.github.metheax;

import io.github.metheax.exception.SotinException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.metheax.constant.Constant.LUNAR_MONTHS;

/**
 * Author: Kuylim TITH
 * Date: 5/27/2021
 */
public class KhmerNewYearCal {

    private final YearInfo info;
    private final int jsYear;
    private final KhmerNewYear khmerNewYear;

    public KhmerNewYearCal(int jsYear) {
        this.jsYear = jsYear;
        this.info = getInfo(jsYear);
        khmerNewYear = new KhmerNewYear(
                info.harkun,
                info.kromathopol,
                info.avaman,
                info.bodithey,
                getHas366day(jsYear),
                getIsAthikameas(jsYear),
                getIsChantreathimeas(jsYear),
                jesthHas30(jsYear),
                getDayLerngSak(),
                getLunarDateLerngSak(),
                getNewYearDaySotins(),
                getNewYearTime()
        );
    }

    public KhmerNewYear getKhmerNewYear() {
        return this.khmerNewYear;
    }

    /**
     * គណនា ហារគុន Kromathopol អវមាន និង បូតិថី
     *
     * @param jsYear
     * @returns {{bodithey: number, avaman: number, kromathopol: number, harkun: number}}
     */
    private YearInfo getInfo(int jsYear) {
        int h = 292207 * jsYear + 373;
        int harkun = (int) Math.floor(h / 800d) + 1;
        int kromathopol = 800 - (h % 800);

        int a = (11 * harkun) + 650;
        int avaman = a % 692;
        int bodithey = (int) (harkun + Math.floor((a / 692d))) % 30;
        return new YearInfo(harkun, kromathopol, avaman, bodithey);
    }

    /**
     * ឆ្នាំចុល្លសករាជដែលមាន៣៦៦ថ្ងៃ
     *
     * @param jsYear
     * @returns {boolean}
     */
    private boolean getHas366day(int jsYear) {
        YearInfo infoOfYear = getInfo(jsYear);
        return infoOfYear.kromathopol <= 207;
    }

    /**
     * រកឆ្នាំអធិកមាស
     *
     * @param jsYear
     * @returns {boolean}
     */
    private boolean getIsAthikameas(int jsYear) {
        YearInfo infoOfYear = getInfo(jsYear);
        YearInfo infoOfNextYear = getInfo((jsYear + 1));
        return (!(infoOfYear.bodithey == 25 && infoOfNextYear.bodithey == 5) &&
                (infoOfYear.bodithey > 24 ||
                        infoOfYear.bodithey < 6 ||
                        (infoOfYear.bodithey == 24 &&
                                infoOfNextYear.bodithey == 6
                        )
                )
        );
    }

    /**
     * រកឆ្នាំចន្ទ្រាធិមាស
     *
     * @param jsYear
     * @returns {boolean}
     */
    private boolean getIsChantreathimeas(int jsYear) {
        YearInfo infoOfYear = getInfo(jsYear);
        YearInfo infoOfNextYear = getInfo((jsYear + 1));
        YearInfo infoOfPreviousYear = getInfo(jsYear);
        boolean has366day = getHas366day(jsYear);
        return ((has366day && infoOfYear.avaman < 127) ||
                (!(infoOfYear.avaman == 137 &&
                        infoOfNextYear.avaman == 0) &&
                        ((!has366day &&
                                infoOfYear.avaman < 138) ||
                                (infoOfPreviousYear.avaman == 137 &&
                                        infoOfYear.avaman == 0
                                )
                        )
                )
        );
    }

    /**
     * ឆែកមើលថាជាឆ្នាំដែលខែជេស្ឋមាន៣០ថ្ងៃឬទេ
     *
     * @type {boolean}
     */
    private boolean jesthHas30(int jsYear) {
        boolean tmp = getIsChantreathimeas(jsYear);
        if (getIsAthikameas(jsYear) && getIsChantreathimeas(jsYear)) {
            tmp = false;
        }
        if (!getIsChantreathimeas(jsYear) && getIsAthikameas(jsYear - 1) && getIsChantreathimeas(jsYear - 1)) {
            tmp = true;
        }
        return tmp;
    }

    /**
     * រកមើលថាតើថ្ងៃឡើងស័កចំថ្ងៃអ្វី
     *
     * @type {int}
     */
    private int getDayLerngSak() {
        return (info.harkun - 2) % 7;
    }

    /**
     * គណនារកថ្ងៃឡើងស័ក
     *
     * @type {{month, day}}
     */
    private LunarDateLerngSak getLunarDateLerngSak() {
        int bodithey = info.bodithey;
        if (getIsAthikameas(jsYear - 1) && getIsChantreathimeas(jsYear - 1)) {
            bodithey = (bodithey + 1) % 30;
        }
        return new LunarDateLerngSak(
                bodithey >= 6 ? (bodithey - 1) : bodithey,
                bodithey >= 6 ? LUNAR_MONTHS.get("ចេត្រ") : LUNAR_MONTHS.get("ពិសាខ")
        );
    }

    private SunInfo getSunInfo(int sotin) { // សុទិន
        YearInfo infoOfPreviousYear = getInfo(jsYear - 1);
        // ១ រាសី = ៣០ អង្សា
        // ១ អង្សា = ៦០ លិប្ដា
        // មធ្យមព្រះអាទិត្យ គិតជាលិប្ដា
        int sunAverageAsLibda = getSunAverageAsLibda(sotin, infoOfPreviousYear);

        int leftOver = getLeftOver(sunAverageAsLibda);

        int kaen = (int) Math.floor(leftOver / (30d * 60d));

        LastOver lastLeftOver = getLastLeftOver(kaen, leftOver);

        // ខណ្ឌ និង pouichalip
        int khan = 0;
        int pouichalip = 0;

        if (lastLeftOver.angsar >= 15) {
            khan = 2 * lastLeftOver.reasey + 1;
            pouichalip = 60 * (lastLeftOver.angsar - 15) + lastLeftOver.libda;
        } else {
            khan = 2 * lastLeftOver.reasey;
            pouichalip = 60 * lastLeftOver.angsar + lastLeftOver.libda;
        }

        // phol
        Phol phol = getPhol(khan, pouichalip);

        int sunInaugurationAsLibda = 0; // សម្ពោធព្រះអាទិត្យ
        int pholAsLibda = (30 * 60 * phol.reasey) + (60 * phol.angsar) + phol.libda;
        if (kaen <= 5) {
            sunInaugurationAsLibda = sunAverageAsLibda - pholAsLibda;
        } else {
            sunInaugurationAsLibda = sunAverageAsLibda + pholAsLibda;
        }
        return new SunInfo(sunAverageAsLibda, khan, pouichalip, phol, sunInaugurationAsLibda);
    }

    private List<NewYearDaySotins> getNewYearDaySotins() { // ចំនួនថ្ងៃវ័នបត
        List<Integer> sotins = getHas366day(jsYear - 1) ?
                new ArrayList<>(Arrays.asList(363, 364, 365, 366)) :
                new ArrayList<>(Arrays.asList(362, 363, 364, 365)); // សុទិន
        List<NewYearDaySotins> newYearDaySotins = new ArrayList<>();
        for (int i : sotins) {
            SunInfo sunInfo = getSunInfo(i);
            int reasey = (int) Math.floor(sunInfo.sunInaugurationAsLibda / (30d * 60d));
            int angsar = (int) Math.floor((sunInfo.sunInaugurationAsLibda % (30d * 60d)) / (60d)); // អង្សាស្មើសូន្យ គីជាថ្ងៃចូលឆ្នាំ, មួយ ឬ ពីរ ថ្ងៃបន្ទាប់ជាថ្ងៃវ័នបត ហើយ ថ្ងៃចុងក្រោយគីឡើងស័ក
            int libda = sunInfo.sunInaugurationAsLibda % 60;
            newYearDaySotins.add(new NewYearDaySotins(i, reasey, angsar, libda));
        }
        return newYearDaySotins;
    }

    private int getSunAverageAsLibda(int sotin, YearInfo info) {
        int r2 = 800 * sotin + info.kromathopol;
        int reasey = (int) Math.floor(r2 / 24350d); // រាសី
        int r3 = r2 % 24350;
        int angsar = (int) Math.floor(r3 / 811d); // អង្សា
        int r4 = r3 % 811;
        int l1 = (int) Math.floor(r4 / 14d);
        int libda = l1 - 3; // លិប្ដា
        return (30 * 60 * reasey) + (60 * angsar) + libda;
    }

    private int getLeftOver(int sunAverageAsLibda) {
        int s1 = ((30 * 60 * 2) + (60 * 20));
        int leftOver = sunAverageAsLibda - s1; // មធ្យមព្រះអាទិត្យ - R2.A20.L0
        if (sunAverageAsLibda < s1) { // បើតូចជាង ខ្ចី ១២ រាសី
            leftOver += (30 * 60 * 12);
        }
        return leftOver;
    }

    private LastOver getLastLeftOver(int kaen, int leftOver) {
        int rs = -1;
        List<Integer> l1 = new ArrayList<>(Arrays.asList(0, 1, 2));
        List<Integer> l2 = new ArrayList<>(Arrays.asList(3, 4, 5));
        List<Integer> l3 = new ArrayList<>(Arrays.asList(6, 7, 8));
        List<Integer> l4 = new ArrayList<>(Arrays.asList(9, 10, 11));
        if (l1.contains(kaen)) {
            rs = kaen;
        } else if (l2.contains(kaen)) {
            rs = (30 * 60 * 6) - leftOver; // R6.A0.L0 - leftover
        } else if (l3.contains(kaen)) {
            rs = leftOver - (30 * 60 * 6); // leftover - R6.A0.L0
        } else if (l4.contains(kaen)) {
            rs = ((30 * 60 * 11) + (60 * 29) + 60) - leftOver; // R11.A29.L60 - leftover
        }
        return new LastOver((int) Math.floor(rs / (30d * 60d)), (int) Math.floor((rs % (30d * 60d)) / (60d)), rs % 60);
    }

    private Phol getPhol(int khan, int pouichalip) {
        int multiplicity = 0;
        int chhaya = 0;
        int[] multiplicities = {35, 32, 27, 22, 13, 5};
        int[] chhayas = {0, 35, 67, 94, 116, 129};

        switch (khan) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                multiplicity = multiplicities[khan];
                chhaya = chhayas[khan];
                break;
            default:
                chhaya = 134;
                break;
        }

        int q = (int) Math.floor((pouichalip * multiplicity) / 900d);

        return new Phol(0, (int) Math.floor((q + chhaya) / 60d), (q + chhaya) % 60);
    }

    private NewYearTime getNewYearTime() {
        List<NewYearDaySotins> sotinNewYear = getNewYearDaySotins().stream()
                .filter(o -> o.angsar == 0).collect(Collectors.toList());
        if (!sotinNewYear.isEmpty()) {
            int libda = sotinNewYear.get(0).libda; // ២៤ ម៉ោង មាន ៦០លិប្ដា
            int minutes = (24 * 60) - (libda * 24);
            return new NewYearTime((int) Math.floor(minutes / 60d), (minutes % 60));
        } else {
            throw new SotinException("Wrong calculation on new years hour. No sotin with angsar = 0");
        }
    }

    public static class YearInfo {
        private final int harkun;
        private final int kromathopol;
        private final int avaman;
        private final int bodithey;

        public YearInfo(int harkun, int kromathopol, int avaman, int bodithey) {
            this.harkun = harkun;
            this.kromathopol = kromathopol;
            this.avaman = avaman;
            this.bodithey = bodithey;
        }

        public int getHarkun() {
            return harkun;
        }

        public int getKromathopol() {
            return kromathopol;
        }

        public int getAvaman() {
            return avaman;
        }

        public int getBodithey() {
            return bodithey;
        }
    }

    public static class LunarDateLerngSak {
        private final int day;
        private final int month;

        public LunarDateLerngSak(int day, int month) {
            this.day = day;
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public int getMonth() {
            return month;
        }
    }

    public static class Phol {
        private final int reasey;
        private final int angsar;
        private final int libda;

        public Phol(int reasey, int angsar, int libda) {
            this.reasey = reasey;
            this.angsar = angsar;
            this.libda = libda;
        }

        public int getReasey() {
            return reasey;
        }

        public int getAngsar() {
            return angsar;
        }

        public int getLibda() {
            return libda;
        }
    }

    public static class NewYearTime {
        private final int hour;
        private final int minute;

        public NewYearTime(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }
    }

    public static class NewYearDaySotins {
        private final int sotin;
        private final int reasey;
        private final int angsar;
        private final int libda;

        public NewYearDaySotins(int sotin, int reasey, int angsar, int libda) {
            this.sotin = sotin;
            this.reasey = reasey;
            this.angsar = angsar;
            this.libda = libda;
        }

        public int getSotin() {
            return sotin;
        }

        public int getReasey() {
            return reasey;
        }

        public int getAngsar() {
            return angsar;
        }

        public int getLibda() {
            return libda;
        }
    }

    public static class SunInfo {
        private final int sunAverageAsLibda;
        private final int khan;
        private final int pouichalip;
        private final Phol phol;
        private final int sunInaugurationAsLibda;

        public SunInfo(int sunAverageAsLibda, int khan, int pouichalip, Phol phol, int sunInaugurationAsLibda) {
            this.sunAverageAsLibda = sunAverageAsLibda;
            this.khan = khan;
            this.pouichalip = pouichalip;
            this.phol = phol;
            this.sunInaugurationAsLibda = sunInaugurationAsLibda;
        }

        public int getSunAverageAsLibda() {
            return sunAverageAsLibda;
        }

        public int getKhan() {
            return khan;
        }

        public int getPouichalip() {
            return pouichalip;
        }

        public Phol getPhol() {
            return phol;
        }

        public int getSunInaugurationAsLibda() {
            return sunInaugurationAsLibda;
        }
    }

    public static class LastOver {
        private final int reasey;
        private final int angsar;
        private final int libda;

        public LastOver(int reasey, int angsar, int libda) {
            this.reasey = reasey;
            this.angsar = angsar;
            this.libda = libda;
        }

        public int getReasey() {
            return reasey;
        }

        public int getAngsar() {
            return angsar;
        }

        public int getLibda() {
            return libda;
        }
    }

    public static class KhmerNewYear {
        private final int harkun;
        private final int kromathopol;
        private final int avaman;
        private final int bodithey;
        private final boolean has366day; // សុរិយគតិខ្មែរ
        private final boolean isAthikameas; // 13 months
        private final boolean isChantreathimeas; // 30ថ្ងៃនៅខែជេស្ឋ
        private final boolean jesthHas30; // ខែជេស្ឋមាន៣០ថ្ងៃ
        private final int dayLerngSak; // ថ្ងៃឡើងស័ក ច័ន្ទ អង្គារ ...
        private final LunarDateLerngSak lunarDateLerngSak; // ថ្ងៃទី ខែ ឡើងស័ក
        private final List<NewYearDaySotins> newYearsDaySotins; // សុទិនសម្រាប់គណនាថ្ងៃចូលឆ្នាំ ថ្ងៃវ័នបត និង ថ្ងៃឡើងស័ក
        private final NewYearTime timeOfNewYear; // ម៉ោងទេវតាចុះ

        public KhmerNewYear(int harkun, int kromathopol, int avaman, int bodithey, boolean has366day, boolean isAthikameas,
                            boolean isChantreathimeas, boolean jesthHas30, int dayLerngSak, LunarDateLerngSak lunarDateLerngSak,
                            List<NewYearDaySotins> newYearsDaySotins, NewYearTime timeOfNewYear) {
            this.harkun = harkun;
            this.kromathopol = kromathopol;
            this.avaman = avaman;
            this.bodithey = bodithey;
            this.has366day = has366day;
            this.isAthikameas = isAthikameas;
            this.isChantreathimeas = isChantreathimeas;
            this.jesthHas30 = jesthHas30;
            this.dayLerngSak = dayLerngSak;
            this.lunarDateLerngSak = lunarDateLerngSak;
            this.newYearsDaySotins = newYearsDaySotins;
            this.timeOfNewYear = timeOfNewYear;
        }

        public int getHarkun() {
            return harkun;
        }

        public int getKromathopol() {
            return kromathopol;
        }

        public int getAvaman() {
            return avaman;
        }

        public int getBodithey() {
            return bodithey;
        }

        public boolean isHas366day() {
            return has366day;
        }

        public boolean isAthikameas() {
            return isAthikameas;
        }

        public boolean isChantreathimeas() {
            return isChantreathimeas;
        }

        public boolean isJesthHas30() {
            return jesthHas30;
        }

        public int getDayLerngSak() {
            return dayLerngSak;
        }

        public LunarDateLerngSak getLunarDateLerngSak() {
            return lunarDateLerngSak;
        }

        public List<NewYearDaySotins> getNewYearsDaySotins() {
            return newYearsDaySotins;
        }

        public NewYearTime getTimeOfNewYear() {
            return timeOfNewYear;
        }
    }
}
