package io.github.metheax.constant;

import io.github.metheax.utils.ChhankitekUtils;

import java.util.Map;

/**
 * Author: Kuylim TITH
 * Date: 5/27/2021
 */
public class Constant {

    public static final Map<String, String> NUMBERS;
    public static final Map<String, String> DAY_OF_WEEK;
    public static final Map<String, Integer> LUNAR_MONTHS;
    public static final Map<Integer, String> LUNAR_MONTHS_FORMAT;
    public static final Map<String, Integer> SOLAR_MONTHS;
    public static final Map<Integer, String> ANIMAL_YEAR;
    public static final Map<Integer, String> ERA_YEAR;
    public static final Map<String, Integer> MOON_STATUS;

    private Constant() {
    }

    static {
        MOON_STATUS = ChhankitekUtils.asMap("កើត", 0, "រោច", 1);

        DAY_OF_WEEK = ChhankitekUtils.asMap("SUNDAY", "អាទិត្យ", "MONDAY", "ច័ន្ទ", "TUESDAY", "អង្គារ",
                "WEDNESDAY", "ពុធ", "THURSDAY", "ព្រហស្បតិ៍", "FRIDAY", "សុក្រ", "SATURDAY", "សៅរ៍");

        NUMBERS = ChhankitekUtils.asMap("0", "០", "1", "១", "2", "២", "3", "៣", "4", "៤", "5", "៥", "6",
                "៦", "7", "៧", "8", "៨", "9", "៩");

        LUNAR_MONTHS = ChhankitekUtils.indexAsValueMap("មិគសិរ", "បុស្ស", "មាឃ", "ផល្គុន", "ចេត្រ", "ពិសាខ", "ជេស្ឋ",
                "អាសាឍ", "ស្រាពណ៍", "ភទ្របទ", "អស្សុជ", "កក្ដិក", "បឋមាសាឍ", "ទុតិយាសាឍ");
        LUNAR_MONTHS_FORMAT = ChhankitekUtils.indexAsKeyMap("មិគសិរ", "បុស្ស", "មាឃ", "ផល្គុន", "ចេត្រ", "ពិសាខ", "ជេស្ឋ",
                "អាសាឍ", "ស្រាពណ៍", "ភទ្របទ", "អស្សុជ", "កក្ដិក", "បឋមាសាឍ", "ទុតិយាសាឍ");

        SOLAR_MONTHS = ChhankitekUtils.indexAsValueMap("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG",
                "SEP", "OCT", "NOV", "DEC");

        ANIMAL_YEAR = ChhankitekUtils.indexAsKeyMap("ជូត", "ឆ្លូវ", "ខាល", "ថោះ", "រោង", "ម្សាញ់", "មមីរ",
                "មមែ", "វក", "រកា", "ច", "កុរ");

        ERA_YEAR = ChhankitekUtils.indexAsKeyMap("សំរឹទ្ធិស័ក", "ឯកស័ក", "ទោស័ក", "ត្រីស័ក", "ចត្វាស័ក", "បញ្ចស័ក",
                "ឆស័ក", "សប្តស័ក", "អដ្ឋស័ក", "នព្វស័ក");
    }
}
