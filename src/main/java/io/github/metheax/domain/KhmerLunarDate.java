package io.github.metheax.domain;

/**
 * Author: Kuylim TITH
 * Date: 5/27/2021
 */
public class KhmerLunarDate {

    private final String dayOfWeek;
    private final String lunarDay;
    private final String lunarMonth;
    private final String lunarZodiac;
    private final String lunarEra;
    private final String lunarYear;

    public KhmerLunarDate(String dayOfWeek, String lunarDay, String lunarMonth, String lunarZodiac, String lunarEra, String lunarYear) {
        this.dayOfWeek = dayOfWeek;
        this.lunarDay = lunarDay;
        this.lunarMonth = lunarMonth;
        this.lunarZodiac = lunarZodiac;
        this.lunarEra = lunarEra;
        this.lunarYear = lunarYear;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getLunarDay() {
        return lunarDay;
    }

    public String getLunarMonth() {
        return lunarMonth;
    }

    public String getLunarZodiac() {
        return lunarZodiac;
    }

    public String getLunarEra() {
        return lunarEra;
    }

    public String getLunarYear() {
        return lunarYear;
    }

    @Override
    public String toString() {
        return String.format("ថ្ងៃ%s %s ខែ%s ឆ្នាំ%s %s ពុទ្ធសករាជ %s", dayOfWeek, lunarDay, lunarMonth, lunarZodiac, lunarEra, lunarYear);
    }
}
