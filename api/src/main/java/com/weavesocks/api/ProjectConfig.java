package com.weavesocks.api;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config.properties"})
public interface ProjectConfig extends Config {

    String baseUrl();

    @DefaultValue("ru")
    String locale();

    boolean logging();

}
