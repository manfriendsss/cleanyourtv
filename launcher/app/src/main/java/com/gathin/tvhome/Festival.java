package com.gathin.tvhome;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Objects;

/**
 * A festive skin set by a theme's config.json: {@code festival}, {@code festival_from}, {@code festival_until},
 * {@code greeting} and {@code accent_color}, or automatically scheduled by {@link #getScheduledEvent(LocalDate)}.
 * While it is active the pills get festive icons (a diya for the network, a bell for Inputs, a marigold for
 * Settings), the greeting is replaced and the focus glow takes the accent colour.
 * When the event window ends, it switches itself off and returns to the default theme.
 */
final class Festival {

    static final Festival NONE = new Festival("", null, null, "", 0);

    /** Warm diya gold, the accent when a theme names a festival but no colour. */
    static final int DEFAULT_ACCENT = 0xFFFFB23C;

    /** Lower-case festival name, or "" for none. */
    final String name;
    /** First day the skin shows, inclusive; null for no start date restriction. */
    final LocalDate from;
    /** Last day the skin shows, inclusive; null for no end date. */
    final LocalDate until;
    /** Text that replaces the time-of-day greeting; "" uses the festival's own default. */
    final String greeting;
    /** Focus glow colour as 0xAARRGGBB, or 0 for {@link #DEFAULT_ACCENT}. */
    final int accent;

    Festival(String name, LocalDate from, LocalDate until, String greeting, int accent) {
        this.name = name;
        this.from = from;
        this.until = until;
        this.greeting = greeting;
        this.accent = accent;
    }

    private Festival(String name, LocalDate until, String greeting, int accent) {
        this(name, null, until, greeting, accent);
    }

    /**
     * Builds the skin from raw config values with until date only.
     */
    static Festival parse(String name, String until, String greeting, String accent) {
        return parse(name, null, until, greeting, accent);
    }

    /**
     * Builds the skin from raw config values with optional start and end dates.
     */
    static Festival parse(String name, String from, String until, String greeting, String accent) {
        String n = name == null ? "" : name.trim().toLowerCase(Locale.ROOT);
        if (n.isEmpty() || n.length() > 40) return NONE;
        LocalDate start = null;
        if (from != null && !from.trim().isEmpty()) {
            try {
                start = LocalDate.parse(from.trim());
            } catch (DateTimeParseException e) {
                return NONE;
            }
        }
        LocalDate end = null;
        if (until != null && !until.trim().isEmpty()) {
            try {
                end = LocalDate.parse(until.trim());
            } catch (DateTimeParseException e) {
                return NONE;
            }
        }
        String g = greeting == null ? "" : Cards.cleanLabel(greeting, "");
        return new Festival(n, start, end, g, parseColor(accent));
    }

    /** Whether the skin shows today. */
    boolean active(LocalDate today) {
        if (name.isEmpty() || today == null) return false;
        if (from != null && today.isBefore(from)) return false;
        if (until != null && today.isAfter(until)) return false;
        return true;
    }

    /** The greeting to show while active, or "" to keep the normal time-of-day greeting. */
    String greetingText() {
        if (!greeting.isEmpty()) return greeting;
        switch (name) {
            case "dussehra":
                return "Happy Dussehra";
            case "diwali":
                return "Happy Diwali";
            case "birthday_momo":
                return "Chúc mừng sinh nhật Momo! 🎂🎉";
            case "birthday_mia":
                return "Chúc mừng sinh nhật Bé Mía! 🎂🌸";
            case "birthday_quang_anh":
                return "Chúc mừng sinh nhật Bố Quang Anh! 🎂🎈";
            case "birthday_me_linh":
                return "Chúc mừng sinh nhật Mẹ Linh! 🎂💐";
            case "tet_nguyen_dan":
                return "Chúc Mừng Năm Mới · Vạn Sự Như Ý 🧧✨";
            case "christmas":
                return "Giáng Sinh An Lành · Merry Christmas 🎄✨";
            case "valentine":
                return "Happy Valentine's Day · Yêu Thương Đong Đầy 💖";
            case "womens_day":
                return "Chúc Mừng Ngày Quốc Tế Phụ Nữ 8/3 💐";
            case "vietnam_30_4":
                return "Tự Hào Việt Nam · Kỷ Niệm 30/4 - 1/5 🇻🇳";
            case "national_day_2_9":
                return "Chúc Mừng Quốc Khánh 2/9 · Tôi Yêu Việt Nam 🇻🇳";
            default:
                return "";
        }
    }

    int accentColor() {
        if (accent != 0) return accent;
        switch (name) {
            case "birthday_momo":
                return 0xFFFFA000; // Amber Gold
            case "birthday_mia":
                return 0xFFFF4081; // Pink
            case "birthday_quang_anh":
                return 0xFF2979FF; // Royal Blue
            case "birthday_me_linh":
                return 0xFFAB47BC; // Lotus Purple
            case "tet_nguyen_dan":
                return 0xFFD32F2F; // Tet Red
            case "christmas":
                return 0xFFE50914; // Christmas Red
            case "valentine":
                return 0xFFE91E63; // Rose Red
            case "womens_day":
                return 0xFFE040FB; // Magenta
            case "vietnam_30_4":
            case "national_day_2_9":
                return 0xFFDA251D; // Vietnam Flag Red
            default:
                return DEFAULT_ACCENT;
        }
    }

    /**
     * Finds the scheduled family birthday or holiday event for {@code today}.
     * Priority:
     * 1. Family Birthdays (1 day before to 1 day after)
     * 2. National / International Holidays (2 weeks before to 1 week after)
     * Returns {@link #NONE} if no event is scheduled for today.
     */
    static Festival getScheduledEvent(LocalDate today) {
        if (today == null) return NONE;
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        // 1. Family Birthdays (1 day before to 1 day after -> 3 days window)
        // Sinh nhật Momo: 17/1 (16/1 - 18/1)
        if (month == 1 && day >= 16 && day <= 18) {
            return new Festival("birthday_momo", LocalDate.of(year, 1, 16), LocalDate.of(year, 1, 18),
                    "Chúc mừng sinh nhật Momo! 🎂🎉", 0xFFFFA000);
        }
        // Sinh nhật Mía: 8/3 (7/3 - 9/3) - Overrides Women's Day on these 3 days
        if (month == 3 && day >= 7 && day <= 9) {
            return new Festival("birthday_mia", LocalDate.of(year, 3, 7), LocalDate.of(year, 3, 9),
                    "Chúc mừng sinh nhật Bé Mía! 🎂🌸", 0xFFFF4081);
        }
        // Sinh nhật Bố Quang Anh: 28/6 (27/6 - 29/6)
        if (month == 6 && day >= 27 && day <= 29) {
            return new Festival("birthday_quang_anh", LocalDate.of(year, 6, 27), LocalDate.of(year, 6, 29),
                    "Chúc mừng sinh nhật Bố Quang Anh! 🎂🎈", 0xFF2979FF);
        }
        // Sinh nhật Mẹ Linh: 20/11 (19/11 - 21/11)
        if (month == 11 && day >= 19 && day <= 21) {
            return new Festival("birthday_me_linh", LocalDate.of(year, 11, 19), LocalDate.of(year, 11, 21),
                    "Chúc mừng sinh nhật Mẹ Linh! 🎂💐", 0xFFAB47BC);
        }

        // 2. Holidays (2 weeks before, 1 week after)
        // Tết Nguyên Đán (Lunar New Year)
        LocalDate tet = getTetFirstDay(year);
        if (tet != null) {
            LocalDate tetStart = tet.minusDays(14);
            LocalDate tetEnd = tet.plusDays(7);
            if (!today.isBefore(tetStart) && !today.isAfter(tetEnd)) {
                return new Festival("tet_nguyen_dan", tetStart, tetEnd,
                        "Chúc Mừng Năm Mới · Vạn Sự Như Ý 🧧✨", 0xFFD32F2F);
            }
        }
        LocalDate prevTet = getTetFirstDay(year - 1);
        if (prevTet != null) {
            LocalDate prevTetEnd = prevTet.plusDays(7);
            if (!today.isAfter(prevTetEnd)) {
                LocalDate prevTetStart = prevTet.minusDays(14);
                if (!today.isBefore(prevTetStart)) {
                    return new Festival("tet_nguyen_dan", prevTetStart, prevTetEnd,
                            "Chúc Mừng Năm Mới · Vạn Sự Như Ý 🧧✨", 0xFFD32F2F);
                }
            }
        }

        // Giáng Sinh (25/12): 2 weeks before (11/12) to 1 week after (01/01 of next year)
        if (month == 12 && day >= 11) {
            LocalDate start = LocalDate.of(year, 12, 11);
            LocalDate end = LocalDate.of(year + 1, 1, 1);
            return new Festival("christmas", start, end,
                    "Giáng Sinh An Lành · Merry Christmas 🎄✨", 0xFFE50914);
        }
        if (month == 1 && day == 1) {
            LocalDate start = LocalDate.of(year - 1, 12, 11);
            LocalDate end = LocalDate.of(year, 1, 1);
            return new Festival("christmas", start, end,
                    "Giáng Sinh An Lành · Merry Christmas 🎄✨", 0xFFE50914);
        }

        // Valentine (14/2): 2 weeks before (31/1) to 1 week after (21/2)
        if ((month == 1 && day == 31) || (month == 2 && day <= 21)) {
            LocalDate start = LocalDate.of(year, 1, 31);
            LocalDate end = LocalDate.of(year, 2, 21);
            return new Festival("valentine", start, end,
                    "Happy Valentine's Day · Yêu Thương Đong Đầy 💖", 0xFFE91E63);
        }

        // Quốc tế Phụ nữ (8/3): 2 weeks before (22/2) to 1 week after (15/3)
        if ((month == 2 && day >= 22) || (month == 3 && day <= 15)) {
            LocalDate start = LocalDate.of(year, 2, 22);
            LocalDate end = LocalDate.of(year, 3, 15);
            return new Festival("womens_day", start, end,
                    "Chúc Mừng Ngày Quốc Tế Phụ Nữ 8/3 💐", 0xFFE040FB);
        }

        // Tự hào Việt Nam (30/4 - 1/5): 2 weeks before (16/4) to 1 week after (8/5)
        if ((month == 4 && day >= 16) || (month == 5 && day <= 8)) {
            LocalDate start = LocalDate.of(year, 4, 16);
            LocalDate end = LocalDate.of(year, 5, 8);
            return new Festival("vietnam_30_4", start, end,
                    "Tự Hào Việt Nam · Kỷ Niệm 30/4 - 1/5 🇻🇳", 0xFFDA251D);
        }

        // Quốc khánh 2/9: 2 weeks before (19/8) to 1 week after (9/9)
        if ((month == 8 && day >= 19) || (month == 9 && day <= 9)) {
            LocalDate start = LocalDate.of(year, 8, 19);
            LocalDate end = LocalDate.of(year, 9, 9);
            return new Festival("national_day_2_9", start, end,
                    "Chúc Mừng Quốc Khánh 2/9 · Tôi Yêu Việt Nam 🇻🇳", 0xFFDA251D);
        }

        return NONE;
    }

    /** First day of Lunar New Year (Mùng 1 Tết Âm Lịch) for years 2024 - 2040. */
    static LocalDate getTetFirstDay(int year) {
        switch (year) {
            case 2024: return LocalDate.of(2024, 2, 10);
            case 2025: return LocalDate.of(2025, 1, 29);
            case 2026: return LocalDate.of(2026, 2, 17);
            case 2027: return LocalDate.of(2027, 2, 6);
            case 2028: return LocalDate.of(2028, 1, 26);
            case 2029: return LocalDate.of(2029, 2, 13);
            case 2030: return LocalDate.of(2030, 2, 3);
            case 2031: return LocalDate.of(2031, 1, 23);
            case 2032: return LocalDate.of(2032, 2, 11);
            case 2033: return LocalDate.of(2033, 1, 31);
            case 2034: return LocalDate.of(2034, 2, 19);
            case 2035: return LocalDate.of(2035, 2, 8);
            case 2036: return LocalDate.of(2036, 1, 28);
            case 2037: return LocalDate.of(2037, 2, 15);
            case 2038: return LocalDate.of(2038, 2, 4);
            case 2039: return LocalDate.of(2039, 1, 24);
            case 2040: return LocalDate.of(2040, 2, 12);
            default: return null;
        }
    }

    /** {@code #RRGGBB} to an opaque colour, anything else to 0. */
    static int parseColor(String s) {
        if (s == null) return 0;
        String t = s.trim();
        if (!t.matches("#[0-9A-Fa-f]{6}")) return 0;
        return 0xFF000000 | Integer.parseInt(t.substring(1), 16);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Festival f = (Festival) o;
        return accent == f.accent &&
                Objects.equals(name, f.name) &&
                Objects.equals(from, f.from) &&
                Objects.equals(until, f.until) &&
                Objects.equals(greeting, f.greeting);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, from, until, greeting, accent);
    }
}
