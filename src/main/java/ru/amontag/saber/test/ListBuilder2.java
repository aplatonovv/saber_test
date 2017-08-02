package ru.amontag.saber.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ListBuilder2 implements IListBuilder {
    //более сложная реализация - не требует дополнительного списка, но сложнее в прочтении
    //более того, при увеличении просеттаных rand-нод этот метод будет приближаться к ListBuilder

    //идея такая, что мы можем потратить дополнительное время на обход списка, чтобы построить обратный индекс
    //rnd-нод и заюзать только необходимый доп. объем памяти для этого. Эффективно только для очень больших списков,
    //которые дешевле подождать-обойти лишний раз, чем строить индекс по всем нодам.
    private ListNode head;
    private ListNode tail;
    private ListNode prevNode;

    private int currentNodeIndex = 0;
    //хранит индексы только тех нод, в которых есть rnd
    private Map<Integer, NodeWithRndIndex> indexes = new HashMap<>();

    @Override
    public void add(String data) {
        add(data, -1);
    }

    @Override
    public void add(String data, Integer rndIndex) {
        if (head == null) {
            head = new ListNode();
            tail = head;
            head.data = data;
        } else {
            ListNode newNode = new ListNode();
            newNode.prev = prevNode;
            prevNode.next = newNode;
            newNode.data = data;
            tail = newNode;
        }

        if (rndIndex != -1) {
            indexes.put(currentNodeIndex, new NodeWithRndIndex(tail, rndIndex));
        }

        currentNodeIndex++;
        prevNode = tail;
    }

    @Override
    public ListRand build() {
        Map<Integer, ListNode> rndIndexToNode = new HashMap<>();
        Set<Integer> allRndIndexes = indexes.values().stream().map(x -> x.indexOfRndNode).collect(Collectors.toSet());

        ListRand answer = new ListRand();
        answer.count = currentNodeIndex;
        answer.head = head;
        answer.tail = tail;

        if (currentNodeIndex != 0) {
            if (currentNodeIndex > 1) { //по условию список не циклический
                head.prev = null;
                tail.next = null;
            }

            ListNode currentNode = head;
            int index = 0;

            while (currentNode != null) {
                if (allRndIndexes.contains(index)) {
                    rndIndexToNode.put(index, currentNode);
                }

                index++;
                currentNode = currentNode.next;
            }

            index = 0;
            currentNode = head;
            while (currentNode != null) {
                NodeWithRndIndex nodeWithRndIndex = indexes.get(index);
                if (nodeWithRndIndex != null) {
                    int rndIndex = nodeWithRndIndex.indexOfRndNode;
                    currentNode.rand = rndIndexToNode.get(rndIndex); //на -1 вернет null, так что ок
                }

                index++;
                currentNode = currentNode.next;
            }

            if(currentNodeIndex == 1) { //а вот если там 1 элемент, то он не может быть не цикликическим
                //сеттаем тут, чтобы не зациклиться
                head.prev = tail;
                tail.next = head;
            }
        }

        return answer;
    }

    private class IndexesTuple {
        private int currentIndex;
        private int rndIndex;

        public IndexesTuple(int currentIndex, int rndIndex) {
            this.currentIndex = currentIndex;
            this.rndIndex = rndIndex;
        }
    }
}
