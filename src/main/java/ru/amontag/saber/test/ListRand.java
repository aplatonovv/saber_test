package ru.amontag.saber.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

//Собственно реализация. Их тут две - первая простая ListBuilder, которая собирает список сначала в массив (на самом
//деле в ArrayList, который тоже массив, просто автоматически ресайзится, когда надо), второй хранит в памяти не весь
//индекс нод, а только те, что нужны для восстановления ссылок на другие внутренние ноды
public class ListRand {
    public ListNode head;
    public ListNode tail;
    public int count;

    public void serialize(FileOutputStream out) {
        Map<ListNode, Integer> nodeIndex = new HashMap<>();
        ListNode currentNode = head;

        int index = 0;
        if (head != null) {
            do {
                nodeIndex.put(currentNode, index);
                index++;
                currentNode = currentNode.next;
            } while (currentNode != head && currentNode != null);

            currentNode = head;

            PrintStream ps = new PrintStream(out);
            do {
                ps.println(currentNode.data + ";" + (currentNode.rand == null ? -1 : nodeIndex.get(currentNode.rand)));
                currentNode = currentNode.next;
            } while (currentNode != head && currentNode != null);
        }
    }

    public void deserialize(FileInputStream in) {
        deserialize(in, new ListBuilder());
    }

    public void deserialize(FileInputStream in, IListBuilder builder) {
        this.head = null;
        this.tail = null;
        this.count = 0;

        new BufferedReader(new InputStreamReader(in)).lines().forEach(line -> {
            String[] split = line.split(";");
            String data = split[0];
            Integer indexOfRndNode = Integer.valueOf(split[1]);

            builder.add(data, indexOfRndNode);
        });

        ListRand result = builder.build();

        this.head = result.head;
        this.tail = result.tail;
        this.count = result.count;
    }
}
