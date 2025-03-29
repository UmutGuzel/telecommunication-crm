package com.gygy.paymentservice.core.web;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BaseController {
    protected final Pipeline pipeline;
}