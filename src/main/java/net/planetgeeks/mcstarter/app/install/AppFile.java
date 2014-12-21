package net.planetgeeks.mcstarter.app.install;

import lombok.Getter;
import lombok.Setter;
import net.planetgeeks.mcstarter.util.http.HttpFile;

public class AppFile
{
    @Getter @Setter
    public HttpFile remoteFile;
    
    @Getter @Setter
    private FileChecksum checksum;
}
