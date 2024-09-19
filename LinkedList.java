/* Name: Nabeel Majid(C3287060)
 * Seng4500-Assignment1
 * Date 22/08/23
description: LinkedList class to store all the Data and also have Iterator to traverse through the data
of Linkedlist
*/

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class LinkedList <T extends TaxScale> implements Iterable<T> {
    //constructor
    private Node<T> sentinel;
    private int mod;
    private int size;
    LinkedList()
    {
        sentinel = new Node<T>();

        size=0;
        mod = 0;

    }
    public int get_Size()
    {
        return size;
    }
// add data at the head of linkedlist






    //To add data at the end of linkedlist
    public void append(T data) {

        Node<T> add = new Node<T>(data);
        Node<T> current = sentinel;
        while (current.get_Next() != null) {
            current= current.get_Next();

        }
        current.set_Next(add);
        add.set_Prev(current);
        size++;
        mod++; // incread modCount
    }
    //Method to add data before current pointer
    public void Insert(T data)
    {
        if (size == 0) {
            append(data);
            return;
        }
        else
        {
            Node<T> temp = new Node<T>(data);
            Node<T> current = sentinel;
            while (current.get_Next() != null) {
                current = current.get_Next();
            }

            temp.set_Next(current.get_Next());
            temp.set_Prev(current);
            current.set_Next(temp);
            mod++;
            size++;
        }

    }
    //method to add after the current
    public void insertAfter(T data, T new_data) {
        Node<T> newNode = new Node<T>(new_data);
        Node<T> temp = new Node<T>(data);
        Node<T> current = sentinel;

        while (current.get_Next() != null)
        {
            current = current.get_Next();
            if (current.get_Data() == temp.get_Data()) {
                break;
            }
        }
        if (current.get_Next() != null)
        {
            current.get_Next().set_Prev(newNode);
            newNode.set_Next(current.get_Next());
            newNode.set_Prev(current);
            current.set_Next(newNode);
        }
        else
        {
            newNode.set_Next(current.get_Next());
            newNode.set_Prev(current);
            current.set_Next(newNode);
        }

        size++;
        mod++;
    }
    public void insertBefore(T data, T new_data) {
        Node<T> new_Node = new Node<T>(data);
        Node<T> temp = new Node<T>(new_data);
        Node<T> current = sentinel;
        while (current.get_Next() != null) {
            current = current.get_Next();
            if (current.get_Data() == new_Node.get_Data()) {
                break;
            }
        }

        temp.set_Prev(current.get_Prev());
        current.get_Prev().set_Next(temp);
        temp.set_Next(current);
        current.set_Prev(temp);

        size++;
        mod++;
    }

    //this method will remove data from the list
    public void remove(T data) {
        Node<T> current = sentinel;
        Node<T> temp = new Node<T>(data);


        while (current.get_Next() != null)
        {
            current = current.get_Next();
            if (current.get_Data() == temp.get_Data())
            {
                break;
            }
        }
        if (current.get_Next() != null && current.get_Prev() != null)
        {
            current.get_Prev().set_Next(current.get_Next());
            current.get_Next().set_Prev(current.get_Prev());

        }
        else if (current.get_Next() == null && current.get_Prev() != null)
        {
            current.get_Prev().set_Next(current.get_Next());
        }
        else if (current.get_Next() != null && current.get_Prev() == null)
        {
            current.get_Next().set_Prev(current.get_Prev());
        }

        size--;
        mod++;
    }


    public void insertInOrder(T new_data) {
        Node<T> current = sentinel;
        TaxScale emptyNode = new TaxScale(0, 0, 0, 0);
        if (size == 0) {
            append(new_data);
            return;
        } else if (size == 1 && current.get_Next().get_Data().get_startIncome().equals(0) &&
                current.get_Next().get_Data().get_endIncome().equals(0)) {
            // when there is only one initialed TaxScale in the List
            current = current.get_Next();
            if (new_data.get_startIncome() == 0) {
                // update range
                current.get_Data().set_endIncome(new_data.get_endIncome());
                current.get_Data().set_baseTax(new_data.get_baseTax());
                current.get_Data().set_TaxPD(new_data.get_TaxPD());
            } else {
                // update range (only update the first TaxScale's end_income)
                current.get_Data().set_endIncome(new_data.get_startIncome() - 1);
                // Insert new TaxScale into List
                Insert(new_data);
            }

        } else {

            while (current.get_Next() != null) { // loop form the node after sentinel
                current = current.get_Next();

                // compare the start_income (current vs. new_data)

                if (current.get_Data().compareTo(new_data) == 1) {
                    // check if new_data's start_income < current.get_Next(). start_income
                    if ((current.get_Next() == null)) {
                        Insert(new_data); // when there is no next TaxScale in List.
                        break;
                    } else if ((current.get_Next() != null)
                            && new_data.get_startIncome() < current.get_Next().get_Data().get_startIncome()) {

                        insertBefore(current.get_Next().get_Data(), new_data);

                        // overwrite the data from current.next()
                        while (current.get_Next() != null) {

                            current = current.get_Next(); // first move cursor point to new_data

                            if ((current.get_Next() != null) && (current.get_Data().get_endIncome()!= -1) &&
                                    (current.get_Data().get_endIncome() >= current.get_Next().get_Data().get_startIncome())) {

                                // compare current.end_income with current.next().end_income
                                if (current.get_Data().get_endIncome() >= current.get_Next().get_Data().get_endIncome()) {
                                    // delete current.next()
                                    remove(current.get_Next().get_Data());

                                } else { // when end_income fall in between current.next()'s range
                                    // update current.next()'s start_income
                                    current.get_Next().get_Data().set_startIncome(current.get_Data().get_endIncome() + 1);
                                }

                            } else if (current.get_Data().get_endIncome() == -1) {
                                // delete all records after current
                                while (current.get_Next() != null) {
                                    current = current.get_Next();
                                    // delete current.next()
                                    remove(current.get_Data());

                                }

                            }

                        }
                        break; // when finish overwrite the Tax Scale , break

                    }

                } // when start_income fall into an income rage
                else if (current.get_Data().compareTo(new_data) == -1) {

                    // check the new_data end_income compare to current.end_income
                    if (current.get_Data().get_endIncome() > new_data.get_endIncome()) {
                        // create a new TaxScale to fill the gap

                        T data = (T) new TaxScale();
                        data.set_startIncome(new_data.get_endIncome() + 1);
                        data.set_endIncome(current.get_Data().get_endIncome());
                        data.set_baseTax(current.get_Data().get_baseTax());
                        data.set_TaxPD(current.get_Data().get_TaxPD());

                        // insert new_data after current
                        insertAfter(current.get_Data(), new_data);
                        // update current's end_income
                        current.get_Data().set_endIncome(current.get_Next().get_Data().get_startIncome() - 1);
                        // insert new_data after current
                        insertAfter(current.get_Next().get_Data(), data);
                        break;

                    } else if (current.get_Data().get_endIncome().equals(new_data.get_endIncome())) {
                        insertAfter(current.get_Next().get_Data(), new_data);
                        current.get_Data().set_endIncome(new_data.get_startIncome() - 1);
                    }

                    else {

                        // insert new_data after current
                        insertAfter(current.get_Data(), new_data);
                        // update current
                        current.get_Data().set_endIncome(new_data.get_startIncome() - 1);

                        // check next TaxScale's range
                        while (current.get_Next() != null) {
                            current = current.get_Next();
                            // when there is no next TaxScale in the list
                            if(current.get_Next() == null){
                                if((current.get_Data().get_startIncome() <= current.get_Prev().get_Data().get_endIncome())){
                                    current.get_Data().set_startIncome(current.get_Prev().get_Data().get_endIncome ()+ 1);
                                }
                            }
                            else if ((current.get_Next().get_Data().get_startIncome() <= current.get_Data().get_endIncome())
                                    && (current.get_Next().get_Data().get_endIncome() > current.get_Data().get_endIncome())) {

                                current.get_Next().get_Data().set_startIncome(current.get_Data().get_endIncome() + 1);

                            } else if (current.get_Next().get_Data().get_endIncome() <= current.get_Data().get_endIncome()) {
                                // clean the TaxScale
                                remove(current.get_Next().get_Data());

                            }

                        }

                        break; // when finish overwrite the Tax Scale , break

                    }
                }

                else if (current.get_Data().compareTo(new_data) == 2) {
                    // compare the end_income
                    if (current.get_Data().get_endIncome() > new_data.get_endIncome()) {
                        current.get_Data().set_startIncome(new_data.get_endIncome() + 1);
                        insertBefore(current.get_Data(), new_data);

                    } else if (current.get_Data().get_endIncome() <= new_data.get_endIncome()) {
                        // update the current TaxScale
                        current.get_Data().set_endIncome(new_data.get_endIncome());
                        current.get_Data().set_baseTax(new_data.get_baseTax());
                        current.get_Data().set_TaxPD(new_data.get_TaxPD());

                        // check next TaxScale's range
                        while (current.get_Next() != null) {
                            if (current.get_Next().get_Data().get_endIncome() <= current.get_Data().get_endIncome()) {
                                // clean the TaxScale
                                remove(current.get_Next().get_Data());

                            } else if ((current.get_Next().get_Data().get_startIncome() <= current.get_Data().get_endIncome())
                                    && (current
                                    .get_Next().get_Data().get_endIncome()> current.get_Data().get_endIncome())) {

                                current.get_Next().get_Data().set_startIncome(current.get_Data().get_endIncome() + 1);
                            }

                            current = current.get_Next();

                            if (current == null) {
                                break;
                            }

                        }

                        break;

                    }

                }

                else {

                    if ((current.get_Next() == null)) {
                        // if current has a un defined end income
                        if (current.get_Data().get_endIncome().equals(-1)) {

                            if (!new_data.get_endIncome().equals(-1)) {
                                // create an end TaxScale and insert after the new_data
                                T data = (T) new TaxScale();
                                data.set_startIncome(new_data.get_endIncome ()+ 1);
                                data.set_endIncome(-1);
                                data.set_baseTax(current.get_Data().get_baseTax());
                                data.set_TaxPD(current.get_Data().get_TaxPD());

                                // update current end_income
                                current.get_Data().set_endIncome(new_data.get_startIncome() - 1);
                                Insert(new_data); // when there is no next TaxScale in List.

                                // insert into List
                                insertAfter(new_data, data);
                            } else {
                                current.get_Data().set_endIncome(new_data.get_startIncome() - 1);
                                Insert(new_data); // when there is no next TaxScale in List.
                            }

                        } else {
                            current.get_Data().set_endIncome(new_data.get_startIncome() - 1);
                            Insert(new_data); // when there is no next TaxScale in List.
                        }

                        break;

                    } else if ((current.get_Next() != null)
                            && new_data.get_startIncome() < current.get_Next().get_Data().get_startIncome()) {

                        //
                        if ((current.get_Data().get_endIncome() == -1) && (new_data.get_endIncome() != -1)) {
                            current.get_Data().set_endIncome(new_data.get_startIncome() - 1);

                            insertAfter(current.get_Data(), new_data);

                            // Insert a TaxScale after new_data.
                            T data = (T) new TaxScale();
                            data.set_startIncome(new_data.get_endIncome() + 1);
                            data.set_endIncome(-1);
                            data.set_baseTax(current.get_Data().get_baseTax());
                            data.set_TaxPD(current.get_Data().get_TaxPD());

                            // insert into List
                            insertAfter(new_data, data);

                            while (current.get_Next() != null) {

                                current = current.get_Next(); // first move cursor point to new_data
                                // delete all TaxScale after new_data
                                remove(current.get_Data());

                            }

                        } else if ((new_data.get_endIncome() == -1) && (current.get_Data().get_endIncome() != -1)) {

                            if (current.get_Data().get_startIncome().equals(new_data.get_startIncome())) {
                                // replace current TaxScale with new_data
                                current.get_Data().set_endIncome(-1);
                                current.get_Data().set_baseTax(new_data.get_baseTax());
                                current.get_Data().set_TaxPD(new_data.get_TaxPD());

                                while (current.get_Next() != null) {
                                    current = current.get_Next(); // first move cursor point to new_data
                                    // delete all TaxScale after new_data
                                    remove(current.get_Data());

                                }

                            } else {
                                insertAfter(current.get_Data(), new_data);
                                current.get_Data().set_endIncome(new_data.get_startIncome() - 1);
                                current = current.get_Next(); // move cursor to new_data
                                while (current.get_Next() != null) {

                                    current = current.get_Next(); // first move cursor point to new_data
                                    // delete all TaxScale after new_data
                                    remove(current.get_Data());

                                }
                            }

                        } else if ((new_data.get_endIncome() == -1) && (current.get_Data().get_endIncome() == -1)) {

                            if (new_data.get_startIncome() > current.get_Data().get_startIncome()) {

                                current.get_Data().set_endIncome(new_data.get_startIncome() - 1);
                                new_data.set_endIncome(-1);
                                insertAfter(current.get_Data(), new_data);

                                while (current.get_Next() != null) {

                                    current = current.get_Next(); // first move cursor point to new_data
                                    // delete all TaxScale after new_data
                                    remove(current.get_Data());

                                }

                            } else if (new_data.get_startIncome().equals(current.get_Data().get_startIncome())) {

                                // replace current TaxScale with new_data
                                current.get_Data().set_endIncome(-1);
                                current.get_Data().set_baseTax(new_data.get_baseTax());
                                current.get_Data().set_TaxPD(new_data.get_TaxPD());

                                while (current.get_Next() != null) {

                                    current = current.get_Next(); // first move cursor point to new_data
                                    // delete all TaxScale after new_data
                                    remove(current.get_Data());
                                }

                            }

                        }

                    }

                }
            }

        }
    }

    @Override
    public Iterator<T> iterator() {
        // TODO Auto-generated method stub
        return new linkedlist_Iterator();
    }

    private class linkedlist_Iterator implements Iterator<T> {
        private int expectedModCount; // count

        private Node<T> current;

        // construtor
        private linkedlist_Iterator() {
            this.current = sentinel;
            expectedModCount = mod;
        }

        // search if the Iterator have next()
        public T next() {
            if (expectedModCount != mod)
                throw new ConcurrentModificationException("Cannot mutate in context of iterator");

            this.current = current.get_Next();
            return current.get_Data();
        }

        public T currentI() {
            if (expectedModCount != mod)
                throw new ConcurrentModificationException("Cannot mutate in context of iterator");

            return this.current.get_Data();
        }

        // if get_Next() == null, the iterator reach till the end of the list
        public boolean hasNext() {
            return this.current.get_Next() != null; // true
        }

    }

}
