package com.tcarroll10.findata.repo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FindataMetadataAbstractFileDao {

    protected byte[] jsonData;

    public FindataMetadataAbstractFileDao(final String filePath) {

        Path path = Paths.get(filePath);
        try {
            this.jsonData = Files.readAllBytes(path);

        } catch (IOException e) {

            throw new RuntimeException("Error reading JSON file", e);
        }

    }

}
