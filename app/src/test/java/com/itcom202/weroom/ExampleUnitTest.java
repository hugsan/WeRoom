package com.itcom202.weroom;

import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.account.models.TenantProfile;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.interaction.swipe.controllers.HaversineCalculator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void Correct_isFinishedProfile(){
        Profile p = new Profile();
        p.setTenant(new TenantProfile());
        ProfileSingleton.initialize(p);
        assertTrue(ProfileSingleton.isFinishedProfile());
    }

    @Test
    public void Correct_Haversine(){
       assertEquals(HaversineCalculator.distance(38.898556,-77.037852,38.897147,-76.043934), 86.012,0.001);
    }








}