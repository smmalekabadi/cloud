package com.cloud.cloud.business.core.controller;

import com.cloud.cloud.business.data.Profile;
import com.cloud.cloud.business.data.ProfileSimple;
import com.cloud.cloud.business.data.RegisterResponse;
import com.cloud.cloud.business.data.ValidateResponse;
import com.cloud.cloud.business.data.repository.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/account/")
public class ProfileController {
    RestTemplate restTemplate;
    ProfileRepository profileRepository;

    public ProfileController(RestTemplate restTemplate, ProfileRepository profileRepository) {
        this.restTemplate = restTemplate;
        this.profileRepository = profileRepository;
    }

    @RequestMapping(value = "/heartbeat", method = RequestMethod.GET)
    @ResponseBody
    public String heartbeat(@RequestHeader("authorization") String headers) {
        System.out.println(headers.substring(7));
        return "Account Management is up and running";
    }


    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    @ResponseBody
    public String createProfile(@RequestParam String email,
                                @RequestParam(required = false) String password,
                                @RequestParam String phoneNo,
                                @RequestParam String nationalCode,
                                @RequestParam String address,
                                @RequestParam String postalCode) {
        Profile profile = new Profile(email, phoneNo, nationalCode, address, postalCode, password);
        ProfileSimple profileSimple = new ProfileSimple(email,password);
        ImmutableMap payload = ImmutableMap.of("email",email,"password",password);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ImmutableMap> requestEntity =
                new HttpEntity<ImmutableMap>(payload, headers);

        RegisterResponse registerResponse = restTemplate.postForEntity("http://localhost:2000/authentiq/v1/user/register", requestEntity, RegisterResponse.class).getBody();
        profileRepository.save(profile);
        return registerResponse.getToken();
    }

    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    @ResponseBody
    public Profile updateProfile(@RequestParam String phoneNo,
                                 @RequestParam String nationalCode,
                                 @RequestParam String address,
                                 @RequestParam String postalCode,
                                 @RequestHeader("authorization") String headers) {
        Profile profile = validate(headers);
        profile.setPhoneNo(phoneNo);
        profile.setNationalCode(nationalCode);
        profile.setAddress(address);
        profile.setPostalCode(postalCode);
        profileRepository.save(profile);
        return profile;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    @ResponseBody
    public Profile getProfile(@RequestHeader("authorization") String headers) {
        return validate(headers);
    }

    public Profile validate(String headers) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Authorization", headers);
        HttpEntity<String> requestEntity =
                new HttpEntity<>(header);
        ValidateResponse validateResponse = restTemplate.exchange("http://localhost:2000/authentiq/v1/validate/token", HttpMethod.GET,requestEntity,ValidateResponse.class).getBody();
        return profileRepository.findByEmail(validateResponse.getEmail());
    }


}
