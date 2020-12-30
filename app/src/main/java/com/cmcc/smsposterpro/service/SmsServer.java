package com.cmcc.smsposterpro.service;

import com.cmcc.smsposterpro.confing.ObservableSMS;

import java.util.Map;

public interface SmsServer {
    void update(ObservableSMS observableSMS, Map<String, String> message);
}