package net.planetgeeks.mcstarter.auth;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true, value = { "session" })
public class Profile
{
    private String id;
    private String name;
    private boolean legacy;
    private Session session;
}
