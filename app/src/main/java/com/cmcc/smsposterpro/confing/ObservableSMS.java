package com.cmcc.smsposterpro.confing;

import com.cmcc.smsposterpro.service.SmsServer;

import java.util.Map;
import java.util.Vector;

public class ObservableSMS {
    private boolean changed = false;
    private Vector<SmsServer> obs;

    public ObservableSMS() {
        obs = new Vector<>();
    }

    private static final ObservableSMS instance = new ObservableSMS();

    public static ObservableSMS getInstance() {
        return instance;
    }

    public synchronized void addObserver(SmsServer o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    public void updateValue(Map<String, String> data) {
        synchronized (this) {
            setChanged();
            notifyObservers(data);
        }
    }

    protected synchronized void setChanged() {
        changed = true;
    }

    public void notifyObservers(Map<String, String> arg) {
        Object[] arrLocal;

        synchronized (this) {
            if (!hasChanged())
                return;
            arrLocal = obs.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length - 1; i >= 0; i--)
            ((SmsServer) arrLocal[i]).update(this, arg);
    }

    public synchronized void deleteObservers() {
        obs.removeAllElements();
    }

    protected synchronized void clearChanged() {
        changed = false;
    }

    public synchronized boolean hasChanged() {
        return changed;
    }

    public synchronized int countObservers() {
        return obs.size();
    }
}
