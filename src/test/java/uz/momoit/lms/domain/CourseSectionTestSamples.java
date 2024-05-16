package uz.momoit.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CourseSectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CourseSection getCourseSectionSample1() {
        return new CourseSection().id(1L).sectionName("sectionName1");
    }

    public static CourseSection getCourseSectionSample2() {
        return new CourseSection().id(2L).sectionName("sectionName2");
    }

    public static CourseSection getCourseSectionRandomSampleGenerator() {
        return new CourseSection().id(longCount.incrementAndGet()).sectionName(UUID.randomUUID().toString());
    }
}
