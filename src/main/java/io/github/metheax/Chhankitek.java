package io.github.metheax;

import io.github.metheax.domain.KhmerLunarDate;
import io.github.metheax.exception.KhmerMonthException;
import io.github.metheax.exception.VisakhabocheaException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import static io.github.metheax.constant.Constant.*;
import static io.github.metheax.utils.ChhankitekUtils.convertIntegerToKhmerNumber;
import static io.github.metheax.utils.ChhankitekUtils.getDayOfWeekInKhmer;

/**
 * Author: Kuylim TITH
 * Date: 5/27/2021
 */
public class Chhankitek {

    private static LocalDateTime newYearDateTime;
    private static final DateTimeFormatter yearFormat = DateTimeFormatter.ofPattern("YYYY");

    private Chhankitek() {
    }

    /**
     * Bodithey: បូតិថី
     * Bodithey determines if a given beYear is a leap-month year. Given year target year in Buddhist Era
     * @return int (0-29)
     */
    private static int getBodithey(int beYear) {
        int ahk = getAharkun(beYear);
        int avml = (int) Math.floor((11d * ahk + 25d) / 692d);
        int m = (avml + ahk + 29);
        return (m % 30);
    }

    /**
     * Avoman: អាវមាន
     * Avoman determines if a given year is a leap-day year. Given a year in Buddhist Era as denoted as adYear
     * @param beYear (0 - 691)
     */
    private static int getAvoman(int beYear) {
        int ahk = getAharkun(beYear);
        return ((11 * ahk) + 25) % 692;
    }

    /**
     * Aharkun: អាហារគុណ ឬ ហារគុណ
     * Aharkun is used for Avoman and Bodithey calculation below. Given adYear as a target year in Buddhist Era
     * @param beYear
     * @returns {int}
     */
    private static int getAharkun(int beYear) {
        int t = (beYear * 292207) + 499;
        return (int) Math.floor(t / 800d) + 4;
    }

    /**
     * Kromathupul
     * @param beYear
     * @returns {int} (1-800)
     */
    private static int kromthupul(int beYear) {
        int ah = getAharkunMod(beYear);
        return 800 - ah;
    }

    /**
     * isKhmerSolarLeap
     * @param beYear
     * @returns {int}
     */
    private static int isKhmerSolarLeap(int beYear) {
        int krom = kromthupul(beYear);
        if (krom <= 207) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * getAkhakunMod
     * @param beYear
     * @returns {int}
     */
    private static int getAharkunMod(int beYear) {
        int t = (beYear * 292207) + 499;
        return t % 800;
    }

    /**
     * * Regular if year has 30 day
     * * leap month if year has 13 months
     * * leap day if Jesth month of the year has 1 extra day
     * * leap day and month: both of them
     * @param adYear
     * @returns {int} return 0:regular, 1:leap month, 2:leap day, 3:leap day and month
     */
    private static int getBoditheyLeap(int adYear) {
        int result = 0;

        int avoman = getAvoman(adYear);
        int bodithey = getBodithey(adYear);

        // check bodithey leap month
        int boditheyLeap = 0;
        if (bodithey >= 25 || bodithey <= 5) {
            boditheyLeap = 1;
        }

        // check for avoman leap-day based on gregorian leap
        int avomanLeap = 0;
        if (isKhmerSolarLeap(adYear) == 1) {
            if (avoman <= 126) {
                avomanLeap = 1;
            }
        } else {
            if (avoman <= 137) {
                // check for avoman case 137/0, 137 must be normal year (p.26)
                if (getAvoman(adYear + 1) == 0) {
                    avomanLeap = 0;
                } else {
                    avomanLeap = 1;
                }
            }
        }

        // case of 25/5 consecutively
        // only bodithey 5 can be leap-month, so set bodithey 25 to none
        if (bodithey == 25) {
            int nextBodithey = getBodithey(adYear + 1);
            if (nextBodithey == 5) {
                boditheyLeap = 0;
            }
        }

        // case of 24/6 consecutively, 24 must be leap-month
        if (bodithey == 24) {
            int nextBodithey = getBodithey(adYear + 1);
            if (nextBodithey == 6) {
                boditheyLeap = 1;
            }
        }

        // format leap result (0:regular, 1:month, 2:day, 3:both)
        if (boditheyLeap == 1 && avomanLeap == 1) {
            result = 3;
        } else if (boditheyLeap == 1) {
            result = 1;
        } else if (avomanLeap == 1) {
            result = 2;
        }

        return result;
    }

    /**
     * bodithey leap can be both leap-day and leap-month but following the khmer calendar rule, they can't be together on the same year, so leap day must be delayed to next year
     * @param beYear
     * @returns {int}
     */
    private static int getProtetinLeap(int beYear) { // return 0:regular, 1:leap month, 2:leap day (no leap month and day together)
        int b = getBoditheyLeap(beYear);
        if (b == 3) {
            return 1;
        }
        if (b == 2 || b == 1) {
            return b;
        }
        // case of previous year is 3
        if (getBoditheyLeap(beYear - 1) == 3) {
            return 2;
        }
        // normal case
        return 0;
    }

    /**
     * Maximum number of day in Khmer Month
     * @param beMonth
     * @param beYear
     * @returns {int}
     */
    private static int getNumberOfDayInKhmerMonth(int beMonth, int beYear) {
        if (beMonth == LUNAR_MONTHS.get("ជេស្ឋ") && isKhmerLeapDay(beYear)) {
            return 30;
        }
        if (beMonth == LUNAR_MONTHS.get("បឋមាសាឍ") || beMonth == LUNAR_MONTHS.get("ទុតិយាសាឍ")) {
            return 30;
        }
        // មិគសិរ : 29 , បុស្ស : 30 , មាឃ : 29 .. 30 .. 29 ..30 .....
        return beMonth % 2 == 0 ? 29 : 30;
    }

    /**
     * A year with an extra day is called Chhantrea Thimeas (ចន្ទ្រាធិមាស) or Adhikavereak (អធិកវារៈ). This year has 355 days.
     * @param beYear
     * @returns {boolean}
     */
    private static boolean isKhmerLeapDay(int beYear) {
        return getProtetinLeap(beYear) == 2;
    }

    /**
     * A year with an extra month is called Adhikameas (អធិកមាស). This year has 384 days.
     *
     * @param beYear
     * @returns {boolean}
     */
    private static boolean isKhmerLeapMonth(int beYear) {
        return getProtetinLeap(beYear) == 1;
    }

    /**
     * Gregorian Leap
     *
     * @param adYear
     * @returns {boolean}
     */
    private static boolean isGregorianLeap(int adYear) {
        return adYear % 4 == 0 && adYear % 100 != 0 || adYear % 400 == 0;
    }

    /**
     * រកថ្ងៃវិសាខបូជា
     * ថ្ងៃដាច់ឆ្នាំពុទ្ធសករាជ
     */
    private static LocalDateTime getVisakhaBochea(int gregorianYear) {
        LocalDateTime date = LocalDateTime.of(gregorianYear, 1, 1, 0, 0, 0, 0);
        for (int i = 0; i < 365; i++) {
            LunarDate lunarDate = findLunarDate(date);
            if (LUNAR_MONTHS.get("ពិសាខ") == lunarDate.month && lunarDate.day == 14) {
                return date;
            }
            date = date.plus(1, ChronoUnit.DAYS);
        }
        throw new VisakhabocheaException("Cannot find Visakhabochea day.");
    }

    /**
     * Calculate date from LocalDateTime to Khmer date
     * @param target : LocalDateTime
     * @returns LunarDate
     */
    private static LunarDate findLunarDate(LocalDateTime target) {
        // Epoch Date: January 1, 1900
        LocalDateTime epochDateTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0, 0);
        int khmerMonth = LUNAR_MONTHS.get("បុស្ស");
        int khmerDay = 0; // 0 - 29 ១កើត ... ១៥កើត ១រោច ...១៤រោច (១៥រោច)

        // Move epoch month
        while (ChronoUnit.DAYS.between(epochDateTime, target) > getNumberOfDayInKhmerMonth(khmerMonth, getMaybeBEYear(epochDateTime))) {
            epochDateTime = epochDateTime.plus(getNumberOfDayInKhmerMonth(khmerMonth, getMaybeBEYear(epochDateTime)), ChronoUnit.DAYS);
            khmerMonth = nextMonthOf(khmerMonth, getMaybeBEYear(epochDateTime));
        }

        khmerDay += ChronoUnit.DAYS.between(epochDateTime, target);

        /**
         * Fix result display 15 រោច ខែ ជេស្ឋ នៅថ្ងៃ ១ កើតខែបឋមាសាធ
         * ករណី ខែជេស្ឋមានតែ ២៩ ថ្ងៃ តែលទ្ធផលបង្ហាញ ១៥រោច ខែជេស្ឋ
         */
        int totalDaysOfTheMonth = getNumberOfDayInKhmerMonth(khmerMonth, getMaybeBEYear(target));
        if (totalDaysOfTheMonth <= khmerDay) {
            khmerDay = khmerDay % totalDaysOfTheMonth;
            khmerMonth = nextMonthOf(khmerMonth, getMaybeBEYear(epochDateTime));
        }
        epochDateTime = epochDateTime.plus(ChronoUnit.DAYS.between(epochDateTime, target), ChronoUnit.DAYS);

        return new LunarDate(khmerDay, khmerMonth, epochDateTime);
    }

    private static int getJolakSakarajYear(LocalDateTime date) {
        int gregorianYear = Integer.parseInt(yearFormat.format(date));
        if (ChronoUnit.MILLIS.between(newYearDateTime, date) < 0) {
            return gregorianYear + 543 - 1182;
        }
        return gregorianYear + 544 - 1182;
    }

    private static int getAnimalYear(LocalDateTime date) {
        int gregorianYear = Integer.parseInt(yearFormat.format(date));
        newYearDateTime = getKhmerNewYearDateTime(gregorianYear);
        if (ChronoUnit.MILLIS.between(newYearDateTime, date) < 0) {
            return (gregorianYear + 543 + 4) % 12;
        }
        return (gregorianYear + 544 + 4) % 12;
    }

    private static LocalDateTime getKhmerNewYearDateTime(int gregorianYear) {
        // ពីគ្រិស្ដសករាជ ទៅ ចុល្លសករាជ
        int jsYear = (gregorianYear + 544) - 1182;
        KhmerNewYearCal info = new KhmerNewYearCal(jsYear);
        int numberNewYearDay = 0;
        if (info.getKhmerNewYear().getNewYearsDaySotins().get(0).getAngsar() == 0) { // ថ្ងៃ ខែ ឆ្នាំ ម៉ោង និង នាទី ចូលឆ្នាំ
            // ចូលឆ្នាំ ៤ ថ្ងៃ
            numberNewYearDay = 4;
        } else {
            // ចូលឆ្នាំ ៣ ថ្ងៃ
            numberNewYearDay = 3;
        }
        LocalDateTime epochLerngSak = LocalDateTime.of(gregorianYear, 4, 17,
                info.getKhmerNewYear().getTimeOfNewYear().getHour(), info.getKhmerNewYear().getTimeOfNewYear().getMinute());
        LunarDate lunarDate = findLunarDate(epochLerngSak);
        int diffFromEpoch = (((lunarDate.month - 4) * 30) + lunarDate.day) -
                (((info.getKhmerNewYear().getLunarDateLerngSak().getMonth() - 4) * 30)
                        + info.getKhmerNewYear().getLunarDateLerngSak().getDay());
        return epochLerngSak.minus((diffFromEpoch + numberNewYearDay - 1), ChronoUnit.DAYS);
    }

    private static int getBeYear(LocalDateTime date) {
        int tmp = Integer.parseInt(yearFormat.format(date));
        if (ChronoUnit.MILLIS.between(getVisakhaBochea(tmp), date) > 0) {
            return tmp + 544;
        }
        return tmp + 543;
    }

    private static LunarDay getKhmerLunarDay(int day) {
        int count = (day % 15) + 1;
        int moonStatus = day > 14 ? MOON_STATUS.get("រោច") : MOON_STATUS.get("កើត");
        return new LunarDay(count, moonStatus);
    }

    public static KhmerLunarDate toKhmerLunarDateFormat(LocalDateTime target) {

        LunarDate lunarDate = findLunarDate(target);
        LunarDay khmerLunarDay = getKhmerLunarDay(lunarDate.day);
        String lunarDay = String.format("%s %s", convertIntegerToKhmerNumber(khmerLunarDay.moonCount),
                khmerLunarDay.moonStatus == 0 ? "កើត" : "រោច");
        int beYear = getBeYear(target);
        int lunarZodiac = getAnimalYear(target);
        int lunarEra = getJolakSakarajYear(target) % 10;
        return new KhmerLunarDate(getDayOfWeekInKhmer(target), lunarDay, LUNAR_MONTHS_FORMAT.get(lunarDate.month), ANIMAL_YEAR.get(lunarZodiac),
                ERA_YEAR.get(lunarEra), convertIntegerToKhmerNumber(beYear));
    }

    private static int nextMonthOf(int khmerMonth, int beYear) {
        if (khmerMonth == LUNAR_MONTHS.get("មិគសិរ"))
            return LUNAR_MONTHS.get("បុស្ស");
        if (khmerMonth == LUNAR_MONTHS.get("បុស្ស"))
            return LUNAR_MONTHS.get("មាឃ");
        if (khmerMonth == LUNAR_MONTHS.get("មាឃ"))
            return LUNAR_MONTHS.get("ផល្គុន");
        if (khmerMonth == LUNAR_MONTHS.get("ផល្គុន"))
            return LUNAR_MONTHS.get("ចេត្រ");
        if (khmerMonth == LUNAR_MONTHS.get("ចេត្រ"))
            return LUNAR_MONTHS.get("ពិសាខ");
        if (khmerMonth == LUNAR_MONTHS.get("ពិសាខ"))
            return LUNAR_MONTHS.get("ជេស្ឋ");
        if (khmerMonth == LUNAR_MONTHS.get("ជេស្ឋ")) {
            if (isKhmerLeapMonth(beYear)) {
                return LUNAR_MONTHS.get("បឋមាសាឍ");
            } else {
                return LUNAR_MONTHS.get("អាសាឍ");
            }
        }
        if (khmerMonth == LUNAR_MONTHS.get("អាសាឍ"))
            return LUNAR_MONTHS.get("ស្រាពណ៍");
        if (khmerMonth == LUNAR_MONTHS.get("ស្រាពណ៍"))
            return LUNAR_MONTHS.get("ភទ្របទ");
        if (khmerMonth == LUNAR_MONTHS.get("ភទ្របទ"))
            return LUNAR_MONTHS.get("អស្សុជ");
        if (khmerMonth == LUNAR_MONTHS.get("អស្សុជ"))
            return LUNAR_MONTHS.get("កក្ដិក");
        if (khmerMonth == LUNAR_MONTHS.get("កក្ដិក"))
            return LUNAR_MONTHS.get("មិគសិរ");
        if (khmerMonth == LUNAR_MONTHS.get("បឋមាសាឍ"))
            return LUNAR_MONTHS.get("ទុតិយាសាឍ");
        if (khmerMonth == LUNAR_MONTHS.get("ទុតិយាសាឍ"))
            return LUNAR_MONTHS.get("ស្រាពណ៍");
        throw new KhmerMonthException("Invalid month");
    }

    private static int getMaybeBEYear(LocalDateTime date) {
        DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("M");
        String d = monthFormat.format(date);
        if (Integer.parseInt(d) <= (SOLAR_MONTHS.get("APR") + 1)) {
            return Integer.parseInt(yearFormat.format(date)) + 543;
        }
        return Integer.parseInt(yearFormat.format(date)) + 544;
    }

    public static class LunarDate {
        private final int day;
        private final int month;
        private final LocalDateTime epochMoved;

        public LunarDate(int day, int month, LocalDateTime epochMoved) {
            this.day = day;
            this.month = month;
            this.epochMoved = epochMoved;
        }

        public int getDay() {
            return day;
        }

        public int getMonth() {
            return month;
        }

        public LocalDateTime getEpochMoved() {
            return epochMoved;
        }
    }

    public static class LunarDay {
        private final int moonCount;
        private final int moonStatus;

        public LunarDay(int moonCount, int moonStatus) {
            this.moonCount = moonCount;
            this.moonStatus = moonStatus;
        }

        public int getMoonCount() {
            return moonCount;
        }

        public int getMoonStatus() {
            return moonStatus;
        }
    }
}
