package com.gygy.userservice.core.pipelines.authorization;

import java.util.List;

public interface RequiresAuthorization {
    List<String> getRequiredAuthorizations();
}