package ru.amontag.saber.test;

public interface IListBuilder {
    ListRand build();
    void add(String data, Integer rndNodeIndex);
    void add(String data);
}
