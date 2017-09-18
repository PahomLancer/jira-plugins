package ut.com.atlassian.externaldbplugin;

import org.junit.Test;
import com.atlassian.externaldbplugin.api.MyPluginComponent;
import com.atlassian.externaldbplugin.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}