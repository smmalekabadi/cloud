package com.cloud.cloud;


import com.cloud.cloud.business.data.Profile;
import com.cloud.cloud.business.data.repository.ProfileRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Test {
    private ProfileRepository profileRepository;
    public Test(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
//        profileRepository.save(new Profile());
    }
//    @Scheduled(fixedDelay = 10000)
    public void test(){
//        profileRepository.save(new Profile());
    }
}
