package ru.amontag.saber.test;

//вспомогательный класс-декоратор, т.к. мы не можем модифицировать исходный
public class NodeWithRndIndex {
    public ListNode node;
    public Integer indexOfRndNode;

    public NodeWithRndIndex(ListNode node, Integer indexOfRndNode) {
        this.node = node;
        this.indexOfRndNode = indexOfRndNode;
    }
}
