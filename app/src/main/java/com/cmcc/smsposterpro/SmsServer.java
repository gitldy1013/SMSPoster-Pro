package com.cmcc.smsposterpro;

import java.util.Map;

public interface SmsServer {
    void update(ObservableSMS observableSMS, Map<String, String> message);
}