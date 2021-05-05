package org.lamisplus.modules.bootstrap;

import com.foreach.across.core.AcrossModule;
import com.foreach.across.core.annotations.AcrossDepends;
import com.foreach.across.core.context.configurer.ComponentScanConfigurer;
import com.foreach.across.modules.hibernate.jpa.AcrossHibernateJpaModule;
import com.foreach.across.modules.web.AcrossWebModule;

@AcrossDepends(required = {AcrossHibernateJpaModule.NAME, AcrossWebModule.NAME})
public class BootstrapModule extends AcrossModule {
    public final static String NAME = "BootstrapModule";

    public BootstrapModule() {
        super();
        addApplicationContextConfigurer(new ComponentScanConfigurer(getClass().getPackage().getName() + ".module",
            getClass().getPackage().getName() + ".configurer", getClass().getPackage().getName() + ".controller",
            getClass().getPackage().getName() + ".domain", getClass().getPackage().getName() + ".repository",
            getClass().getPackage().getName() + ".service", getClass().getPackage().getName() + ".yml",
            getClass().getPackage().getName()+ ".config"));
    }

    @Override
    public String getName() {
        return NAME;
    }
}
