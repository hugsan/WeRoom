package com.itcom202.weroom;

import com.itcom202.weroom.account.models.LandlordProfile;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.account.models.TenantProfile;
import com.itcom202.weroom.framework.ProfileSingleton;
import com.itcom202.weroom.interaction.chat.views.ChatFragment;
import com.itcom202.weroom.interaction.chat.views.SelectChatFragment;
import com.itcom202.weroom.interaction.swipe.controllers.HaversineCalculator;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class WeRoomUnitTests {


    @Test
    public void correct_isFinishedProfileTenant() {
        Profile p = new Profile();
        p.setTenant(new TenantProfile());
        ProfileSingleton.initialize(p);
        assertTrue(ProfileSingleton.isFinishedProfile());
    }

    @Test
    public void correct_isFinishedProfileLandlord() {
        Profile p = new Profile();
        p.setLandlord(new LandlordProfile());
        p.getLandlord().addRoomID("123567899");
        ProfileSingleton.initialize(p);
        assertTrue(ProfileSingleton.isFinishedProfile());
    }

    @Test
    public void wrong_isFinishedProfileUnfinished() {
        Profile p = null;
        ProfileSingleton.initialize(p);
        assertFalse(ProfileSingleton.isFinishedProfile());
    }

    @Test
    public void correct_Haversine() {
        assertEquals(HaversineCalculator.distance(38.898556, -77.037852, 38.897147, -76.043934), 86.012, 0.001);
    }

    @Test
    public void correct_Haversine_same_point() {
        assertEquals(HaversineCalculator.distance(0, 0, 0, 0), 0, 0.001);
    }

    @Test
    public void wrong_Haversine() {
        assertNotEquals(HaversineCalculator.distance(13.56, 67.567, -45.760, 99.4567), 67.678, 0.001);

    }

    @Test
    public void right_CreateChatID() {
        try {
            Class<?> c = SelectChatFragment.class;
            Object o = c.newInstance();

            Method method = c.getDeclaredMethod("createChatID", String.class, String.class);
            method.setAccessible(true);
            assertEquals(method.invoke(o, "123", "abc"), "123_abc");

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


}