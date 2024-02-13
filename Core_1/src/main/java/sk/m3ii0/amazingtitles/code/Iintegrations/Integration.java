package sk.m3ii0.amazingtitles.code.Iintegrations;

import sk.m3ii0.amazingtitles.code.api.AmazingTitles;

import java.io.File;

public interface Integration {

    void reload();

    default File getDataFolder() {
        return AmazingTitles.INTEGRATIONS_FOLDER;
    }

}
