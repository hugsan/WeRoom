package com.itcom202.weroom.interaction.swipe.controllers;

import com.itcom202.weroom.account.models.LandlordProfile;
import com.itcom202.weroom.account.models.Profile;
import com.itcom202.weroom.account.models.RoomPosted;
import com.itcom202.weroom.account.models.TenantProfile;

import java.util.ArrayList;

public class FilterController {

    public static ArrayList<RoomPosted> filterRoomsFromTenant(Profile p, ArrayList<RoomPosted> rooms){
        if (p.getTenant() == null){
            return null;
        }
        ArrayList<RoomPosted> result = new ArrayList<>(rooms);
        TenantProfile tenant = p.getTenant();
        for (RoomPosted r : result){
            if (HaversineCalculator.distance(tenant.getCityLatitude(),tenant.getCityLongitude(),
                    r.getLatitude(),r.getLongitude()) > tenant.getDistanceCenter()){
                result.remove(r);
                continue;
            }
            if (tenant.getMinDeposit() > r.getDeposit() || tenant.getMAxDeposit() < r.getDeposit()){
                result.remove(r);
                continue;
            }
            if (tenant.getMinRent() > r.getRent() || tenant.getMaxRent() < r.getRent() ){
                result.remove(r);
            }
        }
        //TODO implement all other filters in future, for testing it will only filter
        //distance, deposit and rent.

        return result;
    }

    public static ArrayList<Profile> filterProfilesFromLandlord(Profile p, ArrayList<Profile> profiles){
        if (p.getLandlord() == null){
            return null;
        }
        ArrayList<Profile> result = new ArrayList<>(profiles);
        LandlordProfile l = p.getLandlord();

        for (Profile tenant : result){
            if (!l.getTenantGender().equals(tenant.getGender())){
                result.remove(tenant);
                continue;
            }
            if (l.getTenantMinAge() > tenant.getAge() || l.getTenantMaxAge() < tenant.getAge()){
                result.remove(tenant);
                continue;
            }
        }
        //TODO implement all other filters in future, for testing it will only filter
        //tenant age, tenant gender.

        return result;
    }
}
