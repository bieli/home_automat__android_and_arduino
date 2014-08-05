package net.bieli.HomeAutomation.Services.HaHttp;

/**
 * HAService interface
 *
 * Send message into open hardware.
 *
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */
public interface HAService {
    Boolean send(HAMessage message);
    int recive();
    void setLoggerTag(String logTag);
    void setToken(String token);
}
