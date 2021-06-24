package com.smartdev.ufoss.service;

public interface EmailSenderService {

    public void sendEmail(String to, String email);

    public void resetPassword(String email);
}
