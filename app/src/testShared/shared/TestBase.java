package shared;

import java.util.Random;
import java.util.UUID;

/**
 * Created by niltonvasques on 11/3/16.
 */

public class TestBase {
    protected static final String STRING_UNIQUE = UUID.randomUUID().toString();
    protected static final String STRING_UNIQUE2 = UUID.randomUUID().toString();
    protected static final String STRING_UNIQUE3 = UUID.randomUUID().toString();
    protected static final Float FLOAT_RANDOM = new Random().nextFloat();
    protected static final float DELTA = 0.00001f;
}
