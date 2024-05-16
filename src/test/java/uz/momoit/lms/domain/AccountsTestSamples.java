package uz.momoit.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AccountsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Accounts getAccountsSample1() {
        return new Accounts()
            .id(1L)
            .username("username1")
            .fullName("fullName1")
            .sortableName("sortableName1")
            .avatarImageUrl("avatarImageUrl1")
            .phone("phone1")
            .locale("locale1")
            .gender("gender1");
    }

    public static Accounts getAccountsSample2() {
        return new Accounts()
            .id(2L)
            .username("username2")
            .fullName("fullName2")
            .sortableName("sortableName2")
            .avatarImageUrl("avatarImageUrl2")
            .phone("phone2")
            .locale("locale2")
            .gender("gender2");
    }

    public static Accounts getAccountsRandomSampleGenerator() {
        return new Accounts()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .sortableName(UUID.randomUUID().toString())
            .avatarImageUrl(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .locale(UUID.randomUUID().toString())
            .gender(UUID.randomUUID().toString());
    }
}
