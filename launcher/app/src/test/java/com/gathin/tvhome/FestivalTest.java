package com.gathin.tvhome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class FestivalTest {

    private static final LocalDate DAY = LocalDate.of(2026, 10, 20);

    @Test
    public void activeThroughTheLastDayInclusive() {
        Festival f = Festival.parse("Dussehra", "2026-10-21", "", "");
        assertTrue(f.active(DAY));
        assertTrue(f.active(DAY.plusDays(1)));
        assertFalse(f.active(DAY.plusDays(2)));
    }

    @Test
    public void noEndDateStaysOn() {
        assertTrue(Festival.parse("diwali", "", "", "").active(DAY.plusYears(1)));
    }

    @Test
    public void malformedValuesTurnTheSkinOff() {
        assertSame(Festival.NONE, Festival.parse("", "2026-10-21", "", ""));
        assertSame(Festival.NONE, Festival.parse("dussehra", "21/10/2026", "", ""));
        assertFalse(Festival.NONE.active(DAY));
    }

    @Test
    public void greetingDefaultsByFestivalAndCanBeOverridden() {
        assertEquals("Happy Dussehra", Festival.parse("dussehra", "", "", "").greetingText());
        assertEquals("Happy Diwali", Festival.parse("DIWALI", "", "", "").greetingText());
        assertEquals("", Festival.parse("onam", "", "", "").greetingText());
        assertEquals("Shubho Bijoya", Festival.parse("dussehra", "", "  Shubho\u202e Bijoya ", "").greetingText());
    }

    @Test
    public void accentFallsBackToDiyaGold() {
        assertEquals(Festival.DEFAULT_ACCENT, Festival.parse("dussehra", "", "", "").accentColor());
        assertEquals(0xFF102030, Festival.parse("dussehra", "", "", "#102030").accentColor());
        assertEquals(Festival.DEFAULT_ACCENT, Festival.parse("dussehra", "", "", "gold").accentColor());
    }

    @Test
    public void birthdaySchedulingWindowOneDayBeforeAndAfter() {
        // Momo: 17/1 -> Active 16/1 - 18/1
        assertFalse(Festival.getScheduledEvent(LocalDate.of(2026, 1, 15)).name.equals("birthday_momo"));
        assertEquals("birthday_momo", Festival.getScheduledEvent(LocalDate.of(2026, 1, 16)).name);
        assertEquals("birthday_momo", Festival.getScheduledEvent(LocalDate.of(2026, 1, 17)).name);
        assertEquals("birthday_momo", Festival.getScheduledEvent(LocalDate.of(2026, 1, 18)).name);
        assertFalse(Festival.getScheduledEvent(LocalDate.of(2026, 1, 19)).name.equals("birthday_momo"));

        // Mía: 8/3 -> Active 7/3 - 9/3
        assertEquals("birthday_mia", Festival.getScheduledEvent(LocalDate.of(2026, 3, 7)).name);
        assertEquals("birthday_mia", Festival.getScheduledEvent(LocalDate.of(2026, 3, 8)).name);
        assertEquals("birthday_mia", Festival.getScheduledEvent(LocalDate.of(2026, 3, 9)).name);

        // Bố Quang Anh: 28/6 -> Active 27/6 - 29/6
        assertFalse(Festival.getScheduledEvent(LocalDate.of(2026, 6, 26)).name.equals("birthday_quang_anh"));
        assertEquals("birthday_quang_anh", Festival.getScheduledEvent(LocalDate.of(2026, 6, 27)).name);
        assertEquals("birthday_quang_anh", Festival.getScheduledEvent(LocalDate.of(2026, 6, 28)).name);
        assertEquals("birthday_quang_anh", Festival.getScheduledEvent(LocalDate.of(2026, 6, 29)).name);
        assertFalse(Festival.getScheduledEvent(LocalDate.of(2026, 6, 30)).name.equals("birthday_quang_anh"));

        // Mẹ Linh: 20/11 -> Active 19/11 - 21/11
        assertFalse(Festival.getScheduledEvent(LocalDate.of(2026, 11, 18)).name.equals("birthday_me_linh"));
        assertEquals("birthday_me_linh", Festival.getScheduledEvent(LocalDate.of(2026, 11, 19)).name);
        assertEquals("birthday_me_linh", Festival.getScheduledEvent(LocalDate.of(2026, 11, 20)).name);
        assertEquals("birthday_me_linh", Festival.getScheduledEvent(LocalDate.of(2026, 11, 21)).name);
        assertFalse(Festival.getScheduledEvent(LocalDate.of(2026, 11, 22)).name.equals("birthday_me_linh"));
    }

    @Test
    public void holidaySchedulingWindowTwoWeeksBeforeOneWeekAfter() {
        // Tết 2026: Mùng 1 Tết là 17/02/2026 -> Active từ 03/02/2026 đến 24/02/2026
        assertEquals("tet_nguyen_dan", Festival.getScheduledEvent(LocalDate.of(2026, 2, 3)).name);
        assertEquals("tet_nguyen_dan", Festival.getScheduledEvent(LocalDate.of(2026, 2, 17)).name);
        assertEquals("tet_nguyen_dan", Festival.getScheduledEvent(LocalDate.of(2026, 2, 24)).name);

        // Giáng sinh: 25/12 -> Active 11/12 đến 01/01
        assertEquals("christmas", Festival.getScheduledEvent(LocalDate.of(2026, 12, 11)).name);
        assertEquals("christmas", Festival.getScheduledEvent(LocalDate.of(2026, 12, 25)).name);
        assertEquals("christmas", Festival.getScheduledEvent(LocalDate.of(2027, 1, 1)).name);

        // Tự hào Việt Nam: 30/4 -> Active 16/04 đến 08/05
        assertEquals("vietnam_30_4", Festival.getScheduledEvent(LocalDate.of(2026, 4, 16)).name);
        assertEquals("vietnam_30_4", Festival.getScheduledEvent(LocalDate.of(2026, 4, 30)).name);
        assertEquals("vietnam_30_4", Festival.getScheduledEvent(LocalDate.of(2026, 5, 8)).name);

        // Quốc khánh 2/9 -> Active 19/08 đến 09/09
        assertEquals("national_day_2_9", Festival.getScheduledEvent(LocalDate.of(2026, 8, 19)).name);
        assertEquals("national_day_2_9", Festival.getScheduledEvent(LocalDate.of(2026, 9, 2)).name);
        assertEquals("national_day_2_9", Festival.getScheduledEvent(LocalDate.of(2026, 9, 9)).name);
    }

    @Test
    public void miaBirthdayOverridesWomensDayOnMarch7To9() {
        // March 6: Women's day active
        assertEquals("womens_day", Festival.getScheduledEvent(LocalDate.of(2026, 3, 6)).name);
        // March 7-9: Mía birthday takes priority!
        assertEquals("birthday_mia", Festival.getScheduledEvent(LocalDate.of(2026, 3, 7)).name);
        assertEquals("birthday_mia", Festival.getScheduledEvent(LocalDate.of(2026, 3, 8)).name);
        assertEquals("birthday_mia", Festival.getScheduledEvent(LocalDate.of(2026, 3, 9)).name);
        // March 10: Back to Women's day
        assertEquals("womens_day", Festival.getScheduledEvent(LocalDate.of(2026, 3, 10)).name);
    }

    @Test
    public void ordinaryDayReturnsNone() {
        assertSame(Festival.NONE, Festival.getScheduledEvent(DAY));
    }
}
