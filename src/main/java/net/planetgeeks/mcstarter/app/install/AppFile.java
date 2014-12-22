package net.planetgeeks.mcstarter.app.install;

import java.io.File;
import java.net.URL;

import lombok.Getter;
import lombok.Setter;

public class AppFile
{
    @Getter @Setter
    public URL remoteLocation;

    @Getter @Setter
    public File destination;
    
    @Getter @Setter
    private FileChecksum checksum;
}
