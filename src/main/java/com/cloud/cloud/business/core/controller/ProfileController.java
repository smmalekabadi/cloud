package com.cloud.cloud.business.core.controller;

import com.cloud.cloud.business.data.*;
import com.cloud.cloud.business.data.repository.ProfileRepository;
import com.cloud.cloud.business.data.repository.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.*;
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

        ImmutableMap payload = ImmutableMap.of("email", profile.getEmail(), "password", profile.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ImmutableMap> requestEntity =
                new HttpEntity<>(payload, headers);
        try {
            ResponseEntity<RegisterResponse> registerResponseResponseEntity = restTemplate.postForEntity("http://localhost:2052/authentiq/v1/user/register", requestEntity, RegisterResponse.class);
            RegisterResponse registerResponse = registerResponseResponseEntity.getBody();
//            RegisterResponse registerResponse = restTemplate.postForEntity("http://localhost:2052/authentiq/v1/user/register", requestEntity, RegisterResponse.class).getBody();
            long profileId = profileRepository.save(profile).getId();
            Wallet wallet = new Wallet(profileId, 0);
            walletRepository.save(wallet);
            return registerResponse.getToken();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Please check you username and password or maybe this email is already exists";
        }

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
//        return profileRepository.findById(8L).get();
        return validate(headers);
    }

    public Profile validate(String headers) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.set("Authorization", headers);
        HttpEntity<String> requestEntity =
                new HttpEntity<>(header);
        ValidateResponse validateResponse = restTemplate.exchange("http://localhost:2000/authentiq/v1/validate/token", HttpMethod.GET, requestEntity, ValidateResponse.class).getBody();
        return profileRepository.findByEmail(validateResponse.getEmail());
    }


}
