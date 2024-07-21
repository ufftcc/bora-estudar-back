package com.ufftcc.boraestudar.mappers;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;

public abstract class BaseEntityMapper<E> {

    private final Class<E> entityClass;
    protected final ModelMapper modelMapper;

    public BaseEntityMapper(Class<E> entityClass, ModelMapper modelMapper) {
        this.entityClass = entityClass;
        this.modelMapper = modelMapper;
    }

    public final E toEntity(Object source) {
        return modelMapper.map(source, entityClass);
    }

    public <T> T toTransferObject(Object source, Class<T> transferObjectClass) {
        return modelMapper.map(source, transferObjectClass);
    }

    public final List<E> toEntityList(Collection<E> objects) {
        return objects.stream().map(this::toEntity).toList();
    }

    public final <T> List<T> toTransferObjectList(Collection<E> objects, Class<T> transferObjectClass) {
        return objects.stream().map((o) -> this.toTransferObject(o, transferObjectClass)).toList();
    }

}