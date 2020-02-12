package com.cloud.cloud.business.core.controller;

import com.cloud.cloud.business.data.*;
import com.cloud.cloud.business.data.repository.ProfileRepository;
import com.cloud.cloud.business.data.repository.WalletRepository;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
@RequestMapping("/account/")
public class ProfileController {
    RestTemplate restTemplate;
    ProfileRepository profileRepository;
    WalletRepository walletRepository;

    public ProfileController(RestTemplate restTemplate, ProfileRepository profileRepository, WalletRepository walletRepository) {
        this.restTemplate = restTemplate;
        this.profileRepository = profileRepository;
        this.walletRepository = walletRepository;
    }

    @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
    @ResponseBody
    public String heartbeat() {
        return "Account Management is up and running";
    }


    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    @ResponseBody
    public String createProfile(@RequestBody Profile profile) {

        ImmutableMap<String, Object> payload = ImmutableMap.of("email", profile.getEmail(), "password", profile.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ImmutableMap<String, Object>> requestEntity =
                new HttpEntity<ImmutableMap<String, Object>>(payload, headers);
        ResponseEntity<RegisterResponse> registerResponseResponseEntity = null;
        try {
            registerResponseResponseEntity = restTemplate.postForEntity("http://localhost:2052/authentiq/v1/user/register", requestEntity, RegisterResponse.class);
            RegisterResponse registerResponse = registerResponseResponseEntity.getBody();
//            RegisterResponse registerResponse = restTemplate.postForEntity("http://localhost:2052/authentiq/v1/user/register", requestEntity, RegisterResponse.class).getBody();
            long profileId = profileRepository.save(profile).getId();
            Wallet wallet = new Wallet(profileId, 0);
            walletRepository.save(wallet);
            return registerResponse.getToken();
        } catch (Exception ex) {
            ex.printStackTrace();
            if (registerResponseResponseEntity != null) {
                if (registerResponseResponseEntity.getStatusCode().value() == 400)
                    return "invalid parameters";
                else
                    return "email is already exists";
            }
            return "Please check you username and password or maybe this email is already exists";
        }

    }

    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity updateProfile(@RequestBody Profile profile,
                                                 @RequestHeader("authorization") String headers) {
        Profile baseProfile = validate(headers);
        if (profile.getPhoneNo() != null) {
            baseProfile.setPhoneNo(profile.getPhoneNo());
        } else
            return new ResponseEntity<String>("phone no not found",HttpStatus.NOT_FOUND);

        if (profile.getNationalCode() != null) {
            baseProfile.setNationalCode(profile.getNationalCode());
        } else
            return new ResponseEntity<String>("national code not found",HttpStatus.NOT_FOUND);

        if (profile.getAddress() != null) {
            baseProfile.setAddress(profile.getAddress());
        } else
            return new ResponseEntity<String>("address not found",HttpStatus.NOT_FOUND);


        if (profile.getPostalCode() != null) {
            baseProfile.setPostalCode(profile.getPostalCode());
        }
        else
            return new ResponseEntity<String>("postal code not found",HttpStatus.NOT_FOUND);

        profileRepository.save(baseProfile);
        return new ResponseEntity<>(baseProfile, HttpStatus.OK);
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getProfile(@RequestHeader("authorization") String headers) {
        Profile profile = validate(headers);
         if(profile==null){
             return new ResponseEntity<String> ("no user found token is invalid",HttpStatus.NOT_FOUND);
         }else
             return new ResponseEntity<Profile>(profile,HttpStatus.OK);
    }

    public Profile validate(String headers) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Authorization", headers);
        HttpEntity<String> requestEntity =
                new HttpEntity<>(header);
        ValidateResponse validateResponse = null;
        try {
            validateResponse = restTemplate.exchange("http://localhost:2052/authentiq/v1/validate/token", HttpMethod.GET, requestEntity, ValidateResponse.class).getBody();
        }catch (Exception e){
           e.printStackTrace();
           return null;
        }
        return profileRepository.findByEmail(validateResponse.getEmail());
    }


}
