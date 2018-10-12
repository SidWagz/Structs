package org.waglesid.strcuts.parenttree;

import org.junit.Test;
import org.waglesid.structs.parenttree.ParentTreeSet;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ParentTreeSetTest {

    @Test
    public void simpleAdd() {
        final ParentTreeSet<Integer, TestData> treeSet = new ParentTreeSet<>(TestData::keyFunc);
        final TestData data = new TestData(1, "One");
        treeSet.add(data);
        assertTrue(treeSet.contains(data));
    }

    @Test
    public void duplicateAdd() {
        final ParentTreeSet<Integer, TestData> treeSet = new ParentTreeSet<>(TestData::keyFunc);
        final TestData data = new TestData(1, "One");
        final TestData dataDuplicate = new TestData(1, "Not One");
        treeSet.add(data);
        treeSet.add(dataDuplicate);
        assertTrue(treeSet.contains(data));
        assertFalse(treeSet.containsAndEquals(data));
        assertTrue(treeSet.containsAndEquals(dataDuplicate));
    }

    @Test
    public void simpleParent() {
        final ParentTreeSet<Integer, TestData> treeSet = new ParentTreeSet<>(TestData::keyFunc);
        final TestData parent = new TestData(1, "One");
        final TestData child = new TestData(2, "Two");
        treeSet.addParent(child, parent);
        assertTrue(treeSet.contains(child));
        assertTrue(treeSet.contains(parent));
        assertNull(treeSet.parentOf(parent));
        assertEquals(parent, treeSet.parentOf(child));
    }

    @Test
    public void simpleChild() {
        final ParentTreeSet<Integer, TestData> treeSet = new ParentTreeSet<>(TestData::keyFunc);
        final TestData parent = new TestData(1, "One");
        final TestData child = new TestData(2, "Two");
        treeSet.addParent(child, parent);
        assertTrue(treeSet.contains(child));
        assertTrue(treeSet.contains(parent));
        assertThat(treeSet.childrenOf(parent), hasItem(child));
    }

    @Test
    public void manyChildren() {
        final ParentTreeSet<Integer, TestData> treeSet = new ParentTreeSet<>(TestData::keyFunc);
        final TestData parent = new TestData(1, "One");
        final TestData child2 = new TestData(2, "Two");
        final TestData child3 = new TestData(3, "Three");
        final TestData child4 = new TestData(4, "Four");
        final TestData notChild = new TestData(5, "Five");
        treeSet.addParent(child2, parent);
        treeSet.addParent(child3, parent);
        treeSet.addParent(child4, parent);
        treeSet.add(notChild);
        assertTrue(treeSet.contains(parent));
        assertTrue(treeSet.contains(child2));
        assertTrue(treeSet.contains(child3));
        assertTrue(treeSet.contains(child4));
        assertThat(treeSet.childrenOf(parent), hasItem(child2));
        assertThat(treeSet.childrenOf(parent), hasItems(child3, child4));
        assertThat(treeSet.childrenOf(parent), not(hasItems(notChild)));
    }
}

class TestData {

    private int key;
    private String payload;

    public TestData(int key, String payload) {
        this.key = key;
        this.payload = payload;
    }

    public int getKey() {
        return key;
    }

    public String getPayload() {
        return payload;
    }

    public static int keyFunc(TestData testData) {
        return testData.getKey();
    }

    @Override
    public String toString() {
        return "TestData{" +
                "key=" + key +
                ", payload='" + payload + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestData data = (TestData) o;
        return key == data.key &&
                Objects.equals(payload, data.payload);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key, payload);
    }
}