package org.itstep.msk.app.service;

import org.itstep.msk.app.entity.Upload;

import java.util.List;
import java.util.Set;

public interface SearchService<T extends Upload> {
    Set<T> findByName(String name);

    List<T> findAll();
}
