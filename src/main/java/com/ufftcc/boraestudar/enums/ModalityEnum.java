package com.ufftcc.boraestudar.enums;

public enum ModalityEnum {
    PRESENTIAL(1),
    REMOTE(2),
    HYBRID(3);

    public final Integer id;

    private ModalityEnum(Integer id) {
        this.id = id;
    }
}
