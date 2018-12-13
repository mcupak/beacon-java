package com.dnastack.beacon.rest.beans;

import com.dnastack.beacon.adapter.api.BeaconAdapter;
import com.dnastack.beacon.adapter.api.BeaconAdapterFactory;
import com.dnastack.beacon.core.adapter.SampleBeaconAdapterImpl;
import com.dnastack.beacon.service.api.FactoryProvided;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class BeaconAdapterProducer {

    @Inject
    private Instance<BeaconAdapterFactory> beaconAdapterFactory;

    private BeaconAdapter instance;

    @Produces
    @FactoryProvided
    public BeaconAdapter getBeaconAdapter() {

        if (instance == null) {
            if (beaconAdapterFactory.isUnsatisfied()) {
                instance = new SampleBeaconAdapterImpl();
                instance.initAdapter(null);
            } else {
                instance = beaconAdapterFactory.get().getNewInstance();
            }
        }

        return instance;
    }
}
