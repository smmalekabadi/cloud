package com.cloud.cloud.business.core.controller;

import com.cloud.cloud.business.data.Transaction;
import com.cloud.cloud.business.data.Wallet;
import com.cloud.cloud.business.data.repository.TransactionRepository;
import com.cloud.cloud.business.data.repository.WalletRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account/")
public class TransactionController {
    private TransactionRepository transactionRepository;
    private ProfileController profileController;
    private WalletRepository walletRepository;
    public TransactionController(TransactionRepository transactionRepository, ProfileController profileController, WalletRepository walletRepository){
        this.profileController = profileController;
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }
    @RequestMapping(value = "/wallet", method = RequestMethod.GET)
    @ResponseBody
    public long getWalletValue(@RequestHeader("authorization") String headers){
        Wallet wallet = walletRepository.findByProfileId(profileController.validate(headers).getId());

         return 0L;
    }
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    @ResponseBody
    public long pay(@RequestHeader("authorization") String headers) {
        String token = headers.substring(7);
        return 0L;
    }
    @RequestMapping(value = "/pay/callback", method = RequestMethod.POST)
    @ResponseBody
    public void  callback() {
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    @ResponseBody
    public List<Transaction> transactions(@RequestHeader("authorization") String headers) {
        String token = headers.substring(7);
        return transactionRepository.findByProfileId(10L);
    }


}
