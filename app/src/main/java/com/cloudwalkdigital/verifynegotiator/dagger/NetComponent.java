package com.cloudwalkdigital.verifynegotiator.dagger;

import com.cloudwalkdigital.verifynegotiator.LoginActivity;
import com.cloudwalkdigital.verifynegotiator.dagger.modules.AppModule;
import com.cloudwalkdigital.verifynegotiator.dagger.modules.NetModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by alleoindong on 6/19/17.
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(LoginActivity activity);
}
