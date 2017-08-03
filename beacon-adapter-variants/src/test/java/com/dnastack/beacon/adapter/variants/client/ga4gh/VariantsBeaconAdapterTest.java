package com.dnastack.beacon.adapter.variants.client.ga4gh;

import com.dnastack.beacon.adapter.api.BeaconAdapter;
import com.dnastack.beacon.adater.variants.VariantsBeaconAdapter;
import com.dnastack.beacon.exceptions.BeaconException;
import com.dnastack.beacon.utils.AdapterConfig;
import com.dnastack.beacon.utils.ConfigValue;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Adapter test
 *
 * @author Andrey Mochalov (mochalovandrey@gmail.com)
 */
public class VariantsBeaconAdapterTest {

    private final static String BEACON_FILE = "test_beacon.json";
    private final static AdapterConfig adapterConfig = createConfig();

    private static AdapterConfig createConfig() {
        ClassLoader cl = VariantsBeaconAdapter.class.getClassLoader();
        try {
            String beaconJson = cl.getResource(BEACON_FILE).toURI().getPath();
            List<ConfigValue> values = new ArrayList<>();
            values.add(new ConfigValue("beaconJsonFile", beaconJson));

            return new AdapterConfig("variants_test_beacon", AdapterConfig.class.getCanonicalName(), values);
        } catch (URISyntaxException e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    @Test
    public void testInitAdapter() throws BeaconException {
        BeaconAdapter adapter = new VariantsBeaconAdapter();
        adapter.initAdapter(adapterConfig);
        assertThat(adapter.getBeacon()).isNotNull();
    }

    @Test
    public void testAdapterMustBeInitialized() {
        BeaconAdapter adapter = new VariantsBeaconAdapter();
        assertThatThrownBy(adapter::getBeacon).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> adapter.getBeaconAlleleResponse(null,
                null,
                null,
                null,
                null,
                null,
                null)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> adapter.getBeaconAlleleResponse(null)).isInstanceOf(IllegalStateException.class);
    }

}
