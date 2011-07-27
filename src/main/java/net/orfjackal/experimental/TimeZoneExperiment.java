package net.orfjackal.experimental;

import org.joda.time.*;

/**
 * @author Esko Luontola
 * @since 27.7.2011
 */
public class TimeZoneExperiment {

    public static void main(String[] args) {

        LocalDateTime a1 = new LocalDateTime(1927, 12, 31, 0, 0, 0, 0);
        LocalDateTime a2 = new LocalDateTime(1928, 1, 2, 0, 0, 0, 0);
        System.out.println(a1);
        System.out.println(a2);
        System.out.println(Seconds.secondsBetween(a1, a2).getSeconds()); // 172800 == 60 * 60 * 24 * 2 == 2 days without leap seconds

        DateTime b1 = new DateTime(1927, 12, 31, 0, 0, 0, 0, DateTimeZone.forID("Asia/Shanghai"));
        DateTime b2 = new DateTime(1928, 1, 2, 0, 0, 0, 0, DateTimeZone.forID("Asia/Shanghai"));
        System.out.println(b1);
        System.out.println(b2);
        System.out.println(Seconds.secondsBetween(b1, b2).getSeconds());

        LocalDateTime c1 = b1.toLocalDateTime();
        LocalDateTime c2 = b2.toLocalDateTime();
        System.out.println(c1);
        System.out.println(c2);
        System.out.println(Seconds.secondsBetween(c1, c2).getSeconds());
    }
}
