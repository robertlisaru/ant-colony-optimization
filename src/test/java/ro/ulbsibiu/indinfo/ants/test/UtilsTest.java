package ro.ulbsibiu.indinfo.ants.test;

import org.junit.Test;
import ro.ulbsibiu.indinfo.ants.Util;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class UtilsTest {
    @Test
    public void testDistance() {
        Point a = new Point(3, 4);
        Point b = new Point(7, 1);
        assertEquals(5, Util.distanceInt(a, b));
    }
}
