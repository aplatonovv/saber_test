package ru.amontag.saber.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ListRandTest {
    @Test
    public void test1() throws Exception {
        ListBuilder builder = new ListBuilder();
        builder.add("0");
        builder.add("1", 0);
        builder.add("2", 4);
        builder.add("3");
        builder.add("4");

        test(builder.build(), "0\n1:0\n2:4\n3\n4\n");
    }

    //пробуем краевые случаи - 1 нода, 1 нода с сылкой на себя, 0 нод
    @Test
    public void test2() throws Exception {
        ListBuilder builder = new ListBuilder();
        builder.add("0");
        test(builder.build(), "0\n");
    }

    @Test
    public void test3() throws Exception {
        ListBuilder builder = new ListBuilder();
        builder.add("0", 0);
        test(builder.build(), "0:0\n");
    }


    @Test
    public void test4() throws Exception {
        ListBuilder builder = new ListBuilder();
        test(builder.build(), "");
    }

    public static void test(ListRand list, String mustHaveStructure) throws IOException {
        assertEquals(mustHaveStructure, toString(list));
        try (FileOutputStream out = new FileOutputStream("/tmp/builder1")) {
            list.serialize(out);
        }

        try (FileInputStream in = new FileInputStream("/tmp/builder1")) {
            ListRand answer1 = new ListRand();
            answer1.deserialize(in, new ListBuilder());
            assertEquals(toString(list), toString(answer1));
        }

        try (FileInputStream in = new FileInputStream("/tmp/builder1")) {
            ListRand answer2 = new ListRand();
            answer2.deserialize(in, new ListBuilder2());
            assertEquals(toString(list), toString(answer2));
        }
    }

    public static String toString(ListRand rand) {
        ListNode currentNode = rand.head;
        StringBuilder builder = new StringBuilder();
        if (currentNode != null) {
            do {
                builder.append(currentNode.data);
                if (currentNode.rand != null) {
                    builder.append(":");
                    builder.append(currentNode.rand.data);
                }
                builder.append("\n");
                currentNode = currentNode.next;
            } while (currentNode != null && currentNode != rand.head);
        }

        return builder.toString();
    }
}
