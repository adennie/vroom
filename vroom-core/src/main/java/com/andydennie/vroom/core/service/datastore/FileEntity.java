package com.andydennie.vroom.core.service.datastore;


import com.andydennie.vroom.core.domain.IFile;

public class FileEntity<
        F extends IFile,
        DAO extends FileDao<F>>
        extends VroomEntity<F, DAO>
        implements IFileEntity<F>
{

    public FileEntity(final Class<F> fileDoClass, final Class<DAO> fileDaoClass) {
        super(fileDoClass, fileDaoClass);
    }

    @Override
    public void create(final F file, final byte[] bytes) {
        // subclasses should override if they want to support writing the file contents to a persistent store
        throw new UnsupportedOperationException();
    }
}
