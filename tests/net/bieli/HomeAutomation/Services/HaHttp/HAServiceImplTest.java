package net.bieli.HomeAutomation.Services.HaHttp;

import android.util.Log;

import android.test.InstrumentationTestCase;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URI;

import static net.bieli.HomeAutomation.Services.HAMessageType.SET_OUTPUT_DIGITAL;

import static org.junit.Assert.*;

//@RunWith(Runner.class)
public class HAServiceImplTest extends InstrumentationTeestCase {
    @Mock
    private DefaultHttpClient httpClient;

    @Mock
    private HttpPost request;

    @Mock
    private URI uri;

    @Mock
    private Log logger;

    private HAServiceImpl hAServiceImpl;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSaveMessage() throws Exception {
        // given
        Integer value = 1;
        byte mask = 1;
        byte haMessageType = SET_OUTPUT_DIGITAL;
        Boolean realValue = false;

        HAMessage haMessage = new HAMessage(
            value, mask, haMessageType, realValue
        );
        Boolean expectedResult = true;

        // when
        Boolean result = hAServiceImpl.send(haMessage);

        // then
        assertEquals(expectedResult, result);
    }
}