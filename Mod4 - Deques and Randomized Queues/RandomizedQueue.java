/* *****************************************************************************
 *  Name:              Tom Bernens
 *  Coursera User ID:  https://www.coursera.org/learner/tombernens
 *  Last modified:     Aug 25 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private class Node<Item> {
        Item item;
        Node<Item> previous;
        Node<Item> next;
    }

    private Node<Item> first;
    private Node<Item> last;
    private int length;

    // construct an empty randomized queue
    public RandomizedQueue() {
        first = null;
        last = null;
        length = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return length == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return length;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException("item must not be null");

        Node<Item> node = new Node<Item>();
        node.item = item;

        if (length == 0) {
            first = node;
            last = node;
        }
        else {
            node.previous = last;
            last.next = node;
            last = node;
        }
        length += 1;
    }

    private Node<Item> getNthNode(int n) {
        if (n >= length)
            throw new NoSuchElementException();

        if (n < length / 2) {
            Node<Item> node = first;
            for (int i = 0; i < n; i++) {
                node = node.next;
            }
            return node;
        }
        else {
            Node<Item> node = last;
            for (int i = length - 1; i > n; i--) {
                node = node.previous;
            }
            return node;
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (length == 0)
            throw new NoSuchElementException();

        int idx = StdRandom.uniformInt(0, length);
        Node<Item> node = getNthNode(idx);
        if (node.previous != null) node.previous.next = node.next;
        else first = node.next;
        if (node.next != null) node.next.previous = node.previous;
        else last = node.previous;
        length -= 1;
        return node.item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (length == 0)
            throw new NoSuchElementException();

        int idx = StdRandom.uniformInt(0, length);
        Node<Item> node = getNthNode(idx);
        return node.item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator<>(this);
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<String>();


        StdOut.println(queue.size());
        StdOut.println(queue.isEmpty());

        queue.enqueue("a");
        queue.enqueue("b");
        queue.enqueue("c");
        queue.enqueue("d");
        queue.enqueue("e");
        queue.enqueue("f");
        queue.enqueue("g");

        StdOut.println(queue.size());
        StdOut.println(queue.isEmpty());

        StdOut.println("Randomly iterating");
        for (String s : queue) {
            StdOut.println(s);
        }

        StdOut.println("Sampling 10x");
        for (int i = 0; i < 10; i++) {
            StdOut.println(queue.sample());
        }

        StdOut.println("Emptying Queue");
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());

        StdOut.println(queue.size());
        StdOut.println(queue.isEmpty());

        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());

        StdOut.println(queue.size());
        StdOut.println(queue.isEmpty());
    }

    private class RandomizedQueueIterator<Item> implements Iterator<Item> {
        private RandomizedQueue<Item> queue;
        private int[] order;
        private int idx;

        public RandomizedQueueIterator(RandomizedQueue<Item> queueIn) {
            queue = queueIn;
            order = new int[queue.length];
            for (int i = 0; i < queue.length; i++)
                order[i] = i;
            StdRandom.shuffle(order);
            idx = 0;
        }

        public boolean hasNext() {
            return idx != queue.length;
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("no more items in sequence");

            RandomizedQueue<Item>.Node<Item> node = queue.getNthNode(order[idx]);
            idx += 1;
            return node.item;
        }

        public void remove() {
            throw new UnsupportedOperationException("deque iterator doesn't support remove");
        }

    }

}
