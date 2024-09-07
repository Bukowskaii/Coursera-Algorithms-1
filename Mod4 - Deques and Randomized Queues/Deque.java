/* *****************************************************************************
 *  Name:              Tom Bernens
 *  Coursera User ID:  https://www.coursera.org/learner/tombernens
 *  Last modified:     Aug 25 2024
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node<Item> {
        Item item;
        Node<Item> previous;
        Node<Item> next;
    }

    private Node<Item> first;
    private Node<Item> last;
    private int length;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        length = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return length;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException("item must not be null");

        Node<Item> node = new Node<>();
        node.item = item;

        if (length == 0) {
            first = node;
            last = node;
        }
        else {
            node.next = first;
            first.previous = node;
            first = node;
        }
        length += 1;
    }


    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException("item must not be null");

        Node<Item> node = new Node<>();
        node.item = item;

        if (length == 0) {
            first = node;
            last = node;
            length += 1;
        }
        else {
            node.previous = last;
            last.next = node;
            last = node;
            length += 1;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("deque is empty");

        if (length == 1) {
            Item item = first.item;
            first = null;
            last = null;
            length -= 1;
            return item;
        }
        else {
            Item item = first.item;
            first = first.next;
            first.previous = null;
            length -= 1;
            return item;
        }
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("deque is empty");

        if (length == 1) {
            Item item = last.item;
            first = null;
            last = null;
            length -= 1;
            return item;
        }
        else {
            Item item = last.item;
            last = last.previous;
            last.next = null;
            length -= 1;
            return item;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator<Item>(this);
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();

        StdOut.println(deque.isEmpty());

        deque.addLast("this");
        deque.addLast("is");
        deque.addLast("a");

        StdOut.println(deque.size());

        deque.addLast("sentance");
        deque.addFirst("think");
        deque.addFirst("I");

        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());

        for (String s : deque) {
            StdOut.println(s);
        }

        StdOut.println("removed " + deque.removeFirst());
        StdOut.println("removed " + deque.removeLast());

        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());

        for (String s : deque) {
            StdOut.println(s);
        }

        StdOut.println("removed " + deque.removeLast());
        StdOut.println("removed " + deque.removeLast());
        StdOut.println("removed " + deque.removeLast());
        StdOut.println("removed " + deque.removeLast());

        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());
    }

    private class DequeIterator<Item> implements Iterator<Item> {
        private Deque<Item>.Node<Item> current;

        public DequeIterator(Deque<Item> dequeIn) {
            current = dequeIn.first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null)
                throw new NoSuchElementException("no more items in sequence");

            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("deque iterator doesn't support remove");
        }
    }
}


