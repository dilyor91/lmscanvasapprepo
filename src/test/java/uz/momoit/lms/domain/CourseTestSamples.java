package uz.momoit.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CourseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Course getCourseSample1() {
        return new Course()
            .id(1L)
            .courseName("courseName1")
            .courseCode("courseCode1")
            .courseImagePath("courseImagePath1")
            .courseFormat("courseFormat1")
            .selfEnrollmentCode("selfEnrollmentCode1")
            .storageQuota(1);
    }

    public static Course getCourseSample2() {
        return new Course()
            .id(2L)
            .courseName("courseName2")
            .courseCode("courseCode2")
            .courseImagePath("courseImagePath2")
            .courseFormat("courseFormat2")
            .selfEnrollmentCode("selfEnrollmentCode2")
            .storageQuota(2);
    }

    public static Course getCourseRandomSampleGenerator() {
        return new Course()
            .id(longCount.incrementAndGet())
            .courseName(UUID.randomUUID().toString())
            .courseCode(UUID.randomUUID().toString())
            .courseImagePath(UUID.randomUUID().toString())
            .courseFormat(UUID.randomUUID().toString())
            .selfEnrollmentCode(UUID.randomUUID().toString())
            .storageQuota(intCount.incrementAndGet());
    }
}
