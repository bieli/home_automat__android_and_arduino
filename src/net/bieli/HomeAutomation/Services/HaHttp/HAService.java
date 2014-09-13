package net.bieli.HomeAutomation.Services.HaHttp;

import java.net.URISyntaxException;

/**
 * HAService interface
 *
 * Send message into open hardware.
 *
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */
public interface HAService {
    Boolean send(HAMessage message);
    void setServiceUri(String serviceUri) throws URISyntaxException;
    void setLoggerTag(String logTag);
    void setToken(String token);
}
