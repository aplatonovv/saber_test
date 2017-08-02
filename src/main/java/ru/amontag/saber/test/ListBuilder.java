package ru.amontag.saber.test;

import java.util.ArrayList;
import java.util.List;

public class ListBuilder implements IListBuilder {
    //к вопросу а зачем для создания списка список - можно было бы и на базе массива это сделать,
    //но реаллокация массива просто усложнила бы чтение основной логики, в данном случае я опираюсь
    //на то, что тут ArrayList - это массив и мы ничего не теряем
    private List<NodeWithRndIndex> nodes = new ArrayList<>();

    @Override
    public void add(String data) {
        add(data, -1);
    }

    @Override
    public void add(String data, Integer rndIndex) {
        ListNode node = new ListNode();
        node.data = data;
        nodes.add(new NodeWithRndIndex(node, rndIndex));
    }

    @Override
    public ListRand build() {
        ListRand result = new ListRand();

        if (!nodes.isEmpty()) {
            NodeWithRndIndex root = nodes.get(0);
            result.head = root.node;
            result.tail = root.node;
            result.head.next = result.tail;
            result.tail.prev = result.head;

            setRnd(root);
            ListNode prevNode = root.node;
            for (int i = 1; i < nodes.size(); i++) {
                NodeWithRndIndex currentNode = nodes.get(i);
                prevNode.next = currentNode.node;
                currentNode.node.prev = prevNode;
                result.tail = currentNode.node;
                setRnd(currentNode);

                prevNode = currentNode.node;
            }

            if(nodes.size() > 1) { //по условию список не циклический
                result.head.prev = null;
                result.tail.next = null;
            }
        }

        result.count = nodes.size();
        return result;
    }

    private void setRnd(NodeWithRndIndex nodeWithRndIndex) {
        ListNode node = nodeWithRndIndex.node;
        Integer rndIndex = nodeWithRndIndex.indexOfRndNode;

        if (rndIndex > nodes.size()) {
            throw new ArrayIndexOutOfBoundsException("Node with index " + rndIndex + " doesn't exist");
        } else if(rndIndex != -1) {
            node.rand = nodes.get(rndIndex).node;
        }
    }

}
