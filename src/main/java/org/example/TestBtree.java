package org.example;

import btree4j.*;

import java.io.File;

public class TestBtree {
    public static void main(String[] args) throws BTreeException {
//        test();
        testIndex();
    }

    public static void test() throws BTreeException {
        File tFile = new File("t1.idx");
        BTree bTree = new BTree(tFile);
        bTree.init(false);

//        for (int i = 0; i < 1000; i++) {
//            Value k = new Value("k" + i);
//            long v = i;
//            bTree.addValue(k, v);
//        }
//        for (int i = 0; i < 1000; i++) {
//            Value v = new Value("k" + i);
//            long expected = i;
//            long actual = bTree.findValue(v);
//            System.out.println(actual);
//            System.out.println(expected);
//        }
    }

    private static void testIndex() throws BTreeException {
        BTreeIndex btree = new BTreeIndex(new File("test"));
        btree.init(/* bulkload */ false);

//        for (int i = 0; i < 1000; i++) {
//            Value k = new Value("k" + i);
//            Value v = new Value("v" + i);
//
//            btree.addValue(k, v);
//            if (i % 100 == 0) {
//                btree.putValue(k, new Value("v" + i + "_u"));
//            }
//        }
//
//        btree.flush();
//        btree.close();

//        btree.removeValue(new Value("k999"));
//        btree.flush();
//        btree.close();

        for (int i = 0; i < 1000; i++) {
            Value k = new Value("k" + i);
            final Value expected;
            if (i % 100 == 0) {
                expected = new Value("v" + i + "_u");
            } else {
                expected = new Value("v" + i);
            }
            Value actual = btree.getValue(k);
//            btree.
            System.out.println("expected: " + expected);
            System.out.println("actual  : " + actual);
        }

    }
}
