package dev.langchain4j.example.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

// Warning: scope definition required by Helidon (Issue #8502)
@ApplicationScoped 
@ApplicationPath("/api")
public class RestApplication extends Application { }
